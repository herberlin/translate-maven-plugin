package de.herberlin.translate;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

/**
 * Implementing classes walk through the filesystem, locate resources files
 * and translate the content passing it to a given Translator.
 * Implementations for Android, json and java-properties.
 *
 */
public interface FileWalker {
    /**
     * Called on initialization
     * @param translator Translator, either google or dummy
     * @param source source file
     * @param log Logger
     * @throws MojoExecutionException MojoExecutionException
     */
    void init(Translator translator, File source, Log log) throws MojoExecutionException;

    /**
     * Parses the in init(..) given source file into a target language and writes the result
     * to the filesystem.
     * @param language two letter language code.
     * @throws MojoExecutionException in case of error.
     */
    void translate(String language) throws MojoExecutionException;
}
