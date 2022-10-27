package com.piaar_store_manager.server.domain.product_option.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveService;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;

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
     * @see ProductReleaseService#searchListM2OJ
     * @see ProductReceiveService#searchListM2OJ
     */
    public ProductOptionGetDto.RelatedProductReceiveAndProductRelease searchBatchStockStatus(List<UUID> optionIds, Map<String,Object> params) {
        ProductOptionProjection.RelatedProductReceiveAndProductRelease proj = productOptionService.qSearchBatchStockStatus(optionIds, params);
        ProductOptionGetDto.RelatedProductReceiveAndProductRelease stockStatusDto = ProductOptionGetDto.RelatedProductReceiveAndProductRelease.toDto(proj);
        // return statusDto;
        return stockStatusDto;
    }
}
