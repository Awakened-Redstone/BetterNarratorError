package betternarratorerror.mixin;

import betternarratorerror.util.RuntimeProperty;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.text2speech.Narrator;
import com.mojang.text2speech.NarratorLinux;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NarratorLinux.class)
public class NarratorLinuxMixin {
    @Mixin(targets = {"com.mojang.text2speech.NarratorLinux$FliteLibrary", "com.mojang.text2speech.NarratorLinux$FliteLibrary$CmuUsKal16"})
    private static class FliteLibraryMixin {
        @WrapOperation(at = @At(value = "NEW", target = "(Ljava/lang/String;Ljava/lang/Throwable;)Lcom/mojang/text2speech/Narrator$InitializeException;"), method = "loadNative", remap = false)
        private static Narrator.InitializeException pleaseDontGenerateAHugeStackTraceThanks(String message, Throwable cause, Operation<Narrator.InitializeException> original) {
            Throwable newCause;

            String[] strings = cause.getMessage().split("\n");
            StringBuilder newMessage = new StringBuilder();
            for (String string : strings) {
                if (!newMessage.isEmpty()) {
                    newMessage.append("\n  ");
                }

                if (string.startsWith("Native library") && string.contains("not found in resource path") && RuntimeProperty.getBoolean("minecraft.narrator.error.hide_path", true)) {
                    newMessage.append(string, 0, string.lastIndexOf("("));
                    continue;
                }

                newMessage.append(string);
            }

            if (cause instanceof UnsatisfiedLinkError) {
                newCause = new UnsatisfiedLinkError(newMessage.toString());
            } else {
                newCause = new Throwable(newMessage.toString());
            }

            if (RuntimeProperty.getBoolean("minecraft.narrator.error.hide_stacktrace", true)) {
                newCause.setStackTrace(new StackTraceElement[]{});
            }

            return original.call(message, RuntimeProperty.getBoolean("minecraft.narrator.error.replace_exception", true) ? newCause : cause);
        }
    }
}
