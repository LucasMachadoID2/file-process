package br.com.fiap.file_process.dto;

import br.com.fiap.file_process.messaging.dto.VideoUploadMessage;

import br.com.fiap.file_process.service.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoUploadMessageTest {

    @Test
    void shouldReturnValuesFromGetters() {

        VideoUploadMessage message = new VideoUploadMessage();

        TestUtils.setField(message, "videoId", "123");
        TestUtils.setField(message, "email", "user@test.com");
        TestUtils.setField(message, "videoBase64", "BASE64_CONTENT");

        assertEquals("123", message.getVideoId());
        assertEquals("user@test.com", message.getEmail());
        assertEquals("BASE64_CONTENT", message.getVideoBase64());
    }

    @Test
    void shouldReturnNullWhenFieldsNotInitialized() {

        VideoUploadMessage message = new VideoUploadMessage();

        assertNull(message.getVideoId());
        assertNull(message.getEmail());
        assertNull(message.getVideoBase64());
    }
}


