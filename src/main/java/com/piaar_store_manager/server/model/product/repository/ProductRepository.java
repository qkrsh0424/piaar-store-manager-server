package com.piaar_store_manager.server.model.product.repository;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

}
