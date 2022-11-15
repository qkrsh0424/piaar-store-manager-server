package com.piaar_store_manager.server.domain.product_release.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;

@Repository
public interface ProductReleaseRepositoryCustom {
    List<ProductReleaseProjection.RelatedProductAndProductOption> qSearchBatchByOptionIds(List<UUID> optionIds, Map<String, Object> params);
}
