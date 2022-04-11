package com.mysema.maven.apt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileSyncTest {

  @Test
  public void sync1 () throws IOException {

    File source = Files.createTempDir();
    File sourceFile = new File(source, "inSource");
    sourceFile.createNewFile();
    File sourceFolder = new File(source, "inSourceFolder");
    sourceFolder.mkdir();
    File sourceFile2 = new File(sourceFolder, "inSource");
    sourceFile2.createNewFile();

    File target = Files.createTempDir();
    File targetFile = new File(target, "inTarget");
    targetFile.createNewFile();

    FileSync.syncFiles(source, target);
    assertTrue(new File(target, "inSource").exists());
    assertTrue(new File(target, "inSourceFolder" + File.separator + "inSource").exists());
    assertFalse(targetFile.exists());

    FileSync.syncFiles(source, target);
  }

  @Test
  public void sync2 () throws IOException {

    File source = Files.createTempDir();
    File sourceFile = new File(source, "file");
    Files.write("abc", sourceFile, Charsets.UTF_8);
    File target = Files.createTempDir();
    File targetFile = new File(target, "file");
    Files.write("abc", targetFile, Charsets.UTF_8);
    long modified = targetFile.lastModified();

    FileSync.syncFiles(source, target);
    assertEquals(modified, targetFile.lastModified());
  }

  @Test
  public void sync3 () throws IOException {

    File source = Files.createTempDir();
    File target = Files.createTempDir();
    File sourceFile1 = Paths.get(source.getAbsolutePath(), "com", "mysema", "querydsl", "Query.java").toFile();
    File sourceFile2 = Paths.get(source.getAbsolutePath(), "com", "mysema", "Entity.java").toFile();
    File targetFile1 = Paths.get(target.getAbsolutePath(), "com", "mysema", "querydsl", "OldQuery.java").toFile();
    File targetFile2 = Paths.get(target.getAbsolutePath(), "com", "mysema", "querydsl", "support", "Example.java").toFile();
    sourceFile1.getParentFile().mkdirs();
    sourceFile2.getParentFile().mkdirs();
    targetFile1.getParentFile().mkdirs();
    targetFile2.getParentFile().mkdirs();
    Files.write("abc", sourceFile1, Charsets.UTF_8);
    Files.write("def", sourceFile2, Charsets.UTF_8);
    Files.write("ghi", targetFile1, Charsets.UTF_8);
    Files.write("jkl", targetFile2, Charsets.UTF_8);

    FileSync.syncFiles(source, target);
    assertFalse(targetFile1.exists());
    assertFalse(targetFile2.exists());
    assertFalse(targetFile2.getParentFile().exists());
    assertTrue(Paths.get(target.getAbsolutePath(), "com", "mysema", "querydsl", "Query.java").toFile().exists());
    assertTrue(Paths.get(target.getAbsolutePath(), "com", "mysema", "Entity.java").toFile().exists());
  }
}
