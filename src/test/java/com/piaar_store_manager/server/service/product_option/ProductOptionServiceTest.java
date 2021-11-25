package com.piaar_store_manager.server.service.product_option;

import java.util.List;

import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionJoinResDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@Transactional
public class ProductOptionServiceTest {
    
    @Autowired
    private ProductOptionBusinessService optionBusinessService;

    @Autowired
    private ProductOptionService optionService;

    // @Test
    // public void 옵션_단일조회() {
    //     // given
    //     ProductOptionGetDto testDto = optionBusinessService.searchOne(78);

    //     // when, then
    //     Assertions.assertEquals(testDto.getCid(), 78);

    //     System.out.println("##### ProductOptionServiceTest.옵션_단일조회() 성공 #####");
    // }

    // @Test
    // public void 옵션_M2OJ_단일조회() {
    //     // given
    //     ProductOptionJoinResDto answerDto = optionService.searchOneM2OJ(78);
    //     ProductOptionJoinResDto testDto = optionBusinessService.searchOneM2OJTest(78);

    //     System.out.println(answerDto);
    //     System.out.println(testDto);

    //     // when, then
    //     Assertions.assertEquals(answerDto.getProduct().getCid(), testDto.getProduct().getCid());
    //     Assertions.assertEquals(answerDto.getCategory().getCid(), testDto.getCategory().getCid());
    //     Assertions.assertEquals(answerDto.getUser().getId(), testDto.getUser().getId());
    //     Assertions.assertEquals(answerDto.getOption().getCid(), testDto.getOption().getCid());

    //     System.out.println("##### ProductOptionServiceTest.옵션_M2OJ_단일조회() 성공 #####");
    // }

    // @Test
    // public void 옵션_M2OJ_전체조회() {
    //     List<ProductOptionJoinResDto> answerDtos = optionService.searchListM2OJ();
    //     List<ProductOptionJoinResDto> testDtos = optionBusinessService.searchListM2OJ();

    //     for(int i = 0; i < answerDtos.size(); i++) {
    //         Assertions.assertEquals(answerDtos.get(i).getProduct().getCid(), testDtos.get(i).getProduct().getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getCategory().getCid(), testDtos.get(i).getCategory().getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getUser().getId(), testDtos.get(i).getUser().getId());
    //         Assertions.assertEquals(answerDtos.get(i).getOption().getCid(), testDtos.get(i).getOption().getCid());
    //     }

    //     System.out.println("##### ProductOptionServiceTest.옵션_M2OJ_전체조회() 성공 #####");
    // }

    // @Test
    // public void 옵션_전체조회() {
    //     List<ProductOptionGetDto> answerDtos = optionService.searchListByProduct(78);
    //     List<ProductOptionGetDto> testDtos = optionService.searchListByProductTest(78);

    //     for(int i = 0; i < answerDtos.size(); i++) {
    //         Assertions.assertEquals(answerDtos.get(i).getCid(), testDtos.get(i).getCid());
    //         Assertions.assertEquals(answerDtos.get(i).getStockSumUnit(), testDtos.get(i).getStockSumUnit ());
    //     }

    //     System.out.println("##### ProductOptionServiceTest.옵션_전체조회() 성공 #####");

    // }
}
