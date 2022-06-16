package com.piaar_store_manager.server.domain.sub_option_code.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;

@Repository
public interface SubOptionCodeRepository extends JpaRepository<SubOptionCodeEntity, Integer>{
    List<SubOptionCodeEntity> findByProductOptionId(UUID productOptionId);
    Optional<SubOptionCodeEntity> findById(UUID id);
    Optional<SubOptionCodeEntity> findBySubOptionCode(String subOptionCode);
}
