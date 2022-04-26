package com.piaar_store_manager.server.service.product_category;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryBusinessService {
    private final ProductCategoryService productCategoryService;

    public List<ProductCategoryGetDto> searchList(){
        List<ProductCategoryEntity> productCategoryEntities = productCategoryService.searchList();
        List<ProductCategoryGetDto> productCategoryGetDtos = productCategoryEntities.stream().map(entity -> ProductCategoryGetDto.toDto(entity)).collect(Collectors.toList());
        return productCategoryGetDtos;
    }
}
