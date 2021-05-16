package de.herberlin.translate;

import java.util.LinkedList;
import java.util.List;

public class DummyTranslator implements Translator {
    @Override
    public String translate(String text, String languageCode) {
        return text + "-" + languageCode;
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
