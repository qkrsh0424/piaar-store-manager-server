package com.piaar_store_manager.server.service.delivery_ready;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyNaverBusinessService {

    @Autowired
    DeliveryReadyNaverService deliveryReadyNaverService;

    @Autowired
    ProductReleaseService productReleaseService;

    @Autowired
    ProductOptionService productOptionService;

    @Autowired
    ProductOptionRepository productOptionRepository;

    @Autowired
    ProductReceiveService productReceiveService;
    
    @Autowired
    DateHandler dateHandler;
    
    public void releaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos, UUID userId) {
        deliveryReadyNaverService.releaseListStockUnit(dtos);
        List<ProductReleaseGetDto> releaseDtos = new ArrayList<>();
        int optionCid = -1;
        
        for(DeliveryReadyNaverItemViewDto dto : dtos) {
            // 옵션관리코드가 존재하지 않은 경우.
            if(dto.getDeliveryReadyItem().getOptionManagementCode().isEmpty()) continue;

            // 상품 옵션의 cid
            optionCid = productOptionRepository.findCidByCode(dto.getDeliveryReadyItem().getOptionManagementCode());

            // 출고 데이터 생성
            ProductReleaseGetDto releaseDto = ProductReleaseGetDto.builder()
                .id(UUID.randomUUID())
                .releaseUnit(dto.getDeliveryReadyItem().getUnit())
                .memo("test")
                .createdAt(dateHandler.getCurrentDate())
                .createdBy(userId)
                .productOptionCid(optionCid)
                .build();

            releaseDtos.add(releaseDto);
        }

        productReleaseService.createPRList(releaseDtos, userId);
    }

    public void cancelReleaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos, UUID userId) {
        deliveryReadyNaverService.cancelReleaseListStockUnit(dtos);
        List<ProductReceiveGetDto> receiveDtos = new ArrayList<>();
        int optionCid = -1;
        
        for(DeliveryReadyNaverItemViewDto dto : dtos) {
            // 옵션관리코드가 존재하지 않은 경우.
            if(dto.getDeliveryReadyItem().getOptionManagementCode().isEmpty()) continue;

            // 상품 옵션의 cid
            optionCid = productOptionRepository.findCidByCode(dto.getDeliveryReadyItem().getOptionManagementCode());

            // 출고 데이터 생성
            ProductReceiveGetDto receiveDto = ProductReceiveGetDto.builder()
                .id(UUID.randomUUID())
                .receiveUnit(dto.getDeliveryReadyItem().getUnit())
                .memo("test")
                .createdAt(dateHandler.getCurrentDate())
                .createdBy(userId)
                .productOptionCid(optionCid)
                .build();

            receiveDtos.add(receiveDto);
        }

        productReceiveService.createPRList(receiveDtos, userId);
    }
}
