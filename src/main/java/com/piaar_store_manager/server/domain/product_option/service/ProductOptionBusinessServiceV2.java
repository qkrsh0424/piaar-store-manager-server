package com.piaar_store_manager.server.domain.product_option.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductOptionBusinessServiceV2 {
    private final ProductOptionService productOptionService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate와 endDate기간 사이에 등록된 모든 release(출고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * startDate와 endDate기간 사이에 등록된 모든 receive(입고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * 입출고 데이터를 이용해 ProductOptionGetDto.JoinReceiveAndRelease 생성한다.
     *
     * @return ProductOptionGetDto.JoinReceiveAndRelease
     */
    public ProductOptionGetDto.RelatedProductReceiveAndProductRelease searchBatchStockStatus(List<UUID> optionIds, Map<String,Object> params) {
        ProductOptionProjection.RelatedProductReceiveAndProductRelease proj = productOptionService.qSearchBatchStockStatus(optionIds, params);
        ProductOptionGetDto.RelatedProductReceiveAndProductRelease stockStatusDto = ProductOptionGetDto.RelatedProductReceiveAndProductRelease.toDto(proj);
        return stockStatusDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 option 조회, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, category, user를 함께 조회한다.
     *
     * @return List::ProductOptionGetDto.ManyToOneJoin::
     * @see ProductOptionService#searchAllM2OJ
     */
    public List<ProductOptionGetDto.ManyToOneJoin> searchAllM2OJ() {
        List<ProductOptionProj> productOptionProjs = productOptionService.searchListM2OJ();
        List<ProductOptionGetDto.ManyToOneJoin> optionM2OJDtos = productOptionProjs.stream().map(proj -> ProductOptionGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return optionM2OJDtos;
    }
}
