package com.piaar_store_manager.server.domain.aws.s3.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwsS3ReqDto {
    private String uploadPath;
    private String fileName;
    private MultipartFile file;
}
