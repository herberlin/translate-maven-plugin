package de.herberlin.translate;

import java.util.List;

public interface Translator {

    String translate(String text, String languageCode);

    List<String> translate(List<String> list, String languageCode);

}
