package com.piaar_store_manager.server.domain.product_receive.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProjection;

@Repository
public interface ProductReceiveRepositoryCustom {
    List<ProductReceiveProjection.RelatedProductAndProductOption> qSearchBatchByOptionIds(List<UUID> optionIds, Map<String, Object> params);
}
