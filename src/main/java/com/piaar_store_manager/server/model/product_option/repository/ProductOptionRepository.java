package com.piaar_store_manager.server.model.product_option.repository;

import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Integer> {

}
