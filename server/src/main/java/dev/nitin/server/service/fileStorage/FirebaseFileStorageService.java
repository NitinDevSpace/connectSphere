package dev.nitin.server.service.fileStorage;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("dev")
@Primary
public class FirebaseFileStorageService implements FileStorageService {
    private final Storage storage;
    private final String bucketName;

    public FirebaseFileStorageService(Storage storage, @Value("${firebase.bucket-name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    @Override
    public String saveFile(MultipartFile file) {
        try {
            System.out.println(">>> Starting upload to Firebase Storage");
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            // make filename safe and keep extension
            String cleaned = original.replaceAll("[^a-zA-Z0-9._-]", "_");
            String ext = "";
            int idx = cleaned.lastIndexOf('.');
            if (idx > 0) {
                ext = cleaned.substring(idx);
            }
            String uniqueName =  UUID.randomUUID() + ext;
            String objectName = "uploads/" + uniqueName;

            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                    .build();

            System.out.println(">>> Uploading to bucket: " + bucketName + " path: " + objectName);
            // Upload the file
            storage.createFrom(blobInfo, file.getInputStream());

            Blob blob = storage.get(blobInfo.getBlobId());

            if (blob == null) {
                System.out.println("❌ Upload failed — Blob is null (not stored)");
            } else {
                System.out.println("✅ Upload success — Blob path: " + blob.getBlobId().getName());
                System.out.println("✅ Blob storage link: " + blob.getMediaLink());
            }
            // OPTION A: make the file publicly readable (dev/testing)
            // Use with care in prod; better to sign URLs for controlled access.
            storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
//            blob.toBuilder().setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))).build().update();
            // Public URL format
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Firebase Storage", e);
        }
    }
}
