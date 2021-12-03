package com.piaar_store_manager.server.service.product_receive; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveJoinResDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.repository.ProductReceiveRepository;
import com.piaar_store_manager.server.service.user.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@Transactional
public class ProductReceiveServiceTest {
    
    // @Autowired
    // private ProductReceiveService receiveService;

    // @Autowired
    // private ProductReceiveBusinessService receiveBusinessService;

    // @Autowired
    // private UserService userService;

    // @Autowired
    // private ProductReceiveRepository receiveRepository;

    // @Autowired
    // private ProductOptionRepository optionRepository;

    // @Test
    // public void 상품입고_단일조회() {
    //     // given
    //     ProductReceiveGetDto exDto = receiveService.searchOne(14);
    //     ProductReceiveGetDto testDto = receiveBusinessService.searchOneTest(14);

    //     // when, then
    //     Assertions.assertEquals(exDto.getReceiveUnit(), testDto.getReceiveUnit());
    //     Assertions.assertEquals(exDto.getProductOptionCid(), testDto.getProductOptionCid());
        
    //     System.out.println("##### ProductReceiveServiceTest.상품입고_단일조회() #####");
    // }

    // @Test
    // public void 상품입고_M2OJ_단일조회() {
    //     // given
    //     ProductReceiveJoinResDto exDto = receiveService.searchOneM2OJ(14);
    //     ProductReceiveJoinResDto testDto = receiveBusinessService.searchOneM2OJTest(14);

    //     // when, then
    //     Assertions.assertEquals(exDto.getProduct().getCid(), testDto.getProduct().getCid());
    //     Assertions.assertEquals(exDto.getOption().getCid(), testDto.getOption().getCid());
    //     Assertions.assertEquals(exDto.getCategory().getCid(), testDto.getCategory().getCid());
    //     Assertions.assertEquals(exDto.getUser().getId(), testDto.getUser().getId());

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_M2OJ_단일조회() 성공 #####");
    // }

    // @Test
    // public void 상품입고_M2OJ_단일조회2() {
    //     // given
    //     ProductReceiveJoinResDto exDto = receiveBusinessService.searchOneM2OJ(14);
    //     ProductReceiveJoinResDto testDto = receiveBusinessService.searchOneM2OJTest(14);

    //     // when, then
    //     Assertions.assertEquals(exDto.getProduct().getCid(), testDto.getProduct().getCid());
    //     Assertions.assertEquals(exDto.getOption().getCid(), testDto.getOption().getCid());
    //     Assertions.assertEquals(exDto.getCategory().getCid(), testDto.getCategory().getCid());
    //     Assertions.assertEquals(exDto.getUser().getId(), testDto.getUser().getId());

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_M2OJ_단일조회() 성공 #####");
    // }

    // @Test
    // public void 상품입고_전체조회() {
    //     // given
    //     List<ProductReceiveGetDto> exDto = receiveService.searchList();
    //     List<ProductReceiveGetDto> testDto = receiveBusinessService.searchListTest();

    //     // when, then
    //     Assertions.assertEquals(exDto.size(), testDto.size());

    //     for(int i = 0; i < exDto.size(); i++) {
    //         Assertions.assertEquals(exDto.get(i).getCid(), testDto.get(i).getCid());
    //         Assertions.assertEquals(exDto.get(i).getReceiveUnit(), testDto.get(i).getReceiveUnit());
    //         Assertions.assertEquals(exDto.get(i).getProductOptionCid(), testDto.get(i).getProductOptionCid());
    //     }

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_전체조회() 성공 #####");
    // }

    // @Test
    // public void 상품입고_전체조회_byProductOptionCid() {
    //     // given
    //     List<ProductReceiveGetDto> exDto = receiveService.searchListByOptionCid(71);
    //     List<ProductReceiveGetDto> testDto = receiveBusinessService.searchListByOptionCidTest(71);

    //     // when, then
    //     Assertions.assertEquals(exDto.size(), testDto.size());

    //     for(int i = 0; i < exDto.size(); i++) {
    //         Assertions.assertEquals(exDto.get(i).getCid(), testDto.get(i).getCid());
    //         Assertions.assertEquals(exDto.get(i).getReceiveUnit(), testDto.get(i).getReceiveUnit());
    //         Assertions.assertEquals(exDto.get(i).getProductOptionCid(), testDto.get(i).getProductOptionCid());
    //     }

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_전체조회_byProductOptionCid() 성공 #####");
    // }

    // @Test
    // public void 상품입고_M2OJ_전체조회() {
    //     // given
    //     List<ProductReceiveJoinResDto> exDto = receiveService.searchListM2OJ();
    //     List<ProductReceiveJoinResDto> testDto = receiveBusinessService.searchListM2OJTest();

    //     // when, then
    //     Assertions.assertEquals(exDto.size(), testDto.size());

    //     for(int i = 0; i < exDto.size(); i++) {
    //         Assertions.assertEquals(exDto.get(i).getProduct().getCid(), testDto.get(i).getProduct().getCid());
    //         Assertions.assertEquals(exDto.get(i).getCategory().getCid(), testDto.get(i).getCategory().getCid());
    //         Assertions.assertEquals(exDto.get(i).getOption().getCid(), testDto.get(i).getOption().getCid());
    //         Assertions.assertEquals(exDto.get(i).getUser().getId(), testDto.get(i).getUser().getId());
    //         Assertions.assertEquals(exDto.get(i).getReceive().getCid(), testDto.get(i).getReceive().getCid());
    //     }

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_M2OJ_전체조회() 성공 #####");
    // }

    // @Test
    // public void 상품입고_단일생성_옵션재고반영() {
    //     // given
    //     ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
    //         .id(UUID.randomUUID())
    //         .receiveUnit(24)
    //         .memo("test code")
    //         .productOptionCid(71)
    //         .build();

    //     UUID userId = UUID.fromString("#USER_ID#");
    //     ProductReceiveGetDto exDto = receiveBusinessService.createPR(dto, userId);
    //     ProductReceiveGetDto testDto = receiveBusinessService.createPRTest(dto, userId);

    //     // when, then
    //     Assertions.assertEquals(exDto.getClass(), testDto.getClass());
    //     Assertions.assertEquals(exDto.getReceiveUnit(), testDto.getReceiveUnit());
    //     Assertions.assertEquals(exDto.getMemo(), testDto.getMemo());
    //     Assertions.assertEquals(exDto.getProductOptionCid(), testDto.getProductOptionCid());

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_단일생성_옵션재고반영() 성공 #####");
    // }

    // @Test
    // public void 상품입고_다중생성_옵션재고반영() {
    //     // given
    //     List<ProductReceiveGetDto> dtos = new ArrayList<>(); 

    //     for(int i =0 ; i < 3; i++) {
    //         ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
    //             .id(UUID.randomUUID())
    //             .receiveUnit(i)
    //             .memo("test code")
    //             .productOptionCid(71)
    //             .build();

    //         dtos.add(dto);
    //     }

    //     UUID userId = UUID.fromString("#USER_ID#");
    //     List<ProductReceiveGetDto> exDto = receiveBusinessService.createPRList(dtos, userId);
    //     List<ProductReceiveGetDto> testDto = receiveBusinessService.createPRListTest(dtos, userId);

    //     // when, then
    //     for(int i = 0; i < exDto.size(); i++){    
    //         Assertions.assertEquals(exDto.getClass(), testDto.getClass());
    //         Assertions.assertEquals(exDto.get(i).getReceiveUnit(), testDto.get(i).getReceiveUnit());
    //         Assertions.assertEquals(exDto.get(i).getMemo(), testDto.get(i).getMemo());
    //         Assertions.assertEquals(exDto.get(i).getProductOptionCid(), testDto.get(i).getProductOptionCid());
    //     }

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_다중생성_옵션재고반영() 성공 #####");
    // }

    // @Test
    // @Transactional
    // public void 상품입고_단일삭제() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");

    //     Optional<ProductReceiveEntity> exEntity = receiveRepository.findById(14);
    //     Optional<ProductReceiveEntity> testEntity = receiveRepository.findById(15);

    //     Assertions.assertTrue(exEntity.isPresent());
    //     Assertions.assertTrue(testEntity.isPresent());

    //     // when
    //     receiveService.destroyOne(exEntity.get().getCid(), userId);
    //     receiveBusinessService.destroyOneTest(testEntity.get().getCid(), userId);

    //     Optional<ProductReceiveEntity> deleteExEntity = receiveRepository.findById(14);
    //     Optional<ProductReceiveEntity> testExEntity = receiveRepository.findById(15);
    //     // then
    //     Assertions.assertFalse(deleteExEntity.isPresent());
    //     Assertions.assertFalse(testExEntity.isPresent());

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_단일삭제() 성공 #####");
    // }

    // @Test
    // public void 상품입고_단일수정() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");

    //     Optional<ProductReceiveEntity> exEntity = receiveRepository.findById(14);
    //     Optional<ProductOptionEntity> exOptionEntity = optionRepository.findById(71);

    //     Assertions.assertTrue(exEntity.isPresent());
    //     Assertions.assertTrue(exOptionEntity.isPresent());

    //     int exEntityReceiveUnit = exEntity.get().getReceiveUnit();      // 기존 receiveUnit
    //     int exOptionEntityReceiveUnit = exOptionEntity.get().getStockUnit();    // 기존 option의 stockUnit

    //     // 변경 데이터
    //     ProductReceiveGetDto testDto = ProductReceiveGetDto.builder()
    //         .cid(14)
    //         .receiveUnit(5)
    //         .memo("test memo")
    //         .build();

    //     // when
    //     receiveBusinessService.changeOneTest(testDto, userId);

    //     Optional<ProductReceiveEntity> testEntity = receiveRepository.findById(14);
    //     Optional<ProductOptionEntity> testOptionEntity = optionRepository.findById(71);

    //     Assertions.assertTrue(testEntity.isPresent());
    //     Assertions.assertTrue(testOptionEntity.isPresent());

    //     int testEntityReceiveUnit = testEntity.get().getReceiveUnit();  // 변경 receiveUnit
        
    //     // 옵션재고 개수 : 기존 stockUnit + (변경 입고개수 - 기존 입고개수)
    //     int changedReceiveUnit = exOptionEntityReceiveUnit + (testEntityReceiveUnit - exEntityReceiveUnit);

    //     // then
    //     Assertions.assertEquals(changedReceiveUnit, testOptionEntity.get().getStockUnit());

    //     System.out.println("##### ProductReceiveServiceTest.상품입고_단일수정() 성공 #####");
    // }
}
