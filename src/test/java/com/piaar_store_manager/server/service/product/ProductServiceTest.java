package com.piaar_store_manager.server.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.dto.ProductJoinResDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// @ExtendWith(SpringExtension.class)
// @RunWith(SpringRunner.class)
// @Transactional
public class ProductServiceTest {

    // @InjectMocks
    // private ProductBusinessService productBusinessService;

    // @Mock
    // private ProductService productService;

    // @BeforeEach
    // public void init() {
    //     MockitoAnnotations.openMocks(this);
    // }

    // @Autowired
    // private ProductBusinessService productBusinessService;

    // @Autowired
    // private ProductService productService;

    // @Test
    // public void 상품_FJ_단일조회() {
    //     // given
    //     ProductJoinResDto answerDto = productService.searchOneFJ(76);
    //     ProductJoinResDto testDto = productBusinessService.searchOneFJTest(76);

    //     // when, then
    //     Assertions.assertEquals(answerDto.getProduct().getCid(), testDto.getProduct().getCid());
    //     Assertions.assertEquals(answerDto.getCategory().getCid(), testDto.getCategory().getCid());
    //     Assertions.assertEquals(answerDto.getUser().getId(), testDto.getUser().getId());
    //     for(int i = 0; i < answerDto.getOptions().size(); i++) {
    //         Assertions.assertEquals(answerDto.getOptions().get(i).getCid(), testDto.getOptions().get(i).getCid());
    //     }

    //     System.out.println("##### ProductServiceTest.상품_FJ_단일조회() 성공 #####");
    // }

    // @Test
    // public void 상품_M2OJ_전체조회() {
    //     // given
    //     List<ProductJoinResDto> answerDtos = productService.searchListM2OJ();
    //     List<ProductJoinResDto> testDtos = productBusinessService.searchListM2OJTest();
        
    //     // when, then
    //     for(int i = 0; i < answerDtos.size(); i++) {
    //         Assertions.assertEquals(answerDtos.get(i).getProduct().getCid(), testDtos.get(i).getProduct().getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getCategory().getCid(), testDtos.get(i).getCategory().getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getUser().getId(), testDtos.get(i).getUser().getId());
    //     }

    //     System.out.println("##### ProductServiceTest.상품_M2OJ_전체조회() 성공 #####");
    // }

    // @Test
    // public void 상품_FJ_전체조회() {
    //     // given
    //     List<ProductJoinResDto> answerDtos = productService.searchListFJ();
    //     List<ProductJoinResDto> testDtos = productBusinessService.searchListFJ();

    //     // when, then
    //     for(int i = 0; i < answerDtos.size(); i++) {
    //         Assertions.assertEquals(answerDtos.get(i).getProduct().getCid(), testDtos.get(i).getProduct().getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getCategory().getCid(), testDtos.get(i).getCategory().getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getUser().getId(), testDtos.get(i).getUser().getId());
            
    //         for(int j = 0; j < answerDtos.get(i).getOptions().size(); j++){
    //             Assertions.assertEquals(answerDtos.get(i).getOptions().get(j).getCid(), testDtos.get(i).getOptions().get(j).getCid());
    //         }
    //     }

    //     System.out.println("##### ProductServiceTest.상품_FJ_전체조회() 성공 #####");
    // }

    // @Test
    // public void 상품_다중조회() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");

    //     List<ProductEntity> entities = new ArrayList<>();

    //     ProductEntity prod1 = ProductEntity.builder()
    //         .cid(1)
    //         .id(UUID.randomUUID())
    //         .defaultName("상품명1")
    //         .managementName("관리명1")
    //         .stockManagement(true)
    //         .createdBy(userId)
    //         .updatedBy(userId)
    //         .productCategoryCid(1)
    //         .build();

    //     ProductEntity prod2 = ProductEntity.builder()
    //         .cid(2)
    //         .id(UUID.randomUUID())
    //         .defaultName("상품명2")
    //         .managementName("관리명2")
    //         .stockManagement(false)
    //         .createdBy(userId)
    //         .updatedBy(userId)
    //         .productCategoryCid(2)
    //         .build();

    //     entities.add(prod1);
    //     entities.add(prod2);

    //     // when
    //     Mockito.when(productService.searchList()).thenReturn(entities);

    //     List<ProductGetDto> testDto = productBusinessService.searchList();
        
    //     // then
    //     Assertions.assertEquals(prod1.getCid(), testDto.get(0).getCid());
    //     Assertions.assertEquals(prod1.getDefaultName(), testDto.get(0).getDefaultName());
    //     Assertions.assertEquals(prod2.getCid(), testDto.get(1).getCid());
    //     Assertions.assertEquals(prod2.getDefaultName(), testDto.get(1).getDefaultName());

    //     System.out.println("##### ProductServiceTest.상품_다중조회() 성공 #####");
    // }
}
