package com.indi.dev.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.indi.dev.config.S3Config;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final S3Config s3Config;

    public String putFileToS3(File file, String fileName, String directory) {
        try {
            String fullPath = directory + fileName;
            amazonS3Client.putObject(new PutObjectRequest(s3Config.getBucket(), fullPath, file));

            return amazonS3Client.getUrl(s3Config.getBucket(), fullPath).toString();
        } catch (AmazonS3Exception e) {
            throw new CustomException(ErrorCode.S3_ERROR);
        }
    }



    public String convertToRelativePath(String absolutePath) throws URISyntaxException {
        URI uri = new URI(absolutePath);
        String path = uri.getPath();
        return path.substring(s3Config.getBucket().length() + 2);
    }

    // 사용 예
    public void deleteFromS3(String absolutePath) {
        try {
            String relativePath = convertToRelativePath(absolutePath);
            amazonS3Client.deleteObject(s3Config.getBucket(), relativePath);
        } catch (AmazonS3Exception | URISyntaxException e) {
            throw new CustomException(ErrorCode.S3_ERROR);
        }
    }


    public String makefileName(String fileType) {
        String extension;
        if ("profile".equals(fileType) || "thumbnail".equals(fileType) || "totalCgImg".equals(fileType)) {
            extension = ".jpeg";
        } else if ("video".equals(fileType)) {
            extension = ".mp4";
        } else {
            extension = ""; // 기본 확장자 또는 에러 처리
        }
        return UUID.randomUUID() + extension;
    }



}
