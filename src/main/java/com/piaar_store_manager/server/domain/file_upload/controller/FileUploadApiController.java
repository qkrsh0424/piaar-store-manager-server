package com.piaar_store_manager.server.domain.file_upload.controller;

import java.util.List;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.file_upload.service.FileUploadBusinessService;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file-upload")
@RequiredArgsConstructor
@RequiredLogin
public class FileUploadApiController {
    private final FileUploadBusinessService fileUploadBusinessService;

    /**
     * Upload files api for image files to local.
     * <p>
     * <b>GET : API URL => /api/v1/file-upload/local</b>
     */
    @PermissionRole
    @PostMapping("/local")
    public ResponseEntity<?> uploadFilesToLocal(@RequestParam("files") List<MultipartFile> files) {
        Message message = new Message();

        // file extension check.
        fileUploadBusinessService.isImageFile(files);

        message.setData(fileUploadBusinessService.uploadFilesToLocal(files));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Upload files api for image files to cloud(AWS S3).
     * <p>
     * <b>GET : API URL => /api/v1/file-upload/cloud</b>
     */
    @PermissionRole
    @PostMapping("/cloud")
    public ResponseEntity<?> uploadFilesToCloud(@RequestParam("files") List<MultipartFile> files) {
        Message message = new Message();

        // file extension check.
        fileUploadBusinessService.isImageFile(files);

        message.setData(fileUploadBusinessService.uploadFilesToCloud(files));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
