package com.mysema.maven.apt;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.querydsl.apt.QuerydslAnnotationProcessor;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.sonatype.plexus.build.incremental.DefaultBuildContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnnotationProcessorMojoTest {

  private File outputDir;

  private MavenProject project;

  private AnnotationProcessorMojo mojo;

  @Before
  public void setUp ()
    throws Exception {

    outputDir = new File("target/generated-sources/java");
//    Log log = EasyMock.createMock(Log.class);
    BuildContext buildContext = new DefaultBuildContext();
//    project = EasyMock.createMock(MavenProject.class);
    List sourceRoots = Lists.newArrayList("src/test/resources/project-to-test/src/main/java", "notExisting/sourceRoot/folder");
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
//    List classpath = ClassPathUtils.getClassPath(loader);
//    EasyMock.expect(project.getCompileSourceRoots()).andReturn(sourceRoots);
//    EasyMock.expect(project.getCompileSourceRoots()).andReturn(sourceRoots);
//    EasyMock.expect(project.getCompileClasspathElements()).andReturn(classpath);
    project.addCompileSourceRoot(outputDir.getAbsolutePath());
//    EasyMock.expectLastCall();
//    EasyMock.replay(project);

    mojo = new AnnotationProcessorMojo();
    mojo.setBuildContext(buildContext);
    mojo.setCompilerOptions(Maps.<String, String>newHashMap());
    mojo.setIncludes(Sets.<String>newHashSet());
//    mojo.setLog(log);
    mojo.setLogOnlyOnError(false);
    mojo.setOptions(Maps.<String, String>newHashMap());
    mojo.setProcessor(QuerydslAnnotationProcessor.class.getName());
    mojo.setProject(project);
    mojo.setSourceEncoding("UTF-8");
    mojo.setOutputDirectory(outputDir);
  }

  @After
  public void tearDown () throws IOException {

    Files.delete(Paths.get(outputDir.getAbsolutePath()));
    System.setProperty("maven.apt.skip", "");
  }

  @Test
  public void Execute () throws MojoExecutionException {

    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/QEntity.java").exists());
  }

  @Test
  public void Skip () throws MojoExecutionException {

    System.setProperty("maven.apt.skip", "true");
    mojo.execute();
    assertFalse(new File(outputDir, "com/example/QEntity.java").exists());
  }

  @Test
  public void Processors () throws MojoExecutionException {

    mojo.setProcessor(null);
    mojo.setProcessors(new String[] {QuerydslAnnotationProcessor.class.getName()});
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/QEntity.java").exists());
  }

  @Test
  public void Includes () throws MojoExecutionException {

    mojo.setIncludes(Sets.newHashSet("com/example/**"));
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/QEntity.java").exists());
  }

  @Test
  public void Options () throws MojoExecutionException {

    mojo.setOptions(Collections.singletonMap("querydsl.packageSuffix", ".query"));
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/query/QEntity.java").exists());
  }

  @Test
  @Ignore
  public void NullOptions () throws MojoExecutionException {

    Map<String, String> options = Maps.newHashMap();
    options.put("querydsl.packageSuffix", ".query");
    options.put("querydsl.prefix", null);
    mojo.setOptions(options);
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/query/Entity.java").exists());
  }

  @Test
  public void NoSources () throws MojoExecutionException {

    mojo.setIncludes(Sets.newHashSet("xxx"));
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(outputDir.list() == null || outputDir.list().length == 0);
  }

  @Test
  public void LogOnlyOnError () throws MojoExecutionException {

    mojo.setLogOnlyOnError(true);
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/QEntity.java").exists());
  }

  @Test
  public void Artifacts () throws MojoExecutionException {

    DefaultArtifact artifact = new DefaultArtifact("a", "b", VersionRange.createFromVersion("0.1"),
      "compile", "jar", "", null);
    artifact.setFile(new File("target/classes"));
    mojo.setPluginArtifacts(Lists.<Artifact>newArrayList(artifact));
    mojo.execute();
//    EasyMock.verify(project);
    assertTrue(new File(outputDir, "com/example/QEntity.java").exists());
  }
}
