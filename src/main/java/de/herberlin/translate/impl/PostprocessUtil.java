package de.herberlin.translate.impl;

/**
 * Do things with the translated text.
 */
public class PostprocessUtil {

    public static String process(String source, String translated) {
        String result = ucFirst(source, translated);
        result = clearSpacesInXliff(result);
        result = removeXliffTags(result);
        return result;
    }

    private static String removeXliffTags(String result) {
        result = result.replaceAll("</?xliff:g>","");
        result = result.replaceAll("&lt;/?xliff:g>","");
        result = result.replaceAll("&lt;/?xliff:g&gt;","");
        return result;
    }

    /**
     * en: Anchor alert at<xliff:g> %1$s </xliff:g>of <xliff:g>%2$s</xliff:g>m.
     * ar : تنبيه المرساة في<xliff:g> ٪ 1 $ s</xliff:g> من<xliff:g> ٪ 2 $ s</xliff:g> م.
     * el: Ειδοποίηση άγκυρας στο<xliff:g> %1$s</xliff:g> του<xliff:g> %2$s</xliff:g> Μ.
     */
    private static String clearSpacesInXliff(String translated) {
        if (translated == null || !(translated.contains("٪")|| translated.contains(("% ")))) {
            // OK, must not fix
            return translated;
        }
        String str = translated;
        int sIndex = 0;
        int eIndex = 0;
        while (str.indexOf("<xliff:g>", sIndex) >= 0) {
            sIndex = str.indexOf("<xliff:g>", sIndex) + "<xliff:g>".length();
            eIndex = str.indexOf("</xliff:g>", eIndex);
            if (sIndex < eIndex) {
                String actual = str.substring(sIndex, eIndex);
                String sub = " " + actual.replaceAll(" ", "").replaceAll("٪", "%")+ " ";
                str = str.replace(actual, sub);
                sIndex++;
                eIndex++;
            } else {
                break;
            }
        }
        return str;
    }

    private static String ucFirst(String source, String translated) {
        if (source != null && translated != null) {
            if (source.length() > 0 && Character.isUpperCase(source.charAt(0))) {
                if (translated.length() > 0) {
                    char[] t = translated.toCharArray();
                    t[0] = Character.toUpperCase(t[0]);
                    return String.valueOf(t);
                }
            }
        }
        return translated;
    }
}
