package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.piaar_store_manager.server.exception.FileDownloadException;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;
import com.piaar_store_manager.server.service.file_upload.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/file-upload")
public class FileUploadApiController {
    
    @Autowired
    private FileUploadService fileUploadservice;
    
    /**
     * Upload one api for image file.
     * <p>
     * <b>POST : API URL => /api/v1/file-upload/uploadFile</b>
     * 
     * @param file : MultipartFile
     * @return FileUploadResponse
     */
    @PostMapping("/uploadFile")
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
        // TODO : Upload 시에 파일의 extension을 체크하여 이미지가 아닌 파일은 거절해야됨.
        // 개인적인 생각으로는 단일 업로드 메소드를 그냥 서비스로 넣어버리고 멀티업로드 API 만 구현해서 사용하는게 나을것 같아요

        String fileName = fileUploadservice.storeFile(file);
        
        String fileUploadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/v1/file-upload/images/")
                                .path(fileName)
                                .toUriString();
        
        return new FileUploadResponse(fileName, fileUploadUri, file.getContentType(), file.getSize());
    }
    
    /**
     * Upload list api for image file.
     * <p>
     * <b>POST : API URL => /api/v1/file-upload/uploadFiles</b>
     * 
     * @param files : List::MultipartFile::
     * @return List::FileUploadResponse::
     */
    @PostMapping("/uploadFiles")
    public List<FileUploadResponse> uploadFiles(@RequestParam("files") List<MultipartFile> files){
        // TODO : Upload 시에 파일의 extension을 체크하여 이미지가 아닌 파일은 거절해야됨.
        return files.stream()
                    .map(file -> uploadFile(file))
                    .collect(Collectors.toList());
    }

    /**
     * Select one api for image file.
     * <p>
     * <b>POST : API URL => /api/v1/file-upload/images/{fileName}</b>
     * 
     * @param fileName : String
     * @param request : HttpServletRequest
     * @return ResponseEntity::UrlResource::
     */
    // TODO (desc): 정적 리소스 매핑으로 파일 요청을 관리해주는게 더 좋아 보임.
    @GetMapping("/images/{fileName}")
    public ResponseEntity<UrlResource> getUploadFile(@PathVariable String fileName, HttpServletRequest request){

        UrlResource resource = fileUploadservice.loadFileAsResource(fileName);
 
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new FileDownloadException("Could not determine file type.");
        }
 
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
 
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
