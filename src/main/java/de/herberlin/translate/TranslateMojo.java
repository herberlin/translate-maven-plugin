package de.herberlin.translate;

import de.herberlin.translate.impl.AndroidWalker;
import de.herberlin.translate.impl.DummyTranslator;
import de.herberlin.translate.impl.GoogleTranslator;
import de.herberlin.translate.impl.JsonWalker;
import de.herberlin.translate.impl.PropertiesWalker;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * Maven plugin interface.
 */
@Mojo(name = "translate")
public class TranslateMojo extends AbstractMojo {

    /**
     * Possible translation modes.
     */
    private enum Mode {
        android(AndroidWalker.class),
        properties(PropertiesWalker.class),
        json(JsonWalker.class);

        Mode(Class<? extends FileWalker> executor) {
            this.executor = executor;
        }

        private Class<? extends FileWalker> executor = null;
    }

    /**
     * Possible translator implementations,
     * currently Google and Dummy
     */
    private enum TranslatorImplementation {
        google(GoogleTranslator.class), dummy(DummyTranslator.class);

        private Class<? extends Translator> translator;
        TranslatorImplementation(Class<? extends Translator> translator) {
            this.translator = translator;
        }
    }


    /**
     * Maven configuration parameter mode,
     * possible values android, json, default: andorid
     */
    @Parameter(defaultValue = "android") private Mode mode;
    /**
     * Maven configuration parameter translator,
     * possible values google, dummy.
     */
    @Parameter(defaultValue = "google") private TranslatorImplementation translator;

    /**
     * Maven configuration parameter source file.
     */
    @Parameter(required = true) private File source;

    /**
     * Maven configuration parameter certificate location.
     */
    @Parameter(required = true) private File certificate;

    /**
     * Maven configuration parameter serviceUrl, defaults to goocle cloud.
     */
    @Parameter(defaultValue = "https://www.googleapis.com/auth/cloud-platform") private String serviceUrl;

    /**
     * Configured target languages.
     */
    @Parameter(required = true) private List<String> languages;

    /**
     * Plugin entrypoint.
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException {

        if (!source.canRead()) {
            throw new MojoExecutionException("Can't read source: " + source);
        }

        getLog().info("Source: " + source);
        getLog().info("Mode: " + mode);
        getLog().info("Target Languages: " + languages);
        getLog().info("Certificate: " + certificate);
        getLog().info("Service URL: " + serviceUrl);

        Translator trans = null;
        try {
            trans = translator.translator.getDeclaredConstructor().newInstance();
            trans.setCredentials(certificate, serviceUrl);
        } catch (Exception e) {
            throw new MojoExecutionException("Error creating " + translator.translator, e);
        }
        getLog().info("Using translator: " + trans.getClass());

        FileWalker fileWalker = null;
        try {
            fileWalker = mode.executor.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new MojoExecutionException("Error creating " + mode.executor, e);
        }
        fileWalker.init(trans, source, getLog());
        for (String language : languages) {
            fileWalker.translate(language);
        }
    }
}