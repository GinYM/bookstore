package com.bookstore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AmazonS3ClientService {
    void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess, String fileName);

    void uploadSingleFileToS3Bucket(File multipartFile, boolean enablePublicReadAccess, String fileName);

    void deleteFileFromS3Bucket(String fileName);
}
