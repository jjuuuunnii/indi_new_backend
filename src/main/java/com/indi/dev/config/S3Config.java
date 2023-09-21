package com.indi.dev.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class S3Config {
    @Value("${cloud.aws.credentials.access-key}")
    private String iamAccessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String iamSecretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.default-img}")
    private String defaultImgPath;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.profile-img}")
    private String profileImgDir;
    @Value("${cloud.aws.s3.video}")
    private String videoDir;
    @Value("${cloud.aws.s3.thumbnail}")
    private String thumbnailDir;

    @Bean
    public AmazonS3Client amazonS3Client(){
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region).enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}