package com.piaar_store_manager.server.domain.product_receive.service.strategy.create;

import java.util.List;

import com.piaar_store_manager.server.domain.product_receive.service.strategy.type.ObjectTypeStrategy;

public interface CreateStrategy extends ObjectTypeStrategy {
    <T> void createOne(T dto);
    <T> void createList(List<T> dtos);
}
