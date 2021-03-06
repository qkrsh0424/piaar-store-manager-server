package com.piaar_store_manager.server.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.user.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(UUID id);
}
