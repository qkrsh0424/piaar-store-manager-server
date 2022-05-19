package com.piaar_store_manager.server.config.aws;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("cloud.aws")
public class AwsS3Configuration {
    private Map<String, String> credentials;
    private Map<String, String> s3;
    private Map<String, String> region;
    private AmazonS3 s3Client;

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, String> credentials) {
        this.credentials = credentials;
    }

    public Map<String, String> getS3() {
        return s3;
    }

    public void setS3(Map<String, String> s3) {
        this.s3 = s3;
    }

    public Map<String, String> getRegion() {
        return region;
    }

    public void setRegion(Map<String, String> region) {
        this.region = region;
    }

    /**
    //  * <b>S3 Upload Setting Related Method</b>
    //  * <p>
    //  * AWS S3 설정 메소드.
    //  *
    //  * @param accessKey : String
    //  * @param secretKey : String
    //  */
    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.getCredentials().get("access-key"),
        this.getCredentials().get("secret-key"));

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.getRegion().get("static"))
                .build();
    }

    public AmazonS3 getS3Client() {
        return s3Client;
    }
}
