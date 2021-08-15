package de.herberlin.translate;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TranslatePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        TranslatePluginExtension extension = project.getExtensions()
                .create("translate", TranslatePluginExtension.class);
        project.task("translate")
                .doLast(task -> {
                    System.out.println("Mode= " + extension.getMode());
                    System.out.println("Hello Gradle!");
                });
    }
}
