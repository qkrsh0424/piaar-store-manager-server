package com.piaar_store_manager.server.domain.product.service.strategy.search;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.service.strategy.type.ObjectTypeStrategy;

public interface SearchStrategy extends ObjectTypeStrategy {
    <T> T searchOne(UUID id);
    <T> List<T> searchAll();
    
    default <T> List<T> searchBatchOfManagedStock() { return null; };
}
