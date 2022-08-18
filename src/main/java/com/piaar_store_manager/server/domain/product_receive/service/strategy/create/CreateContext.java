package com.piaar_store_manager.server.domain.product_receive.service.strategy.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;

@Component
public class CreateContext {
    private ProductReceiveObjectType objectType;
    private CreateStrategy createStrategy;
    private Map<ProductReceiveObjectType, CreateStrategy> createStrategies;

    @Autowired
    public CreateContext(Set<CreateStrategy> createStrategySet) {
        makeCreateStrategies(createStrategySet);
    }

    public void setCreateStrategy(String type) {
        objectType = ProductReceiveObjectType.getObjectType(type);
        createStrategy = createStrategies.get(objectType);
    }

    public void makeCreateStrategies(Set<CreateStrategy> createStrategySet) {
        createStrategies = new HashMap<>();
        createStrategySet.forEach(strategy -> {
            createStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> void createOne(T dto) {
        createStrategy.createOne(dto);
    }

    public <T> void createList(List<T> dtos) {
        createStrategy.createList(dtos);
    }
}
