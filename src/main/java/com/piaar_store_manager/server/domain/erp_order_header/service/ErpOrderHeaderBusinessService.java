package com.piaar_store_manager.server.domain.erp_order_header.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.erp_order_header.dto.ErpOrderHeaderDto;
import com.piaar_store_manager.server.domain.erp_order_header.entity.ErpOrderHeaderEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpOrderHeaderBusinessService {
    private final ErpOrderHeaderService erpOrderHeaderService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * erp order header를 등록한다.
     *
     * @param headerDto : ErpOrderHeaderDto
     * @see ErpOrderHeaderEntity#toEntity
     * @see ErpOrderHeaderService#saveAndModify
     */
    public void saveOne(ErpOrderHeaderDto headerDto) {
        UUID USER_ID = userService.getUserId();

        headerDto
                .setId(headerDto.getId())
                .setCreatedAt(CustomDateUtils.getCurrentDateTime())
                .setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        ErpOrderHeaderEntity headerEntity = ErpOrderHeaderEntity.toEntity(headerDto);
        erpOrderHeaderService.saveAndModify(headerEntity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 저장된 erp order header를 모두 조회한다.
     *
     * @return ErpOrderHeaderDto
     * @see ErpOrderHeaderService#findAll
     * @see ErpOrderHeaderDto#toDto
     */
    public List<ErpOrderHeaderDto> searchAll() {
        List<ErpOrderHeaderEntity> entities = erpOrderHeaderService.findAll();
        List<ErpOrderHeaderDto> dtos = entities.stream().map(entity -> ErpOrderHeaderDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 저장된 erp order header를 변경한다.
     *
     * @param headerDto : ErpOrderHeaderDto
     * @see ErpOrderHeaderBusinessService#searchOne
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpOrderHeaderEntity#toEntity
     */
    public void updateOne(ErpOrderHeaderDto headerDto) {
        ErpOrderHeaderEntity entity = erpOrderHeaderService.searchOne(headerDto.getId());
        ErpOrderHeaderDto dto = ErpOrderHeaderDto.toDto(entity);

        dto.setHeaderTitle(headerDto.getHeaderTitle());
        dto.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        dto.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpOrderHeaderService.saveAndModify(ErpOrderHeaderEntity.toEntity(dto));
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * erp order header을 제거한다.
     * 
     * @param entity
     */
    public void deleteOne(UUID headerId) {
        ErpOrderHeaderEntity entity = erpOrderHeaderService.searchOne(headerId);
        erpOrderHeaderService.deleteOne(entity);
    }
}
