package com.piaar_store_manager.server.service.delivery_ready_view_header;

import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto.DeliveryReadyPiaarViewHeaderDto;
import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.entity.DeliveryReadyPiaarViewHeaderEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyPiaarViewHeaderBusinessService {
    private DeliveryReadyPiaarViewHeaderService deliveryReadyPiaarViewHeaderService;

    @Autowired
    public DeliveryReadyPiaarViewHeaderBusinessService(
        DeliveryReadyPiaarViewHeaderService deliveryReadyPiaarViewHeaderService
    ) {
        this.deliveryReadyPiaarViewHeaderService = deliveryReadyPiaarViewHeaderService;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 유저가 커스터마이징한 Piaar View 엑셀 양식을 등록한다.
     * 
     * @param viewHeaderDto : DeliveryReadyPiaarViewHeaderDto
     * @param userId : UUID
     * @return DeliveryReadyPiaarViewHeaderDto
     * @see DeliveryReadyPiaarViewHeaderEntity#toEntity
     * @see DeliveryReadyPiaarViewHeaderDto#toDto
     */
    public DeliveryReadyPiaarViewHeaderDto createOne(DeliveryReadyPiaarViewHeaderDto viewHeaderDto, UUID userId) {
        viewHeaderDto.setCreatedAt(DateHandler.getCurrentLocalDateTime()).setCreatedBy(userId).setUpdatedAt(DateHandler.getCurrentLocalDateTime());

        DeliveryReadyPiaarViewHeaderEntity entity = deliveryReadyPiaarViewHeaderService.saveOne(DeliveryReadyPiaarViewHeaderEntity.toEntity(viewHeaderDto));
        DeliveryReadyPiaarViewHeaderDto dto = DeliveryReadyPiaarViewHeaderDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * User가 저장한 Piaar View 엑셀 양식을 조회한다.
     *
     * @return DeliveryReadyPiaarViewHeaderDto
     * @see ProductService#searchOne
     * @see DeliveryReadyPiaarViewHeaderDto#toDto
     */
    public DeliveryReadyPiaarViewHeaderDto searchOneByUser(UUID userId) {
        DeliveryReadyPiaarViewHeaderEntity entity = deliveryReadyPiaarViewHeaderService.searchOneByUser(userId);
        DeliveryReadyPiaarViewHeaderDto dto = DeliveryReadyPiaarViewHeaderDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 유저가 커스터마이징한 Piaar View 엑셀 양식을 등록한다.
     * 
     * @param viewHeaderDto : DeliveryReadyPiaarViewHeaderDto
     * @param userId : UUID
     * @return DeliveryReadyPiaarViewHeaderDto
     * @see DeliveryReadyPiaarViewHeaderEntity#toEntity
     * @see DeliveryReadyPiaarViewHeaderDto#toDto
     */
    public DeliveryReadyPiaarViewHeaderDto changeOne(DeliveryReadyPiaarViewHeaderDto viewHeaderDto, UUID userId) {
        DeliveryReadyPiaarViewHeaderEntity entity = deliveryReadyPiaarViewHeaderService.searchOneByUser(userId);
        DeliveryReadyPiaarViewHeaderDto dto = DeliveryReadyPiaarViewHeaderDto.toDto(entity);
        
        dto.getViewHeaderDetail().setDetails(viewHeaderDto.getViewHeaderDetail().getDetails());
        dto.setUpdatedAt(DateHandler.getCurrentLocalDateTime());

        DeliveryReadyPiaarViewHeaderEntity changedEntity = deliveryReadyPiaarViewHeaderService.saveOne(DeliveryReadyPiaarViewHeaderEntity.toEntity(dto));
        DeliveryReadyPiaarViewHeaderDto changedDto = DeliveryReadyPiaarViewHeaderDto.toDto(changedEntity);
        return changedDto;
    }

}
