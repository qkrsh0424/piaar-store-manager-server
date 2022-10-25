package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;
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
     * 다중 receive등록.
     * receive로 넘어온 productOptionCid로 option 데이터를 조회한다.
     * 1) - option의 packageYn이 n인 상품은 receive 데이터를 바로 생성,
     * 2) - option의 packageYn이 y인 상품은 package를 구성하는 option을 찾아 receive 데이터 생성.
     * 
     * FIX => 세트상품 여부와 관계없이 현재 선택된 옵션에 대한 입고 데이터 추가.
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
