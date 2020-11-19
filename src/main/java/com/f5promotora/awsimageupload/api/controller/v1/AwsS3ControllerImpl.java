package com.f5promotora.awsimageupload.api.controller.v1;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.f5promotora.awsimageupload.api.controller.AwsS3Controller;
import com.f5promotora.awsimageupload.api.domain.service.v1.AwsS3ServiceImpl;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1")
@CrossOrigin("*")
public class AwsS3ControllerImpl implements AwsS3Controller {

  private final AwsS3ServiceImpl awsS3ServiceImpl;

  @Autowired
  public AwsS3ControllerImpl(AwsS3ServiceImpl awsS3ServiceImpl) {
    this.awsS3ServiceImpl = awsS3ServiceImpl;
  }

  @Override
  @PostMapping(
      path = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  @ApiOperation(httpMethod = "POST", value = "Uploading objects to S3 AWS bucket")
  public String upload(@RequestParam("file") MultipartFile file) {
    return awsS3ServiceImpl.upload(file);
  }

  @Override
  @GetMapping("/download")
  @ApiOperation(
      httpMethod = "GET",
      value = "Download the file by name in the bucket.",
      consumes = "multipart/form-data")
  public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "force-download"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    return ResponseEntity.ok().headers(headers).body(awsS3ServiceImpl.download(fileName));
  }

  @GetMapping
  @ApiOperation(httpMethod = "GET", value = "Get list all objects to bucket name.")
  public ResponseEntity<List<S3ObjectSummary>> getObjectsList(
      @RequestParam("bucketName") String bucketName) {
    return ResponseEntity.ok().body(awsS3ServiceImpl.getObjectsList(bucketName));
  }
}
