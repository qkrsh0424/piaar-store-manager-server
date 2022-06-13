package com.piaar_store_manager.server.domain.sub_option_code.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sub_option_code.dto.SubOptionCodeDto;
import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDataFormatUtils;
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

    public void createOne(SubOptionCodeDto dto) {
        UUID USER_ID = userService.getUserId();
        String subOptionCode = CustomDataFormatUtils.removeBlank(dto.getSubOptionCode());
        subOptionCodeService.duplicationCodeCheck(subOptionCode);

        SubOptionCodeEntity entity = SubOptionCodeEntity.builder()
            .id(dto.getId())
            .subOptionCode(subOptionCode)
            .memo(dto.getMemo())
            .productOptionId(dto.getProductOptionId())
            .productOptionCode(dto.getProductOptionCode())
            .createdAt(CustomDateUtils.getCurrentDateTime())
            .createdBy(USER_ID)
            .updatedAt(CustomDateUtils.getCurrentDateTime())
            .build();

        subOptionCodeService.saveAndModify(entity);
    }

    public void updateOne(SubOptionCodeDto dto) {
        SubOptionCodeEntity entity = subOptionCodeService.searchOne(dto.getId());
        String subOptionCode = CustomDataFormatUtils.removeBlank(dto.getSubOptionCode());
        subOptionCodeService.duplicationCodeCheck(subOptionCode);
        
        entity.setSubOptionCode(subOptionCode).setMemo(dto.getMemo()).setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        subOptionCodeService.saveAndModify(entity);
    }
}
