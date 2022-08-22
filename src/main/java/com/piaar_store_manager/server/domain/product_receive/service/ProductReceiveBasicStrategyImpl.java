package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// public class BasicStrategyImpl implements SearchStrategy, CreateStrategy, DeleteStrategy, UpdateStrategy {
public class ProductReceiveBasicStrategyImpl implements SearchStrategy {
    private final ProductReceiveService productReceiveService;

    public ProductReceiveObjectType findObjectType() {
        return ProductReceiveObjectType.Basic;
    }

    @Override
    public <T> T searchOne(UUID id) {
        return (T) ProductReceiveGetDto.toDto(productReceiveService.searchOne(id));
    }

    @Override
    public <T> List<T> searchList() {
        List<ProductReceiveEntity> dtos = productReceiveService.searchList();
        return dtos.stream().map(entity -> (T) ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> searchListByOptionCid(Integer optionCid) {
        List<ProductReceiveEntity> entities = productReceiveService.searchListByOptionCid(optionCid);
        return entities.stream().map(entity -> (T) ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
    }
}
