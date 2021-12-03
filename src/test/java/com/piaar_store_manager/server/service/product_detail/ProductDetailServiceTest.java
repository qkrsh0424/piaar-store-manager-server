package com.piaar_store_manager.server.service.product_detail;

import java.util.UUID;

import com.piaar_store_manager.server.model.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ProductDetailServiceTest {
    
    // @InjectMocks
    // private ProductDetailBusinessService detailBusinessService;

    // @Mock
    // private ProductDetailService detailService;

    // @BeforeEach
    // public void init() {
    //     MockitoAnnotations.openMocks(this);
    // }
    
    // @Test
    // public void 상품상세_단일수정() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");

    //     ProductDetailEntity entity = ProductDetailEntity.builder()
    //         .cid(1)
    //         .id(UUID.randomUUID())
    //         .detailWidth(3)
    //         .detailLength(3)
    //         .detailHeight(3)
    //         .detailQuantity(1)
    //         .detailWeight(10)
    //         .build();
        
    //     // when
    //     Mockito.when(detailService.searchOne(1)).thenReturn(entity);
    //     ProductDetailGetDto exDto = detailBusinessService.searchOne(1);

    //     // 변경 데이터
    //     ProductDetailGetDto testDto = ProductDetailGetDto.builder()
    //         .cid(1)
    //         .detailWidth(6)
    //         .detailLength(6)
    //         .detailHeight(6)
    //         .detailQuantity(1)
    //         .detailWeight(10)
    //         .build();

    //     detailBusinessService.changeOne(testDto, userId);
    //     ProductDetailGetDto resultDto = detailBusinessService.searchOne(1);

    //     // then
    //     Assertions.assertEquals(testDto.getDetailWidth(), resultDto.getDetailWidth());
    //     Assertions.assertNotEquals(testDto.getDetailWidth(), exDto.getDetailWidth());

    //     System.out.println("##### ProductDetailServiceTest.상품상세_단일수정() 성공 #####");
    // }
    
}
