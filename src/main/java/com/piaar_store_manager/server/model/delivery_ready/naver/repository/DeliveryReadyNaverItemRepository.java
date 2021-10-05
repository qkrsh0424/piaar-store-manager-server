package com.piaar_store_manager.server.model.delivery_ready.naver.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyNaverItemRepository extends JpaRepository<DeliveryReadyNaverItemEntity, Integer>{
    
    @Query("SELECT dri.prodOrderNumber FROM DeliveryReadyNaverItemEntity dri")
    Set<String> findAllProdOrderNumber();

    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyNaverItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE dri.released=false")
    List<DeliveryReadyNaverItemViewProj> findSelectedUnreleased();

    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyNaverItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE (dri.releasedAt BETWEEN :date1 AND :date2) AND dri.released=true")
    List<DeliveryReadyNaverItemViewProj> findSelectedReleased(Date date1, Date date2);

    @Query("SELECT po.code AS optionCode, p.defaultName AS prodDefaultName, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName FROM ProductOptionEntity po\n"
        + "JOIN ProductEntity p ON p.cid = po.productCid")
    List<DeliveryReadyItemOptionInfoProj> findAllOptionInfo();

    @Query("SELECT dri FROM DeliveryReadyNaverItemEntity dri WHERE dri.prodName=:prodName AND dri.optionInfo=:optionInfo")
    List<DeliveryReadyNaverItemEntity> findByItems(String prodName, String optionInfo);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE delivery_ready_naver_item AS dri SET dri.released=true, dri.released_at=:currentDate WHERE cid IN :cidList", nativeQuery = true)
    int updateReleasedAtByCid(List<Integer> cidList, Date currentDate);
}