package com.piaar_store_manager.server.domain.erp_order_item.service.strategy.search;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.piaar_store_manager.server.domain.erp_order_item.service.type.ObjectTypeStrategy;

public interface SearchStrategy extends ObjectTypeStrategy {
    <T> List<T> searchList(Map<String, Object> params);
    default <T> Page<T> searchListByPage(Map<String, Object> params, Pageable pageable) { return null; }
    default <T> List<T> searchBatchByIds(List<UUID> ids, Map<String, Object> params) { return null; }
}
