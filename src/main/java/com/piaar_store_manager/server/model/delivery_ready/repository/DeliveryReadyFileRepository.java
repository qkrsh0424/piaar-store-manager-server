package com.piaar_store_manager.server.model.delivery_ready.repository;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyFileRepository extends JpaRepository<DeliveryReadyFileEntity, Integer>{
    
}
