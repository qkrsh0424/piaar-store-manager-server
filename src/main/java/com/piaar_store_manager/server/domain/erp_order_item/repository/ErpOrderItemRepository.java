package com.piaar_store_manager.server.domain.erp_order_item.repository;

import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpOrderItemRepository extends JpaRepository<ErpOrderItemEntity, Integer> {
    
}
