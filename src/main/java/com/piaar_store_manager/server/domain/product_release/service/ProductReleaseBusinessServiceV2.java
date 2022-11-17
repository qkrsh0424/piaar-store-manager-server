package com.piaar_store_manager.server.domain.product_release.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;
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

        List<ProductReleaseEntity> productReleaseEntities = releaseDtos.stream().map(dto -> {
            ProductReleaseEntity entity = ProductReleaseEntity.builder()
                .id(UUID.randomUUID())
                .releaseUnit(dto.getReleaseUnit())
                .memo(dto.getMemo().strip())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productOptionCid(dto.getProductOptionCid())
                .productOptionId(dto.getProductOptionId())
                .build();

            return entity;
        }).collect(Collectors.toList());
        productReleaseService.saveListAndModify(productReleaseEntities);
    }

    public List<ProductReleaseGetDto.RelatedProductAndProductOption> searchBatchByOptionIds(List<UUID> optionIds, Map<String,Object> params) {
        List<ProductReleaseProjection.RelatedProductAndProductOption> projs = productReleaseService.qSearchBatchByOptionIds(optionIds, params);
        List<ProductReleaseGetDto.RelatedProductAndProductOption> dtos = projs.stream().map(proj -> ProductReleaseGetDto.RelatedProductAndProductOption.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void patchOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity releaseEntity = productReleaseService.searchOne(releaseDto.getId());

        if (releaseDto.getReleaseUnit() != null) {
            releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        }
        if (releaseDto.getMemo() != null) {
            releaseEntity.setMemo(releaseDto.getMemo().strip());
        }
    }
}
