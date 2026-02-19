package br.com.fiap.file_process.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3StorageServiceTest {

    @Mock
    private S3Client s3Client;

    private S3StorageService service;

    @BeforeEach
    void setup() {
        service = new S3StorageService(s3Client);

        // injeta bucket manualmente (sem Spring)
        TestUtils.setField(service, "bucketName", "meu-bucket");
    }

    @Test
    void shouldUploadZipWithSanitizedEmail() throws Exception {

        File temp = File.createTempFile("test", ".zip");

        String url = service.uploadZip(temp, "user+test@email.com", "123");

        ArgumentCaptor<PutObjectRequest> captor =
                ArgumentCaptor.forClass(PutObjectRequest.class);

        verify(s3Client).putObject(captor.capture(), eq(temp.toPath()));

        PutObjectRequest request = captor.getValue();

        assertEquals("meu-bucket", request.bucket());
        assertTrue(request.key().contains("123"));
        assertEquals("application/zip", request.contentType());

        assertTrue(url.contains("meu-bucket"));
    }

    @Test
    void shouldUploadZipRabbit() throws Exception {

        File temp = File.createTempFile("test", ".zip");

        String url = service.uploadZipRabbit(temp, "email@test.com", "999");

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), eq(temp.toPath()));

        assertTrue(url.contains("999"));
        assertTrue(url.contains("email@test.com"));
    }
}
