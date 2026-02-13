package br.com.fiap.file_process.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static void zipDirectory(File sourceDir, File zipFile) throws IOException {

        try (ZipOutputStream zos =
                     new ZipOutputStream(new FileOutputStream(zipFile))) {

            File[] files = sourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                zos.putNextEntry(new ZipEntry(file.getName()));
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
                }
            }
        }
    }
}

