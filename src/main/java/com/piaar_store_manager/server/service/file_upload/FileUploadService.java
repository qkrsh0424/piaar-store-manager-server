package com.piaar_store_manager.server.service.file_upload;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.piaar_store_manager.server.exception.FileDownloadException;
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.property.FileUploadProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService{

    // TODO : @Value를 이용하여 한번 application.properties의 값을 가져와서 사용해보세요
    private final Path fileLocation;
    
    // Value 어노테이션 이용시 굳이 구현할 필요 없음.
    @Autowired
    public FileUploadService(FileUploadProperties prop) {
        this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();
        
        try {
            // TODO (desc): 개인적인 생각에는 인위적으로 자신이 지정하지 않은 디렉터리를 생성한다는것 자체가 위험요지가 있음.
            Files.createDirectories(this.fileLocation);
        }catch(Exception e) {
            throw new FileUploadException("Directory creation error.", e);
        }
    }

    /**
     * <b>File storage Method</b>
     * <p>
     * 설정 디렉토리에 파일을 저장한다.
     * 
     * @param file : MultipartFile
     * @return String
     */
    public String storeFile(MultipartFile file) {

        // TODO (desc) : original 파일 네임을 가지면서, 실제로 업로드시에는 다른 명칭 즉 단일값을 가지는 파일명으로 중복을 피하도록 한다.
        // UUID 혹은 타임데이터 + 랜덤값을 이용하여 이미지의 인덱스를 생성한다.
        // ex1) PiaarImage + {UUID} + {originalFileName}
        // ex2) PiaarImage + {TimeStamp} + {originalFileName}

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // fileLocation에 부분 경로 추가
            Path targetLocation = this.fileLocation.resolve(fileName);
            
            // targetLocation에 업로드 파일 복사
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] File upload failed.",e);
        }

        return fileName;
    }

    /**
     * <b>File load Method</b>
     * <p>
     * 지정된 경로에서 파일을 가져온다.
     * 
     * @param fileName : String
     * @return UrlResource
     */
    // TODO (desc): 정적 리소스 매핑으로 굳이 만들지 않아도되는 메소드 같음.
    public UrlResource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            }else {
                throw new FileDownloadException("Not found fileName=" + fileName + ".");
            }
        }catch(MalformedURLException e) {
            throw new FileDownloadException("Not found fileName=" + fileName + ".");
        }
    }

}
