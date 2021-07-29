package com.piaar_store_manager.server.model.product_release.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductReleaseGetDto {
    private Integer cid;
    private UUID id;
    private Integer releaseUnit;
    private String memo;
    private Date createdAt;
    private UUID createdBy;
    private Integer productOptionCid;
}
