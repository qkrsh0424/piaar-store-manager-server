package com.piaar_store_manager.server.model.option_package.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OptionPackageRepository extends JpaRepository<OptionPackageEntity, Integer> {

    @Query(
        "SELECT op FROM OptionPackageEntity op\n"
        + "WHERE op.parentOptionId =:parentOptionId"
    )
    List<OptionPackageEntity> findAllByParentOptionId(UUID parentOptionId);

    @Query(
        "SELECT op FROM OptionPackageEntity op\n"
        + "WHERE op.parentOptionId IN :parentOptionIdList"
    )
    List<OptionPackageEntity> findAllByParentOptionIdList(List<UUID> parentOptionIdList);

    Optional<OptionPackageEntity> findById(UUID id);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Transactional
    @Query(
        "DELETE FROM OptionPackageEntity op\n"
        + "WHERE op.id IN :idList"
    )
    void deleteBatch(List<UUID> idList);

    @Modifying
    @Transactional
    @Query(
            "DELETE FROM OptionPackageEntity  op\n"
            + "WHERE op.parentOptionId = :parentOptionId"
    )
    void deleteBatchByParentOptionId(UUID parentOptionId);
}
