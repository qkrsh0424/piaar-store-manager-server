package com.piaar_store_manager.server.model.delivery_ready.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemViewProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyItemRepository extends JpaRepository<DeliveryReadyItemEntity, Integer>{
    
    @Query(value = "SELECT prod_order_number FROM delivery_ready_item", nativeQuery = true)
    Set<String> findAllProdOrderNumber();

    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, p.managementName AS prodManagementName FROM DeliveryReadyItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE dri.released=false")
    List<DeliveryReadyItemViewProj> findAllUnreleased();

    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, p.managementName AS prodManagementName FROM DeliveryReadyItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE (dri.releasedAt BETWEEN :date1 AND :date2) AND dri.released=true")
    List<DeliveryReadyItemViewProj> findSelectedReleased(Date date1, Date date2);

    @Query("SELECT dri FROM DeliveryReadyItemEntity dri WHERE dri.id=:itemId")
    Optional<DeliveryReadyItemEntity> findByItemId(UUID itemId);
}
