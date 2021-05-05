package de.herberlin.translate;

import java.util.List;

public class DummyTranslator implements Translator {
    @Override
    public String translate(String text, String languageCode) {
        return text + "-" + languageCode;
    }

    @Override
    public List<String> translate(List<String> list, String languageCode) {
        return list;
    }
}
