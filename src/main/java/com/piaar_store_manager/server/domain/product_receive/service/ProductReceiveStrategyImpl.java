package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.product_receive.service.strategy.create.CreateStrategy;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.delete.DeleteStrategy;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;

import lombok.RequiredArgsConstructor;

public class ProductReceiveStrategyImpl {
    
    @Component
    @RequiredArgsConstructor
    public static class Basic implements SearchStrategy, CreateStrategy, DeleteStrategy {
        private final ProductReceiveService productReceiveService;
        private final ProductOptionService productOptionService;
        private final OptionPackageService optionPackageService;
        private final UserService userService;

        public ProductReceiveObjectType findObjectType(){
            return ProductReceiveObjectType.Basic;
        }

        @Override
        public <T> T searchOne(UUID id) {
            return (T) ProductReceiveGetDto.toDto(productReceiveService.searchOne(id));
        }

        @Override
        public <T> List<T> searchList() {
            List<ProductReceiveEntity> dtos = productReceiveService.searchList();
            return dtos.stream().map(entity -> (T)ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
        }

        @Override
        public <T> List<T> searchListByOptionCid(Integer optionCid) {
            List<ProductReceiveEntity> entities = productReceiveService.searchListByOptionCid(optionCid);
            return entities.stream().map(entity ->(T)ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
        }

        @Override
        @Transactional
        public <T> void createOne(T dto) {
            UUID USER_ID = userService.getUserId();
            ProductReceiveGetDto receiveDto = (ProductReceiveGetDto)dto;
            ProductOptionEntity optionEntity = productOptionService.searchOne(receiveDto.getProductOptionCid());
            if (optionEntity.getPackageYn().equals("n")) {
                ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                        .id(UUID.randomUUID())
                        .receiveUnit(receiveDto.getReceiveUnit())
                        .memo(receiveDto.getMemo())
                        .createdAt(CustomDateUtils.getCurrentDateTime())
                        .createdBy(USER_ID)
                        .productOptionCid(receiveDto.getProductOptionCid())
                        .build();

                productReceiveService.saveAndModify(ProductReceiveEntity.toEntity(receiveGetDto));
            } else {
                List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionId(optionEntity.getId());
                List<ProductReceiveEntity> productReceiveEntities = optionPackageEntities.stream().map(option -> {
                    ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                            .id(UUID.randomUUID())
                            .receiveUnit(option.getPackageUnit() * receiveDto.getReceiveUnit())
                            .memo(receiveDto.getMemo())
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .productOptionCid(option.getOriginOptionCid())
                            .build();
                    return ProductReceiveEntity.toEntity(receiveGetDto);
                }).collect(Collectors.toList());
                productReceiveService.saveListAndModify(productReceiveEntities);
            }
        }

        @Override
        @Transactional
        public <T> void createList(List<T> dtos) {
            UUID USER_ID = userService.getUserId();
            List<ProductReceiveGetDto> productReceiveGetDtos = dtos.stream().map(dto-> (ProductReceiveGetDto)dto).collect(Collectors.toList());
            List<Integer> optionCids = productReceiveGetDtos.stream().map(r -> r.getProductOptionCid()).collect(Collectors.toList());
            List<ProductOptionEntity> optionEntities = productOptionService.searchListByCids(optionCids);
            // 패키지 옵션 분류
            List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
            List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

            // 1) 실행
            List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
            productReceiveGetDtos.forEach(dto -> {
                originOptionEntities.forEach(option -> {
                    if (dto.getProductOptionCid().equals(option.getCid())) {
                        ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                                .id(UUID.randomUUID())
                                .receiveUnit(dto.getReceiveUnit())
                                .memo(dto.getMemo())
                                .createdAt(CustomDateUtils.getCurrentDateTime())
                                .createdBy(USER_ID)
                                .productOptionCid(option.getCid())
                                .build();

                        productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
                    }
                });
            });

            productReceiveService.saveListAndModify(productReceiveEntities);
            productReceiveEntities.clear();

            // 2) 실행
            // 패키지 상품들의 입고 데이터 생성
            if (parentOptionEntities.size() > 0) {
                List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
                List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

                productReceiveGetDtos.forEach(dto -> {
                    parentOptionEntities.forEach(parentOption -> {
                        if (dto.getProductOptionCid().equals(parentOption.getCid())) {
                            optionPackageEntities.forEach(option -> {
                                if (option.getParentOptionId().equals(parentOption.getId())) {
                                    ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                                            .id(UUID.randomUUID())
                                            .receiveUnit(option.getPackageUnit() * dto.getReceiveUnit())
                                            .memo(dto.getMemo())
                                            .createdAt(CustomDateUtils.getCurrentDateTime())
                                            .createdBy(USER_ID)
                                            .productOptionCid(option.getOriginOptionCid())
                                            .build();

                                    productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
                                }
                            });
                        }
                    });
                });
                productReceiveService.saveListAndModify(productReceiveEntities);
            }
        }

        @Override
        public <T> void destroyOne(UUID id) {
            productReceiveService.destroyOne(id);
        }

        
    }

    @Component
    @RequiredArgsConstructor
    public static class M2OJ implements SearchStrategy {
        private final ProductReceiveService productReceiveService;

        public ProductReceiveObjectType findObjectType(){
            return ProductReceiveObjectType.M2OJ;
        }

        @Override
        public <T> T searchOne(UUID id) {
            ProductReceiveProj receiveProj = productReceiveService.searchOneM2OJ(id);
            return (T) ProductReceiveGetDto.ManyToOneJoin.toDto(receiveProj);
        }

        @Override
        public <T> List<T> searchList() {
            List<ProductReceiveProj> projs = productReceiveService.searchListM2OJ();
            return projs.stream().map(proj -> (T)ProductReceiveGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        }
    }
}
