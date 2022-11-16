package com.piaar_store_manager.server.domain.product_release.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductReleaseRepository extends JpaRepository<ProductReleaseEntity, Integer>, ProductReleaseRepositoryCustom {

    Optional<ProductReleaseEntity> findById(UUID id);

    /**
     * 다중 cid값에 대응되는 release를 모두 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReleaseEntity::
     */
    @Query(
        "SELECT pl\n" +
        "FROM ProductReleaseEntity pl\n" +
        "WHERE pl.cid IN :cids"
    )
    List<ProductReleaseEntity> selectAllByCid(List<Integer> cids);

    /**
     * productOptionCid값에 대응되는 release를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReleaseEntity::
     */
    List<ProductReleaseEntity> findByProductOptionCid(Integer productOptionCid);

    /**
     * 다중 erpOrderItemId값에 대응되는 release를 모두 제거한다.
     * 
     * @param ids : List::UUID::
     */
    @Transactional
    @Modifying
    @Query(
            "DELETE FROM ProductReleaseEntity rs\n" + 
            "WHERE rs.erpOrderItemId IN :ids"
    )
    void deleteByErpOrderItemIds(List<UUID> ids);

    List<ProductReleaseEntity> findByErpOrderItemId(UUID erpOrderItemId);

    @Query(
        "SELECT pl\n" + 
        "FROM ProductReleaseEntity pl\n" + 
        "WHERE pl.erpOrderItemId IN :erpOrderItemIds"
    )
    List<ProductReleaseEntity> findByErpOrderItemIds(List<UUID> erpOrderItemIds);
}
