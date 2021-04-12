package com.github.anivanovic.jarscript;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

public class JarScriptPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final JarScriptExtension extension =
                project.getExtensions().create("jarscript", JarScriptExtension.class, project);

        project.getPlugins().withType(JavaPlugin.class, javaPlugin -> {
            final JarScriptTask jarscript = project.getTasks()
                    .register(
                            "jarscript",
                            JarScriptTask.class,
                            project)
                    .get();
            TaskProvider<Jar> jar = project.getTasks().withType(Jar.class).named("jar");
            jarscript.getJar().set(jar.get().getArchiveFile());
            jarscript.getOutputDir().set(jar.get().getDestinationDirectory());
            jarscript.getScriptName().set(extension.getScriptName());
            jarscript.getScriptPath().set(extension.getScriptPath());
            jarscript.setGroup(BasePlugin.BUILD_GROUP);
            jarscript.setDescription("Creates single runnable app script with all dependencies inside.");
            jarscript.dependsOn(jar);
        });
    }
}
