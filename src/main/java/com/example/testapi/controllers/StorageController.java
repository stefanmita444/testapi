package com.example.testapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.PictureResponseDto;
import com.example.testapi.services.S3SignedUrlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class StorageController {

    private final S3SignedUrlService s3SignedUrlService;
 
    @PostMapping("/generate-get-url/{userId}")
    public ResponseEntity<PictureResponseDto> generateGetPresignedUrlRequest(
            @PathVariable String userId ) {
        log.info("Generating GET url for key");
        return ResponseEntity.ok().body(new PictureResponseDto(s3SignedUrlService.generateGetSignedUrl(userId)));
    }

    @PostMapping("/generate-put-url/{userId}")
    public ResponseEntity<PictureResponseDto> generatePutPresignedUrlRequest(
            @PathVariable String userId ) {
        return ResponseEntity.ok().body(new PictureResponseDto(s3SignedUrlService.generatePutSignedUrl(userId)));
    }

    @PostMapping("/generate-delete-url/{userId}")
    public ResponseEntity<PictureResponseDto> generateDeletePresignedUrlRequest(
            @PathVariable String userId ) {
        return ResponseEntity.ok().body(new PictureResponseDto(s3SignedUrlService.generateDeleteSignedUrl(userId)));
    }

}
