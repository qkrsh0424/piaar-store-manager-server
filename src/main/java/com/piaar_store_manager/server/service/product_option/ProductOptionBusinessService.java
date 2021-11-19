package com.piaar_store_manager.server.service.product_option;

import java.util.List;

import com.piaar_store_manager.server.model.product_option.dto.ProductOptionStatusDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionBusinessService {
    
    @Autowired
    private ProductReleaseService productReleaseService;

    @Autowired
    private ProductReceiveService productReceiveService;

    // public ProductOptionStatusDto searchStockStatus(Integer optionCid) {
    //     // 1. 출고데이터 조회
    //     List<ProductReleaseGetDto> releaseDtos = productReleaseService.searchList(optionCid);
    //     // 2. 입고데이터 조회
    //     List<ProductReceiveGetDto> receiveDtos = productReceiveService.searchList(optionCid);
        
    //     // 3. 합쳐서 ProductOptionStatusDto 생성
    //     ProductOptionStatusDto statusDto = ProductOptionStatusDto.builder()
    //         .productRelease(releaseDtos)
    //         .productReceive(receiveDtos)
    //         .build();

    //     return statusDto;
    // }
}
