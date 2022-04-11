/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.maven.apt;

import java.io.File;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "process", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AnnotationProcessorMojo extends AbstractProcessorMojo {

  @Parameter
  private File outputDirectory;

  @Override
  public File getOutputDirectory () {

    return outputDirectory;
  }

  public void setOutputDirectory (File outputDirectory) {

    this.outputDirectory = outputDirectory;
  }
}
