package com.piaar_store_manager.server.model.delivery_ready.coupang.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyCoupangItemRepository extends JpaRepository<DeliveryReadyCoupangItemEntity, Integer> {
    
    @Query("SELECT dri.prodOrderNumber FROM DeliveryReadyCoupangItemEntity dri")
    Set<String> findAllProdOrderNumber();

    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyCoupangItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE dri.released=false")
    List<DeliveryReadyCoupangItemViewProj> findSelectedUnreleased();

    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyCoupangItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE (dri.releasedAt BETWEEN :date1 AND :date2) AND dri.released=true")
    List<DeliveryReadyCoupangItemViewProj> findSelectedReleased(Date date1, Date date2);
}
