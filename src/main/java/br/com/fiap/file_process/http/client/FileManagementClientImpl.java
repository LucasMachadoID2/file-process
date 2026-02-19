package br.com.fiap.file_process.http.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FileManagementClientImpl implements FileManagementClient {

    private final RestTemplate restTemplate;

    @Value("${client.file-management.url}")
    private String fileManagementUrl;

    @Value("${client.file-management.integration-key.value}")
    private String integrationKey;

    public FileManagementClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void updateVideoStatus(String videoId, String status, String urlS3) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("integration-name", "FILE_PROCESS_INTEGRATION");
        headers.set("integration-key", integrationKey);
        headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);

        HttpEntity<String> entity = new HttpEntity<>(urlS3, headers);

        String url = UriComponentsBuilder.fromUriString(
                        fileManagementUrl + "/v1/files/update-status/" + videoId
                )
                .queryParam("status", status)
                .toUriString();

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw e;
        }
    }
}
