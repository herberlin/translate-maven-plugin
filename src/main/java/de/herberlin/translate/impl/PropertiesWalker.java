package de.herberlin.translate.impl;

import de.herberlin.translate.FileWalker;
import de.herberlin.translate.Translator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;

/**
 * TODO: Walker implementation for java properties.
 */
public class PropertiesWalker implements FileWalker {
    @Override
    public void init(Translator translator, File source, Log log) throws MojoExecutionException {
        throw new MojoExecutionException("TODO: Walker for properties not implemented!");
    }

    @Override
    public void translate(String language) throws MojoExecutionException {

    }
}
