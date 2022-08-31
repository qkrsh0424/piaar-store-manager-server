package com.piaar_store_manager.server.domain.erp_release_complete_header.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.erp_release_complete_header.dto.ErpReleaseCompleteHeaderDto;
import com.piaar_store_manager.server.domain.erp_release_complete_header.entity.ErpReleaseCompleteHeaderEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReleaseCompleteHeaderBusinessService {
    private final ErpReleaseCompleteHeaderService erpReleaseCompleteHeaderService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * erp release complete header를 등록한다.
     * 
     * @param headerDto : ErpReleaseCompleteHeaderDto
     * @see ErpReleaseCompleteHeaderEntity#toEntity
     */
    public void saveOne(ErpReleaseCompleteHeaderDto headerDto) {
        UUID USER_ID = userService.getUserId();
        headerDto
                .setId(headerDto.getId())
                .setCreatedAt(CustomDateUtils.getCurrentDateTime())
                .setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime());
                
        ErpReleaseCompleteHeaderEntity headerEntity = ErpReleaseCompleteHeaderEntity.toEntity(headerDto);
        erpReleaseCompleteHeaderService.saveAndModify(headerEntity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 저장된 erp release complete header를 조회한다.
     *
     * @return ErpReleaseCompleteHeaderDto
     * @see ErpReleaseCompleteHeaderService#findAll
     * @see ErpReleaseCompleteHeaderDto#toDto
     */
    public List<ErpReleaseCompleteHeaderDto> searchAll() {
        List<ErpReleaseCompleteHeaderEntity> entities = erpReleaseCompleteHeaderService.findAll();
        List<ErpReleaseCompleteHeaderDto> dtos = entities.stream().map(entity -> ErpReleaseCompleteHeaderDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 저장된 erp release complete header를 변경한다.
     * 
     * @param headerDto : ErpReleaseCompleteHeaderDto
     * @see ErpReleaseCompleteHeaderBusinessService#searchOne
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpReleaseCompleteHeaderEntity#toEntity
     */
    public void updateOne(ErpReleaseCompleteHeaderDto headerDto) {
        ErpReleaseCompleteHeaderEntity entity = erpReleaseCompleteHeaderService.searchOne(headerDto.getId());
        ErpReleaseCompleteHeaderDto dto = ErpReleaseCompleteHeaderDto.toDto(entity);

        dto.setHeaderTitle(headerDto.getHeaderTitle());
        dto.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        dto.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpReleaseCompleteHeaderService.saveAndModify(ErpReleaseCompleteHeaderEntity.toEntity(dto));
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * erp release complete header을 제거한다.
     * 
     * @param headerId : UUID
     * @see ErpReleaseCompleteHeaderService#searchOne
     * @see ErpReleaseCompleteHeaderService#deleteOne
     */
    public void deleteOne(UUID headerId) {
        ErpReleaseCompleteHeaderEntity entity = erpReleaseCompleteHeaderService.searchOne(headerId);
        erpReleaseCompleteHeaderService.deleteOne(entity);
    }
}
