package com.example.testapi.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.testapi.exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName; //your bucket name

    public String uploadImage(String key, MultipartFile image) {
        log.info("Starting S3 Upload process");

        ObjectMetadata objectMetadata = new ObjectMetadata();
        String contentType = image.getContentType();

        if (contentType != null && contentType.equals("application/octet-stream")) {
            log.info("Content type found: " + contentType);
            contentType = "image/jpeg";
        }

        log.info("Checking content type: "  + contentType);
        if (!isValidContentType(contentType)) {
            log.warn("Invalid content type: " + contentType);
            throw new CustomException("Unsupported content type\n\n");
        }

        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(image.getSize());

        try (InputStream inputStream = image.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key + "/" + image.getOriginalFilename(), inputStream, objectMetadata);
            s3Client.putObject(putObjectRequest);
            log.info("Image uploaded to S3: " + image.getOriginalFilename());
        } catch (IOException e) {
            log.error("Error reading the image\n\n", e);
            throw new CustomException("Error reading or uploading the image\n\n");
        }

        return s3Client.getUrl(bucketName, key + "/" + image.getOriginalFilename()).toExternalForm();
    }

    private boolean isValidContentType(String contentType) {
        // Add your list of supported content types here.
        return Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg", "image/jfif", "image/bmp", "image/tiff", "image/webp", "image/svg").contains(contentType);
    }


    public String getUrl(String key) {
        return s3Client.getUrl(bucketName, key).toString();
    }

    public void deleteImage(String key) {
        s3Client.deleteObject(bucketName, key);
        log.info("Image deleted from S3");
    }
}

