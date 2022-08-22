package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.search.ProductReceiveSearchContext;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductReceiveBusinessService {
    private final ProductReceiveSearchContext productReceiveSearchContext;
    private final ProductReceiveService productReceiveService;
    private final UserService userService;

    public <T> T searchOne(UUID productReceiveId, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveSearchContext.setSearchStrategy(objectType);

        T dto = productReceiveSearchContext.searchOne(productReceiveId);
        return dto;
    }

    public <T> List<T> searchList(Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveSearchContext.setSearchStrategy(objectType);
        
        List<T> dtos = productReceiveSearchContext.searchList();
        return dtos;
    }

    public <T> List<T> searchListByOptionCid(Integer productOptionCid, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveSearchContext.setSearchStrategy(objectType);

        List<T> dtos = productReceiveSearchContext.searchListByOptionCid(productOptionCid);
        return dtos;
    }

    @Transactional
    public void createOne(ProductReceiveGetDto productReceiveDto) {
        UUID USER_ID = userService.getUserId();

        ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                .id(UUID.randomUUID())
                .receiveUnit(productReceiveDto.getReceiveUnit())
                .memo(productReceiveDto.getMemo())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productOptionCid(productReceiveDto.getProductOptionCid())
                .build();

        productReceiveService.saveAndModify(ProductReceiveEntity.toEntity(receiveGetDto));
    }

    @Transactional
    public void createList(List<ProductReceiveGetDto> productReceiveGetDtos) {
        UUID USER_ID = userService.getUserId();
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

    @Transactional
    public void destroyOne(UUID productReceiveId) {
        productReceiveService.destroyOne(productReceiveId);
    }

    @Transactional
    public void changeOne(ProductReceiveGetDto receiveDto) {
        ProductReceiveEntity entity = productReceiveService.searchOne(receiveDto.getId());
        entity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
    }

    @Transactional
    public void changeList(List<ProductReceiveGetDto> receiveDtos) {
        receiveDtos.stream().forEach(dto -> this.changeOne(dto));
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
