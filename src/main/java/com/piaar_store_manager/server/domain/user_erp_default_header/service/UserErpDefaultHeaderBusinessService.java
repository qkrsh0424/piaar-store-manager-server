package com.piaar_store_manager.server.domain.user_erp_default_header.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.domain.user_erp_default_header.dto.UserErpDefaultHeaderDto;
import com.piaar_store_manager.server.domain.user_erp_default_header.entity.UserErpDefaultHeaderEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserErpDefaultHeaderBusinessService {
    private final UserErpDefaultHeaderService userErpDefaultHeaderService;
    private final UserService userService;

    public UserErpDefaultHeaderDto searchOne() {
        UUID USER_ID = userService.getUserId();
        UserErpDefaultHeaderEntity entity = userErpDefaultHeaderService.findByUserId(USER_ID);
        
        if(entity == null) {
            return null;
        }

        UserErpDefaultHeaderDto dto = UserErpDefaultHeaderDto.toDto(entity);
        return dto;
    }
    
    public void createOne(UserErpDefaultHeaderDto dto) {
        UUID USER_ID = userService.getUserId();
        UserErpDefaultHeaderEntity savedEntity = userErpDefaultHeaderService.findByUserId(USER_ID);
        
        if(savedEntity == null) {
            UserErpDefaultHeaderDto defaultHeaderDto = UserErpDefaultHeaderDto.builder()
                .id(UUID.randomUUID())
                .userId(USER_ID)
                .orderHeaderId(dto.getOrderHeaderId())
                .salesHeaderId(dto.getSalesHeaderId())
                .releaseCompleteHeaderId(dto.getReleaseCompleteHeaderId())
                .build();
                
            UserErpDefaultHeaderEntity entity = UserErpDefaultHeaderEntity.toEntity(defaultHeaderDto);
            userErpDefaultHeaderService.saveAndModify(entity);
        }
    }

    public void changeOne(UserErpDefaultHeaderDto dto) {
        UUID USER_ID = userService.getUserId();
        UserErpDefaultHeaderEntity entity = userErpDefaultHeaderService.findByUserId(USER_ID);

        if(dto.getOrderHeaderId() != null) {
            entity.setOrderHeaderId(dto.getOrderHeaderId());
        }
        if(dto.getSalesHeaderId() != null) {
            entity.setSalesHeaderId(dto.getSalesHeaderId());
        }
        if(dto.getReleaseCompleteHeaderId() != null) {
            entity.setReleaseCompleteHeaderId(dto.getReleaseCompleteHeaderId());
        }

        userErpDefaultHeaderService.saveAndModify(entity);
    }
}
