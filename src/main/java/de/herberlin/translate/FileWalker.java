package de.herberlin.translate;

import org.apache.maven.plugin.logging.Log;

import java.io.File;

public interface FileWalker {
    void init(Translator translator, File source, Log log);
    void translate(String language);
}
