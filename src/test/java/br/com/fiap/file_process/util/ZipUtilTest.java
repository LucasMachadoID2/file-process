package br.com.fiap.file_process.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilTest {

    @Test
    void shouldZipDirectoryWithFiles() throws Exception {

        File dir = new File(System.getProperty("java.io.tmpdir"), "zip_test_dir");
        dir.mkdirs();

        File file = new File(dir, "test.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("hello");
        }

        File zip = File.createTempFile("test", ".zip");

        ZipUtil.zipDirectory(dir, zip);

        assertTrue(zip.exists());
        assertTrue(zip.length() > 0);

        try (ZipFile zipFile = new ZipFile(zip)) {
            assertNotNull(zipFile.getEntry("test.txt"));
        }

        file.delete();
        dir.delete();
        zip.delete();
    }

    @Test
    void shouldZipEmptyDirectory() throws Exception {

        File dir = new File(System.getProperty("java.io.tmpdir"), "zip_empty_dir");
        dir.mkdirs();

        File zip = File.createTempFile("empty", ".zip");

        ZipUtil.zipDirectory(dir, zip);

        assertTrue(zip.exists());

        try (ZipFile zipFile = new ZipFile(zip)) {
            assertEquals(0, zipFile.size());
        }

        dir.delete();
        zip.delete();
    }
}
