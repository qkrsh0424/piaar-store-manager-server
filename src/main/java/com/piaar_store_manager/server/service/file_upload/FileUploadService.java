package com.piaar_store_manager.server.service.file_upload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileUploadService{

    @Value("${file.upload-dir}")
    String fileLocation;

    private final List<String> EXTENSIONS_IMAGE = Arrays.asList("jpg", "gif", "png", "jpeg", "bmp", "tif");

    /**
     * <b>File storage Method</b>
     * <p>
     * 설정 디렉토리에 파일을 저장한다.
     * 
     * @param file : MultipartFile
     * @return String
     */
    public String storeFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String newFileName = "PiaarMS_" + UUID.randomUUID().toString().replaceAll("-", "") + fileName;

        try {
            // fileLocation에 부분 경로 추가
            Path targetLocation = Paths.get(this.fileLocation + "\\" +newFileName);

            // targetLocation에 업로드 파일 복사
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] File upload failed.",e);
        }

        return newFileName;
    }

    /**
     * Upload one api for image file.
     * <p>
     * 
     * @param file : MultipartFile
     * @return FileUploadResponse
     */
    public FileUploadResponse uploadFile(MultipartFile file) {

        String fileName = this.storeFile(file);
            
        String fileUploadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path("/uploads/")
                                    .path(fileName)
                                    .toUriString();
            
        return new FileUploadResponse(fileName, fileUploadUri, file.getContentType(), file.getSize());
    }

    /**
     * Upload list api for image file.
     * <p>
     * 
     * @param file : List::MultipartFile::
     * @return List::FileUploadResponse::
     */
    public List<FileUploadResponse> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                    .map(file -> uploadFile(file))
                    .collect(Collectors.toList());
    }

    /**
     * File extension check.
     */
    public void isImageFile(List<MultipartFile> files) {
        for(MultipartFile file : files){
            String fileName = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());

            if(EXTENSIONS_IMAGE.contains(fileName)){
                return;
            }
            throw new FileUploadException("This is not an image file.");
        }
    }

}
