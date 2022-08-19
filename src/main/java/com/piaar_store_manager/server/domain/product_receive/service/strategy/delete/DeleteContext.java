package com.piaar_store_manager.server.domain.product_receive.service.strategy.delete;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;

// @Component
public class DeleteContext {
    private ProductReceiveObjectType objectType;
    private DeleteStrategy deleteStrategy;

    private Map<ProductReceiveObjectType, DeleteStrategy> deleteStrategies;

    @Autowired
    public DeleteContext(Set<DeleteStrategy> deleteStrategySet) {
        makeDeleteStrategies(deleteStrategySet);
    }

    public void setDeleteStrategy(String type) {
        objectType = ProductReceiveObjectType.getObjectType(type);
        deleteStrategy = deleteStrategies.get(objectType);
    }

    public void makeDeleteStrategies(Set<DeleteStrategy> deleteStrategySet) {
        deleteStrategies = new HashMap<>();
        deleteStrategySet.forEach(strategy -> {
            deleteStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> void destroyOne(UUID id) {
        deleteStrategy.destroyOne(id);
    }
}
