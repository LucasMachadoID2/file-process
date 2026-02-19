package br.com.fiap.file_process.http.client.dto;

public class FileUpdateRequest {

    private String status;

    private String link;

    public FileUpdateRequest(String status, String link) {
        this.status = status;
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
