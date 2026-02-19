package br.com.fiap.file_process.http.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import br.com.fiap.file_process.service.TestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileManagementClientImplTest {

    private RestTemplate restTemplate;
    private FileManagementClientImpl client;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        client = new FileManagementClientImpl(restTemplate);

        TestUtils.setField(client, "fileManagementUrl", "http://file-mgmt");
        TestUtils.setField(client, "integrationKey", "secret-key");
    }


    @Test
    void shouldRethrowNotFoundException() {

        doThrow(HttpClientErrorException.NotFound.class)
                .when(restTemplate)
                .exchange(anyString(), any(), any(), eq(Void.class));

        assertThrows(HttpClientErrorException.NotFound.class, () ->
                client.updateVideoStatus("123", "FINISHED", "url")
        );
    }
}
