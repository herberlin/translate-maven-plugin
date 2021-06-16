package de.herberlin.translate;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.List;

public interface Translator {

    String translate(String text, String languageCode) throws MojoExecutionException;
    void setCredentials(File credentialFile, String serviceUrl) throws MojoExecutionException;
    List<String> translate(List<String> list, String languageCode) throws MojoExecutionException;

}
