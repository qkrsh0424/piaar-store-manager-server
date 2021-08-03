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
     * Upload files api for image files.
     * <p>
     * <b>GET : API URL => /api/v1/file-upload/uploadFiles</b>
     * 
     * @param files : List::MultipartFile::
     * @return ResponseEntity(message, HttpStatus)
     * @see FileUploadService#uploadFiles
     */
    @PostMapping("/uploadFiles")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        Message message = new Message();
        
        try{
            fileUploadservice.isImageFile(files);
        } catch(Exception e){
            message.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            message.setMessage("file_extension_error");
            message.setMemo("This is not an image file.");
            return new ResponseEntity<>(message, message.getStatus());
        }

        if (userService.isManager()) {
            try{
                message.setData(fileUploadservice.uploadFiles(files));
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
}
