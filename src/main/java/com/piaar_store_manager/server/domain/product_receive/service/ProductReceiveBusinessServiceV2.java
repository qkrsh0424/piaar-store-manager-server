package com.piaar_store_manager.server.domain.product_receive.service;

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
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProjection;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReceiveBusinessServiceV2 {
    private final ProductReceiveService productReceiveService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    @Transactional
    public Integer createBatch(List<ProductReceiveGetDto> receiveDtos) {
        receiveDtos.forEach(receiveDto -> ProductReceiveGetDto.removeBlank(receiveDto));

        List<UUID> optionIds = receiveDtos.stream().map(ProductReceiveGetDto::getProductOptionId).collect(Collectors.toList());
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByIds(optionIds);
        
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());
        
        AtomicInteger count = new AtomicInteger();
        // 일반 옵션 반영
        count.addAndGet(this.createOriginOptionReceiveData(receiveDtos, originOptionEntities));
        // 세트상품 옵션 반영
        count.addAndGet(this.createParentOptionReceiveData(receiveDtos, parentOptionEntities));
        return count.get();       
    }

    public int createOriginOptionReceiveData(List<ProductReceiveGetDto> receiveDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        List<ProductReceiveEntity> receiveEntities = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        for (ProductReceiveGetDto dto : receiveDtos) {
            originOptionEntities.forEach(optionEntity -> {
                if(optionEntity.getId().equals(dto.getProductOptionId())) {
                    count.getAndIncrement();
                    ProductReceiveEntity entity = ProductReceiveEntity.builder()
                        .id(UUID.randomUUID())
                        .receiveUnit(dto.getReceiveUnit())
                        .memo(dto.getMemo())
                        .createdAt(CustomDateUtils.getCurrentDateTime())
                        .createdBy(USER_ID)
                        .productOptionCid(dto.getProductOptionCid())
                        .productOptionId(dto.getProductOptionId())
                        .build();

                    receiveEntities.add(entity);
                }
            });
        }

        productReceiveService.saveListAndModify(receiveEntities);
        return count.get();
    }

    public int createParentOptionReceiveData(List<ProductReceiveGetDto> receiveDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();
        List<ProductReceiveEntity> receiveEntities = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIds = parentOptionEntities.stream().map(ProductOptionEntity::getId).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageProjection.RelatedProductAndOption> optionPackageEntities = optionPackageService.searchBatchByParentOptionIds(parentOptionIds);
        
        for(ProductReceiveGetDto dto : receiveDtos) {
            parentOptionEntities.forEach(parentOption -> {
                optionPackageEntities.forEach(option -> {
                    if(option.getOptionPackage().getParentOptionId().equals(parentOption.getId())) {
                        count.getAndIncrement();

                        ProductReceiveEntity receiveEntity = ProductReceiveEntity.builder()
                            .id(UUID.randomUUID())
                            .receiveUnit(dto.getReceiveUnit() * option.getOptionPackage().getPackageUnit())
                            .memo(dto.getMemo())
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .productOptionCid(option.getProductOption().getCid())
                            .productOptionId(option.getOptionPackage().getOriginOptionId())
                            .build();

                        receiveEntities.add(receiveEntity);
                    }
                });
            });
        }

        productReceiveService.bulkInsert(receiveEntities);
        return count.get();
    }

    public List<ProductReceiveGetDto.RelatedProductAndProductOption> searchBatchByOptionIds(List<UUID> optionIds, Map<String,Object> params) {
        List<ProductReceiveProjection.RelatedProductAndProductOption> projs = productReceiveService.qSearchBatchByOptionIds(optionIds, params);
        List<ProductReceiveGetDto.RelatedProductAndProductOption> dtos = projs.stream().map(proj -> ProductReceiveGetDto.RelatedProductAndProductOption.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void patchOne(ProductReceiveGetDto receiveDto) {
        ProductReceiveEntity receiveEntity = productReceiveService.searchOne(receiveDto.getId());

        if (receiveDto.getReceiveUnit() != null) {
            receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
        }
        if (receiveDto.getMemo() != null) {
            receiveEntity.setMemo(receiveDto.getMemo());
        }
    }
}
