package de.herberlin.translate;

import de.herberlin.translate.impl.*;

/**
 * Class holds some setting enumerations
 */
public class Settings {

    /**
     * Possible translation modes.
     */
    public enum Mode {
        android(AndroidWalker.class),
        properties(PropertiesWalker.class),
        json(JsonWalker.class);

        Mode(Class<? extends FileWalker> executor) {
            this.executor = executor;
        }

        Class<? extends FileWalker> executor = null;
    }

    /**
     * Possible translator implementations,
     * currently Google and Dummy
     */
    public enum TranslatorImplementation {
        google(GoogleTranslator.class), dummy(DummyTranslator.class);

        Class<? extends Translator> translator;

        TranslatorImplementation(Class<? extends Translator> translator) {
            this.translator = translator;
        }
    }
}
