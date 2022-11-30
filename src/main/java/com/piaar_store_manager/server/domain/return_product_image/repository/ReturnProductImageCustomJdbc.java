package com.piaar_store_manager.server.domain.return_product_image.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;

@Repository
public interface ReturnProductImageCustomJdbc {
    void jdbcBulkInsert(List<ReturnProductImageEntity> entities);
}
