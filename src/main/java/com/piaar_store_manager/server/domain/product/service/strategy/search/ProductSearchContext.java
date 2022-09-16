package com.piaar_store_manager.server.domain.product.service.strategy.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product.type.ProductObjectType;

@Component
public class ProductSearchContext {
    private ProductObjectType objectType;
    private SearchStrategy searchStrategy;

    private Map<ProductObjectType, SearchStrategy> searchStrategies;

    public ProductSearchContext(Set<SearchStrategy> searchStrategies) {
        makeSearchStrategies(searchStrategies);
    }

    public void setSearchStrategy(String type) {
        objectType = ProductObjectType.getObjectType(type);
        searchStrategy = searchStrategies.get(objectType);
    }

    public void makeSearchStrategies(Set<SearchStrategy> searchStrategySet) {
        searchStrategies = new HashMap<>();
        searchStrategySet.forEach(strategy -> {
            searchStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> T searchOne(UUID id) {
        return searchStrategy.searchOne(id);
    }

    public <T> List<T> searchAll() {
        return searchStrategy.searchAll();
    }

    public <T> List<T> searchBatchOfManagedStock() {
        return searchStrategy.searchBatchOfManagedStock();
    }
}
