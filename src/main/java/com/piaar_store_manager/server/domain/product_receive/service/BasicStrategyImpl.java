package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.create.CreateStrategy;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.delete.DeleteStrategy;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.update.UpdateStrategy;
import com.piaar_store_manager.server.domain.product_receive.type.ProductReceiveObjectType;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BasicStrategyImpl implements SearchStrategy, CreateStrategy, DeleteStrategy, UpdateStrategy {
    private final ProductReceiveService productReceiveService;
    private final UserService userService;

    public ProductReceiveObjectType findObjectType() {
        return ProductReceiveObjectType.Basic;
    }

    @Override
    public <T> T searchOne(UUID id) {
        return (T) ProductReceiveGetDto.toDto(productReceiveService.searchOne(id));
    }

    @Override
    public <T> List<T> searchList() {
        List<ProductReceiveEntity> dtos = productReceiveService.searchList();
        return dtos.stream().map(entity -> (T) ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> searchListByOptionCid(Integer optionCid) {
        List<ProductReceiveEntity> entities = productReceiveService.searchListByOptionCid(optionCid);
        return entities.stream().map(entity -> (T) ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public <T> void createOne(T dto) {
        UUID USER_ID = userService.getUserId();
        ProductReceiveGetDto receiveDto = (ProductReceiveGetDto) dto;

        ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                .id(UUID.randomUUID())
                .receiveUnit(receiveDto.getReceiveUnit())
                .memo(receiveDto.getMemo())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productOptionCid(receiveDto.getProductOptionCid())
                .build();

        productReceiveService.saveAndModify(ProductReceiveEntity.toEntity(receiveGetDto));
    }

    @Override
    @Transactional
    public <T> void createList(List<T> dtos) {
        UUID USER_ID = userService.getUserId();
        List<ProductReceiveGetDto> productReceiveGetDtos = dtos.stream().map(dto -> (ProductReceiveGetDto) dto).collect(Collectors.toList());
        List<ProductReceiveEntity> productReceiveEntities = productReceiveGetDtos.stream().map(dto -> {
            ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                    .id(UUID.randomUUID())
                    .receiveUnit(dto.getReceiveUnit())
                    .memo(dto.getMemo())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .productOptionCid(dto.getProductOptionCid())
                    .build();

            return ProductReceiveEntity.toEntity(receiveGetDto);
        }).collect(Collectors.toList());

        productReceiveService.saveListAndModify(productReceiveEntities);
    }

    @Override
    @Transactional
    public <T> void changeOne(T dto) {
        ProductReceiveGetDto receiveDto = (ProductReceiveGetDto)dto;
        ProductReceiveEntity entity = productReceiveService.searchOne(receiveDto.getId());
        entity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
    }        
    
    @Override
    @Transactional
    public <T> void changeList(List<T> dtos) {
        dtos.stream().forEach(dto -> this.changeOne(dto));
    }

    @Override
    @Transactional
    public <T> void patchOne(T dto) {
        ProductReceiveGetDto receiveDto = (ProductReceiveGetDto)dto;
        ProductReceiveEntity receiveEntity = productReceiveService.searchOne(receiveDto.getId());

        if (receiveDto.getReceiveUnit() != null) {
            receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
        }
        if (receiveDto.getMemo() != null) {
            receiveEntity.setMemo(receiveDto.getMemo());
        }
    }

    @Override
    public <T> void destroyOne(UUID id) {
        productReceiveService.destroyOne(id);
    }
}
