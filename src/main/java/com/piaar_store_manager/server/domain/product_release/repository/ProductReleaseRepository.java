package com.piaar_store_manager.server.domain.product_release.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductReleaseRepository extends JpaRepository<ProductReleaseEntity, Integer>{
    
    Optional<ProductReleaseEntity> findById(UUID id);
    
   /**
     * cid값에 대응되는 release, release와 Many To One Join(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param cid : Integer
     * @return Optional::ProductReleaseProj::
     */
    // deprecated
    @Query(
        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pl\n"+
        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pl.createdBy\n"+
        "WHERE pl.cid=:cid"
    )
    Optional<ProductReleaseProj> searchOneM2OJ(Integer cid);

    @Query(
        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pl\n"+
        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pl.createdBy\n"+
        "WHERE pl.id=:id"
    )
    Optional<ProductReleaseProj> searchOneM2OJ(UUID id);

    /**
     * 모든 release, release와 Many To One Join(m2oj) 연관관계에 놓여있는 product, option ,category, user를 함께 조회한다.
     * 
     * @return List::ProductReleaseProj::
     */
    @Query(
        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pl\n"+
        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pl.createdBy"
    )
    List<ProductReleaseProj> searchListM2OJ();

    /**
     * startDate와 endDate기간 사이에 등록된 모든 release, release와 Many To One Join(m2oj) 연관관계에 놓여있는 product, option ,category, user를 함께 조회한다.
     * 조회된 데이터를 생성 시간 내림차순으로 정렬한다.
     * 
     * @return List::ProductReleaseProj::
     */
    @Query(
        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pl\n"+
        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pl.createdBy\n"+
        "WHERE pl.createdAt BETWEEN :startDate AND :endDate\n"+
        "ORDER By pl.createdAt DESC"
    )
    List<ProductReleaseProj> searchListM2OJ(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 다중 cid값에 대응되는 release를 모두 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReleaseEntity::
     */
    @Query(
        "SELECT pl\n" +
        "FROM ProductReleaseEntity pl\n" +
        "WHERE pl.cid IN :cids"
    )
    List<ProductReleaseEntity> selectAllByCid(List<Integer> cids);

    /**
     * productOptionCid값에 대응되는 release를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReleaseEntity::
     */
    List<ProductReleaseEntity> findByProductOptionCid(Integer productOptionCid);

    /**
     * 다중 erpOrderItemId값에 대응되는 release를 모두 제거한다.
     * 
     * @param ids : List::UUID::
     */
    @Transactional
    @Modifying
    @Query(
            "DELETE FROM ProductReleaseEntity rs\n" + 
            "WHERE rs.erpOrderItemId IN :ids"
    )
    void deleteByErpOrderItemIds(List<UUID> ids);
}
