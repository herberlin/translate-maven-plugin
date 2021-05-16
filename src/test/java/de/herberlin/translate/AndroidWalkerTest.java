package de.herberlin.translate;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.File;

public class AndroidWalkerTest {
    Translator translator = new DummyTranslator();
    File source = new File("data/android/values/strings.xml");
    Log log = new SystemStreamLog();

    @org.junit.Test
    public void translate() {
        AndroidWalker testObj = new AndroidWalker();
        testObj.init(translator, source, log );

    }
}