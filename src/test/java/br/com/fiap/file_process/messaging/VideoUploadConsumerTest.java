package br.com.fiap.file_process.messaging;

import br.com.fiap.file_process.service.VideoProcessingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoUploadConsumerTest {

    @Test
    void shouldConsumeMessageSuccessfully() throws Exception {

        VideoProcessingService service = mock(VideoProcessingService.class);
        VideoUploadConsumer consumer = new VideoUploadConsumer(service);

        byte[] bytes = new byte[]{1, 2, 3};
        String base64 = Base64.getEncoder().encodeToString(bytes);

        String json = """
            {
              "videoId": "123",
              "email": "user@test.com",
              "videoBase64": "%s"
            }
            """.formatted(base64);

        consumer.consume(json);

        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);

        verify(service).processVideoAndUploadRabbit(
                captor.capture(),
                eq("user@test.com"),
                eq("123")
        );

        assertArrayEquals(bytes, captor.getValue());
    }

    @Test
    void shouldHandleInvalidMessageGracefully() {

        VideoProcessingService service = mock(VideoProcessingService.class);
        VideoUploadConsumer consumer = new VideoUploadConsumer(service);

        String invalidJson = "mensagem totalmente inválida";

        consumer.consume(invalidJson);

        verifyNoInteractions(service);
    }
}
