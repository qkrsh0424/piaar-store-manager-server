package com.piaar_store_manager.server.model.delivery_ready.naver.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;
import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeliveryReadyNaverItemRepository extends JpaRepository<DeliveryReadyNaverItemEntity, Integer>{
    
    /**
     * 배송준비 엑셀 데이터의 상품주문번호를 전체 조회한다.
     * 
     * @return Set::String::
     */
    @Query("SELECT dri.prodOrderNumber FROM DeliveryReadyNaverItemEntity dri")
    Set<String> findAllProdOrderNumber();

    /**
     * 배송준비 엑셀 데이터 중 미출고 데이터를 조회한다.
     * 그 데이터와 연관된 상품정보를 함께 조회한다.
     * DeliveryReadyNaverItemEntity, optionDefaultName, optionManagementName, optionStockUnit, optionNosUniqueCode, optionMemo, prodManagementName, prodManufacturingCode
     * 
     * @return List::DeliveryReadyNaverItemViewProj::
     */
    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, po.memo AS optionMemo, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyNaverItemEntity dri\n"
        // + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.releaseOptionCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE dri.released=false")
    List<DeliveryReadyNaverItemViewProj> findSelectedUnreleased();

    /**
     * 배송준비 엑셀 데이터 중 특정 기간 동안의 출고 데이터를 조회한다.
     * 그 데이터와 연관된 상품정보를 함께 조회한다.
     * DeliveryReadyNaverItemEntity, optionDefaultName, optionManagementName, optionStockUnit, optionNosUniqueCode, optionMemo, prodManagementName, prodManufacturingCode
     * 
     * @param date1 : Date
     * @param date2 : Date
     */
    @Query("SELECT dri AS deliveryReadyItem, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName, po.stockUnit AS optionStockUnit, po.nosUniqueCode AS optionNosUniqueCode, po.memo AS optionMemo, p.managementName AS prodManagementName, p.manufacturingCode AS prodManufacturingCode FROM DeliveryReadyNaverItemEntity dri\n"
        // + "LEFT JOIN ProductOptionEntity po ON dri.optionManagementCode = po.code\n"
        + "LEFT JOIN ProductOptionEntity po ON dri.releaseOptionCode = po.code\n"
        + "LEFT JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "WHERE (dri.releasedAt BETWEEN :date1 AND :date2) AND dri.released=true")
    List<DeliveryReadyNaverItemViewProj> findSelectedReleased(Date date1, Date date2);

    /**
     * 옵션 정보를 전체 조회한다.
     * 
     * @return List::DeliveryReadyItemOptionInfoProj::
     */
    @Query("SELECT po.code AS optionCode, p.defaultName AS prodDefaultName, po.defaultName AS optionDefaultName, po.managementName AS optionManagementName FROM ProductOptionEntity po\n"
        + "JOIN ProductEntity p ON p.cid = po.productCid")
    List<DeliveryReadyItemOptionInfoProj> findAllOptionInfo();

    /**
     * 배송준비 데이터 중 prodName(상품명), optionInfo(옵션명)에 대응하는 데이터를 전체 조회한다.
     * 
     * @return List::DeliveryReadyNaverItemEntity::
     * @param prodName : String
     * @param optionInfo : String
     */
    @Query("SELECT dri FROM DeliveryReadyNaverItemEntity dri WHERE dri.prodName=:prodName AND dri.optionInfo=:optionInfo")
    List<DeliveryReadyNaverItemEntity> findByItems(String prodName, String optionInfo);

    /**
     * 배송준비 데이터 cid값들에 대응하는 데이터를 출고 처리한다.
     * 
     * @return int
     * @param itemCids : List::Integer::
     * @param currentDate : Date
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE delivery_ready_naver_item AS dri SET dri.released=true, dri.released_at=:currentDate WHERE cid IN :itemCids", nativeQuery = true)
    int updateReleasedAtByCid(List<Integer> itemCids, Date currentDate);

    /**
     * 배송준비 데이터 cid값들에 대응하는 데이터를 전체 조회한다.
     * 
     * @param itemCids : List::Integer::
     * @return List::DeliveryReadyNaverItemEntity::
     */
    @Query(
        "SELECT dri FROM DeliveryReadyNaverItemEntity dri\n" +
        "WHERE dri.cid IN :itemCids"
    )
    List<DeliveryReadyNaverItemEntity> selectAllByCids(List<Integer> itemCids);

    /**
     * 상품의 정보들을 모두 추출한다.
     * 네이버, 쿠팡의 발주된 상품의 수량을 조회한다.
     */
    @Query("SELECT pc AS productCategory, p AS product, po AS productOption,\n"
        + "(SELECT CASE WHEN SUM(drni.unit) IS NULL THEN 0 ELSE SUM(drni.unit) END\n"
        + "FROM DeliveryReadyNaverItemEntity drni\n"
        + "WHERE drni.optionManagementCode = po.code AND (drni.createdAt BETWEEN :date1 AND :date2)) AS deliveryReadyNaverSalesUnit,\n"
        + "(SELECT CASE WHEN SUM(drci.unit) IS NULL THEN 0 ELSE SUM(drci.unit) END\n"
        + "FROM DeliveryReadyCoupangItemEntity drci\n"
        + "WHERE drci.optionManagementCode = po.code AND (drci.createdAt BETWEEN :date1 AND :date2)) AS deliveryReadyCoupangSalesUnit\n"
        + "FROM ProductOptionEntity po\n"
        + "JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"
        + "ORDER BY deliveryReadyNaverSalesUnit DESC, deliveryReadyCoupangSalesUnit DESC"
    )
    List<SalesAnalysisItemProj> findSalesAnalysisItem(Date date1, Date date2);

    /**
     * 대량 삭제
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM DeliveryReadyNaverItemEntity drn WHERE drn.id IN :idList")
    void deleteBatchById(List<UUID> idList);
}
