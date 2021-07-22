package com.piaar_store_manager.server.model.product_option.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProductOptionGetDto {
    private Integer cid;
    private UUID id;
    private String defaultName;
    private String managementName;
    private Integer salesPrice;
    private Integer stockUnit;
    private String status;
    private String memo;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private Integer productCid;
    private UUID productId;
}
