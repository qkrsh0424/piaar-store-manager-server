package com.piaar_store_manager.server.domain.product_option.service.strategy.search;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_option.service.strategy.type.ObjectTypeStrategy;

public interface SearchStrategy extends ObjectTypeStrategy {
    <T> T searchOne(UUID id);
    <T> List<T> searchAll();
    <T> T searchForStockStatus(Map<String, Object> params);
    
    default <T> List<T> searchBatchByProductCid(Integer cid) { return null; }
}
