package com.piaar_store_manager.server.domain.delivery_ready.coupang.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.piaar_store_manager.server.domain.delivery_ready.common.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeliveryReadyCoupangItemRepository extends JpaRepository<DeliveryReadyCoupangItemEntity, Integer> {
    
    @Query("SELECT dri.prodOrderNumber FROM DeliveryReadyCoupangItemEntity dri")
    Set<String> findAllProdOrderNumber();

    /**
     * 배송준비 엑셀 데이터 중 미출고 데이터를 조회한다.
     * 그 데이터와 연관된 상품정보를 함께 조회한다.
     * DeliveryReadyCoupangItemEntity, optionDefaultName, optionManagementName, optionStockUnit, optionNosUniqueCode, optionMemo, prodManagementName, prodManufacturingCode
     * 
     * @return List::DeliveryReadyCoupangItemViewProj::
     */
    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, po.memo AS optionMemo, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyCoupangItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.releaseOptionCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE dri.released=false")
    List<DeliveryReadyCoupangItemViewProj> findUnreleasedItemList();

    /**
     * 배송준비 엑셀 데이터 중 특정 기간 동안의 출고 데이터를 조회한다.
     * 그 데이터와 연관된 상품정보를 함께 조회한다.
     * DeliveryReadyCoupangItemEntity, optionDefaultName, optionManagementName, optionStockUnit, optionNosUniqueCode, optionMemo, prodManagementName, prodManufacturingCode
     * 
     * @param date1 : LocalDateTime
     * @param date2 : LocalDateTime
     */
    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, po.memo AS optionMemo, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyCoupangItemEntity dri\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.releaseOptionCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE (dri.releasedAt BETWEEN :date1 AND :date2) AND dri.released=true")
    List<DeliveryReadyCoupangItemViewProj> findReleasedItemList(LocalDateTime date1, LocalDateTime date2);

    /**
     * 옵션 정보를 전체 조회한다.
     * optionCode, prodDefaultName, optionDefaultName, optionManagementName
     * 
     * @return List::DeliveryReadyItemOptionInfoProj::
     */
    @Query("SELECT po.code AS optionCode, p.defaultName AS prodDefaultName, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName FROM ProductOptionEntity po\n"
        + "JOIN ProductEntity p ON p.cid = po.productCid")
    List<DeliveryReadyItemOptionInfoProj> findAllOptionInfo();

    /**
     * 배송준비 데이터 중 prodName(상품명), optionInfo(옵션명)에 대응하는 데이터를 전체 조회한다.
     * 
     * @param prodName : String
     * @param optionInfo : String
     * @return List::DeliveryReadyCoupangItemEntity::
     */
    @Query("SELECT dri FROM DeliveryReadyCoupangItemEntity dri WHERE dri.prodName=:prodName AND dri.optionInfo=:optionInfo")
    List<DeliveryReadyCoupangItemEntity> findByItems(String prodName, String optionInfo);

    /**
     * 배송준비 데이터 cid값들에 대응하는 데이터를 출고 처리한다.
     * 
     * @return int
     * @param itemCids : List::Integer::
     * @param currentDate : Date
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE delivery_ready_coupang_item AS dri SET dri.released=true, dri.released_at=:currentDate WHERE cid IN :itemCids", nativeQuery = true)
    int updateReleasedInfoByCid(List<Integer> itemCids, LocalDateTime currentDate);

    /**
     * 배송준비 데이터 cid값들에 대응하는 데이터를 전체 조회한다.
     * 
     * @param itemCids : List::Integer::
     * @return List::DeliveryReadyCoupangItemEntity::
     */
    @Query(
        "SELECT dri FROM DeliveryReadyCoupangItemEntity dri\n" +
        "WHERE dri.cid IN :itemCids"
    )
    List<DeliveryReadyCoupangItemEntity> selectAllByCids(List<Integer> itemCids);

    /**
     * 대량 삭제
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DeliveryReadyCoupangItemEntity drn WHERE drn.id IN :idList"
    )
    void deleteBatchById(List<UUID> idList);
}
