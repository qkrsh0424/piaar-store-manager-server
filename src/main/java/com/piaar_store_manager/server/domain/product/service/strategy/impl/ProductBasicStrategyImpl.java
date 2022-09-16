package com.piaar_store_manager.server.domain.product.service.strategy.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.service.ProductService;
import com.piaar_store_manager.server.domain.product.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product.type.ProductObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductBasicStrategyImpl implements SearchStrategy {
    private final ProductService productService;

    @Override
    public ProductObjectType findObjectType() {
        return ProductObjectType.Basic;
    }

    @Override
    public <T> T searchOne(UUID id) {
        ProductEntity entity = productService.searchOne(id);
        return (T) ProductGetDto.toDto(entity);
    }

    @Override
    public <T> List<T> searchAll() {
        List<ProductEntity> entities = productService.searchAll();
        List<ProductGetDto> dtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos.stream().map(dto -> (T)dto).collect(Collectors.toList());
    }
}
