package com.piaar_store_manager.server.domain.product_option.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Integer>, ProductOptionRepositoryCustom {
    
    Optional<ProductOptionEntity> findById(UUID id);
    
    /**
     * cid값에 대응하는 option, option과 Many To One Join(m2oj) 연관관계에 놓여있는 product, user, category
     * 
     * @param cid : Integer
     * @return ProductOptionProj
     */
    @Query(
        "SELECT po AS productOption, p AS product, u AS user, pc AS category FROM ProductOptionEntity po\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN UserEntity u ON po.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid = p.productCategoryCid\n"+
        "WHERE po.cid=:cid"
    )
    Optional<ProductOptionProj> searchOneM2OJ(Integer cid);

    // FIX : Added "ORDER BY" query for product_option.created_at ASC
    /**
     * 모든 option, option과 Full Join(fj) 연관관계에 놓여있는 product, user, category
     * 
     * @return List::ProductOptionProj::
     */
    @Query(
        "SELECT po AS productOption, p AS product, u AS user, pc AS category FROM ProductOptionEntity po\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN UserEntity u ON po.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid = p.productCategoryCid\n"+
        "ORDER BY po.createdAt ASC"
    )
    List<ProductOptionProj> searchListM2OJ();

    /**
     * code값에 대응되는 option을 조회한다.
     * 
     * @param code : String
     * @return Optional::ProductOptionEntity::
     */
    Optional<ProductOptionEntity> findByCode(String code);

    /**
     * ProductOption 데이터의 code들에 대응하는 옵션데이터를 조회한다.
     * 
     * @param codes : List::String::
     * @return List::ProductOptionEntity::
     */
    @Query(
        "SELECT po FROM ProductOptionEntity po\n" +
        "WHERE po.code IN :codes"
    )
    List<ProductOptionEntity> selectListByCodes(List<String> codes);

    /**
     * Product cid에 대응하는 상품옵션 데이터를 조회한다.
     * 
     * @param productCid : Integer
     * @return List::ProductOptionEntity::
     */
    List<ProductOptionEntity> findByProductCid(Integer productCid);

    List<ProductOptionEntity> findByProductId(UUID productId);

    /**
     * 다중 Product cid에 대응하는 상품옵션 데이터를 모두 조회한다.
     * 
     * @param productCids : List::Integer::
     * @return List::ProductOptionEntity::
     */
    @Query(
        "SELECT po FROM ProductOptionEntity po\n" +
        "WHERE po.productCid IN :productCids"
    )
    List<ProductOptionEntity> searchListByProductCids(@Param("productCids") List<Integer> productCids);

    /**
     * 다중 ProductOption cid에 대응하는 옵션데이터의 재고수량을 계산한다.
     * option cid값에 대응하는 입고데이터의 모든 수량합을 조회한다.
     * option cid값에 대응하는 출고데이터의 모든 수량합을 조회한다.
     * 
     * @param optionCids : List::Integer::
     * @return po.cid, sum(receive_unit), sum(release_unit)
     */
    @Query(value="SELECT po.cid AS cid, \n" +
                "(SELECT SUM(prl.release_unit) FROM product_release prl WHERE po.cid=prl.product_option_cid) AS releasedSum, \n" + 
                "(SELECT SUM(prc.receive_unit) FROM product_receive prc WHERE po.cid=prc.product_option_cid) AS receivedSum \n" +
                "FROM product_option po WHERE po.cid IN :optionCids", nativeQuery = true)
    List<Tuple> sumStockUnitByOption(@Param("optionCids") List<Integer> optionCids);

    @Query(
        "SELECT po FROM ProductOptionEntity po\n" +
        "WHERE po.cid IN :cids"
    )
    List<ProductOptionEntity> findAllByCids(@Param("cids") List<Integer> cids);

    /**
     * 상품의 정보들을 모두 추출한다.
     * 네이버, 쿠팡, 피아르의 발주된 상품 수량을 조회한다.
     * 
     * @param date1 : LocalDateTime
     * @param date2 : LocalDateTime
     * @return List::SalesAnalysisItemProj::
     */
    @Query("SELECT pc AS productCategory, p AS product, po AS productOption,\n"
        + "(SELECT CASE WHEN SUM(eoi.unit) IS NULL THEN 0 ELSE SUM(eoi.unit) END\n"
        + "FROM ErpOrderItemEntity eoi\n"
        + "WHERE eoi.optionCode = po.code AND (eoi.createdAt BETWEEN :date1 AND :date2)) AS erpSalesUnit\n"
        + "FROM ProductOptionEntity po\n"
        + "JOIN ProductEntity p ON po.productCid = p.cid\n"
        + "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"
        + "ORDER BY erpSalesUnit DESC"
    )
    List<SalesAnalysisItemProj> findSalesAnalysisItem(@Param("date1") LocalDateTime date1, @Param("date2") LocalDateTime date2);

    @Modifying
    @Transactional
    @Query(
        "DELETE FROM ProductOptionEntity po\n"
        + "WHERE po.id IN :ids"
    )
    void deleteBatch(@Param("ids") List<UUID> ids);
}
