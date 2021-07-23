package com.piaar_store_manager.server.model.product.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProductGetDto {
    private Integer cid;
    private UUID id;
    private String code;
    private String manufacturingCode;
    private String nProductCode;
    private String defaultName;
    private String managementName;
    private String imageUrl;
    private String imageFileName;
    private String memo;
    private Date createdAt;
    private UUID createdBy;
    private Date updatedAt;
    private UUID updatedBy;
    private Integer productCategoryCid;
}
