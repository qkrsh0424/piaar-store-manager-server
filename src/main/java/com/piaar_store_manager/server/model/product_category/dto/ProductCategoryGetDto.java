package com.piaar_store_manager.server.model.product_category.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain=true)
public class ProductCategoryGetDto {
    private Integer cid;
    private String id;
    private String name;
}
