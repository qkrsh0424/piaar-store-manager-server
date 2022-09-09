package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;

@Repository
public interface ErpReturnItemCustomJdbc {
    void jdbcBulkInsert(List<ErpReturnItemEntity> entities);
}
