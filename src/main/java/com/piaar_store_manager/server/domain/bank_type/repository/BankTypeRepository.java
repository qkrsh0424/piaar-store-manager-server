package com.piaar_store_manager.server.domain.bank_type.repository;

import com.piaar_store_manager.server.domain.bank_type.entity.BankTypeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTypeRepository extends JpaRepository<BankTypeEntity, Integer>{
    
}
