package br.com.fiap.file_process.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.fiap.file_process.service.VideoProcessingService;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoProcessingService service;

    public VideoController(VideoProcessingService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
        @RequestParam MultipartFile video,
        @RequestParam String email,
        @RequestParam String videoId
        ) throws Exception {

        String s3Url = service.processVideoAndUpload(video, email, videoId);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Frames processados e enviados com sucesso",
                        "url", s3Url
                )
        );
    }
}
