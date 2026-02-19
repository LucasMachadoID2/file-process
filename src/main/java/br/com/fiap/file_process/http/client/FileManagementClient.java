package br.com.fiap.file_process.http.client;

public interface FileManagementClient {

    void updateVideoStatus(String videoId, String status, String url);
}