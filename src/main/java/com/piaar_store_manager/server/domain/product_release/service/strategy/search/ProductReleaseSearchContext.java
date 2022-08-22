package com.piaar_store_manager.server.domain.product_release.service.strategy.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_release.type.ProductReleaseObjectType;

@Component
public class ProductReleaseSearchContext {
    private ProductReleaseObjectType objectType;
    private SearchStrategy searchStrategy;
    
    private Map<ProductReleaseObjectType, SearchStrategy> searchStrategies;
    
    @Autowired
    public ProductReleaseSearchContext(Set<SearchStrategy> searchStrategySet) {
        makeSearchStrategy(searchStrategySet);
    }

    public void setSearchStrategy(String type) {
        objectType = ProductReleaseObjectType.getObjectType(type);
        searchStrategy = searchStrategies.get(objectType);
    }

    private void makeSearchStrategy(Set<SearchStrategy> searchStrategySet) {
        searchStrategies = new HashMap<>();
        searchStrategySet.forEach(strategy -> {
            searchStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> T searchOne(UUID releaseId) {
        return searchStrategy.searchOne(releaseId);
    }

    public <T> List<T> searchBatch() {
        return searchStrategy.searchBatch();
    }

    public <T> List<T> searchBatchByOptionCid(Integer optionCid) {
        return searchStrategy.searchBatchByOptionCid(optionCid);
    }
}
