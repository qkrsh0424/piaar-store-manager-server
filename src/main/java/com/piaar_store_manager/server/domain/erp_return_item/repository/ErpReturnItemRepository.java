package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;

@Repository
public interface ErpReturnItemRepository extends JpaRepository<ErpReturnItemEntity, Integer>, ErpReturnItemRepositoryCustom {
    Optional<ErpReturnItemEntity> findById(UUID id);
}
