package com.piaar_store_manager.server.domain.product_receive.service.strategy.search;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_receive.service.strategy.type.ObjectTypeStrategy;

public interface SearchStrategy extends ObjectTypeStrategy {
    <T> T searchOne(UUID id);
    <T> List<T> searchList();
    
    default <T> List<T> searchListByOptionCid(Integer optionCid) { return null; }
}
