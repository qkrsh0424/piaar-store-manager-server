package com.piaar_store_manager.server.service.product_release;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReleaseBusinessService {
    
    @Autowired
    private ProductReleaseService productReleaseService;

    @Autowired
    private ProductOptionService productOptionService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 한개 등록한다.
     * 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @param userId : UUID
     * @see ProductReleaseService#createPR
     * @see ProductOptionService#releaseProductUnit
     */
    public void createPR(ProductReleaseGetDto productReleaseGetDto, UUID userId) {
        ProductReleaseEntity entity = productReleaseService.createPR(productReleaseGetDto, userId);

        productOptionService.releaseProductUnit(productReleaseGetDto.getProductOptionCid(), userId, entity.getReleaseUnit());
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 여러개 등록한다.
     * 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @param userId : UUID
     * @see ProductReleaseService#createPRList
     * @see ProductOptionService#releaseProductUnit
     */
    public void createPRList(List<ProductReleaseGetDto> productReleaseGetDtos, UUID userId) {
        List<ProductReleaseEntity> entities = productReleaseService.createPRList(productReleaseGetDtos, userId);

        for(ProductReleaseEntity entity : entities) {
            productOptionService.releaseProductUnit(entity.getProductOptionCid(), userId, entity.getReleaseUnit());
        }
    }
}
