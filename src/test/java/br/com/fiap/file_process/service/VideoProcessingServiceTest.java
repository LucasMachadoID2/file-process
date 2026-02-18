package br.com.fiap.file_process.service;

import br.com.fiap.file_process.http.client.FileManagementClient;
import br.com.fiap.file_process.util.VideoFrameExtractor;
import br.com.fiap.file_process.util.ZipUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessingServiceTest {

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private FileManagementClient fileManagementClient;

    @Mock
    private EmailService emailService;

    @Test
    void shouldProcessVideoSuccessfully() throws Exception {

        VideoProcessingService service =
                new VideoProcessingService(s3StorageService, fileManagementClient, emailService);

        when(s3StorageService.uploadZipRabbit(any(), any(), any()))
                .thenReturn("http://zip-url");

        try (MockedStatic<VideoFrameExtractor> extractor = mockStatic(VideoFrameExtractor.class);
             MockedStatic<ZipUtil> zipUtil = mockStatic(ZipUtil.class)) {

            service.processVideoAndUploadRabbit(new byte[]{1, 2}, "email@test.com", "321");

            verify(s3StorageService).uploadZipRabbit(any(File.class), eq("email@test.com"), eq("321"));
            verify(fileManagementClient).updateVideoStatus("321", "FINISHED", "http://zip-url");
        }
    }

    @Test
    void shouldSendEmailWhenFailureOccurs() throws Exception {

        VideoProcessingService service =
                new VideoProcessingService(s3StorageService, fileManagementClient, emailService);

        try (MockedStatic<VideoFrameExtractor> extractor = mockStatic(VideoFrameExtractor.class)) {

            extractor.when(() -> VideoFrameExtractor.extractFrames(any(), any()))
                    .thenThrow(new RuntimeException("erro"));

            try {
                service.processVideoAndUploadRabbit(new byte[]{1}, "user@test.com", "999");
            } catch (Exception ignored) {}

            verify(emailService).sendErrorEmail(eq("user@test.com"), eq("999"), any());
        }
    }


    @Test
    void shouldDeleteRegularFilesInsideDirectory() throws Exception {

        VideoProcessingService service =
                new VideoProcessingService(s3StorageService, fileManagementClient, emailService);

        File rootDir = new File(System.getProperty("java.io.tmpdir"), "test_dir_files");
        rootDir.mkdirs();

        File file = new File(rootDir, "file.txt");
        file.createNewFile();

        // chamar cleanup via reflexão (método privado)
        TestUtils.invokeMethod(service, "deleteDirectoryRecursively", rootDir);

        assertFalse(rootDir.exists());
        assertFalse(file.exists());
    }

    @Test
    void shouldDeleteNestedDirectoriesRecursively() throws Exception {

        VideoProcessingService service =
                new VideoProcessingService(s3StorageService, fileManagementClient, emailService);

        File rootDir = new File(System.getProperty("java.io.tmpdir"), "test_dir_nested");
        File nestedDir = new File(rootDir, "nested");

        nestedDir.mkdirs();

        File nestedFile = new File(nestedDir, "inner.txt");
        nestedFile.createNewFile();

        TestUtils.invokeMethod(service, "deleteDirectoryRecursively", rootDir);

        assertFalse(rootDir.exists());
        assertFalse(nestedDir.exists());
        assertFalse(nestedFile.exists());
    }


}
