package com.piaar_store_manager.server.domain.user_erp_default_header.repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.user_erp_default_header.entity.UserErpDefaultHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserErpDefaultHeaderRepository extends JpaRepository<UserErpDefaultHeaderEntity, Integer> {
    Optional<UserErpDefaultHeaderEntity> findByUserId(UUID userId);
}
