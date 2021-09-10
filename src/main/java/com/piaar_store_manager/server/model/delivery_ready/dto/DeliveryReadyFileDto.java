package com.piaar_store_manager.server.model.delivery_ready.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyFileDto {
    private Integer cid;
    private UUID id;
    private String filePath;
    private String fileName;
    private Integer fileSize;
    private String fileExtension;
    private Date createdAt;
    private UUID createdBy;
    private Boolean deleted;

    public static DeliveryReadyFileDto toDto(DeliveryReadyFileEntity entity) {
        DeliveryReadyFileDto dto = new DeliveryReadyFileDto();

        dto.setCid(entity.getCid()).setId(entity.getId()).setFilePath(entity.getFilePath())
            .setFileName(entity.getFileName()).setFileSize(entity.getFileSize()).setFileExtension(entity.getFileExtension())
            .setCreatedAt(entity.getCreatedAt()).setCreatedBy(entity.getCreatedBy()).setDeleted(entity.getDeleted());

        return dto;
    }
}
