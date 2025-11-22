package com.reservation.reservation_server.controller;

import com.reservation.reservation_server.config.S3.S3Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final S3Service s3Service;

    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // 이미지 업로드
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        String key = s3Service.uploadFile(file);
        return s3Service.getFileUrl(key);
    }
}
