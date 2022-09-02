package com.piaar_store_manager.server.domain.erp_return_item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;

@Repository
public interface ErpReturnItemRepository extends JpaRepository<ErpReturnItemEntity, Integer> {
    
}
