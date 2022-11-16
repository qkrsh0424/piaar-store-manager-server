package com.piaar_store_manager.server.domain.file_download.controller;

import java.io.IOException;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.aws.s3.dto.AwsS3ReqDto;
import com.piaar_store_manager.server.domain.aws.s3.service.AwsS3Service;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/api/v1/file-download")
@RequiredArgsConstructor
@RequiredLogin
public class FileDownloadApiController {
    private final AwsS3Service awsS3Service;

    @PermissionRole
    @PostMapping("/cloud")
    public void downloadFileByCloud(HttpServletResponse response, @RequestBody AwsS3ReqDto dto) throws IOException {
        byte[] result = awsS3Service.getObject(dto);
        response.setContentType("image/png");
        response.setContentLength(result.length);
        response.setHeader("Content-Disposition", "attachment;");
        response.getOutputStream().write(result);
    }
}
