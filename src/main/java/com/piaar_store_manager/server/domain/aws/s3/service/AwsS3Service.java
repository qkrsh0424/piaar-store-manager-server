package com.piaar_store_manager.server.domain.aws.s3.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.piaar_store_manager.server.config.aws.AwsS3Configuration;
import com.piaar_store_manager.server.domain.aws.s3.dto.AwsS3ReqDto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AwsS3Configuration awsS3Configuration;
    private AmazonS3 s3Client;

    /**
     * <b>AWS S3에 파일을 업로드한다.</b>
     * <p>
     *
     * @param reqDto : AwsS3ReqDto
     * @see AwsS3Configuration#getS3Client
     */
    public void putObject(AwsS3ReqDto reqDto) {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(reqDto.getFile().getSize());
        objMeta.setContentType(reqDto.getFile().getContentType());

        try{
            // S3 업로드
            s3Client = awsS3Configuration.getS3Client();
            // s3Client.putObject(new PutObjectRequest(reqDto.getUploadPath(), reqDto.getFileName(), reqDto.getFile().getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
            s3Client.putObject(new PutObjectRequest(reqDto.getUploadPath(), reqDto.getFileName(), reqDto.getFile().getInputStream(), objMeta));
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    public byte[] getObject(AwsS3ReqDto reqDto) throws UnsupportedEncodingException {   
        String storedBucketName = awsS3Configuration.getS3().get("bucket") + "/upload/image";
        GetObjectRequest getObjectRequest = new GetObjectRequest(storedBucketName, reqDto.getFileName());

        byte[] bytes;
        try {
            s3Client = awsS3Configuration.getS3Client();
            S3Object object = s3Client.getObject(getObjectRequest);
            S3ObjectInputStream objectInputStream = object.getObjectContent();
            bytes = IOUtils.toByteArray(objectInputStream);
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        return bytes;
    }
}
