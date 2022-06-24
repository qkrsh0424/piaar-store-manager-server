package com.piaar_store_manager.server.domain.erp_sales_header.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.erp_sales_header.dto.ErpSalesHeaderDto;
import com.piaar_store_manager.server.domain.erp_sales_header.entity.ErpSalesHeaderEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpSalesHeaderBusinessService {
    private final ErpSalesHeaderService erpSalesHeaderService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * erp sales header를 등록한다.
     * 
     * @param headerDto : ErpSalesHeaderDto
     * @see ErpSalesHeaderEntity#toEntity
     */
    public void saveOne(ErpSalesHeaderDto headerDto) {
        UUID USER_ID = userService.getUserId();
        headerDto
                .setId(headerDto.getId())
                .setCreatedAt(CustomDateUtils.getCurrentDateTime())
                .setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        ErpSalesHeaderEntity headerEntity = ErpSalesHeaderEntity.toEntity(headerDto);
        erpSalesHeaderService.saveAndModify(headerEntity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 저장된 erp sales header를 모두 조회한다.
     *
     * @return ErpOrderHeaderDto
     * @see ErpOrderHeaderService#findAll
     * @see ErpOrderHeaderDto#toDto
     */
    public List<ErpSalesHeaderDto> searchList() {
        List<ErpSalesHeaderEntity> entities = erpSalesHeaderService.findAll();
        List<ErpSalesHeaderDto> dtos = entities.stream().map(entity -> ErpSalesHeaderDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 저장된 erp sales header를 변경한다.
     * 
     * @param headerDto : ErpSalesHeaderDto
     * @see ErpSalesHeaderBusinessService#searchOne
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpSalesHeaderDto#toDto
     */
    public void updateOne(ErpSalesHeaderDto headerDto) {
        ErpSalesHeaderEntity entity = erpSalesHeaderService.searchOne(headerDto.getId());
        ErpSalesHeaderDto dto = ErpSalesHeaderDto.toDto(entity);

        dto.setHeaderTitle(headerDto.getHeaderTitle());
        dto.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        dto.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpSalesHeaderService.saveAndModify(ErpSalesHeaderEntity.toEntity(dto));
    }

    public void deleteOne(UUID headerId) {
        ErpSalesHeaderEntity entity = erpSalesHeaderService.searchOne(headerId);
        erpSalesHeaderService.deleteOne(entity);
    }
}
