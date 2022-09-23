package com.piaar_store_manager.server.domain.product_receive.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;

@Repository
public interface ProductReceiveCustomJdbc {
    void jdbcBulkInsert(List<ProductReceiveEntity> entities);
}
