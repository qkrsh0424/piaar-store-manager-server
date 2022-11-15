package com.piaar_store_manager.server.domain.sub_option_code.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.sub_option_code.dto.SubOptionCodeDto;
import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubOptionCodeBusinessService {
    private final SubOptionCodeService subOptionCodeService;
    private final UserService userService;

    public List<SubOptionCodeDto> searchListByProductOptionId(UUID optionId) {
        List<SubOptionCodeEntity> entities = subOptionCodeService.searchListByProductOptionId(optionId);
        List<SubOptionCodeDto> dtos = entities.stream().map(entity -> SubOptionCodeDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    public void destroyOne(UUID subOptionCodeId) {
        subOptionCodeService.destroyOne(subOptionCodeId);
    }

    @Transactional
    public void createOne(SubOptionCodeDto dto) {
        UUID USER_ID = userService.getUserId();
        if(dto.getSubOptionCode() == null || dto.getSubOptionCode().isBlank()) {
            throw new CustomInvalidDataException("대체코드는 공백으로 생성할 수 없습니다.");
        }

        // 중복체크
        subOptionCodeService.duplicationCodeCheck(dto.getSubOptionCode());

        SubOptionCodeEntity entity = SubOptionCodeEntity.builder()
            .id(UUID.randomUUID())
            .subOptionCode(dto.getSubOptionCode())
            .memo(dto.getMemo())
            .productOptionId(dto.getProductOptionId())
            // .productOptionCode(dto.getProductOptionCode())
            .createdAt(CustomDateUtils.getCurrentDateTime())
            .createdBy(USER_ID)
            .updatedAt(CustomDateUtils.getCurrentDateTime())
            .updatedBy(USER_ID)
            .build();

        subOptionCodeService.saveAndModify(entity);
    }

    public void updateOne(SubOptionCodeDto dto) {
        UUID USER_ID = userService.getUserId();
        
        SubOptionCodeEntity entity = subOptionCodeService.searchOne(dto.getId());
        if(dto.getSubOptionCode() == null || dto.getSubOptionCode().isBlank()) {
            throw new CustomInvalidDataException("대체코드는 공백으로 생성할 수 없습니다.");
        }

        // 동일한 옵션코드로 등록 하지 않는 경우
        if(!entity.getSubOptionCode().equals(dto.getSubOptionCode())){
            // 중복체크
            subOptionCodeService.duplicationCodeCheck(dto.getSubOptionCode());
        }
        
        entity.setSubOptionCode(dto.getSubOptionCode())
            .setMemo(dto.getMemo())
            .setUpdatedAt(CustomDateUtils.getCurrentDateTime())
            .setUpdatedBy(USER_ID);

        subOptionCodeService.saveAndModify(entity);
    }
}
