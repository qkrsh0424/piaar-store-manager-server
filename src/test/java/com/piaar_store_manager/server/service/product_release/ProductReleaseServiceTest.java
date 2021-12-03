package com.piaar_store_manager.server.service.product_release;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.ServerApplication;
import com.piaar_store_manager.server.controller.api.ProductReleaseApiController;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.repository.ProductReleaseRepository;
import com.piaar_store_manager.server.service.user.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// @ExtendWith(MockitoExtension.class)
// @Transactional
public class ProductReleaseServiceTest {
    
    // @Autowired
    // private ProductReleaseBusinessService releaseBuinessService;

    // @Autowired
    // private ProductReleaseRepository releaseRepository;

    // @InjectMocks
    // private ProductReleaseBusinessService releaseBusinessService;

    // @Mock
    // private ProductReleaseService releaseService;

    // @Mock
    // private ProductReleaseRepository releaseRepository;

    // @Autowired
    // private DateHandler dateHandler;

    // @Autowired
    // private UserService userService;

    // @BeforeEach
    // public void init() {
    //     MockitoAnnotations.openMocks(this);
    // }

    // @Test
    // @Transactional
    // public void 상품출고_단일삭제() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");

    //     Optional<ProductReleaseEntity> testEntity = releaseRepository.findById(5);
    //     Assertions.assertTrue(testEntity.isPresent());

    //     // when
    //     releaseBuinessService.destroyOne(testEntity.get().getCid(), userId);

    //     Optional<ProductReleaseEntity> testExEntity = releaseRepository.findById(5);

    //     // then
    //     Assertions.assertFalse(testExEntity.isPresent());

    //     System.out.println("##### ProductReleaseServiceTest.상품출고_단일삭제() 성공 #####");
    // }

    // @Test
    // public void 상품출고_단일조회() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");
    //     ProductReleaseEntity answer = ProductReleaseEntity.builder()
    //         .cid(1)
    //         .id(UUID.randomUUID())
    //         .releaseUnit(11)
    //         .memo("test")
    //         .createdAt(DateHandler.getCurrentDate2())
    //         .createdBy(userId)
    //         .productOptionCid(77)
    //         .build();

    //     ProductReleaseGetDto answerDto = ProductReleaseGetDto.toDto(answer);

    //     // when
    //     Mockito.when(releaseService.searchOne(1)).thenReturn(java.util.Optional.of(answer).get());
        
    //     ProductReleaseGetDto resultDto = releaseBusinessService.searchOne(1);

    //     // then
    //     Assertions.assertEquals(answerDto.getCid(), resultDto.getCid());

    //     System.out.println("##### ProductReleaseServiceTest.상품출고_단일조회() 성공 #####");
    // }
}
