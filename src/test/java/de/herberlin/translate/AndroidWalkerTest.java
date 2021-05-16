package de.herberlin.translate;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.IOException;

public class AndroidWalkerTest {
    Translator translator = new DummyTranslator();
    File source = new File("target/data/android/values/strings.xml");
    Log log = new SystemStreamLog();

    @Before
    public void setUp() throws IOException {
        File target= new File("target/data");
        File source = new File("data");
        FileUtils.deleteDirectory(target);
        FileUtils.copyDirectory(source, target);
     }

    @org.junit.Test
    public void translate() throws MojoExecutionException {
        AndroidWalker testObj = new AndroidWalker();
        testObj.init(translator, source, log );
        testObj.translate("ru");
        Assert.assertTrue(new File("target/data/android/values-ru/strings.xml").exists() );

    }
}