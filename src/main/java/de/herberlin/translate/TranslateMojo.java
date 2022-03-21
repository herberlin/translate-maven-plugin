package de.herberlin.translate;

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
     * Maven configuration parameter mode,
     * possible values android, json, default: andorid
     */
    @Parameter(defaultValue = "android")
    private Settings.Mode mode;
    /**
     * Maven configuration parameter translator,
     * possible values google, dummy.
     */
    @Parameter(defaultValue = "google")
    private Settings.TranslatorImplementation translator;

    /**
     * Maven configuration parameter source file.
     */
    @Parameter(required = true)
    private File source;

    /**
     * Maven configuration parameter certificate location.
     */
    @Parameter(required = true)
    private File certificate;

    /**
     * Maven configuration parameter serviceUrl, defaults to goocle cloud.
     */
    @Parameter(defaultValue = "https://www.googleapis.com/auth/cloud-platform")
    private String serviceUrl;

    /**
     * Configured target languages.
     */
    @Parameter(required = true)
    private List<String> languages;

    /**
     * Plugin entrypoint.
     *
     * @throws MojoExecutionException in case of error
     */
    public void execute() throws MojoExecutionException {
        Log log = new Log.MavenLogger(getLog());

        try {
            Gateway.process(source, languages, mode, translator, certificate, serviceUrl, log);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}