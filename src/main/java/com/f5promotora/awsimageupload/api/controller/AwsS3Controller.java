package com.f5promotora.awsimageupload.api.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Controller {
  String upload(MultipartFile file);

  ResponseEntity<Resource> download(String fileName);
}
