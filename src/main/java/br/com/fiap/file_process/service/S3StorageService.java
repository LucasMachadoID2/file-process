package br.com.fiap.file_process.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadZip(File zipFile, String email, String videoId) {


        String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9@._-]", "_");

        String key = String.format(
            "%s/%s/frames.zip",
            sanitizedEmail,
            videoId
        );

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/zip")
                .build();

        s3Client.putObject(request, zipFile.toPath());

        return getFileUrl(key);
    }

    private String getFileUrl(String key) {
        return String.format(
                "https://%s.s3.amazonaws.com/%s",
                bucketName,
                key
        );
    }


    //COM RABBITMQ
    public String uploadZipRabbit(File zipFile, String email, String videoId) {

        String key = email + "/" + videoId + "/frames.zip";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/zip")
                .build();

        s3Client.putObject(request, zipFile.toPath());

        return getFileUrl(key);
    }

}
