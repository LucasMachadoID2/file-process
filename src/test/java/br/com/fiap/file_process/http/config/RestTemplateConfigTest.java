package br.com.fiap.file_process.http.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class RestTemplateConfigTest {

    @Test
    void shouldCreateRestTemplateBean() {

        RestTemplateConfig config = new RestTemplateConfig();
        RestTemplateBuilder builder = new RestTemplateBuilder();

        RestTemplate restTemplate = config.restTemplate(builder);

        assertNotNull(restTemplate);
    }
}
