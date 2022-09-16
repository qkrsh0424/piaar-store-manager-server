package com.piaar_store_manager.server.domain.product_release.service.strategy.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.domain.product_release.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_release.type.ProductReleaseObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductReleaseBasicStrategyImpl implements SearchStrategy {
    private final ProductReleaseService productReleaseService;

    @Override
    public ProductReleaseObjectType findObjectType() {
        return ProductReleaseObjectType.Basic;
    }

    @Override
    public <T> T searchOne(UUID id) {
        return (T) ProductReleaseGetDto.toDto(productReleaseService.searchOne(id));
    }

    @Override
    public <T> List<T> searchAll() {
        List<ProductReleaseEntity> entities = productReleaseService.searchAll();
        return entities.stream().map(entity -> (T) ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> searchBatchByOptionCid(Integer optionCid) {
        List<ProductReleaseEntity> entities = productReleaseService.searchListByOptionCid(optionCid);
        return entities.stream().map(entity -> (T) ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
    }
}
