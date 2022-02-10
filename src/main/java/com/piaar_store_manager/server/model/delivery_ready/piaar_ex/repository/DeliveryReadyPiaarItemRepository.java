package com.piaar_store_manager.server.model.delivery_ready.piaar_ex.repository;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.piaar_ex.entity.DeliveryReadyPiaarItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyPiaarItemRepository extends JpaRepository<DeliveryReadyPiaarItemEntity, Integer> {
    
    @Query(
        "SELECT pi FROM DeliveryReadyPiaarItemEntity pi\n" + 
        "WHERE pi.createdBy=:userId"
    )
    List<DeliveryReadyPiaarItemEntity> searchOrderListByUser(UUID userId);
}
