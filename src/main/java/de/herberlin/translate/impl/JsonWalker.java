package de.herberlin.translate.impl;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import de.herberlin.translate.FileWalker;
import de.herberlin.translate.Translator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonWalker implements FileWalker {
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
            Gson gson = new Gson();
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

    private void processMap(Map<String, Object> sourceMap, Map<String, Object> targetMap) {
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            if (entry.getValue() instanceof Map) {
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
            } else {
                if (targetMap.get(entry.getKey()) == null) {
                    String translated = translator.translate(entry.getValue().toString(), language);
                    targetMap.put(entry.getKey(), translated);
                    log.debug(String.format("Translate %s -> %s", entry.getValue(), translated));
                } else {
                    log.debug("Key exists: " + entry.getKey());
                }
            }
        }
    }

    private void processList(List<String> sourceList, List<String> targetList) {
        for (String s : sourceList) {
            String t = translator.translate(s, language);
            targetList.add(t);
            log.debug(String.format("Translate %s -> %s", s, t));

        }
    }
}
