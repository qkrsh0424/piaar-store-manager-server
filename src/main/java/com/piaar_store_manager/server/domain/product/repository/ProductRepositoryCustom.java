package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product.proj.ProductFJProj;

@Repository
public interface ProductRepositoryCustom {
    List<ProductFJProj> qfindAllFJ(Map<String, Object> params);
}
