package com.piaar_store_manager.server.model.product_receive.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReceiveRepository extends JpaRepository<ProductReceiveEntity, Integer>{
    
    /**
     * cid값에 대응되는 receive, receive와 Many To One Join(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param cid : Integer
     * @return Optional::ProductReceiveProj::
     */
    @Query(
        "SELECT pr AS productReceive, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReceiveEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy\n"+
        "WHERE pr.cid=:cid"
    )
    Optional<ProductReceiveProj> searchOneM2OJ(Integer cid);

    /**
     * 모든 receive, receive와 Many To One Join(m2oj) 연관관계에 놓여있는 product, option ,category, user를 함께 조회한다.
     * 
     * @return List::ProductReceiveProj::
     */
    @Query(
        "SELECT pr AS productReceive, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReceiveEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy"
    )
    List<ProductReceiveProj> selectAll();

    /**
     * startDate와 endDate기간 사이에 등록된 모든 receive, receive와 Many To One Join(m2oj) 연관관계에 놓여있는 product, option ,category, user를 함께 조회한다.
     * 조회된 데이터를 생성 시간 내림차순으로 정렬한다.
     * 
     * @return List::ProductReceiveProj::
     */
    @Query(
        "SELECT pr AS productReceive, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReceiveEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy\n"+
        "WHERE pr.createdAt BETWEEN :startDate AND :endDate\n"+
        "ORDER By pr.createdAt DESC"
    )
    List<ProductReceiveProj> selectAll(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 다중 cid값에 대응되는 receive를 모두 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReceiveEntity::
     */
    @Query(
        "SELECT pr\n" +
        "FROM ProductReceiveEntity pr\n" +
        "WHERE pr.cid IN :cids"
    )
    List<ProductReceiveEntity> selectAllByCid(List<Integer> cids);

    /**
     * productOptionCid값에 대응되는 receive를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveEntity::
     */
    List<ProductReceiveEntity> findByProductOptionCid(Integer productOptionCid);
}
