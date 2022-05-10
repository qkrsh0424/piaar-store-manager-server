package com.piaar_store_manager.server.domain.erp_delivery_header.service;

import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_delivery_header.dto.ErpDeliveryHeaderDto;
import com.piaar_store_manager.server.domain.erp_delivery_header.entity.ErpDeliveryHeaderEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpDeliveryHeaderBusinessService {
    private final ErpDeliveryHeaderService erpDeliveryHeaderService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * erp delivery header를 등록한다.
     * 
     * @param headerDto : ErpDeliveryHeaderDto
     * @see ErpDeliveryHeaderEntity#toEntity
     */
    public void saveOne(ErpDeliveryHeaderDto headerDto) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        UUID ID = UUID.randomUUID();
        UUID USER_ID = userService.getUserId();
        
        headerDto
            .setId(ID)
            .setCreatedAt(CustomDateUtils.getCurrentDateTime())
            .setCreatedBy(USER_ID)
            .setUpdatedAt(CustomDateUtils.getCurrentDateTime());
        
        ErpDeliveryHeaderEntity headerEntity = ErpDeliveryHeaderEntity.toEntity(headerDto);
        erpDeliveryHeaderService.saveAndModify(headerEntity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 저장된 erp delivery header를 조회한다.
     *
     * @return ErpDeliveryHeaderDto
     * @see ErpDeliveryHeaderService#findAll
     * @see ErpDeliveryHeaderDto#toDto
     */
    public ErpDeliveryHeaderDto searchOne() {
        // access check
        userService.userLoginCheck();
        
        ErpDeliveryHeaderEntity headerEntity = erpDeliveryHeaderService.findAll().stream().findFirst().orElse(null);
        
        return ErpDeliveryHeaderDto.toDto(headerEntity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 저장된 erp delivery header를 변경한다.
     * 
     * @param headerDto : ErpDeliveryHeaderDto
     * @see ErpDeliveryHeaderBusinessService#searchOne
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpDeliveryHeaderEntity#toEntity
     */
    public void updateOne(ErpDeliveryHeaderDto headerDto) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        ErpDeliveryHeaderDto dto = this.searchOne();
        
        if(dto == null) {
            throw new CustomNotFoundDataException("수정하려는 데이터를 찾을 수 없습니다.");
        }

        dto.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        dto.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpDeliveryHeaderService.saveAndModify(ErpDeliveryHeaderEntity.toEntity(dto));
    }
}
