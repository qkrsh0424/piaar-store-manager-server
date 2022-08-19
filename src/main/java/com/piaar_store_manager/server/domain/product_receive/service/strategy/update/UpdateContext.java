package com.piaar_store_manager.server.domain.product_receive.service.strategy.update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;

// @Component
public class UpdateContext {
    private ProductReceiveObjectType objectType;
    private UpdateStrategy updateStrategy;

    private Map<ProductReceiveObjectType, UpdateStrategy> updateStrategies;

    @Autowired
    public UpdateContext(Set<UpdateStrategy> updateStrategySet) {
        makeUpdateStrategies(updateStrategySet);
    }

    public void setUpdateStrategy(String type) {
        objectType = ProductReceiveObjectType.getObjectType(type);
        updateStrategy = updateStrategies.get(objectType);
    }

    public void makeUpdateStrategies(Set<UpdateStrategy> updateStrategySet) {
        updateStrategies = new HashMap<>();
        updateStrategySet.forEach(strategy -> {
            updateStrategies.put(strategy.findObjectType(), strategy);
        });
    }

    public <T> void changeOne(T dto) {
        updateStrategy.changeOne(dto);
    }

    public <T> void changeList(List<T> dtos) {
        updateStrategy.changeList(dtos);
    }

    public <T> void patchOne(T dto) {
        updateStrategy.patchOne(dto);
    }
}
