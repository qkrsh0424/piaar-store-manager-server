package com.piaar_store_manager.server.domain.erp_return_header.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_return_header.entity.ErpReturnHeaderEntity;

@Repository
public interface ErpReturnHeaderRepository extends JpaRepository<ErpReturnHeaderEntity, Integer> {
    Optional<ErpReturnHeaderEntity> findById(UUID id);
}
