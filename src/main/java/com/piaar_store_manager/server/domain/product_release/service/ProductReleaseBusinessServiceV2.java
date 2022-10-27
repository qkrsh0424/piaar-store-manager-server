package com.piaar_store_manager.server.domain.product_release.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseBusinessServiceV2 {
    private final ProductReleaseService productReleaseService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 출고데이터 다중 등록.
     *
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @see ProductReleaseService#saveAndModify
     */
    @Transactional
    public void createBatch(List<ProductReleaseGetDto> releaseDtos) {
        UUID USER_ID = userService.getUserId();

        List<ProductReleaseGetDto> dtos = releaseDtos.stream().filter(r -> !r.getReleaseUnit().equals(0)).collect(Collectors.toList());
        List<ProductReleaseEntity> productReleaseEntities = dtos.stream().map(dto -> {
            ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                    .id(UUID.randomUUID())
                    .releaseUnit(dto.getReleaseUnit())
                    .memo(dto.getMemo())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .productOptionCid(dto.getProductOptionCid())
                    .productOptionId(dto.getProductOptionId())
                    .build();

            return ProductReleaseEntity.toEntity(releaseGetDto);
        }).collect(Collectors.toList());

        productReleaseService.saveListAndModify(productReleaseEntities);
    }
}
