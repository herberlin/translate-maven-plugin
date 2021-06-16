package de.herberlin.translate;

import com.google.gson.Gson;
import de.herberlin.translate.impl.DummyTranslator;
import de.herberlin.translate.impl.JsonWalker;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JsonWalkerTest {

    Translator translator = new DummyTranslator();
    File source = new File("target/data/json/de.json");
    Log log = new SystemStreamLog();

    @BeforeClass
    public static void setUp() throws IOException {
        File target = new File("target/data");
        File source = new File("data");
        FileUtils.deleteDirectory(target);
        FileUtils.copyDirectory(source, target);
    }

    @Test
    public void testRu() throws MojoExecutionException, FileNotFoundException {
        File ruFile = new File("target/data/json/ru.json");
        JsonWalker testObj = new JsonWalker();
        testObj.init(translator, source, log);
        testObj.translate("ru");
        Assert.assertTrue(ruFile.exists());
        Map<String, Object> map = new Gson().fromJson(new FileReader(ruFile), Map.class);
        Assert.assertEquals("Fails for 'base'", "Basiseintrag-ru", ((Map<String, Object>)map.get("common")).get("base"));
        Assert.assertEquals("Fails for 'back'", "zurück-ru", ((Map<String, Object>)map.get("entries")).get("back"));


    }

    @Test
    public void testEn() throws MojoExecutionException, FileNotFoundException {
        File enFile = new File("target/data/json/en.json");
        JsonWalker testObj = new JsonWalker();
        testObj.init(translator, source, log);
        testObj.translate("en");
        Assert.assertTrue(enFile.exists());
        Map<String, Object> map = new Gson().fromJson(new FileReader(enFile), Map.class);
        Assert.assertEquals("Fails for 'base'", "existing", ((Map<String, Object>)map.get("common")).get("base"));
        Assert.assertEquals("Fails for 'last_entry'", "Letzter Eintrag-en", ((Map<String, Object>)map.get("entries")).get("last_entry"));
    }
}
