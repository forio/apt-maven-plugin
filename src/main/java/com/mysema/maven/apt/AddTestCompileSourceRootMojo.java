/*
 * Copyright (c) 2012 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.maven.apt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "add-test-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class AddTestCompileSourceRootMojo extends AbstractMojo {

  @Parameter(readonly = true, property = "project")
  private MavenProject project;
  @Parameter
  private String outputDirectory;
  @Parameter
  private String testOutputDirectory;

  @Override
  public void execute ()
    throws MojoExecutionException {

    String directory = testOutputDirectory != null ? testOutputDirectory : outputDirectory;
    Path path = Paths.get(project.getBuild().getDirectory(), directory);

    try {
      if (!Files.exists(path)) {
        Files.createDirectories(path);
      }
    } catch (IOException ioException) {
      throw new MojoExecutionException(ioException);
    }

    project.addTestCompileSourceRoot(path.toAbsolutePath().toString());
  }

  public void setProject (MavenProject project) {

    this.project = project;
  }

  public void setOutputDirectory (String outputDirectory) {

    this.outputDirectory = outputDirectory;
  }

  public void setTestOutputDirectory (String testOutputDirectory) {

    this.testOutputDirectory = testOutputDirectory;
  }
}
