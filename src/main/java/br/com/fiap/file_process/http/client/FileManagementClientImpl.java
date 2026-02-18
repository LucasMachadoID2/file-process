package br.com.fiap.file_process.http.client;

import br.com.fiap.file_process.http.client.dto.FileUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
    public void updateFileManagement(String videoId, FileUpdateRequest fileUpdateRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("integration-name", "FILE_PROCESS_INTEGRATION");
        headers.set("integration-key", integrationKey);

        HttpEntity<FileUpdateRequest> entity = new HttpEntity<>(fileUpdateRequest, headers);

        try {
            restTemplate.exchange(
                    fileManagementUrl + "/v1/files/post-process-file/" + videoId,
                    HttpMethod.PATCH,
                    entity,
                    Void.class
            );
        } catch (Exception e) {
            throw e;
        }
    }
}
