package com.piaar_store_manager.server.domain.erp_return_header.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.erp_return_header.dto.ErpReturnHeaderDto;
import com.piaar_store_manager.server.domain.erp_return_header.entity.ErpReturnHeaderEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReturnHeaderBusinessService {
    private final ErpReturnHeaderService erpReturnHeaderService;
    private final UserService userService;

    public List<ErpReturnHeaderDto> searchAll() {
        List<ErpReturnHeaderEntity> entities = erpReturnHeaderService.findAll();
        List<ErpReturnHeaderDto> dtos = entities.stream().map(entity -> ErpReturnHeaderDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    public void createOne(ErpReturnHeaderDto headerDto) {
        UUID USER_ID = userService.getUserId();

        headerDto
                .setId(headerDto.getId())
                .setCreatedAt(CustomDateUtils.getCurrentDateTime())
                .setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        ErpReturnHeaderEntity headerEntity = ErpReturnHeaderEntity.toEntity(headerDto);
        erpReturnHeaderService.saveAndModify(headerEntity);
    }

    public void updateOne(ErpReturnHeaderDto headerDto) {
        ErpReturnHeaderEntity entity = erpReturnHeaderService.searchOne(headerDto.getId());
        ErpReturnHeaderDto dto = ErpReturnHeaderDto.toDto(entity);

        dto.setHeaderTitle(headerDto.getHeaderTitle());
        dto.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        dto.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpReturnHeaderService.saveAndModify(ErpReturnHeaderEntity.toEntity(dto));
    }
    
    public void deleteOne(UUID headerId) {
        ErpReturnHeaderEntity entity = erpReturnHeaderService.searchOne(headerId);
        erpReturnHeaderService.deleteOne(entity);
    }
}
