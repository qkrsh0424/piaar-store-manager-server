package com.piaar_store_manager.server.service.product_detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;
import com.piaar_store_manager.server.model.product_detail.repository.ProductDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private DateHandler dateHandler;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 조회한다.
     *
     * @param detailCid : Integer
     * @return ProductDetailGetDto
     * @see ProductDetailRepository#findById
     */
    public ProductDetailGetDto searchOne(Integer detailCid) {
        Optional<ProductDetailEntity> detailEntityOpt = productDetailRepository.findById(detailCid);
        ProductDetailGetDto detailDto = new ProductDetailGetDto();

        if (detailEntityOpt.isPresent()) {
            detailDto = ProductDetailGetDto.toDto(detailEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return detailDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductDetail 데이터를 모두 조회한다.
     * 
     * @return List::ProductDetailGetDto::
     * @see ProductDetailRepository#findAll
     * @see ProductDetailGetDto#toDto
     */
    public List<ProductDetailGetDto> searchList(Integer optionCid) {
        List<ProductDetailEntity> entities = productDetailRepository.findByProductOptionCid(optionCid);
        List<ProductDetailGetDto> detailDtos = new ArrayList<>();
        
        for(ProductDetailEntity entity : entities) {
            detailDtos.add(ProductDetailGetDto.toDto(entity));
        }

        return detailDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductDetail 내용을 한개 등록한다.
     * 
     * @param productDetailGetDto : ProductDetailGetDto
     * @param userId : UUID
     * @see ProductDetailEntity#toEntity
     * @see ProductDetailRepository#save
     */
    public void createOne(ProductDetailGetDto productDetailGetDto, UUID userId) {
        Float detailCbmValue = ((float)(productDetailGetDto.getDetailWidth() * productDetailGetDto.getDetailLength() * productDetailGetDto.getDetailHeight())) / 1000000;
        
        productDetailGetDto.setDetailCbm(detailCbmValue)
            .setCreatedAt(dateHandler.getCurrentDate())
            .setCreatedBy(userId)
            .setUpdatedAt(dateHandler.getCurrentDate())
            .setUpdatedBy(userId);

        ProductDetailEntity entity = ProductDetailEntity.toEntity(productDetailGetDto);
        productDetailRepository.save(entity);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param detailCid : Integer
     * @see ProductDetailRepository#findById
     * @see ProductDetailRepository#delete
     */
    public void destroyOne(Integer detailCid) {
        productDetailRepository.findById(detailCid).ifPresent(product -> {
            productDetailRepository.delete(product);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param dto : ProductDetailGetDto
     * @param userId : UUID
     * @see ProductDetailRepository#findById
     * @see ProductDetailRepository#save
     */
    public void changeOne(ProductDetailGetDto dto, UUID userId) {
        Float detailCbmValue = ((float)(dto.getDetailWidth() * dto.getDetailLength() * dto.getDetailHeight())) / 1000000;

        productDetailRepository.findById(dto.getCid()).ifPresentOrElse(productDetailEntity -> {
            productDetailEntity.setDetailWidth(dto.getDetailWidth())
                        .setDetailLength(dto.getDetailLength())
                        .setDetailHeight(dto.getDetailHeight())
                        .setDetailQuantity(dto.getDetailQuantity())
                        .setDetailWeight(dto.getDetailWeight())
                        .setDetailCbm(detailCbmValue)
                        .setUpdatedAt(dateHandler.getCurrentDate())
                        .setUpdatedBy(userId)
                        .setProductOptionCid(dto.getProductOptionCid());

            productDetailRepository.save(productDetailEntity);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductDetail id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param dto : ProductDetailGetDto
     * @param userId : UUID
     * @see ProductDetailRepository#findById
     * @see ProductDetailRepository#save
     */
    public void patchOne(ProductDetailGetDto dto, UUID userId) {
        productDetailRepository.findById(dto.getCid()).ifPresentOrElse(productDetailEntity -> {
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
            if (dto.getDetailCbm() != null) {
                productDetailEntity.setDetailCbm(dto.getDetailCbm());
            }

            productDetailEntity.setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

            productDetailRepository.save(productDetailEntity);
        }, null);
    }
    
}
