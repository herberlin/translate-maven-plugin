package de.herberlin.translate;

import com.google.api.client.json.Json;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JsonWalkerTest {

    Translator translator = new DummyTranslator();
    File source = new File("target/data/json/de.json");
    Log log = new SystemStreamLog();

    @Before
    public void setUp() throws IOException {
        File target = new File("target/data");
        File source = new File("data");
        FileUtils.deleteDirectory(target);
        FileUtils.copyDirectory(source, target);
    }

    @Test
    public void testRu() throws MojoExecutionException {
        File sourceFile = new File("target/data/json/de.json");
        File ruFile = new File("target/data/json/ru.json");
        JsonWalker testObj = new JsonWalker();
        testObj.init(translator, source, log);
        testObj.translate("ru");
        Assert.assertTrue(ruFile.exists());


    }
}
