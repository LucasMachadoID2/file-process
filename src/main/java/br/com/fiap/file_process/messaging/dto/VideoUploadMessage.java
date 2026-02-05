package br.com.fiap.file_process.messaging.dto;

public class VideoUploadMessage {

    private String videoId;
    private String email;
    private String videoBase64;

    public String getVideoId() {
        return videoId;
    }

    public String getEmail() {
        return email;
    }

    public String getVideoBase64() {
        return videoBase64;
    }
}
