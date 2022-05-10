package com.piaar_store_manager.server.domain.erp_order_item.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;

@Repository
public interface ErpOrderItemCustomJdbc {
    void jdbcBulkInsert(List<ErpOrderItemEntity> entities);
}
