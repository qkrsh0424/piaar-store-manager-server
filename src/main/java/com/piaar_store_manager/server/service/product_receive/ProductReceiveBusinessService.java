package com.piaar_store_manager.server.service.product_receive;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReceiveBusinessService {
    
    @Autowired
    private ProductReceiveService productReceiveService;

    @Autowired
    private ProductOptionService productOptionService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 한개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveService#createPR
     * @see ProductOptionService#receiveProductUnit
     */
    public void createPR(ProductReceiveGetDto productReceiveGetDto, UUID userId) {
        ProductReceiveEntity entity = productReceiveService.createPR(productReceiveGetDto, userId);

        productOptionService.receiveProductUnit(productReceiveGetDto.getProductOptionCid(), userId, entity.getReceiveUnit());
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 여러개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDto : List::ProductReceiveGetDto::
     * @param userId : UUID
     * @see ProductReceiveService#createPRList
     * @see ProductOptionService#receiveProductUnit
     */
    public void createPRList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {
        List<ProductReceiveEntity> entities = productReceiveService.createPRList(productReceiveGetDtos, userId);

        for(ProductReceiveEntity entity : entities) {
            productOptionService.receiveProductUnit(entity.getProductOptionCid(), userId, entity.getReceiveUnit());
        }
    }

}
