package com.piaar_store_manager.server.domain.product_release.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.product_release.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_release.type.ProductReleaseObjectType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductReleaseM2OJStrategyImpl implements SearchStrategy {
    private final ProductReleaseService productReleaseService;

    @Override
    public ProductReleaseObjectType findObjectType() {
        return ProductReleaseObjectType.M2OJ;
    }
    
    @Override
    public <T> T searchOne(UUID id) {
        ProductReleaseProj releaseProj = productReleaseService.searchOneM2OJ(id);
        return (T) ProductReleaseGetDto.ManyToOneJoin.toDto(releaseProj);
    }

    @Override
    public <T> List<T> searchBatch() {
        List<ProductReleaseProj> projs = productReleaseService.searchListM2OJ();
        return projs.stream().map(proj -> (T)ProductReleaseGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
    }
}
