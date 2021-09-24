package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.file_upload.FileUploadService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file-upload")
public class FileUploadApiController {
    
    @Autowired
    private FileUploadService fileUploadservice;

    @Autowired
    private UserService userService;

    /**
     * Upload files api for image files to local.
     * <p>
     * <b>GET : API URL => /api/v1/file-upload/uploadFilesToLocal</b>
     * 
     * @param files : List::MultipartFile::
     * @return ResponseEntity(message, HttpStatus)
     * @see FileUploadService#uploadFiles
     * @see UserService#userDenyCheck
     */
    @PostMapping("/uploadFilesToLocal")
    public ResponseEntity<?> uploadFilesToLocal(@RequestParam("files") List<MultipartFile> files) {
        Message message = new Message();
        
        // file extension check.
        fileUploadservice.isImageFile(files);
        
        if (userService.isManager()) {
            try{
                message.setData(fileUploadservice.uploadFilesToLocal(files));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Upload files api for image files to cloud.
     * <p>
     * <b>GET : API URL => /api/v1/file-upload/uploadFilesToCloud</b>
     * 
     * @param files : List::MultipartFile::
     * @return ResponseEntity(message, HttpStatus)
     * @see FileUploadService#uploadFiles
     * @see UserService#userDenyCheck
     */
    @PostMapping("/uploadFilesToCloud")
    public ResponseEntity<?> uploadFilesToCloud(@RequestParam("files") List<MultipartFile> files) {
        Message message = new Message();

        // file extension check.
        fileUploadservice.isImageFile(files);

        if (userService.isManager()) {
            message.setData(fileUploadservice.uploadFilesToCloud(files));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
