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
    private final EmailService emailService;

    public VideoProcessingService(S3StorageService s3StorageService, FileManagementClient fileManagementClient,EmailService emailService) {
        this.s3StorageService = s3StorageService;
        this.fileManagementClient = fileManagementClient;
        this.emailService = emailService;
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

        File tempVideo = null;
        File framesDir = null;
        File zip = null;

        try {
            tempVideo = File.createTempFile("video-rabbit-", ".mp4");
            Files.write(tempVideo.toPath(), videoBytes);

            framesDir = new File(System.getProperty("java.io.tmpdir") + "/fiap_frames_" + videoId);
            framesDir.mkdirs();

            VideoFrameExtractor.extractFrames(tempVideo, framesDir);

            zip = File.createTempFile("frames-rabbit-", ".zip");
            ZipUtil.zipDirectory(framesDir, zip);
            fileManagementClient.updateVideoStatus(videoId, "FINISHED");
            return s3StorageService.uploadZipRabbit(zip, email, videoId);

        } catch (Exception e) {
            handleError(email, videoId, e);
            throw e;
        } finally {
            cleanup(tempVideo, framesDir, zip);
        }
    }

    private void handleError(String email, String videoId, Exception e) {
        System.err.println("Erro técnico no processamento: " + e.getMessage());
        emailService.sendErrorEmail(email, videoId, e.getMessage());
    }

    private void cleanup(File video, File dir, File zip) {
        if (video != null && video.exists()) video.delete();
        if (zip != null && zip.exists()) zip.delete();
        if (dir != null && dir.exists()) {
            deleteDirectoryRecursively(dir);
        }
    }

    private void deleteDirectoryRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursively(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}

