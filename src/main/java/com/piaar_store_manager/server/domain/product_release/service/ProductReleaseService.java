package com.piaar_store_manager.server.domain.product_release.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;
import com.piaar_store_manager.server.domain.product_release.repository.ProductReleaseCustomJdbc;
import com.piaar_store_manager.server.domain.product_release.repository.ProductReleaseRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseService {
    private final ProductReleaseRepository productReleaseRepository;
    private final ProductReleaseCustomJdbc productReleaseCustomJdbc;

    public ProductReleaseEntity searchOne(UUID releaseId) {
        Optional<ProductReleaseEntity> entityOpt = productReleaseRepository.findById(releaseId);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public ProductReleaseEntity searchOneByErpOrderItemId(UUID erpOrderItemId) {
        List<ProductReleaseEntity> releaseEntities = productReleaseRepository.findByErpOrderItemId(erpOrderItemId);

        return releaseEntities.stream().findFirst().get();
    }

    public void saveAndModify(ProductReleaseEntity entity) {
        productReleaseRepository.save(entity);
    }

    @Transactional
    public void saveListAndModify(List<ProductReleaseEntity> entities) {
        productReleaseRepository.saveAll(entities);
    }

    public void destroyOne(Integer productReleaseCid) {
        productReleaseRepository.findById(productReleaseCid).ifPresent(product -> {
            productReleaseRepository.delete(product);
        });
    }

    public void bulkInsert(List<ProductReleaseEntity> entities){
        productReleaseCustomJdbc.jdbcBulkInsert(entities);
    }

    public void deleteByErpOrderItemIds(List<UUID> erpOrderItemIds){
        productReleaseRepository.deleteByErpOrderItemIds(erpOrderItemIds);
    }

    public List<ProductReleaseEntity> findByErpOrderItemIds(List<UUID> orderItemIds) {
        return productReleaseRepository.findByErpOrderItemIds(orderItemIds);
    }

    public List<ProductReleaseProjection.RelatedProductAndProductOption> qSearchBatchByOptionIds(List<UUID> optionIds, Map<String, Object> params) {
        return productReleaseRepository.qSearchBatchByOptionIds(optionIds, params);
    }
}
