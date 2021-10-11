package com.piaar_store_manager.server.service.delivery_ready;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyCoupangBusinessService {
    @Autowired
    DeliveryReadyCoupangService deliveryReadyCoupangService;

    @Autowired
    ProductReleaseService productReleaseService;

    @Autowired
    ProductReceiveService productReceiveService;
    
    public void releaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos, UUID userId) {
        deliveryReadyCoupangService.releaseListStockUnit(dtos);
        List<ProductReleaseGetDto> releaseDtos = deliveryReadyCoupangService.createReleaseDtos(dtos, userId);

        productReleaseService.createPRList(releaseDtos, userId);
    }

    public void cancelReleaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos, UUID userId) {
        deliveryReadyCoupangService.cancelReleaseListStockUnit(dtos);
        List<ProductReceiveGetDto> receiveDtos = deliveryReadyCoupangService.createReceiveDtos(dtos, userId);
        
        productReceiveService.createPRList(receiveDtos, userId);
    }
}
