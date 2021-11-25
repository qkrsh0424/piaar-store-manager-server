package com.piaar_store_manager.server.model.product.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.proj.ProductProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    
    /**
     * 단일 Product cid에 대응하는 상품데이터의 M2OJ 관계인(상품, 카테고리, 유저) 데이터를 조회한다.
     * 
     * @return Optional::ProductProj::
     * @param cid : Integer
     */
    @Query(
        "SELECT p AS product, u AS user, pc AS category FROM ProductEntity p\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"+
        "WHERE p.cid=:cid"
    )
    Optional<ProductProj> selectByCid(Integer cid);

    // FIX : Added "ORDER BY" query for product.created_at ASC
    /**
     * 상품데이터의 M2OJ 관계인(상품, 카테고리, 유저) 데이터를 모두 조회한다.
     * 
     * @return List::ProductProj::
     */
    @Query(
        "SELECT p AS product, u AS user, pc AS category FROM ProductEntity p\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"+
        "ORDER BY p.createdAt ASC"
    )
    List<ProductProj> selectAll();
    
    /**
     * ProductCategory cid에 대응하는 Product 데이터를 조회한다.
     * 
     * @return List::ProductEntity::
     * @param productCategoryCid : Integer
     */
    List<ProductEntity> findByProductCategoryCid(Integer productCategoryCid);
}
