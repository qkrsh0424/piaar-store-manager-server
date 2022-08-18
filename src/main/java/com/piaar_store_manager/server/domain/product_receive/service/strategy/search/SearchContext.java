package com.piaar_store_manager.server.domain.product_receive.service.strategy.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;

@Component
public class SearchContext {
    private ProductReceiveObjectType objectType;
    private SearchStrategy searchStrategy;
    
    private Map<ProductReceiveObjectType, SearchStrategy> searchStrategies;

    @Autowired
    public SearchContext(Set<SearchStrategy> searchStrategySet) {
        makeSearchStrategies(searchStrategySet);
    }

    public void setSearchStrategy(String type) {
        objectType = ProductReceiveObjectType.getObjectType(type);
        searchStrategy = searchStrategies.get(objectType);
    }

    public void makeSearchStrategies(Set<SearchStrategy> searchStrategySet) {
        searchStrategies = new HashMap<>();
        searchStrategySet.forEach(strategy -> {
            searchStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> T searchOne(UUID receiveId) {
        return searchStrategy.searchOne(receiveId);
    }

    public <T> List<T> searchList() {
        return searchStrategy.searchList();
    }

    public <T> List<T> searchListByOptionCid(Integer optionCid) {
        return searchStrategy.searchListByOptionCid(optionCid);
    }
}
