package com.piaar_store_manager.server.domain.product.service.strategy.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.proj.ProductProj;
import com.piaar_store_manager.server.domain.product.service.ProductService;
import com.piaar_store_manager.server.domain.product.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product.type.ProductObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductM2OJStrategyImpl implements SearchStrategy {
    private final ProductService productService;

    @Override
    public ProductObjectType findObjectType() {
        return ProductObjectType.M2OJ;
    }

    @Override
    public <T> T searchOne(UUID id) {
        ProductProj productProj = productService.searchOneM2OJ(id);
        ProductGetDto.ManyToOneJoin dto = ProductGetDto.ManyToOneJoin.toDto(productProj);
        return (T) dto;
    }

    @Override
    public <T> List<T> searchAll() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        List<ProductGetDto.ManyToOneJoin> dtos = productProjs.stream().map(proj -> ProductGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return dtos.stream().map(dto -> (T)dto).collect(Collectors.toList());
    }
    
}
