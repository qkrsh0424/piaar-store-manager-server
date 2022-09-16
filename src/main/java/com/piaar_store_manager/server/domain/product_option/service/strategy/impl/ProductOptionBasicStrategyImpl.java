package com.piaar_store_manager.server.domain.product_option.service.strategy.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockStatusDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_option.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_option.type.ProductOptionObjectType;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveService;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.exception.CustomNotMatchedParamsException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductOptionBasicStrategyImpl implements SearchStrategy {
    private final ProductOptionService productOptionService;
    private final ProductReleaseService productReleaseService;
    private final ProductReceiveService productReceiveService;
    
    @Override
    public ProductOptionObjectType findObjectType() {
        return ProductOptionObjectType.Basic;
    }

    @Override
    public <T> T searchOne(UUID id) {
        ProductOptionEntity entity = productOptionService.searchOne(id);
        List<ProductOptionGetDto> dtos = productOptionService.searchStockUnit(Arrays.asList(entity));
        return (T) dtos.stream().findFirst().get();
    }

    @Override
    public <T> List<T> searchAll() {
        List<ProductOptionEntity> entities = productOptionService.searchAll();
        List<ProductOptionGetDto> dtos = productOptionService.searchStockUnit(entities);
        return dtos.stream().map(dto -> (T) dto).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> searchBatchByProductCid(Integer optionCid) {
        List<ProductOptionEntity> entities = productOptionService.searchBatchByProductCid(optionCid);
        List<ProductOptionGetDto> dtos = productOptionService.searchStockUnit(entities);
        return dtos.stream().map(dto -> (T) dto).collect(Collectors.toList());
    }

    @Override
    public <T> T searchForStockStatus(Map<String, Object> params) {
        Integer optionCid = params.get("optionCid") != null ? Integer.parseInt(params.get("optionCid").toString()) : null;

        if(optionCid == null) {
            throw new CustomNotMatchedParamsException("정상적인 요청값이 아닙니다.");
        }

        List<ProductReleaseEntity> releaseEntities = productReleaseService.searchListByOptionCid(optionCid);
        List<ProductReceiveEntity> receiveEntities = productReceiveService.searchListByOptionCid(optionCid);

        List<ProductReleaseGetDto> releaseDtos = releaseEntities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        List<ProductReceiveGetDto> receiveDtos = receiveEntities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());

        ProductOptionStockStatusDto statusDto = ProductOptionStockStatusDto.builder()
            .productRelease(releaseDtos)
            .productReceive(receiveDtos)
            .build();
            
        return (T) statusDto;
    }
}
