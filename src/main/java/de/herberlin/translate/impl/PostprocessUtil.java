package de.herberlin.translate.impl;

/**
 * Do things with the translated text.
 */
public class PostprocessUtil {

    public static String process(String source, String translated) {
        return ucFirst(source,translated);
    }

    private static String ucFirst(String source, String translated) {
        if (source != null && translated != null) {
            if (source.length() > 0 && Character.isUpperCase(source.charAt(0))) {
                if (translated.length() > 0 ) {
                    char[] t = translated.toCharArray();
                    t[0] = Character.toUpperCase(t[0]);
                    return String.valueOf(t);
                }
            }
        }
        return translated;
    }
}
