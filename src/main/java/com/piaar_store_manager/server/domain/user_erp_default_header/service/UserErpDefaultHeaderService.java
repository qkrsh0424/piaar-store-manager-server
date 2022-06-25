package com.piaar_store_manager.server.domain.user_erp_default_header.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.user_erp_default_header.entity.UserErpDefaultHeaderEntity;
import com.piaar_store_manager.server.domain.user_erp_default_header.repository.UserErpDefaultHeaderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserErpDefaultHeaderService {
    private final UserErpDefaultHeaderRepository userErpDefaultHeaderRepository;

    public UserErpDefaultHeaderEntity findByUserId(UUID userId) {
        Optional<UserErpDefaultHeaderEntity> entityOpt = userErpDefaultHeaderRepository.findByUserId(userId);

        if(entityOpt.isPresent()) {
            return entityOpt.get();
        }else {
            return null;
        }
    }

    public void saveAndModify(UserErpDefaultHeaderEntity entity) {
        userErpDefaultHeaderRepository.save(entity);
    }
}
