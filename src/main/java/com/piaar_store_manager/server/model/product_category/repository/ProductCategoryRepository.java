package com.piaar_store_manager.server.model.product_category.repository;

import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Integer> {

}
