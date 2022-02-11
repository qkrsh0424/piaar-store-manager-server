package com.piaar_store_manager.server.model.delivery_ready.piaar.repository;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyPiaarItemRepository extends JpaRepository<DeliveryReadyPiaarItemEntity, Integer> {
    
    List<DeliveryReadyPiaarItemEntity> findByCreatedBy(UUID userId);
}
