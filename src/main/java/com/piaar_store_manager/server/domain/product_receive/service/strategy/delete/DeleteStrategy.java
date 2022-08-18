package com.piaar_store_manager.server.domain.product_receive.service.strategy.delete;

import java.util.UUID;

import com.piaar_store_manager.server.domain.product_receive.service.strategy.type.ObjectTypeStrategy;

public interface DeleteStrategy extends ObjectTypeStrategy {
    <T> void destroyOne(UUID id);
}
