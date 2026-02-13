package br.com.fiap.file_process.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessingServiceTest {

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VideoProcessingService videoProcessingService;

    @Test
    @DisplayName("Deve enviar e-mail quando ocorrer erro no processamento do Rabbit")
    void deveEnviarEmailQuandoProcessamentoFalhar() throws Exception {

        byte[] videoBytes = new byte[0];
        String email = "usuario@teste.com";
        String videoId = "12345";

        assertThrows(Exception.class, () -> {
            videoProcessingService.processVideoAndUploadRabbit(videoBytes, email, videoId);
        });

        verify(emailService, times(1)).sendErrorEmail(
                eq(email),
                eq(videoId),
                anyString()
        );
    }
}
