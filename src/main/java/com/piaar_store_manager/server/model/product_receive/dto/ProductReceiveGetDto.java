package com.piaar_store_manager.server.model.product_receive.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductReceiveGetDto {
    private Integer cid;
    private UUID id;
    private Integer receiveUnit;
    private String memo;
    private Date createdAt;
    private UUID createdBy;
    private Integer productOptionCid;
}
