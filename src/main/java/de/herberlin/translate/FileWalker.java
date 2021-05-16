package de.herberlin.translate;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;

public interface FileWalker {
    void init(Translator translator, File source, Log log) throws MojoExecutionException;
    void translate(String language) throws MojoExecutionException;
}
