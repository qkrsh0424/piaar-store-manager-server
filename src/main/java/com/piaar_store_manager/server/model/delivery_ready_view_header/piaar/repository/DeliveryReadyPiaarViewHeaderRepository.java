package com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.entity.DeliveryReadyPiaarViewHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyPiaarViewHeaderRepository extends JpaRepository<DeliveryReadyPiaarViewHeaderEntity, Integer> {
    
    @Query(
        "SELECT pv FROM DeliveryReadyPiaarViewHeaderEntity pv\n" + 
        "WHERE pv.createdBy=:userId"    
    )
    Optional<DeliveryReadyPiaarViewHeaderEntity> searchOneByUser(UUID userId);
}
