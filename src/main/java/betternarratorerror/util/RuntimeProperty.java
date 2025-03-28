package betternarratorerror.util;

public class RuntimeProperty {
    public static boolean getBoolean(String name, boolean fallback) {
        String property = System.getProperty(name);
        boolean result = fallback;
        if (property != null) {
            try {
                result = Boolean.parseBoolean(System.getProperty(name));
            } catch (IllegalArgumentException ignored) {}
        }

        return result;
    }
}
