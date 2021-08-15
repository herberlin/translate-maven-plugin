package de.herberlin.translate.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.herberlin.translate.FileWalker;
import de.herberlin.translate.Translator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Walker implementation for Json.
 */
public class JsonWalker implements FileWalker {
    private static final String PREFIX_UPDATED = "_UPDATED_";
    private Translator translator;
    private File source;
    private Log log;
    private String language = null;

    @Override
    public void init(Translator translator, File source, Log log) throws MojoExecutionException {
        this.log = log;
        this.source = source;
        this.translator = translator;

    }


    @Override
    public void translate(String language) throws MojoExecutionException {
        this.language = language;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
            File targetFile = new File(source.getParentFile(), language + ".json");
            boolean targetFileIsNew = false;
            Map<String, Object> targetMap;
            if (!targetFile.exists()) {
                targetFileIsNew = targetFile.createNewFile();
                targetMap = new HashMap<>();

            } else {
                targetMap = gson.fromJson(new FileReader(targetFile), Map.class);
            }
            Map<String, Object> sourceMap = gson.fromJson(new FileReader(source), Map.class);
            processMap(sourceMap, targetMap);

            FileWriter writer = new FileWriter(targetFile);
            writer.write(gson.toJson(targetMap));
            writer.close();
        } catch (Exception e) {
            log.error("Error parsing source", e);
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void processMap(Map<String, Object> sourceMap, Map<String, Object> targetMap) throws MojoExecutionException {
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            if (entry.getValue() instanceof String) {
                translateString(targetMap, entry);
            } else if (entry.getValue() instanceof Map) {
                Map<String, Object> subMap = (Map) targetMap.get(entry.getKey());
                if (subMap == null) {
                    subMap = new HashMap<>();
                    targetMap.put(entry.getKey(), subMap);
                }
                processMap((Map) entry.getValue(), subMap);
            } else if (entry.getValue() instanceof List) {
                List<String> targetList = (List) targetMap.get(entry.getKey());
                if (targetList == null) {
                    targetList = new LinkedList<>();
                    targetMap.put(entry.getKey(), targetList);
                }
                processList((List) entry.getValue(), targetList);
            }
        }
    }

    private void translateString(Map<String, Object> targetMap, Map.Entry<String, Object> entry) throws MojoExecutionException {
        boolean doTranslate = true;
        if (targetMap.get(entry.getKey()) == null) {
            // translate it
        } else if (entry.getValue() != null && entry.getValue().toString().startsWith(PREFIX_UPDATED)) {
            log.debug("Updated prefix found for: " + entry.getKey());
        } else {
            log.debug("Key exists: " + entry.getKey());
            doTranslate = false;
        }
        if (doTranslate) {
            String toBeTranslated = entry.getValue().toString().replace(PREFIX_UPDATED, "");
            String translated = translator.translate(toBeTranslated, language);
            targetMap.put(entry.getKey(), translated);
            log.debug(String.format("Translate %s -> %s", entry.getValue(), translated));
        }
    }


    private void processList(List<String> sourceList, List<String> targetList) throws MojoExecutionException {
        for (String s : sourceList) {
            String t = translator.translate(s, language);
            targetList.add(t);
            log.debug(String.format("Translate %s -> %s", s, t));

        }
    }
}
