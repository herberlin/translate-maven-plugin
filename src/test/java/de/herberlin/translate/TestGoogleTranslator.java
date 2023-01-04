package de.herberlin.translate;

import de.herberlin.translate.impl.GoogleTranslator;
import de.herberlin.translate.impl.PostprocessUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestGoogleTranslator {

    private static final String source = "Anchor alert at<xliff:g> %1$s </xliff:g>of <xliff:g>%2$s</xliff:g>m.";
    private static final String googleResult = "تنبيه المرساة في<xliff:g> ٪ 1 $ s</xliff:g> من<xliff:g> ٪ 2 $ s</xliff:g> م.";

    /**
     * ```
     * en: Anchor alert at<xliff:g> %1$s </xliff:g>of <xliff:g>%2$s</xliff:g>m.
     * ar : تنبيه المرساة في<xliff:g> ٪ 1 $ s</xliff:g> من<xliff:g> ٪ 2 $ s</xliff:g> م.
     * el: Ειδοποίηση άγκυρας στο<xliff:g> %1$s</xliff:g> του<xliff:g> %2$s</xliff:g> Μ.
     * ```
     * Google Translator spreads xliff content in rtl languages.
     */
    // @Test
    public void testXliff() throws Exception {
        File key = new File("/home/aherbertz/Dokumente/goolge-cert/key.boatspeed-translate.json");
        GoogleTranslator translator = new GoogleTranslator();
        translator.setCredentials(key, "https://www.googleapis.com/auth/cloud-platform");
        String result = translator.translate(source, "ar");
        Assert.assertEquals( googleResult , result);
    }

    @Test
    public void fixXliffSpread() throws  Exception {
        String result = PostprocessUtil.process(source, googleResult);
        Assert.assertEquals("تنبيه المرساة في %1$s  من %2$s  م.", result);
    }

}
