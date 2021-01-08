package com.f5promotora.awsimageupload.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileStore {

  private final AmazonS3 s3;

  @Autowired
  public FileStore(AmazonS3 s3) {
    this.s3 = s3;
  }

  public String save(
      String path,
      String fileName,
      Optional<Map<String, String>> optionalMetadata,
      InputStream inputStream) {
    ObjectMetadata metadata = new ObjectMetadata();

    optionalMetadata.ifPresent(
        map -> {
          if (!map.isEmpty()) {
            map.forEach(metadata::addUserMetadata);
          }
        });

    try {
      s3.putObject(path, fileName, inputStream, metadata);

      return s3.generatePresignedUrl(
              path,
              fileName,
              Date.from(
                  LocalDateTime.now()
                      .plusDays(7)
                      .atZone(ZoneId.of("America/Fortaleza"))
                      .toInstant()))
          .toString();
    } catch (AmazonServiceException e) {
      throw new IllegalStateException("Failed to store file to s3", e);
    }
  }

  public Resource download(String path, String key) {
    S3Object object = s3.getObject(path, key);
    try (S3ObjectInputStream s3is = object.getObjectContent();
        ByteArrayOutputStream fos = new ByteArrayOutputStream()) {
      byte[] read_buf = new byte[1024];
      int read_len;
      while ((read_len = s3is.read(read_buf)) > 0) {
        fos.write(read_buf, 0, read_len);
      }
      return new ByteArrayResource(fos.toByteArray());
    } catch (AmazonServiceException | IOException e) {
      throw new IllegalStateException("Failed to download file to s3", e);
    }
  }

  public List<S3ObjectSummary> getObjectsList(String bucketName) throws SdkClientException {
    ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
    ListObjectsV2Result result;
    List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
    do {
      result = s3.listObjectsV2(req);
      for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
        s3ObjectSummaries.add(objectSummary);
        log.info(" - {} (size: {})", objectSummary.getKey(), objectSummary.getSize());
      }
    } while (result.isTruncated());
    return s3ObjectSummaries;
  }
}
