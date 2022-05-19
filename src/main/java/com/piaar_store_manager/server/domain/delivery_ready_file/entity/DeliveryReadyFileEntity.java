package com.piaar_store_manager.server.domain.delivery_ready_file.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.delivery_ready_file.dto.DeliveryReadyFileDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "delivery_ready_file")
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReadyFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "created_at")
    private Date createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "deleted")
    private Boolean deleted;

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyFileDto => DeliveryReadyFileEntity
     * 
     * @param dto : DeliveryReadyFileDto
     * @return DeliveryReadyFileEntity
     */
    public static DeliveryReadyFileEntity toEntity(DeliveryReadyFileDto dto) {
        DeliveryReadyFileEntity entity = DeliveryReadyFileEntity.builder()
            .id(dto.getId())
            .filePath(dto.getFilePath())
            .fileName(dto.getFileName())
            .fileSize(dto.getFileSize())
            .fileExtension(dto.getFileExtension())
            .createdAt(dto.getCreatedAt())
            .createdBy(dto.getCreatedBy())
            .deleted(dto.getDeleted())
            .build();

        return entity;
    }
}
