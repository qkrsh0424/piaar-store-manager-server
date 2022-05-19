package com.piaar_store_manager.server.domain.product_detail.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.domain.product_detail.entity.ProductDetailEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.DateHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailBusinessService {
    private final ProductDetailService productDetailService;
    private final UserService userService;
    private final Integer CBM_CONVERT_VALUE = 1000000;

    public ProductDetailGetDto searchOne(Integer detailCid) {
        ProductDetailEntity entity = productDetailService.searchOne(detailCid);
        ProductDetailGetDto dto = ProductDetailGetDto.toDto(entity);
        return dto;
    }

    public List<ProductDetailGetDto> searchAll() {
        List<ProductDetailEntity> entities = productDetailService.searchAll();
        List<ProductDetailGetDto> dtos = entities.stream().map(entity -> ProductDetailGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * optionCid에 대응되는 product detail을 모두 조회한다.
     */
    public List<ProductDetailGetDto> searchList(Integer optionCid) {
        List<ProductDetailEntity> entities = productDetailService.searchList(optionCid);
        List<ProductDetailGetDto> dtos = entities.stream().map(entity -> ProductDetailGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 product detail등록.
     * 입력된 가로, 세로, 높이 값으로 cbm을 구한다.
     */
    public void createOne(ProductDetailGetDto productDetailGetDto) {
        UUID USER_ID = userService.getUserId();
        Float detailCbmValue = ((float)(productDetailGetDto.getDetailWidth() * productDetailGetDto.getDetailLength() * productDetailGetDto.getDetailHeight())) / CBM_CONVERT_VALUE;
        
        productDetailGetDto.setDetailCbm(detailCbmValue)
            .setCreatedAt(DateHandler.getCurrentDate2())
            .setCreatedBy(USER_ID)
            .setUpdatedAt(DateHandler.getCurrentDate2())
            .setUpdatedBy(USER_ID);

        productDetailService.saveAndModify(ProductDetailEntity.toEntity(productDetailGetDto));
    }

    public void destroyOne(Integer detailCid) {
        productDetailService.destroyOne(detailCid);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 product detail 수정.
     * 수정된 가로, 세로, 높이 값으로 cbm을 구한다.
     */
    public void changeOne(ProductDetailGetDto dto) {
        UUID USER_ID = userService.getUserId();
        Float detailCbmValue = ((float)(dto.getDetailWidth() * dto.getDetailLength() * dto.getDetailHeight())) / CBM_CONVERT_VALUE;

        // 상세 데이터 조회
        ProductDetailEntity entity = productDetailService.searchOne(dto.getCid());

        entity.setDetailWidth(dto.getDetailWidth())
                        .setDetailLength(dto.getDetailLength())
                        .setDetailHeight(dto.getDetailHeight())
                        .setDetailQuantity(dto.getDetailQuantity())
                        .setDetailWeight(dto.getDetailWeight())
                        .setDetailCbm(detailCbmValue)
                        .setUpdatedAt(DateHandler.getCurrentDate2())
                        .setUpdatedBy(USER_ID)
                        .setProductOptionCid(dto.getProductOptionCid());

        productDetailService.saveAndModify(entity);
    }

    public void patchOne(ProductDetailGetDto dto) {
        UUID USER_ID = userService.getUserId();

        ProductDetailEntity productDetailEntity = productDetailService.searchOne(dto.getCid());
        Float detailCbmValue = ((float)(dto.getDetailWidth() * dto.getDetailLength() * dto.getDetailHeight())) / CBM_CONVERT_VALUE;

        if (dto.getDetailWidth() != null) {
            productDetailEntity.setDetailWidth(dto.getDetailWidth());
        }
        if (dto.getDetailLength() != null) {
            productDetailEntity.setDetailLength(dto.getDetailLength());
        }
        if (dto.getDetailHeight() != null) {
            productDetailEntity.setDetailHeight(dto.getDetailHeight());
        }
        if (dto.getDetailQuantity() != null) {
            productDetailEntity.setDetailQuantity(dto.getDetailQuantity());
        }
        if (dto.getDetailWeight() != null) {
            productDetailEntity.setDetailWeight(dto.getDetailWeight());
        }
        productDetailEntity.setDetailCbm(detailCbmValue).setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        productDetailService.saveAndModify(productDetailEntity);
    }
}
