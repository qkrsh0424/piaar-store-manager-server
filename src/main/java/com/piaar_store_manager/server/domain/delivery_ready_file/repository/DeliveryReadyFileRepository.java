package com.piaar_store_manager.server.domain.delivery_ready_file.repository;

import com.piaar_store_manager.server.domain.delivery_ready_file.entity.DeliveryReadyFileEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyFileRepository extends JpaRepository<DeliveryReadyFileEntity, Integer>{
    
}
