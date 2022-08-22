package com.piaar_store_manager.server.domain.erp_order_item.service.strategy.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.erp_order_item.type.ErpOrderItemObjectType;

@Component
public class ErpOrderItemSearchContext {
    private ErpOrderItemObjectType objectType;
    private SearchStrategy searchStrategy;

    private Map<ErpOrderItemObjectType, SearchStrategy> searchStrategies;

    @Autowired
    public ErpOrderItemSearchContext(Set<SearchStrategy> searchStrategySet) {
        makeSearchStrategy(searchStrategySet);
    }

    public void setSearchStrategy(String type) {
        objectType = ErpOrderItemObjectType.getObjectType(type);
        searchStrategy = searchStrategies.get(objectType);
    }

    public void makeSearchStrategy(Set<SearchStrategy> searchStrategySet) {
        searchStrategies = new HashMap<>();
        searchStrategySet.forEach(strategy -> {
            searchStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> List<T> searchList(Map<String, Object> params) {
        return searchStrategy.searchList(params);
    }

    public <T> Page<T> searchListByPage(Map<String, Object> params, Pageable pageable) {
        return searchStrategy.searchListByPage(params, pageable);
    }

    public <T> List<T> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        return searchStrategy.searchBatchByIds(ids, params);
    }
}
