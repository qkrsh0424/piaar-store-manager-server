package com.piaar_store_manager.server.domain.product.service.strategy.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product.proj.ProductProj;
import com.piaar_store_manager.server.domain.product.service.ProductService;
import com.piaar_store_manager.server.domain.product.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product.type.ProductObjectType;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFJStrategyImpl implements SearchStrategy {
    private final ProductService productService;
    private final ProductOptionService productOptionService;

    @Override
    public ProductObjectType findObjectType() {
        return ProductObjectType.FJ;
    }

    @Override
    public <T> T searchOne(UUID id) {
        ProductProj proj = productService.searchOneM2OJ(id);
        List<ProductOptionEntity> optionEntities = productOptionService.searchBatchByProductCid(proj.getProduct().getCid());
        List<ProductOptionGetDto> optionDtos = productOptionService.searchStockUnit(optionEntities);
        
        ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(proj);
        productFJDto.setOptions(optionDtos);
        return (T) productFJDto;
    }

    @Override
    public <T> List<T> searchAll() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        List<Integer> productCids = productProjs.stream().map(r -> r.getProduct().getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchBatchByProductCids(productCids);
        
        // option setting
        List<ProductGetDto.FullJoin> productFJDtos = productProjs.stream().map(r -> {
            List<ProductOptionGetDto> optionDtos = optionGetDtos.stream().filter(option -> r.getProduct().getCid().equals(option.getProductCid())).collect(Collectors.toList());

            ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(r);
            productFJDto.setOptions(optionDtos);
            return productFJDto;
        }).collect(Collectors.toList());
        return productFJDtos.stream().map(dto -> (T)dto).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> searchBatchOfManagedStock() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        // 재고관리 상품 추출
        List<ProductProj> stockManagementProductProjs = productProjs.stream().filter(proj -> proj.getProduct().getStockManagement()).collect(Collectors.toList());
        List<Integer> productCids = stockManagementProductProjs.stream().map(proj -> proj.getProduct().getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchBatchByProductCids(productCids);

        // option setting
        List<ProductGetDto.FullJoin> productFJDtos = stockManagementProductProjs.stream().map(r -> {
            List<ProductOptionGetDto> optionDtos = optionGetDtos.stream().filter(option -> r.getProduct().getCid().equals(option.getProductCid())).collect(Collectors.toList());

            ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(r);
            productFJDto.setOptions(optionDtos);
            return productFJDto;
        }).collect(Collectors.toList());
        return productFJDtos.stream().map(dto -> (T) dto).collect(Collectors.toList());
    }

    
    
}
