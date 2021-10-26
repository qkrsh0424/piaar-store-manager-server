package com.piaar_store_manager.server.service.delivery_ready;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveBusinessService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseBusinessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyNaverBusinessService {

    @Autowired
    private DeliveryReadyNaverService deliveryReadyNaverService;

    @Autowired
    private ProductReleaseBusinessService productReleaseBusinessService;

    @Autowired
    private ProductReceiveBusinessService productReceiveBusinessService;

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 출고(재고 반영) 데이터를 생성하여 재고에 반영한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @param userId : UUID
     * @see DeliveryReadyNaverService#updateListReleaseCompleted
     * @see DeliveryReadyNaverService#getOptionCid
     * @see ProductReleaseGetDto#toDto
     * @see productReleaseBusinessService#createPRList
     */
    public void releaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos, UUID userId) {
        
        // 재고 반영되지 않은 데이터들만 재고 반영
        for(DeliveryReadyNaverItemViewDto dto : dtos) {
            if(!dto.getDeliveryReadyItem().getReleaseCompleted())
            deliveryReadyNaverService.updateListReleaseCompleted(dtos, true);
        }

        List<ProductReleaseGetDto> productReleaseGetDtos = new ArrayList<>();
        
        for(DeliveryReadyNaverItemViewDto dto : dtos){
            // 옵션명이 존재하지 않는 경우
            if(dto.getOptionDefaultName() == null) continue;

            int optionCid = deliveryReadyNaverService.getOptionCid(dto);

            ProductReleaseGetDto productReleaseGetDto = ProductReleaseGetDto.toDto(dto, optionCid);
            productReleaseGetDtos.add(productReleaseGetDto);
        }

        productReleaseBusinessService.createPRList(productReleaseGetDtos, userId);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Cancel the stock unit reflection of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 입고(재고 반영 취소) 데이터를 생성하여 재고에 반영한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @param userId : UUID
     * @see DeliveryReadyNaverService#updateListReleaseCompleted
     * @see DeliveryReadyNaverService#getOptionCid
     * @see ProductReceiveGetDto#toDto
     * @see productReceiveBusinessService#createPRList
     */
    public void cancelReleaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos, UUID userId) {
        
        // 재고 반영이 선행된 데이터들만 재고 반영 취소
        for(DeliveryReadyNaverItemViewDto dto : dtos) {
            if(dto.getDeliveryReadyItem().getReleaseCompleted())
                deliveryReadyNaverService.updateListReleaseCompleted(dtos, false);
        }

        List<ProductReceiveGetDto> productReceiveGetDtos = new ArrayList<>();
        for(DeliveryReadyNaverItemViewDto dto : dtos){
            // 옵션명이 존재하지 않는 경우
            if(dto.getOptionDefaultName() == null) continue;

            int optionCid = deliveryReadyNaverService.getOptionCid(dto);

            ProductReceiveGetDto productRceiveGetDto = ProductReceiveGetDto.toDto(dto, optionCid);
            productReceiveGetDtos.add(productRceiveGetDto);
        }
        
        productReceiveBusinessService.createPRList(productReceiveGetDtos, userId);
    }
}
