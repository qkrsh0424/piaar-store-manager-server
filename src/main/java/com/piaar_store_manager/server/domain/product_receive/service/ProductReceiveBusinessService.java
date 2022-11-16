package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReceiveBusinessService {
    private final ProductReceiveService productReceiveService;
    private final UserService userService;

    public List<ProductReceiveGetDto> searchList() {
        List<ProductReceiveEntity> entities = productReceiveService.searchList();
        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid에 대응하는 receive를 조회한다.
     *
     * @param productOptionCid : Integer
     * @see ProductReceiveService#searchListByOptionCid
     */
    public List<ProductReceiveGetDto> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReceiveEntity> entities = productReceiveService.searchListByOptionCid(productOptionCid);
        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    
    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 receive등록.
     * receive로 넘어온 productOptionCid로 option 데이터를 조회한다.
     * 1) - option의 packageYn이 n인 상품은 receive 데이터를 바로 생성하고,
     * 2) - option의 packageYn이 y인 상품은 package를 구성하는 option을 찾아 receive 데이터 생성.
     *
     * FIX => 세트상품 여부와 관계없이 현재 선택된 옵션에 대한 입고 데이터 추가.
     * 
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @see ProductReceiveService#saveAndModify
     * @see ProductReceiveService#saveListAndModify
     * @see OptionPackageService#searchListByParentOptionId
     */
    @Transactional
    public void createOne(ProductReceiveGetDto productReceiveGetDto) {
        UUID USER_ID = userService.getUserId();

        ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                .id(UUID.randomUUID())
                .receiveUnit(productReceiveGetDto.getReceiveUnit())
                .memo(productReceiveGetDto.getMemo())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productOptionCid(productReceiveGetDto.getProductOptionCid())
                .build();
        productReceiveService.saveAndModify(ProductReceiveEntity.toEntity(receiveGetDto));
    }

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

    public void destroyOne(Integer productReceiveCid) {
        productReceiveService.destroyOne(productReceiveCid);
    }

    @Transactional
    public void changeOne(ProductReceiveGetDto receiveDto) {
        ProductReceiveEntity entity = productReceiveService.searchOne(receiveDto.getCid());
        entity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
    }

    @Transactional
    public void changeList(List<ProductReceiveGetDto> receiveDtos) {
        receiveDtos.stream().forEach(r -> this.changeOne(r));
    }

    @Transactional
    public void patchOne(ProductReceiveGetDto receiveDto) {
        ProductReceiveEntity receiveEntity = productReceiveService.searchOne(receiveDto.getCid());

        if (receiveDto.getReceiveUnit() != null) {
            receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
        }
        if (receiveDto.getMemo() != null) {
            receiveEntity.setMemo(receiveDto.getMemo());
        }
    }
}
