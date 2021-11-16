package com.piaar_store_manager.server.model.product_receive.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductReceiveRepository extends JpaRepository<ProductReceiveEntity, Integer>{
    
    @Query(
        "SELECT pr AS productReceive, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReceiveEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy\n"+
        "WHERE pr.cid=:cid"
    )
    Optional<ProductReceiveProj> selectByCid(Integer cid);

    @Query(
        "SELECT pr AS productReceive, po AS productOption, p AS product, u AS user, pc AS category FROM ProductReceiveEntity pr\n"+
        "JOIN ProductOptionEntity po ON po.cid=pr.productOptionCid\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
        "JOIN UserEntity u ON u.id=pr.createdBy"
    )
    List<ProductReceiveProj> selectAll();

    List<ProductReceiveEntity> findByProductOptionCid(Integer productOptionCid);

    @Query(
        "SELECT sum(rc.receiveUnit) FROM ProductReceiveEntity rc WHERE rc.productOptionCid=:productOptionCid"
    )
    Integer sumByProductOptionCid(Integer productOptionCid);

    @Query(
        "SELECT prc FROM ProductReceiveEntity prc\n" +
        "WHERE prc.productOptionCid IN :optionCids"
    )
    List<ProductReceiveEntity> selectAllByOptionCids(List<Integer> optionCids);
}
