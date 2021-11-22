package com.piaar_store_manager.server.service.product_detail;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailBusinessService {
    private final ProductDetailService productDetailService;
    
    @Autowired
    public ProductDetailBusinessService(
        ProductDetailService productDetailService
    ) {
        this.productDetailService = productDetailService;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 조회한다.
     *
     * @param detailCid : Integer
     * @return ProductDetailGetDto
     * @see ProductDetailService#searchOne
     * @see ProductDetailGetDto#toDto
     */
    public ProductDetailGetDto searchOne(Integer detailCid) {
        ProductDetailEntity entity = productDetailService.searchOne(detailCid);
        ProductDetailGetDto dto = ProductDetailGetDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid와 상응하는 ProductDetail 데이터를 모두 조회한다.
     * 
     * @return List::ProductDetailGetDto::
     * @see ProductDetailService#searchList
     * @see ProductDetailGetDto#toDto
     */
    public List<ProductDetailGetDto> searchList(Integer optionCid) {
        List<ProductDetailEntity> entities = productDetailService.searchList(optionCid);
        List<ProductDetailGetDto> dtos = entities.stream().map(r -> ProductDetailGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductDetail 내용을 한개 등록한다.
     * 
     * @param productDetailGetDto : ProductDetailGetDto
     * @param userId : UUID
     * @see ProductDetailRepository#save
     */
    public ProductDetailGetDto createOne(ProductDetailGetDto productDetailGetDto, UUID userId) {
        Float detailCbmValue = ((float)(productDetailGetDto.getDetailWidth() * productDetailGetDto.getDetailLength() * productDetailGetDto.getDetailHeight())) / 1000000;
        
        productDetailGetDto.setDetailCbm(detailCbmValue)
            .setCreatedAt(DateHandler.getCurrentDate2())
            .setCreatedBy(userId)
            .setUpdatedAt(DateHandler.getCurrentDate2())
            .setUpdatedBy(userId);

        ProductDetailEntity entity = productDetailService.createOne(ProductDetailEntity.toEntity(productDetailGetDto));
        ProductDetailGetDto dto = ProductDetailGetDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param detailCid : Integer
     * @see ProductDetailService#destroyOne
     */
    public void destroyOne(Integer detailCid) {
        productDetailService.destroyOne(detailCid);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param dto : ProductDetailGetDto
     * @param userId : UUID
     * @see ProductDetailService#searchOne
     * @see ProductDetailService#createOne
     */
    public void changeOne(ProductDetailGetDto dto, UUID userId) {
        // 상세 데이터 조회
        ProductDetailEntity entity = productDetailService.searchOne(dto.getCid());

        // 상세 데이터 변경
        Float detailCbmValue = ((float)(dto.getDetailWidth() * dto.getDetailLength() * dto.getDetailHeight())) / 1000000;

        entity.setDetailWidth(dto.getDetailWidth())
                        .setDetailLength(dto.getDetailLength())
                        .setDetailHeight(dto.getDetailHeight())
                        .setDetailQuantity(dto.getDetailQuantity())
                        .setDetailWeight(dto.getDetailWeight())
                        .setDetailCbm(detailCbmValue)
                        .setUpdatedAt(DateHandler.getCurrentDate2())
                        .setUpdatedBy(userId)
                        .setProductOptionCid(dto.getProductOptionCid());

        productDetailService.createOne(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductDetail id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param dto : ProductDetailGetDto
     * @param userId : UUID
     * 
     */
    public void patchOne(ProductDetailGetDto dto, UUID userId) {
        ProductDetailEntity productDetailEntity = productDetailService.searchOne(dto.getCid());
        Float detailCbmValue = ((float)(dto.getDetailWidth() * dto.getDetailLength() * dto.getDetailHeight())) / 1000000;

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
        productDetailEntity.setDetailCbm(detailCbmValue).setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(userId);

        productDetailService.createOne(productDetailEntity);
    }
}
