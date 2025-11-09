package dev.nitin.server.service.fileStorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3FileStorageService implements FileStorageService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;
    private final String region;

    public S3FileStorageService(S3Client s3Client, @Value("${aws.region}") String region) {
        this.s3Client = s3Client;
        this.region = region;
    }

    @Override
    public String saveFile(MultipartFile file) {
        try {
            String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String uniqueFileName = UUID.randomUUID() + "_" + System.currentTimeMillis() + extension;
            String key = "uploads/" + uniqueFileName;

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage());
        }
    }
}