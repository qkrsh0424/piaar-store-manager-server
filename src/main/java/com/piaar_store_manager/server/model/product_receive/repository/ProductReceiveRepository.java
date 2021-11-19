package com.piaar_store_manager.server.model.product_receive.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductReceiveRepository extends JpaRepository<ProductReceiveEntity, Integer>{
    
    /**
     * ProductReceive cid에 대응하는 입고데이터의 M2OJ 관계인(상품, 상품옵션, 카테고리, 입고, 유저) 데이터를 조회한다.
     * 
     * @param cid
     * @return ProductReceiveProj
     */
    @Query(
        "SELECT pr AS productReceive, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReceiveEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy\n"+
        "WHERE pr.cid=:cid"
    )
    Optional<ProductReceiveProj> selectByCid(Integer cid);

    /**
     * ProductReceive 입고데이터의 M2OJ 관계인(상품, 상품옵션, 카테고리, 입고, 유저) 데이터를 모두 조회한다. 
     * 
     * @return ProductReceiveProj
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
     * ProductReceive cid값에 대응하는 입고데이터를 조회한다.
     * 
     * @param cids
     * @return List::ProductReceiveEntity
     */
    @Query(
        "SELECT pr\n" +
        "FROM ProductReceiveEntity pr\n" +
        "WHERE pr.cid IN :cids"
    )
    List<ProductReceiveEntity> selectAllByCid(List<Integer> cids);

    /**
     * productOption cid값에 대응하는 ProductReceive 입고데이터를 조회한다.
     * 
     * @param productOptionCid : Integer
     * @return List::ProductReceiveEntity::
     */
    List<ProductReceiveEntity> findByProductOptionCid(Integer productOptionCid);

    /**
     * productOpiton cid값에 대응하는 ProductReceive 입고데이터의 입고수량을 합하여 반환한다.
     * 
     * @param productOptionCid : Integer
     * @return Integer
     */
    // TODO :: ProductOption 리팩토링하면서 삭제
    @Query(
        "SELECT sum(rc.receiveUnit) FROM ProductReceiveEntity rc WHERE rc.productOptionCid=:productOptionCid"
    )
    Integer sumByProductOptionCid(Integer productOptionCid);
}
