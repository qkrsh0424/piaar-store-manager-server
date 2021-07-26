package com.piaar_store_manager.server.model.product_option.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;

import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Integer> {
    // TODO(READ) :: ADD NEW 
    @Query(
        "SELECT po FROM ProductOptionEntity po\n"+
        "WHERE po.productCid=:productCid"
    )
    List<ProductOptionEntity> findAll(Integer productCid);

    @Query(
        "SELECT po AS productOption, p AS product, u AS user, pc AS category FROM ProductOptionEntity po\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid = p.productCategoryCid\n"+
        "WHERE po.cid=:cid"
    )
    Optional<ProductOptionProj> selectByCid(Integer cid);

    @Query(
        "SELECT po AS productOption, p AS product, u AS user, pc AS category FROM ProductOptionEntity po\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid = p.productCategoryCid"
    )
    List<ProductOptionProj> selectAll();
}
