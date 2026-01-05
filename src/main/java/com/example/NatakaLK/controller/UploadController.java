package com.example.NatakaLK.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.core.sync.RequestBody;

@RestController
@RequestMapping("api/v1/upload")
@CrossOrigin
public class UploadController {
    private final S3Client s3Client;
    private final String CLOUDFRONT_URL = "https://d3ay14vkclriu.cloudfront.net/";

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // Constructor Injection
    public UploadController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PreAuthorize("hasAnyRole('Admin','Customer','TheatreManager')")
    @PostMapping("")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam(value = "title", required = false) String title
    ) {
        try {
            // Sanitize the filename and path
            String safeCategory = category.toLowerCase().replaceAll("[^a-z0-9]", "");

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String safeTitle = (title != null) ? title.toLowerCase().replaceAll("[^a-z0-9]", "_") : "file";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // This is the "Key" (Path) in S3.
            String fileName = "uploads/" + safeCategory + "/" + safeTitle + "_" + timestamp + extension;
            String fileUrl = CLOUDFRONT_URL + fileName;

            // Create the Upload Request
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // Upload the file to S3
            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("filePath", fileUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "S3 Upload failed: " + e.getMessage()));
        }
    }

//    private final String UPLOAD_DIR = "uploads/";
//
//    @PreAuthorize("hasAnyRole('Admin','Customer','TheatreManager')")
//    @PostMapping("")
//    public ResponseEntity<Map<String, String>> uploadFile(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("category") String category,
//            @RequestParam(value = "title", required = false) String title
//    ) {
//        try {
//            // Sanitize category and create category folder if not exists
//            String safeCategory = category.toLowerCase().replaceAll("[^a-z0-9]", "");
//            String uploadDir = UPLOAD_DIR + safeCategory + "/";
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // Build filename: title_timestamp.extension
//            String extension = "";
//            String originalName = file.getOriginalFilename();
//            if (originalName != null && originalName.contains(".")) {
//                extension = originalName.substring(originalName.lastIndexOf("."));
//            }
//
//            String safeTitle = (title != null) ? title.toLowerCase().replaceAll("[^a-z0-9]", "_") : "file";
//            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//            String filename = safeTitle + "_" + timestamp + extension;
//
//            Path filePath = Paths.get(uploadDir + filename);
//            Files.write(filePath, file.getBytes());
//
//            // Return relative path (e.g., dramas/rathriya_20250708.jpg)
//            Map<String, String> response = new HashMap<>();
//            response.put("fileName",  filename);
//            return ResponseEntity.ok(response);
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Upload failed"));
//        }
//    }
}
