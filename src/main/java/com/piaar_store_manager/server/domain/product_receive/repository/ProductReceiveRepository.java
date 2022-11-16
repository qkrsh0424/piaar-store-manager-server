package com.piaar_store_manager.server.domain.product_receive.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductReceiveRepository extends JpaRepository<ProductReceiveEntity, Integer>, ProductReceiveRepositoryCustom {
    
    Optional<ProductReceiveEntity> findById(UUID id);

    /**
     * 다중 cid값에 대응되는 receive를 모두 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReceiveEntity::
     */
    @Query(
        "SELECT pr\n" +
        "FROM ProductReceiveEntity pr\n" +
        "WHERE pr.cid IN :cids"
    )
    List<ProductReceiveEntity> selectAllByCid(List<Integer> cids);

    /**
     * productOptionCid값에 대응되는 receive를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveEntity::
     */
    List<ProductReceiveEntity> findByProductOptionCid(Integer productOptionCid);

    /**
     * 다중 erpReturnItemId값에 대응되는 receive를 모두 제거한다.
     * 
     * @param ids : List::UUID::
     */
    @Transactional
    @Modifying
    @Query(
            "DELETE FROM ProductReceiveEntity rs\n" + 
            "WHERE rs.erpOrderItemId IN :ids"
    )
    void deleteByErpOrderItemIds(List<UUID> ids);
}
