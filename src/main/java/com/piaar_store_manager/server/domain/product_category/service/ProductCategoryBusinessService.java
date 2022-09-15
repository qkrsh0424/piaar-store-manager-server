package com.piaar_store_manager.server.domain.product_category.service;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryBusinessService {
    private final ProductCategoryService productCategoryService;

    public List<ProductCategoryGetDto> searchAll(){
        List<ProductCategoryEntity> productCategoryEntities = productCategoryService.searchAll();
        List<ProductCategoryGetDto> productCategoryGetDtos = productCategoryEntities.stream().map(entity -> ProductCategoryGetDto.toDto(entity)).collect(Collectors.toList());
        return productCategoryGetDtos;
    }
}
