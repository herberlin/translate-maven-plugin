package de.herberlin.translate;

import org.apache.maven.plugin.logging.Log;

import java.io.File;

public class AndroidWalker implements FileWalker {

    private Translator translator;
    private File source;
    private Log log;

    @Override
    public void init(Translator translator, File source, Log log) {
        this.translator = translator;
        this.source = source;
        this.log = log;
    }

    @Override
    public void translate(String language) {

    }
}
