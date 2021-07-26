package com.piaar_store_manager.server.model.product.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.entity.ProductJEntity;
import com.piaar_store_manager.server.model.product.proj.ProductProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    // TODO(READ) :: ADD NEW 
    @Query(
        "SELECT p AS product, u AS user, pc AS category FROM ProductEntity p\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"+
        "WHERE p.cid=:cid"
    )
    Optional<ProductProj> selectByCid(Integer cid);

    @Query(
        "SELECT p AS product, u AS user, pc AS category FROM ProductEntity p\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid"
    )
    List<ProductProj> selectAll();
}
