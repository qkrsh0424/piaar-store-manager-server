package com.piaar_store_manager.server.domain.product_release.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
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
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 출고데이터 다중 등록.
     *
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @see ProductReleaseService#saveAndModify
     */
    // @Transactional
    // public void createBatch(List<ProductReleaseGetDto> releaseDtos) {
    //     UUID USER_ID = userService.getUserId();

    //     List<ProductReleaseEntity> productReleaseEntities = releaseDtos.stream().map(dto -> {
    //         ProductReleaseEntity entity = ProductReleaseEntity.builder()
    //             .id(UUID.randomUUID())
    //             .releaseUnit(dto.getReleaseUnit())
    //             .memo(dto.getMemo().strip())
    //             .createdAt(CustomDateUtils.getCurrentDateTime())
    //             .createdBy(USER_ID)
    //             .productOptionCid(dto.getProductOptionCid())
    //             .productOptionId(dto.getProductOptionId())
    //             .build();

    //         return entity;
    //     }).collect(Collectors.toList());
    //     productReleaseService.saveListAndModify(productReleaseEntities);
    // }

    // 출고 방식 - 세트상품, 일반상품 구분해서 재고반영
    @Transactional
    public Integer createBatch(List<ProductReleaseGetDto> releaseDtos) {
        List<UUID> optionIds = releaseDtos.stream().map(ProductReleaseGetDto::getProductOptionId).collect(Collectors.toList());
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByIds(optionIds);
        AtomicInteger count = new AtomicInteger();
        
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        // 일반 옵션 반영
        count.addAndGet(this.createOriginOption(releaseDtos, originOptionEntities));
        // 세트상품 옵션 반영
        count.addAndGet(this.createParentOption(releaseDtos, parentOptionEntities));
        return count.get();       
    }

    public int createOriginOption(List<ProductReleaseGetDto> releaseDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        List<ProductReleaseEntity> releaseEntities = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        for (ProductReleaseGetDto dto : releaseDtos) {
            originOptionEntities.forEach(optionEntity -> {
                if(optionEntity.getId().equals(dto.getProductOptionId())) {
                    count.getAndIncrement();
                    ProductReleaseEntity entity = ProductReleaseEntity.builder()
                        .id(UUID.randomUUID())
                        .releaseUnit(dto.getReleaseUnit())
                        .memo(dto.getMemo() != null ? dto.getMemo().strip() : null)
                        .createdAt(CustomDateUtils.getCurrentDateTime())
                        .createdBy(USER_ID)
                        .productOptionCid(dto.getProductOptionCid())
                        .productOptionId(dto.getProductOptionId())
                        .build();

                    releaseEntities.add(entity);
                }
            });
        }

        productReleaseService.saveListAndModify(releaseEntities);
        return count.get();
    }

    public int createParentOption(List<ProductReleaseGetDto> releaseDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();
        List<ProductReleaseEntity> releaseEntities = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIds = parentOptionEntities.stream().map(ProductOptionEntity::getId).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageProjection.RelatedProductOption> optionPackageEntities = optionPackageService.searchBatchByParentOptionIds(parentOptionIds);
        
        for(ProductReleaseGetDto dto : releaseDtos) {
            parentOptionEntities.forEach(parentOption -> {
                optionPackageEntities.forEach(option -> {
                    if(option.getOptionPackage().getParentOptionId().equals(parentOption.getId())) {
                        count.getAndIncrement();

                        ProductReleaseEntity releaseEntity = ProductReleaseEntity.builder()
                            .id(UUID.randomUUID())
                            .releaseUnit(dto.getReleaseUnit() * option.getOptionPackage().getPackageUnit())
                            .memo(dto.getMemo() != null ? dto.getMemo().strip() : null)
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .productOptionCid(option.getProductOption().getCid())
                            .productOptionId(option.getOptionPackage().getOriginOptionId())
                            .build();

                        releaseEntities.add(releaseEntity);
                    }
                });
            });
        }

        productReleaseService.bulkInsert(releaseEntities);
        return count.get();
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
            releaseEntity.setMemo(releaseDto.getMemo() != null ? releaseDto.getMemo().strip() : null);
        }
    }
}
