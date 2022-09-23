package com.piaar_store_manager.server.domain.return_type.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.return_type.entity.ReturnReasonTypeEntity;

@Repository
public interface ReturnReasonTypeRepository extends JpaRepository<ReturnReasonTypeEntity, Integer> {
    
}
