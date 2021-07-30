package com.piaar_store_manager.server.model.product.dto;


import java.util.List;

import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductJoinResDto {
    ProductGetDto product;
    ProductCategoryGetDto category;
    UserGetDto user;
    List<ProductOptionGetDto> options;

    public ProductJoinResDto(){}
}
