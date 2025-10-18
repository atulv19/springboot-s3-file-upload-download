package com.s3example.controller;

import com.s3example.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // 📤 Upload API
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String response = s3Service.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    // 📥 Download API
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName) {
        byte[] data = s3Service.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}

