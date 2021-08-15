package de.herberlin.translate;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.BuildException;

import java.io.File;
import java.util.Arrays;

public class TranslatePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        Log log = new Log.GradleLogger(project.getLogger());

        TranslatePluginExtension extension = project.getExtensions()
                .create("translate", TranslatePluginExtension.class);

        project.task("translate")
                .doFirst(task -> {
                    try {
                        Gateway.process(
                                new File(extension.getSource()),
                                Arrays.asList(extension.getLanguages()),
                                Settings.Mode.valueOf(extension.getMode()),
                                Settings.TranslatorImplementation.valueOf(extension.getTranslator()),
                                new File(extension.getCertificate()),
                                extension.getServiceUrl(),
                                log
                                );
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw  new BuildException(e.getMessage(), e);
                    }
                });
    }
}
