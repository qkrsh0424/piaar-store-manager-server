package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// Product Receive JOIN Product, Product Category, Product Option, User 
public class ProductReceiveM2OJStrategyImpl implements SearchStrategy {
    private final ProductReceiveService productReceiveService;

    public ProductReceiveObjectType findObjectType(){
        return ProductReceiveObjectType.M2OJ;
    }

    @Override
    public <T> T searchOne(UUID id) {
        ProductReceiveProj receiveProj = productReceiveService.searchOneM2OJ(id);
        return (T) ProductReceiveGetDto.ManyToOneJoin.toDto(receiveProj);
    }

    @Override
    public <T> List<T> searchList() {
        List<ProductReceiveProj> projs = productReceiveService.searchListM2OJ();
        return projs.stream().map(proj -> (T)ProductReceiveGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
    }
}
