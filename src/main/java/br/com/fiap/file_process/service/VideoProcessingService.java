package br.com.fiap.file_process.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

import br.com.fiap.file_process.util.VideoFrameExtractor;
import br.com.fiap.file_process.util.ZipUtil;

@Service
public class VideoProcessingService {

    private final S3StorageService s3StorageService;

    public VideoProcessingService(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    public String processVideoAndUpload(MultipartFile video, String email, String videoId) throws Exception {

        File tempVideo = File.createTempFile("video", ".mp4");
        video.transferTo(tempVideo);

        File framesDir = Files.createTempDirectory("frames").toFile();
        VideoFrameExtractor.extractFrames(tempVideo, framesDir);

        File zip = File.createTempFile("frames", ".zip");
        ZipUtil.zipDirectory(framesDir, zip);

        // Upload para o S3
        return s3StorageService.uploadZip(zip, email, videoId);
    }


    public String processVideoAndUploadRabbit(byte[] videoBytes, String email, String videoId) throws Exception {

        File tempVideo = File.createTempFile("video-", ".mp4");
        Files.write(tempVideo.toPath(), videoBytes);

        File framesDir = new File(
            System.getProperty("java.io.tmpdir") +
            "/" + email + "/" + videoId
        );
        framesDir.mkdirs();

        VideoFrameExtractor.extractFrames(tempVideo, framesDir);

        File zip = File.createTempFile("frames-", ".zip");
        ZipUtil.zipDirectory(framesDir, zip);

        return s3StorageService.uploadZipRabbit(zip, email, videoId);
    }
}
