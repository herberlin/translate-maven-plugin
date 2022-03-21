package de.herberlin.translate;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.List;

/**
 * Translator interface. Implementing classes call a translator service.
 */
public interface Translator {

    /**String
     * Initialization.
     * @param credentialFile Credential file
     * @param serviceUrl Service URL.
     * @throws MojoExecutionException  in case of error.
     */
    void setCredentials(File credentialFile, String serviceUrl) throws MojoExecutionException;

    /**
     * Translates a string into a target language calling the service.
     * @param text Text to translate
     * @param languageCode iso language code, two or four digets.
     * @throws MojoExecutionException  in case of error.
     * @return Translated string.
     */
    String translate(String text, String languageCode) throws MojoExecutionException;

    /**
     * Translates a collection of strings into the target language.
     * Possible performance optimization compared with "String translate(String text, String languageCode)"
     * @param list Text to translate.
     * @param languageCode iso language code, two or four digets.
     * @throws MojoExecutionException  in case of error.
     * @return List of translated strings.
     */
    List<String> translate(List<String> list, String languageCode) throws MojoExecutionException;

}
