package com.piaar_store_manager.server.model.product.repository;

import java.util.Optional;

import com.piaar_store_manager.server.model.product.entity.ProductJEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJRepository extends JpaRepository<ProductJEntity, Integer>{
}
