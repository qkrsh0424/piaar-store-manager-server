package com.piaar_store_manager.server.domain.release_stock.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.piaar_store_manager.server.domain.release_stock.entity.ReleaseStockEntity;

@Repository
public interface ReleaseStockCustomJdbc {
    void jdbcBulkInsert(List<ReleaseStockEntity> entities);
}
