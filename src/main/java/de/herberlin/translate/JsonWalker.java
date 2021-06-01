package de.herberlin.translate;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonWalker implements FileWalker {
    private Translator translator;
    private File source;
    private Log log;

    @Override
    public void init(Translator translator, File source, Log log) throws MojoExecutionException {
        this.log = log;
        this.source = source;
        this.translator = translator;

    }


    @Override
    public void translate(String language) throws MojoExecutionException {

        try {
            Map<String, Object> map = new Gson().fromJson(new FileReader(source), Map.class);
            log.info("Map = " + map);

        } catch (Exception e) {
            log.error("Error parsing source", e);
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
