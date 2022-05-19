package com.piaar_store_manager.server.domain.aws.s3.service;

import java.io.IOException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.piaar_store_manager.server.config.aws.AwsS3Configuration;
import com.piaar_store_manager.server.domain.aws.s3.dto.AwsS3ReqDto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AwsS3Configuration awsS3Configuration;
    private AmazonS3 s3Client;

    public void putObject(AwsS3ReqDto reqDto) {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(reqDto.getFile().getSize());

        try{
            // AWS S3 업로드
            s3Client = awsS3Configuration.getS3Client();
            s3Client.putObject(new PutObjectRequest(reqDto.getUploadPath(), reqDto.getFileName(), reqDto.getFile().getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }
}
