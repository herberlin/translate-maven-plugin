package de.herberlin.translate;

import java.io.File;
import java.util.List;

/**
 * Common entrypoint for gradle and maven.
 */
public class Gateway {

    public static void process(File source,
                               List<String> languages, Settings.Mode mode,
                               Settings.TranslatorImplementation translator,
                               File certificate, String serviceUrl, Log log) throws Exception{

        if (!source.canRead()) {
            throw new Exception("Can't read source: " + source);
        }

        log.info("Source: " + source);
        log.info("Mode: " + mode);
        log.info("Target Languages: " + languages);
        log.info("Certificate: " + certificate);
        log.info("Service URL: " + serviceUrl);

        Translator trans = null;
        try {
            trans = translator.translator.getDeclaredConstructor().newInstance();
            trans.setCredentials(certificate, serviceUrl);
        } catch (Exception e) {
            throw new Exception("Error creating " + translator.translator, e);
        }
        log.info("Using translator: " + trans.getClass());

        FileWalker fileWalker = null;
        try {
            fileWalker = mode.executor.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new Exception("Error creating " + mode.executor, e);
        }
        fileWalker.init(trans, source, log);
        for (String language : languages) {
            fileWalker.translate(language);
        }

    }
}
