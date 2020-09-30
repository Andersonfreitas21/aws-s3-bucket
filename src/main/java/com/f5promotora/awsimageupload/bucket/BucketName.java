package com.f5promotora.awsimageupload.bucket;

public enum BucketName {

    PROFILE_IMAGE("aws-image-upload-test");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

}
