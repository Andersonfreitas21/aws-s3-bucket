package com.f5promotora.awsimageupload.api.domain.service.v1;

import com.amazonaws.AmazonServiceException;
import com.f5promotora.awsimageupload.api.domain.service.AwsS3Service;
import com.f5promotora.awsimageupload.bucket.BucketName;
import com.f5promotora.awsimageupload.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

    private final FileStore fileStore;

    @Autowired
    public AwsS3ServiceImpl(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void upload(MultipartFile file) {
        isFileEmpty(file);

        Map<String, String> metadata = extractMetadata(file);
        String path = String.format("%s", BucketName.PROFILE_IMAGE.getBucketName());

        try {
            fileStore.save(path, file.getOriginalFilename(), Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] download(String fileName) {
        String path = String.format("%s",
                BucketName.PROFILE_IMAGE.getBucketName());
        try {
            return fileStore.download(path, fileName);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Illegal state exception",e);
        }
    }

    @Override
    public List<String> getObjectsList(String bucketName) throws IOException {
        return fileStore.getObjectsList(bucketName);
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }
}
