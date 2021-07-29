package com.piaar_store_manager.server.model.product_receive.dto;

import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductReceiveJoinResDto {
    ProductReceiveGetDto receive;
    ProductGetDto product;
    ProductCategoryGetDto category;
    UserGetDto user;
    ProductOptionGetDto option;

    public ProductReceiveJoinResDto(){}
}
