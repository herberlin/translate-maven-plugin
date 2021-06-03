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

@Mojo(name = "translate")
public class TranslateMojo extends AbstractMojo {

    private enum Mode {
        android(AndroidWalker.class),
        properties(PropertiesWalker.class),
        json(JsonWalker.class);

        Mode(Class<? extends FileWalker> executor) {
            this.executor = executor;
        }

        private Class<? extends FileWalker> executor = null;
    }

    private enum TranslatorImplementation {
        google(GoogleTranslator.class), dummy(DummyTranslator.class);

        private Class<? extends Translator> translator;
        TranslatorImplementation(Class<? extends Translator> translator) {
            this.translator = translator;
        }
    }


    @Parameter(defaultValue = "android") private Mode mode;
    @Parameter(defaultValue = "google") private TranslatorImplementation translator;

    @Parameter(required = true) private File source;

    @Parameter(required = true) private File certificate;

    @Parameter(defaultValue = "https://www.googleapis.com/auth/cloud-platform") private String serviceUrl;

    @Parameter(required = true) private List<String> languages;

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