package dev.nitin.server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    // Optional: absolute path to your service account JSON (set in properties/env)
    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    // Your Firebase Storage bucket name (set in properties/env)
    @Value("${firebase.bucket-name:}")
    private String bucketName;


    /**
     * Creates a Google Cloud Storage client configured with the Firebase service account.
     * The credentialsPath property (absolute file path) is checked first; if not set, we look
     * for firebase-service-account.json on the classpath (src/main/resources).
     */
    @Bean
    public Storage googleStorage() throws IOException {
        GoogleCredentials credentials;
        if (credentialsPath != null && !credentialsPath.isEmpty()) {
            try (FileInputStream fis = new FileInputStream(credentialsPath)) {
                credentials = GoogleCredentials.fromStream(fis);
            }
        } else {
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("firebase-service-account.json")) {
                if (is == null) {
                    throw new IOException("Firebase credentials not found. Set firebase.credentials.path or add firebase-service-account.json to resources.");
                }
                credentials = GoogleCredentials.fromStream(is);
            }
        }
        System.out.println(">>> FirebaseConfig initializing...");
        System.out.println(">>> Bucket name: " + bucketName);

        // Initialize FirebaseApp only once
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setStorageBucket(bucketName) // default bucket
                    .build();
            FirebaseApp.initializeApp(options);
        }
        System.out.println(">>> Firebase initialized with app name: " + FirebaseApp.getInstance().getName());
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();

            // Try to fetch the bucket info to confirm connection
            if (bucketName != null && !bucketName.isEmpty()) {
                var bucket = storage.get(bucketName);
                if (bucket != null) {
                    System.out.println("✅ Firebase Storage connected successfully to bucket: " + bucketName);
                } else {
                    System.out.println("⚠️ Firebase initialized, but bucket not found or access denied: " + bucketName);
                }
            } else {
                System.out.println("⚠️ Firebase initialized, but bucket name not configured!");
            }
        } catch (Exception e) {
            System.out.println("❌ Firebase connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Return a Storage client that uses the same credentials
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

    @Bean
    public String firebaseBucketName() {
        return bucketName;
    }



}
