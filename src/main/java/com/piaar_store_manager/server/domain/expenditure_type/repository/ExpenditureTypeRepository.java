package com.piaar_store_manager.server.domain.expenditure_type.repository;

import com.piaar_store_manager.server.domain.expenditure_type.entity.ExpenditureTypeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenditureTypeRepository extends JpaRepository<ExpenditureTypeEntity, Integer>{
    
}
