package com.s3example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String BucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        System.out.println("✅ File uploaded successfully: " + fileName);
        return fileName;
    }


    public byte[] downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        try {
            return s3Object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading file", e);
        }
    }

    // 🧾 Helper: Convert MultipartFile to File
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
