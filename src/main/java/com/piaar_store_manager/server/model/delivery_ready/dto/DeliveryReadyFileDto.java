package com.piaar_store_manager.server.model.delivery_ready.dto;

import java.util.Date;
import java.util.UUID;

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
}
