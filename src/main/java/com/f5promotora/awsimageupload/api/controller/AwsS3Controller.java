package com.f5promotora.awsimageupload.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Controller {
    void upload(MultipartFile file);
    ResponseEntity<byte[]> download(String fileName);
}
