package com.piaar_store_manager.server.domain.release_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.domain.release_stock.entity.ReleaseStockEntity;

@Repository
public interface ReleaseStockRepository extends JpaRepository<ReleaseStockEntity, Integer>{
    @Transactional
    @Modifying
    @Query(
            "DELETE FROM ReleaseStockEntity rs\n" +
                    "WHERE rs.erpOrderItemId IN :ids"
    )
    void deleteByErpOrderItemIds(List<UUID> ids);
}
