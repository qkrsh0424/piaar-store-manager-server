package com.piaar_store_manager.server.service.product_category;

import java.util.List;

import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

// @RunWith(SpringRunner.class)
// @ExtendWith(SpringRunner.class)
// @AutoConfigureMockMvc
// @DataJpaTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@Transactional
public class ProductCategoryServiceTest {
    @Autowired
    private ProductCategoryService categoryService;

    @Autowired
    private ProductCategoryBusinessService categoryBusinessService;

    @BeforeEach
    public void init(){

    }

    // @Test
    // public void 카테고리_전체조회() {
    //     // given
    //     List<ProductCategoryGetDto> exDto = categoryService.searchList();
    //     List<ProductCategoryGetDto> testDto = categoryBusinessService.searchListTest();

    //     // when, then
    //     for(int i = 0; i < exDto.size(); i++){
    //         Assertions.assertThat(exDto.get(i).getCid()).isEqualTo(testDto.get(i).getCid());
    //         Assertions.assertThat(exDto.get(i).getId()).isEqualTo(testDto.get(i).getId());
    //         Assertions.assertThat(exDto.get(i).getName()).isEqualTo(testDto.get(i).getName());
    //     }

    //      System.out.println("##### ProductCategoryServiceTest.카테고리_전체조회() 성공 #####");
    // }
}
