package com.piaar_store_manager.server.model.product_category.repository;

import java.util.List;

import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryJEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryJRepository extends JpaRepository<ProductCategoryJEntity, Integer>{

}
