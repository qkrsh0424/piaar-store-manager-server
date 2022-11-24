package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product.proj.ProductProjection;

@Repository
public interface ProductRepositoryCustom {
    List<ProductProjection.RelatedCategoryAndOptions> qfindAllFJ(Map<String, Object> params);
    Page<ProductProjection.RelatedCategoryAndOptions> qfindAllFJByPage(Map<String, Object> params, Pageable pageable);
    ProductProjection.RelatedCategoryAndOptions qSelectProductAndOptions(UUID productId);
}
