package dev.nitin.server.service.fileStorage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

//@Service
//@Profile("dev")
public class LocalFileStorageService implements FileStorageService {
    private final Path root = Paths.get("uploads");

    @Override
    public String saveFile(MultipartFile file) {
        try{
            // Always resolve uploads folder relative to the project directory
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");

            // Create the uploads directory if it doesn't exist
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            //Create a Unique file name to avoid overwriting
            String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String uniqueFileName =  UUID.randomUUID() + "_" + System.currentTimeMillis() + extension;

            // Save the file inside that directory
            Path filePath = uploadDir.resolve(uniqueFileName);
            file.transferTo(filePath.toFile());
            return "/uploads/" + uniqueFileName;
        }catch (IOException e){
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename()+ e.getMessage());
        }
    }

}
