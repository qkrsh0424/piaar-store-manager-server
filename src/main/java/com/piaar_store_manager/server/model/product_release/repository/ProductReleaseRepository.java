package com.piaar_store_manager.server.model.product_release.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductReleaseRepository extends JpaRepository<ProductReleaseEntity, Integer>{
    
    /**
     * ProductRelease cid에 대응하는 출고데이터의 M2OJ 관계인(상품, 상품옵션, 카테고리, 출고, 유저) 데이터를 조회한다.
     * 
     * @param cid
     * @return ProductReleaseProj
     */
    @Query(
        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pl\n"+
        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pl.createdBy\n"+
        "WHERE pl.cid=:cid"
    )
    Optional<ProductReleaseProj> selectByCid(Integer cid);

    /**
     * ProductRelease 출고데이터의 M2OJ 관계인(상품, 상품옵션, 카테고리, 출고, 유저) 데이터를 모두 조회한다. 
     * 
     * @return ProductReleaseProj
     */
    @Query(
        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pl\n"+
        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pl.createdBy"
    )
    List<ProductReleaseProj> selectAll();

    /**
     * ProductRelease cid값에 대응하는 출고데이터를 조회한다.
     * 
     * @param cids
     * @return List::ProductReleaseEntity
     */
    @Query(
        "SELECT pl\n" +
        "FROM ProductReleaseEntity pl\n" +
        "WHERE pl.cid IN :cids"
    )
    List<ProductReleaseEntity> selectAllByCid(List<Integer> cids);

    /**
     * productOption cid값에 대응하는 ProductRelease 출고데이터를 조회한다.
     * 
     * @param productOptionCid : Integer
     * @return List::ProductReleaseEntity::
     */
    List<ProductReleaseEntity> findByProductOptionCid(Integer productOptionCid);

    /**
     * productOpiton cid값에 대응하는 ProductRelease 출고데이터의 출고수량을 합하여 반환한다.
     * 
     * @param productOptionCid : Integer
     * @return Integer
     */
    // TODO :: ProductOption 리팩토링하면서 삭제
    @Query(
        "SELECT sum(pl.releaseUnit) FROM ProductReleaseEntity pl WHERE pl.productOptionCid=:productOptionCid"
    )
    Integer sumByProductOptionCid(Integer productOptionCid);
}
