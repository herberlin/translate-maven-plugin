package de.herberlin.translate;

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
        properties(null),
        json(null);

        Mode(Class<? extends FileWalker> executor) {
            this.executor = executor;
        }

        private Class<? extends FileWalker> executor = null;
    }

    @Parameter(defaultValue = "properties") private Mode mode;

    @Parameter(required = true) private File source;

    @Parameter(required = true) private File certificate;

    @Parameter(defaultValue = "https://www.googleapis.com/auth/cloud-platform") private String serviceUrl;

    @Parameter(required = true) private List<String> languages;

    public void execute() throws MojoExecutionException {

        if (!source.canRead()) {
            throw new MojoExecutionException("Can't read source: " + source);
        }

        getLog().info("Mode: " + mode);
        getLog().info("Target Languages: " + languages);
        getLog().info("Certificate: " + certificate);
        getLog().info("Service URL: " + serviceUrl);
        getLog().info("Source: " + source);

        Translator translator = new DummyTranslator();
        FileWalker fileWalker = null;
        try {
            fileWalker = mode.executor.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new MojoExecutionException("Error creating " + mode.executor, e);
        }
        fileWalker.init(translator, source, getLog());
        for (String language : languages) {
            fileWalker.translate(language);
        }
    }
}