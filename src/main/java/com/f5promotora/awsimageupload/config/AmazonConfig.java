package com.f5promotora.awsimageupload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

  private static final String ACCESSKEY;
  private static final String SECRETKEY;
  private static final String REGION;

  static {
    ACCESSKEY = "";
    SECRETKEY = "";
    REGION = "";
  }

  @Bean
  public AmazonS3 s3() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESSKEY, SECRETKEY);
    return AmazonS3ClientBuilder.standard()
        .withRegion(REGION)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }
}
