package com.f5promotora.awsimageupload.api.domain.service.v1;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AwsS3ServiceImplTest {

  @Test
  void download() {

    final String ACCESSKEY = "AKIAJ2KAX5AFIH2YXJCA";
    final String SECRETKEY = "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%";
    final String KEYNAME = "Extrato_4005148477.pdf";
    final String BUCKET = "aws-image-upload-test";
    final String REGION = "us-east-1";

    log.debug("Downloading {} from S3 bucket {}...\n", KEYNAME, BUCKET);

    AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESSKEY, SECRETKEY);
    final AmazonS3 s3 =
        AmazonS3ClientBuilder.standard()
            .withRegion(REGION)
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();

    try {
      S3Object o = s3.getObject(BUCKET, KEYNAME);
      S3ObjectInputStream s3is = o.getObjectContent();
      FileOutputStream fos = new FileOutputStream(new File(KEYNAME));
      byte[] read_buf = new byte[1024];
      int read_len;
      while ((read_len = s3is.read(read_buf)) > 0) {
        fos.write(read_buf, 0, read_len);
      }
      s3is.close();
      fos.close();
    } catch (AmazonServiceException e) {
      System.err.println(e.getErrorMessage());
      System.exit(1);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
