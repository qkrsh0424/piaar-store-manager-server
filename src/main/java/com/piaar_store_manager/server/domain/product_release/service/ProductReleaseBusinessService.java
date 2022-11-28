package com.piaar_store_manager.server.domain.product_release.service;

import java.util.UUID;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseBusinessService {
    private final ProductReleaseService productReleaseService;

    public ProductReleaseGetDto searchOneByErpOrderItemId(UUID erpOrderId) {
        ProductReleaseEntity entity = productReleaseService.searchOneByErpOrderItemId(erpOrderId);
        ProductReleaseGetDto dto = ProductReleaseGetDto.toDto(entity);
        return dto;
    }
}
