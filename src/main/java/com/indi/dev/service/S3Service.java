package com.indi.dev.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.indi.dev.entity.Genre;
import com.indi.dev.entity.User;
import com.indi.dev.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.profile-img}")
    private String profileImgDir;

    @Value("${cloud.aws.s3.video}")
    private String videoDir;

    @Value("${cloud.aws.s3.thumbnail}")
    private String thumbnailDir;

    public String putVideoToS3(File videoFile, String videoFileName) {
        amazonS3Client.putObject(new PutObjectRequest(videoDir, videoFileName, videoFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, videoFileName).toString();
    }


    public String putThumbnailToS3(File thumbnail, String thumbnailName) {
        amazonS3Client.putObject(new PutObjectRequest(thumbnailDir, thumbnailName, thumbnail).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, thumbnailName).toString();
    }

    public String putProfileImgToS3(File profileImg, String profileName){
        amazonS3Client.putObject(new PutObjectRequest(profileImgDir, profileName, profileImg).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, profileName).toString();
    }

    public String makeVideoFileName(String videoFileName) {
        return UUID.randomUUID()+ "." + videoFileName;
    }

    public String makeThumbnailName(String thumbnailName){
        return UUID.randomUUID() + "." + thumbnailName;
    }

    public String makeProfileName(String profileName) {
        return UUID.randomUUID() + "." + profileName;}

    public Video saveVideo(User user, String videoPath, String thumbnailPath, Genre genre, String videoTitle) {
        return Video.makeVideoEntity(user, videoPath, thumbnailPath, genre, videoTitle);
    }
}
