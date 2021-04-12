package com.github.anivanovic.jarscript;

import com.google.common.io.CharSource;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFile;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JarScriptTask extends DefaultTask {

  private final Project project;

  private final Property<String> scriptName;
  private final Property<String> scriptPath;
  private final Property<RegularFile> jar;
  private final DirectoryProperty outDir;

  @Inject
  public JarScriptTask(Project project) {
    final ObjectFactory objectFactory = project.getObjects();
    scriptName = objectFactory.property(String.class);
    scriptPath = objectFactory.property(String.class);
    jar = objectFactory.fileProperty();
    outDir = objectFactory.directoryProperty();
    this.project = project;
  }

  @Input
  @Optional
  public Property<String> getScriptName() {
    return scriptName;
  }

  @Input
  @Optional
  public Property<String> getScriptPath() {
    return scriptPath;
  }

  @InputFile
  public Property<RegularFile> getJar() {
    return jar;
  }

  @OutputDirectory
  public DirectoryProperty getOutputDir() {
    return outDir;
  }

  @TaskAction
  public void execute() throws IOException {
    final String script = getScriptPath().getOrNull();
    final CharSource charSource;
    if (script == null) {
      final InputStream in = getClass().getResourceAsStream("/unixScript");
      charSource = new CharSource() {
        @Override
        public Reader openStream() {
          return new BufferedReader(new InputStreamReader(in));
        }
      };
    } else {
      charSource = new CharSource() {
        @Override
        public Reader openStream() throws IOException {
          return new FileReader(Paths.get(script).toFile());
        }
      };
    }

    String scriptName = getScriptName().getOrElse(project.getName());
    final File outFile = getOutputDir().file(scriptName).get().getAsFile();
    outFile.setExecutable(true);
    outFile.setReadable(true);
    try (OutputStream out = new FileOutputStream(outFile)) {
      out.write(String.join(System.lineSeparator(), charSource.readLines()).getBytes());
      final File jar = getJar().get().getAsFile();
      out.write(System.lineSeparator().getBytes());
      out.write(Files.readAllBytes(jar.toPath()));
      out.flush();
    }
  }
}
