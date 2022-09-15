package com.piaar_store_manager.server.domain.product_option.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockStatusDto;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_option.type.ProductOptionObjectType;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveService;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.exception.CustomNotMatchedParamsException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductOptionM2OJStrategyImpl implements SearchStrategy {
    private final ProductOptionService productOptionService;
    private final ProductReleaseService productReleaseService;
    private final ProductReceiveService productReceiveService;

    @Override
    public ProductOptionObjectType findObjectType() {
        return ProductOptionObjectType.M2OJ;
    }

    @Override
    public <T> T searchOne(UUID id) {
        ProductOptionProj optionProj = productOptionService.searchOneM2OJ(id);
        return (T) ProductOptionGetDto.ManyToOneJoin.toDto(optionProj);
    }

    @Override
    public <T> List<T> searchAll() {
        List<ProductOptionProj> productOptionProjs = productOptionService.searchListM2OJ();
        return productOptionProjs.stream().map(proj -> (T) ProductOptionGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
    }

    @Override
    public <T> T searchForStockStatus(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        String start = params.get("startDate") != null ? params.get("startDate").toString() : null;
        String end = params.get("endDate") != null ? params.get("endDate").toString() : null;

        if(start == null || end == null) {
            throw new CustomNotMatchedParamsException("날짜값이 올바르지 않습니다.");
        }

        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ(startDate, endDate);
        List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ(startDate, endDate);

        List<ProductReceiveGetDto.JoinProdAndOption> receiveDtos = receiveProjs.stream().map(proj -> ProductReceiveGetDto.JoinProdAndOption.toDto(proj)).collect(Collectors.toList());
        List<ProductReleaseGetDto.JoinProdAndOption> releaseDtos = releaseProjs.stream().map(proj -> ProductReleaseGetDto.JoinProdAndOption.toDto(proj)).collect(Collectors.toList());

        ProductOptionStockStatusDto.JoinReceiveAndRelease statusDto = ProductOptionStockStatusDto.JoinReceiveAndRelease.builder()
            .productReceive(receiveDtos)
            .productRelease(releaseDtos)
            .build();

        return (T) statusDto;
    }

    
}
