package com.example.testapi.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3SignedUrlService {
    
    @Value("${aws.s3.bucketName}")
    private String bucketName; 

    private final AmazonS3 s3Client;

    public String generatePutSignedUrl(String key) {
        return s3Client
                    .generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(new Date(System.currentTimeMillis() + 120000))).toString();
    }

    public String generateGetSignedUrl(String key) {
        return s3Client
                    .generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(new Date(System.currentTimeMillis() + 120000))).toString();
    }

    public String generateDeleteSignedUrl(String key) {
        return s3Client
                    .generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.DELETE)
                    .withExpiration(new Date(System.currentTimeMillis() + 120000))).toString();
    }
}
