package com.example.NatakaLK.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/upload")
@CrossOrigin
public class UploadController {
    private final String UPLOAD_DIR = "uploads/";

    @PreAuthorize("hasAnyRole('Admin','Customer','TheatreManager')")
    @PostMapping("")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam(value = "title", required = false) String title
    ) {
        try {
            // Sanitize category and create category folder if not exists
            String safeCategory = category.toLowerCase().replaceAll("[^a-z0-9]", "");
            String uploadDir = UPLOAD_DIR + safeCategory + "/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Build filename: title_timestamp.extension
            String extension = "";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String safeTitle = (title != null) ? title.toLowerCase().replaceAll("[^a-z0-9]", "_") : "file";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = safeTitle + "_" + timestamp + extension;

            Path filePath = Paths.get(uploadDir + filename);
            Files.write(filePath, file.getBytes());

            // Return relative path (e.g., dramas/rathriya_20250708.jpg)
            Map<String, String> response = new HashMap<>();
            response.put("fileName",  filename);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed"));
        }
    }
}
