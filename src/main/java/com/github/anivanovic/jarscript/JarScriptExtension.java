package com.github.anivanovic.jarscript;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class JarScriptExtension {

    private Property<String> scriptName;
    private Property<String> scriptPath;

    public JarScriptExtension(Project project) {
        this.scriptName = project.getObjects().property(String.class);
        this.scriptPath = project.getObjects().property(String.class);
    }

    public Property<String> getScriptName() {
        return scriptName;
    }

    public void setScriptName(String name) {
        this.scriptName.set(name);
    }

    public Property<String> getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String path) {
        this.scriptPath.set(path);
    }
}
