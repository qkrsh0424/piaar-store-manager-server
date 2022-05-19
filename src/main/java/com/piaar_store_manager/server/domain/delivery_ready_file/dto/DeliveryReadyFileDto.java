package com.piaar_store_manager.server.domain.delivery_ready_file.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.domain.delivery_ready_file.entity.DeliveryReadyFileEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyFileEntity => DeliveryReadyFileDto
     * 
     * @param entity : DeliveryReadyFileEntity
     * @return DeliveryReadyFileDto
     */
    public static DeliveryReadyFileDto toDto(DeliveryReadyFileEntity entity) {
        DeliveryReadyFileDto dto = DeliveryReadyFileDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .filePath(entity.getFilePath())
            .fileName(entity.getFileName())
            .fileSize(entity.getFileSize())
            .fileExtension(entity.getFileExtension())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .deleted(entity.getDeleted())
            .build();

        return dto;
    }
}
