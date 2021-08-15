package de.herberlin.translate.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import de.herberlin.translate.Translator;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Translator implementation for Google - Cloud.
 */
public class GoogleTranslator implements Translator {
    private Translate translate;

    public void setCredentials(File credentialFile, String serviceUrl) throws MojoExecutionException {

        if (!credentialFile.canRead()) {
            throw new MojoExecutionException("Can't read certificate: " + credentialFile);
        }

        try {
            GoogleCredentials credentials =
                    GoogleCredentials.fromStream(new FileInputStream(credentialFile)).createScoped(serviceUrl);
            translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
        }catch (Exception e) {
            throw new MojoExecutionException("Error creating translator", e);
        }
    }

    /**
     * @param languageCode eg de or zh-CN
     */
    @Override
    public String translate(String text, String languageCode) {
        return translate.translate(text, Translate.TranslateOption.targetLanguage(languageCode),
            Translate.TranslateOption.model("nmt")).getTranslatedText();
    }

    @Override
    public List<String> translate(List<String> list, String languageCode) {
        List<Translation> transList = translate.translate(list, Translate.TranslateOption.targetLanguage(languageCode),
            Translate.TranslateOption.model("nmt"));
        List<String> resultList = new LinkedList<String>();
        for (Translation t : transList) {
            resultList.add(t.getTranslatedText());
        }
        return resultList;
    }

}
