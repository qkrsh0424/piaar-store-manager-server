package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;

@Repository
public interface ErpReturnItemRepository extends JpaRepository<ErpReturnItemEntity, Integer>, ErpReturnItemRepositoryCustom {
    Optional<ErpReturnItemEntity> findById(UUID id);

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM ErpReturnItemEntity item\n" +
        "WHERE item.id IN :ids"
    )
    void deleteAllById(List<UUID> ids);
}
