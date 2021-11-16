package com.piaar_store_manager.server.model.product_release.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductReleaseRepository extends JpaRepository<ProductReleaseEntity, Integer>{
    
    @Query(
        "SELECT pr AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy\n"+
        "WHERE pr.cid=:cid"
    )
    Optional<ProductReleaseProj> selectByCid(Integer cid);

    @Query(
        "SELECT pr AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReleaseEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy"
    )
    List<ProductReleaseProj> selectAll();

    List<ProductReleaseEntity> findByProductOptionCid(Integer productOptionCid);

    @Query(
        "SELECT sum(rl.releaseUnit) FROM ProductReleaseEntity rl WHERE rl.productOptionCid=:productOptionCid"
    )
    Integer sumByProductOptionCid(Integer productOptionCid);

    @Query(
        "SELECT prc FROM ProductReleaseEntity prc\n" +
        "WHERE prc.productOptionCid IN :optionCids"
    )
    List<ProductReleaseEntity> selectAllByOptionCids(List<Integer> optionCids);
}
