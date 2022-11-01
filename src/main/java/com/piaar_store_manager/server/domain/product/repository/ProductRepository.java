package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, ProductRepositoryCustom {
    
    Optional<ProductEntity> findById(UUID id);

    /**
     * cid값에 대응되는 product, product와 Many To One Join(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @param cid : Integer
     * @return Optional::ProductProj::
     */
    @Query(
        "SELECT p AS product, u AS user, pc AS category FROM ProductEntity p\n"+
        "JOIN UserEntity u ON p.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON p.productCategoryCid = pc.cid\n"+
        "WHERE p.cid=:cid"
    )
    Optional<ProductProj> searchOneM2OJ(@Param("cid") Integer cid);

    /**
     * 모든 product, product와 Many To One Join(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     * 조회된 데이터를 생성 시간 오름차순으로 정렬한다.
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
     * 상품의 등록된 카테고리 cid값이 productCategoryCid에 대응되는 데이터를 조회한다.
     *
     * @param productCategoryCid : Integer
     * @return List::ProductEntity::
     */
    List<ProductEntity> findByProductCategoryCid(@Param("productCategoryCid") Integer productCategoryCid);
}
