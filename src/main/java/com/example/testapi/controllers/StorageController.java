package com.example.testapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.ResponseWrapper;
import com.example.testapi.services.S3SignedUrlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class StorageController {

    private final S3SignedUrlService s3SignedUrlService;
 
    @PostMapping("/generate-get-url/{username}")
    public ResponseEntity<ResponseWrapper<String>> generateGetPresignedUrlRequest(
            @PathVariable String username ) {
        log.info("Generating GET url for key");
        return ResponseEntity.ok().body(new ResponseWrapper<>(s3SignedUrlService.generateGetSignedUrl(username)));
    }

    @PostMapping("/generate-put-url/{username}")
    public ResponseEntity<ResponseWrapper<String>> generatePutPresignedUrlRequest(
            @PathVariable String username ) {
        return ResponseEntity.ok().body(new ResponseWrapper<>(s3SignedUrlService.generatePutSignedUrl(username)));
    }

    @PostMapping("/generate-delete-url/{username}")
    public ResponseEntity<ResponseWrapper<String>> generateDeletePresignedUrlRequest(
            @PathVariable String username ) {
        return ResponseEntity.ok().body(new ResponseWrapper<>(s3SignedUrlService.generateDeleteSignedUrl(username)));
    }

}
