package com.piaar_store_manager.server.domain.erp_order_header.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.erp_order_header.dto.ErpOrderHeaderDto;
import com.piaar_store_manager.server.domain.erp_order_header.entity.ErpOrderHeaderEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;
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
     * 저장된 erp order header를 조회한다.
     *
     * @return ErpOrderHeaderDto
     * @see ErpOrderHeaderService#findAll
     * @see ErpOrderHeaderDto#toDto
     */
    // public ErpOrderHeaderDto searchOne() {
    //     ErpOrderHeaderEntity headerEntity = erpOrderHeaderService.findAll().stream().findFirst().orElse(null);
    //     return ErpOrderHeaderDto.toDto(headerEntity);
    // }
    public ErpOrderHeaderDto searchOne(UUID headerId) {
        ErpOrderHeaderEntity entity = erpOrderHeaderService.searchOne(headerId);
        ErpOrderHeaderDto dto = ErpOrderHeaderDto.toDto(entity);
        return dto;
    }
    
    public List<ErpOrderHeaderDto> searchTitleList() {
        List<ErpOrderHeaderEntity> entities = erpOrderHeaderService.findAll();
        List<ErpOrderHeaderDto> dtos = entities.stream().map(r -> {
            ErpOrderHeaderDto dto = ErpOrderHeaderDto.builder()
                .id(r.getId())
                .headerTitle(r.getHeaderTitle())
                .build();

            return dto;
        }).collect(Collectors.toList());
        
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
        ErpOrderHeaderDto dto = this.searchOne(headerDto.getId());

        if (dto == null) {
            throw new CustomNotFoundDataException("수정하려는 데이터를 찾을 수 없습니다.");
        }

        dto.setHeaderTitle(headerDto.getHeaderTitle());
        dto.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        dto.setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        System.out.println(dto);
        erpOrderHeaderService.saveAndModify(ErpOrderHeaderEntity.toEntity(dto));
    }

    public void deleteOne(UUID headerId) {
        ErpOrderHeaderDto dto = this.searchOne(headerId);

        if (dto == null) {
            throw new CustomNotFoundDataException("제거하려는 데이터를 찾을 수 없습니다.");
        }

        ErpOrderHeaderEntity entity = ErpOrderHeaderEntity.toEntity(dto);
        erpOrderHeaderService.deleteOne(entity);
    }
}
