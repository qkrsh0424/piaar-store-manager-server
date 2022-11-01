package com.piaar_store_manager.server.domain.option_package.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OptionPackageRepository extends JpaRepository<OptionPackageEntity, Integer> {

    Optional<OptionPackageEntity> findById(UUID id);

    /**
     * parentOptionId에 대응되는 option package 데이터를 모두 조회한다. (단일 옵션에 대한 패키지 리스트 조회)
     * 
     * @param parentOptionId : UUID
     * @return List::OptionPackageEntity::
     */
    @Query(
        "SELECT op FROM OptionPackageEntity op\n"
        + "WHERE op.parentOptionId =:parentOptionId"
    )
    List<OptionPackageEntity> findAllByParentOptionId(@Param("parentOptionId") UUID parentOptionId);

    /**
     * parentOptionIdList에 대응되는 option package 데이터를 모두 조회한다. (다중 옵션에 대한 패키지 리스트 조회)
     * 
     * @param parentOptionIdList : List::UUID::
     * @return List::OptionPackageEntity::
     */
    @Query(
        "SELECT op FROM OptionPackageEntity op\n"
        + "WHERE op.parentOptionId IN :parentOptionIdList"
    )
    List<OptionPackageEntity> findAllByParentOptionIdList(@Param("parentOptionIdList") List<UUID> parentOptionIdList);

    /**
     * parentOptionId에 대응되는 option package 데이터를 모두 제거한다. (단일 옵션에 대한 패키지 리스트 전체 제거))
     * 
     * @param parentOptionId : UUID
     */
    @Modifying
    @Transactional
    @Query(
            "DELETE FROM OptionPackageEntity  op\n"
            + "WHERE op.parentOptionId = :parentOptionId"
    )
    void deleteBatchByParentOptionId(@Param("parentOptionId") UUID parentOptionId);
}
