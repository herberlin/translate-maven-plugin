package de.herberlin.translate.impl;

import de.herberlin.translate.Translator;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Dummy translator implementation for testing.
 * Just adds the language code to the text to be translated.
 */
public class DummyTranslator implements Translator {
    @Override
    public String translate(String text, String languageCode) {
        return text + "-" + languageCode;
    }

    @Override
    public void setCredentials(File credentialFile, String serviceUrl) throws MojoExecutionException {

    }

    @Override
    public List<String> translate(List<String> list, String languageCode) {
        List<String> result = new LinkedList<>();
        for (String s : list) {
            result.add(translate(s, languageCode));
        }
        return result;
    }
}
