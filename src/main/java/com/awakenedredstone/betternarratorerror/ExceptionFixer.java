package com.awakenedredstone.betternarratorerror;

import com.awakenedredstone.betternarratorerror.config.NarratorConfig;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.IOException;
import java.util.function.Function;

public class ExceptionFixer {
    private static final StackTraceElement[] NO_STACK_TRACE = new StackTraceElement[]{};

    public static Throwable fixException(Throwable cause) {
        if (NarratorConfig.INSTANCE.hidePath || NarratorConfig.INSTANCE.hideSuppressed) {
            var exception = replaceException(cause);
            if (exception != null) cause = exception;
        }

        hideStacktraces(cause);

        return cause;
    }

    public static void hideStacktraces(Throwable exception) {
        if (!NarratorConfig.INSTANCE.hideStacktraces) return;

        exception.setStackTrace(NO_STACK_TRACE);

        for (Throwable suppressed : exception.getSuppressed()) {
            suppressed.setStackTrace(NO_STACK_TRACE);
        }

        Throwable cause = exception.getCause();
        while (cause != null) {
            cause.setStackTrace(NO_STACK_TRACE);
            cause = cause.getCause();
        }
    }

    private static @Nullable Throwable replaceException(Throwable cause) {
        Function<String, Throwable> constructor = exceptionConstructor(cause);

        if (constructor == null) return null;

        String originalMessage = cause.getMessage();
        String message = replacePath(originalMessage);

        //noinspection StringEquality
        if (message == originalMessage && !NarratorConfig.INSTANCE.hideSuppressed) {
            return null;
        }

        var exception = constructor.apply(message);
        if (!NarratorConfig.INSTANCE.hideSuppressed) {
            // Add the suppressed exceptions back
            for (Throwable suppressed : cause.getSuppressed()) {
                var replacement = replaceException(suppressed);
                if (replacement != null) suppressed = replacement;

                exception.addSuppressed(suppressed);
            }
        }

        if (!NarratorConfig.INSTANCE.hideStacktraces) {
            // Keep the old stacktrace when not hiding it
            exception.setStackTrace(cause.getStackTrace());
        }

        return exception;
    }

    private static String replacePath(@UnknownNullability String message) {
        if (!NarratorConfig.INSTANCE.hidePath || !message.contains("not found in resource path")) {
            return message;
        }

        String[] strings = message.split("\n");
        StringBuilder newMessage = new StringBuilder();
        for (String string : strings) {
            if (!newMessage.isEmpty()) {
                newMessage.append("\n");
            }

            if (string.startsWith("Native library") && string.contains("not found in resource path")) {
                newMessage.append(string, 0, string.lastIndexOf("("));
                continue;
            }

            newMessage.append(string);
        }

        return newMessage.toString();
    }

    private static @Nullable Function<String, Throwable> exceptionConstructor(Throwable original) {
        if (original instanceof UnsatisfiedLinkError) {
            return UnsatisfiedLinkError::new;
        } else if (original instanceof IOException) {
            return message -> new IOException(message, original.getCause());
        } else {
            // Only the library loading errors has the message edited or suppressed exceptions
            return null;
        }
    }
}
