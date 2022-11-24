package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, ProductRepositoryCustom {
    
    Optional<ProductEntity> findById(UUID id);
    
    /**
     * 상품의 등록된 카테고리 cid값이 productCategoryCid에 대응되는 데이터를 조회한다.
     *
     * @param productCategoryCid : Integer
     * @return List::ProductEntity::
     */
    List<ProductEntity> findByProductCategoryCid(Integer productCategoryCid);
}
