package com.piaar_store_manager.server.service.product_release;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.repository.ProductReleaseRepository;

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
public class ProductReleaseServiceTest {
    
    @Autowired
    private ProductReleaseService releaseService;

    @Autowired
    private ProductReleaseRepository releaseRepository;

    // @Test
    // @Transactional
    // public void 상품출고_단일삭제() {
    //     // given
    //     UUID userId = UUID.fromString("#USER_ID#");

    //     Optional<ProductReleaseEntity> testEntity = releaseRepository.findById(5);
    //     Assertions.assertTrue(testEntity.isPresent());

    //     // when
    //     releaseService.destroyOne(testEntity.get().getCid(), userId);

    //     Optional<ProductReleaseEntity> testExEntity = releaseRepository.findById(5);

    //     // then
    //     Assertions.assertFalse(testExEntity.isPresent());

    //     System.out.println("##### ProductReleaseServiceTest.상품출고_단일삭제() 성공 #####");
    // }
}
