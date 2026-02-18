package br.com.fiap.file_process.http.client;

import br.com.fiap.file_process.http.client.dto.FileUpdateRequest;

public interface FileManagementClient {

    void updateFileManagement(String videoId,  FileUpdateRequest fileUpdateRequest);
}
