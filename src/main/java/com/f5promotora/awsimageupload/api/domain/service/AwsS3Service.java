package com.f5promotora.awsimageupload.api.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AwsS3Service {
    void upload(MultipartFile file);
    byte[] download(String fileName);
    List<String> getObjectsList(String bucketName) throws IOException;
}
