package com.piaar_store_manager.server.domain.product_release.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.service.strategy.search.ProductReleaseSearchContext;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseBusinessService {
    private final ProductReleaseSearchContext productReleaseSearchContext;

    private final ProductReleaseService productReleaseService;
    private final UserService userService;


    public <T> T searchOne(UUID productReceiveId, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReleaseSearchContext.setSearchStrategy(objectType);

        T dto = productReleaseSearchContext.searchOne(productReceiveId);
        return dto;
    }

    public <T> List<T> searchAll(Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReleaseSearchContext.setSearchStrategy(objectType);

        List<T> dto = productReleaseSearchContext.searchAll();
        return dto;
    }

    public <T> List<T> searchBatchByOptionCid(Integer productOptionCid, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReleaseSearchContext.setSearchStrategy(objectType);

        List<T> dtos = productReleaseSearchContext.searchBatchByOptionCid(productOptionCid);
        return dtos;
    }

    @Transactional
    public void createOne(ProductReleaseGetDto productReleaseDto) {
        UUID USER_ID = userService.getUserId();

        ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                .id(UUID.randomUUID())
                .releaseUnit(productReleaseDto.getReleaseUnit())
                .memo(productReleaseDto.getMemo())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productOptionCid(productReleaseDto.getProductOptionCid())
                .build();

        productReleaseService.saveAndModify(ProductReleaseEntity.toEntity(releaseGetDto));
    }

    @Transactional
    public void createBatch(List<ProductReleaseGetDto> productReleaseGetDtos) {
        UUID USER_ID = userService.getUserId();
        List<ProductReleaseEntity> productReleaseEntities = productReleaseGetDtos.stream().map(dto -> {
            ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                    .id(UUID.randomUUID())
                    .releaseUnit(dto.getReleaseUnit())
                    .memo(dto.getMemo())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .productOptionCid(dto.getProductOptionCid())
                    .build();

            return ProductReleaseEntity.toEntity(releaseGetDto);
        }).collect(Collectors.toList());
        
        productReleaseService.saveListAndModify(productReleaseEntities);
    }

    @Transactional
    public void destroyOne(UUID productReleaseId) {
        productReleaseService.destroyOne(productReleaseId);
    }

    @Transactional
    public void changeOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity entity = productReleaseService.searchOne(releaseDto.getId());
        entity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
    }

    @Transactional
    public void changeBatch(List<ProductReleaseGetDto> releaseDtos) {
        releaseDtos.stream().forEach(r -> this.changeOne(r));
    }

    @Transactional
    public void patchOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity releaseEntity = productReleaseService.searchOne(releaseDto.getId());

        if (releaseDto.getReleaseUnit() != null) {
            releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        }
        if (releaseDto.getMemo() != null) {
            releaseEntity.setMemo(releaseDto.getMemo());
        }
    }
}
