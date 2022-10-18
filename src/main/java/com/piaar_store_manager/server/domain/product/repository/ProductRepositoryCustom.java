package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product.proj.ProductManagementProj;

@Repository
public interface ProductRepositoryCustom {
    List<ProductManagementProj> qfindAllFJ(Map<String, Object> params);
    Page<ProductManagementProj> qfindAllFJByPage(Map<String, Object> params, Pageable pageable);
}
