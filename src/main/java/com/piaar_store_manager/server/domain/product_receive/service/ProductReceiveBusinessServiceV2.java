package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReceiveBusinessServiceV2 {
    private final ProductReceiveService productReceiveService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 입고데이터 다중 등록.
     *
     * @param productReceiveGetDtos : List::ProductReceiveGetDto::
     * @see ProductReceiveService#saveAndModify
     */
    @Transactional
    public void createBatch(List<ProductReceiveGetDto> receiveDtos) {
        UUID USER_ID = userService.getUserId();

        List<ProductReceiveGetDto> dtos = receiveDtos.stream().filter(r -> !r.getReceiveUnit().equals(0)).collect(Collectors.toList());
        List<ProductReceiveEntity> productReceiveEntities = dtos.stream().map(dto -> {
            ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                    .id(UUID.randomUUID())
                    .receiveUnit(dto.getReceiveUnit())
                    .memo(dto.getMemo())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .productOptionCid(dto.getProductOptionCid())
                    .productOptionId(dto.getProductOptionId())
                    .build();

            return ProductReceiveEntity.toEntity(receiveGetDto);
        }).collect(Collectors.toList());

        productReceiveService.saveListAndModify(productReceiveEntities);
    }
}
