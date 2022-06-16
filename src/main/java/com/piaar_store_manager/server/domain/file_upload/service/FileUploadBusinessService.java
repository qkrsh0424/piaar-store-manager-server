package com.piaar_store_manager.server.domain.file_upload.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Arrays;

import com.piaar_store_manager.server.config.aws.AwsS3Configuration;
import com.piaar_store_manager.server.domain.aws.s3.dto.AwsS3ReqDto;
import com.piaar_store_manager.server.domain.aws.s3.service.AwsS3Service;
import com.piaar_store_manager.server.domain.file_upload.dto.FileUploadResponse;
import com.piaar_store_manager.server.exception.FileUploadException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadBusinessService{
    private final AwsS3Configuration awsS3Configuration;
    private final AwsS3Service awsS3Service;

    @Value("${file.upload-dir}")
    String fileLocation;

    // Image extension.
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
     * Upload one api for image file to local.
     * <p>
     * 
     * @param file : MultipartFile
     * @return FileUploadResponse
     */
    public FileUploadResponse uploadFileToLocal(MultipartFile file) {
        String fileName = this.storeFile(file);     
        String fileUploadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path("/uploads/")
                                    .path(fileName)
                                    .toUriString();

        return new FileUploadResponse(fileName, fileUploadUri, file.getContentType(), file.getSize());
    }

    /**
     * Upload list api for image file to local.
     * <p>
     * 
     * @param file : List::MultipartFile::
     * @return List::FileUploadResponse::
     */
    public List<FileUploadResponse> uploadFilesToLocal(List<MultipartFile> files) {
        return files.stream().map(file -> uploadFileToLocal(file)).collect(Collectors.toList());
    }

    /**
     * Upload one api for image file to cloud.
     * <p>
     * 
     * @param file : MultipartFile
     * @return FileUploadResponse
     */
    public FileUploadResponse uploadFileToCloud(MultipartFile file) {
        String newFileName = "PiaarMS_" + UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename();
        String uploadPath = awsS3Configuration.getS3().get("bucket") + "/upload/image";
        // String fileUrl = "https://" + bucket + "/" + fileName;

        // aws s3 저장
        AwsS3ReqDto reqDto = AwsS3ReqDto.builder()
            .uploadPath(uploadPath)
            .fileName(newFileName)
            .file(file)
            .build();
        awsS3Service.putObject(reqDto);

        return new FileUploadResponse(newFileName, awsS3Configuration.getS3Client().getUrl(uploadPath, newFileName).toString(), file.getContentType(), file.getSize());
    }

    /**
     * Upload list api for image file to cloud.
     * <p>
     * 
     * @param file : List::MultipartFile::
     * @return List::FileUploadResponse::
     * @see FileUploadBusinessService#uploadFileToCloud
     */
    public List<FileUploadResponse> uploadFilesToCloud(List<MultipartFile> files){
        List<FileUploadResponse> uploadFiles = files.stream().map(file -> this.uploadFileToCloud(file)).collect(Collectors.toList());
        return uploadFiles;
    }

    /**
     * File extension check.
     * 
     * @param files : List::MultipartFile::
     */
    public void isImageFile(List<MultipartFile> files) {
        files.stream().forEach(file -> {
            String fileName = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());

            if(EXTENSIONS_IMAGE.contains(fileName)){
                return;
            }
            throw new FileUploadException("This is not an image file.");
        });
    }

}
