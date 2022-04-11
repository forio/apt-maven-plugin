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

@Mojo(name = "test-process", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST)
public class TestAnnotationProcessorMojo extends AbstractProcessorMojo {

  @Parameter
  private File outputDirectory;
  @Parameter
  private File testOutputDirectory;

  @Override
  public File getOutputDirectory () {

    return testOutputDirectory != null ? testOutputDirectory : outputDirectory;
  }

  public void setOutputDirectory (File outputDirectory) {

    this.outputDirectory = outputDirectory;
  }

  @Override
  protected boolean isForTest () {

    return true;
  }

  public void setTestOutputDirectory (File testOutputDirectory) {

    this.testOutputDirectory = testOutputDirectory;
  }
}
