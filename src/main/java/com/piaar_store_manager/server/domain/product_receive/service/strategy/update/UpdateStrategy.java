package com.piaar_store_manager.server.domain.product_receive.service.strategy.update;

import java.util.List;

import com.piaar_store_manager.server.domain.product_receive.service.strategy.type.ObjectTypeStrategy;

public interface UpdateStrategy extends ObjectTypeStrategy {
    <T> void changeOne(T dto);
    <T> void changeList(List<T> dtos);
    <T> void patchOne(T dto);   
}
