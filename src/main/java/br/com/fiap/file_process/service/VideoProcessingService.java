package br.com.fiap.file_process.service;

import br.com.fiap.file_process.http.client.FileManagementClient;
import br.com.fiap.file_process.util.VideoFrameExtractor;
import br.com.fiap.file_process.util.ZipUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class VideoProcessingService {

    private final S3StorageService s3StorageService;
    private final FileManagementClient fileManagementClient;

    public VideoProcessingService(S3StorageService s3StorageService, FileManagementClient fileManagementClient) {
        this.s3StorageService = s3StorageService;
        this.fileManagementClient = fileManagementClient;
    }

    public void processVideoAndUploadRabbit(byte[] videoBytes, String email, String videoId) throws Exception {

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

        s3StorageService.uploadZipRabbit(zip, email, videoId);

        fileManagementClient.updateVideoStatus(videoId, "FINISHED");
    }
}
