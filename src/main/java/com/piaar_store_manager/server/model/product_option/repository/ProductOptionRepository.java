package com.piaar_store_manager.server.model.product_option.repository;

import java.util.List;

import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
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
}
