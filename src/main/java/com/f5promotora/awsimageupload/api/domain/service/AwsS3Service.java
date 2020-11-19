package com.f5promotora.awsimageupload.api.domain.service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
  String upload(MultipartFile file);

  Resource download(String fileName);

  List<S3ObjectSummary> getObjectsList(String bucketName) throws IOException;
}
