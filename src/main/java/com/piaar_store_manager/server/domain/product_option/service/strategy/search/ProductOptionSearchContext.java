package com.piaar_store_manager.server.domain.product_option.service.strategy.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_option.type.ProductOptionObjectType;

@Component
public class ProductOptionSearchContext {
    private ProductOptionObjectType objectType;
    private SearchStrategy searchStrategy;

    private Map<ProductOptionObjectType, SearchStrategy> searchStrategies;

    public ProductOptionSearchContext(Set<SearchStrategy> searchStrategies) {
        makeSearchStrategies(searchStrategies);
    }

    public void setSearchStrategy(String type) {
        objectType = ProductOptionObjectType.getObjectType(type);
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

    public <T> List<T> searchBatchByProductCid(Integer productCid) { 
        return searchStrategy.searchBatchByProductCid(productCid);
    }

    public <T> T searchForStockStatus(Map<String, Object> params) {
        return searchStrategy.searchForStockStatus(params);
    }
}
