package com.piaar_store_manager.server.service.product;

import java.util.List;

import com.piaar_store_manager.server.model.product.dto.ProductJoinResDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
public class ProductServiceTest {
    @Autowired
    private ProductBusinessService productBusinessService;

    @Autowired
    private ProductService productService;

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
}
