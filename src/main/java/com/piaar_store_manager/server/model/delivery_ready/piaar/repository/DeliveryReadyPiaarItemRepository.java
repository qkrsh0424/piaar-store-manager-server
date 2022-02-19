package com.piaar_store_manager.server.model.delivery_ready.piaar.repository;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.proj.DeliveryReadyPiaarItemViewProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryReadyPiaarItemRepository extends JpaRepository<DeliveryReadyPiaarItemEntity, Integer> {
    
    List<DeliveryReadyPiaarItemEntity> findByCreatedBy(UUID userId);

    // @Query("SELECT dri AS deliveryReadyItem, p.defaultName AS prodDefaultName, p.managementName AS prodManagementName, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, pc.name AS categoryName FROM DeliveryReadyPiaarItemEntity dri\n"
    //     + "LEFT JOIN ProductOptionEntity po ON JSON_UNQUOTE(JSON_EXTRACT(dri.uploadDetail, '$.details[19].cellValue')) = po.code\n"
    //     + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
    //     + "LEFT JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"
    //     + "WHERE dri.createdBy=:userId")
    // List<DeliveryReadyPiaarItemViewProj> findMappingDataByPiaarOptionCodeAndUser(UUID userId);

    @Query("SELECT dri AS deliveryReadyItem, p.defaultName AS prodDefaultName, p.managementName AS prodManagementName, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, pc.name AS categoryName FROM DeliveryReadyPiaarItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.optionCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "LEFT JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"
        + "WHERE dri.createdBy=:userId"
    )
    List<DeliveryReadyPiaarItemViewProj> findMappingDataByPiaarOptionCodeAndUser(UUID userId);


    @Query(
        "SELECT dri FROM DeliveryReadyPiaarItemEntity dri\n" +
        "WHERE dri.id IN :itemIdList"
    )
    List<DeliveryReadyPiaarItemEntity> selectAllByIdList(List<UUID> itemIdList);

    @Query(
        "SELECT dri FROM DeliveryReadyPiaarItemEntity dri\n" +
        "WHERE dri.releasedYn ='y'"
    )
    List<DeliveryReadyPiaarItemEntity> findAllByReleased();
}
