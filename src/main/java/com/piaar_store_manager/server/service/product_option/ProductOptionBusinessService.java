package com.piaar_store_manager.server.service.product_option;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionJoinResDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionStatusDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionBusinessService {
    private ProductReleaseService productReleaseService;
    private ProductReceiveService productReceiveService;
    private ProductOptionService productOptionService;

    @Autowired
    public ProductOptionBusinessService(
        ProductReleaseService productReleaseService,
        ProductReceiveService productReceiveService,
        ProductOptionService productOptionService
    ) {
        this.productReleaseService = productReleaseService;
        this.productReceiveService = productReceiveService;
        this.productOptionService = productOptionService;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 조회한다.
     * 
     * @param productOptionCid : Integer
     * @return ProductOptionGetDto
     * @see ProductOptionService#searchOne
     * @see ProductOptionGetDto#toDto
     */
    public ProductOptionGetDto searchOne(Integer productOptionCid) {
        ProductOptionEntity entity = productOptionService.searchOne(productOptionCid);
        ProductOptionGetDto dto = ProductOptionGetDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductOption와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return ProductOptionJoinResDto
     * @see ProductOptionService#searchOneM2OJ
     * @see ProductGetDto#toDto
     * @see UserGetDto#toDto
     * @see ProductCategoryGetDto#toDto
     * @see ProductOptionGetDto#toDto
     */
    public ProductOptionJoinResDto searchOneM2OJ(Integer productOptionCid) {
        ProductOptionProj optionProj = productOptionService.searchOneM2OJ(productOptionCid);
        ProductOptionJoinResDto productOptionResDto = ProductOptionJoinResDto.toDto(optionProj);
        return productOptionResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     *
     * @return List::ProductOptionGetDto::
     * @see ProductOptionService#searchList
     * @see ProductOptionGetDto#toDto
     */
    public List<ProductOptionGetDto> searchList() {
        List<ProductOptionEntity> entities = productOptionService.searchList();
        List<ProductOptionGetDto> dtos = entities.stream().map(r -> ProductOptionGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     * 해당 ProductOption와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductOptionJoinResDto::
     * @see ProductOptionService#searchListM2OJ
     * @see ProductOptionJoinResDto#toDto
     */
    public List<ProductOptionJoinResDto> searchListM2OJ() {
        List<ProductOptionProj> productOptionProjs = productOptionService.searchListM2OJ();
        List<ProductOptionJoinResDto> resDtos = productOptionProjs.stream().map(r -> ProductOptionJoinResDto.toDto(r)).collect(Collectors.toList());
        return resDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     * ProductOption의 재고수량을 계산한다.
     *
     * @return List::ProductOptionGetDto::
     * @see ProductOptionRepository#findAll
     * @see ProductOptionGetDto#toDto
     */
    public List<ProductOptionGetDto> searchListByProduct(Integer productCid) {
        return productOptionService.searchListByProduct(productCid);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값에 대응하는 옵션의 출고데이터를 모두 조회한다.
     * ProductOption cid 값에 대응하는 옵션의 입고데이터를 모두 조회한다.
     * 입, 출고 데이터를 이용해 ProductOptionStatusDto 생성한다.
     *
     * @return ProductOptionStatusDto
     * @param optionCid : Integer
     * @see ProductReleaseService#searchListByOptionCid
     * @see ProductReleaseGetDto#toDto
     * @see ProductReceiveService#searchListByOptionCid
     * @see ProductReceiveService#toDto
     */
    public ProductOptionStatusDto searchStockStatus(Integer optionCid) {
        // 1. 출고데이터 조회
        List<ProductReleaseEntity> releaseEntities = productReleaseService.searchListByOptionCid(optionCid);
        List<ProductReleaseGetDto> releaseDtos = releaseEntities.stream().map(r -> ProductReleaseGetDto.toDto(r)).collect(Collectors.toList());
        
        // 2. 입고데이터 조회
        List<ProductReceiveEntity> receiveEntities = productReceiveService.searchListByOptionCid(optionCid);
        List<ProductReceiveGetDto> receiveDtos = receiveEntities.stream().map(r -> ProductReceiveGetDto.toDto(r)).collect(Collectors.toList());
        
        // 3. 합쳐서 ProductOptionStatusDto 생성
        ProductOptionStatusDto statusDto = ProductOptionStatusDto.builder()
            .productRelease(releaseDtos)
            .productReceive(receiveDtos)
            .build();

        return statusDto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 한개 등록한다.
     * 
     * @param optionGetDto : ProductOptionGetDto
     * @param userId : UUID
     * @see ProductOptionEntity#toEntity
     * @see ProductOptionService#createOne
     * @see ProductOptionGetDto#toDto
     */
    public ProductOptionGetDto createOne(ProductOptionGetDto optionGetDto, UUID userId) {
        optionGetDto.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(userId)
            .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(userId);

        ProductOptionEntity entity = productOptionService.createOne(ProductOptionEntity.toEntity(optionGetDto));
        ProductOptionGetDto dto = ProductOptionGetDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product에 종속되는 옵션(ProductOption)을 한개 등록한다.
     * 
     * @param optionGetDto : ProductOptionGetDto
     * @param userId : UUID
     * @param productCid : Integer
     * @see ProductOptionEntity#toEntity
     * @see ProductOptionService#createOne
     * @see ProductOptionGetDto#toDto
     */
    // public ProductOptionGetDto createOne(ProductOptionGetDto optionGetDto, UUID userId, Integer productCid) {
    //     optionGetDto.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(userId)
    //         .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(userId).setProductCid(productCid);

    //     ProductOptionEntity entity = productOptionService.createOne(ProductOptionEntity.toEntity(optionGetDto));
    //     ProductOptionGetDto dto = ProductOptionGetDto.toDto(entity);
    //     return dto;
    // }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productOptionCid : Integer
     * @see ProductOptionService#destroyOne
     */
    public void destroyOne(Integer productOptionCid) {
        productOptionService.destroyOne(productOptionCid);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productOptionDto : ProductOptionGetDto
     * @param userId : UUID
     * @see ProductOptionService#changeOne
     */
    public void changeOne(ProductOptionGetDto productOptionDto, UUID userId) {
        productOptionService.changeOne(productOptionDto, userId);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param productOptionDto : ProductOptionGetDto
     * @param userId : UUID
     * @see ProductOptionService#searchOne
     * @see ProductOptionService#createOne
     */
    public void patchOne(ProductOptionGetDto productOptionDto, UUID userId) {
        ProductOptionEntity productOptionEntity = productOptionService.searchOne(productOptionDto.getCid());

        if (productOptionDto.getCode() != null) {
            productOptionEntity.setCode(productOptionDto.getCode());
        }
        if (productOptionDto.getNosUniqueCode() != null) {
            productOptionEntity.setNosUniqueCode(productOptionDto.getNosUniqueCode());
        }
        if (productOptionDto.getDefaultName() != null) {
            productOptionEntity.setDefaultName(productOptionDto.getDefaultName());
        }
        if (productOptionDto.getManagementName() != null) {
            productOptionEntity.setManagementName(productOptionDto.getManagementName());
        }
        if (productOptionDto.getSalesPrice() != null) {
            productOptionEntity.setSalesPrice(productOptionDto.getSalesPrice());
        }
        if (productOptionDto.getStockUnit() != null) {
            productOptionEntity.setStockUnit(productOptionDto.getStockUnit());
        }
        if (productOptionDto.getStatus() != null) {
            productOptionEntity.setStatus(productOptionDto.getStatus());
        }
        if (productOptionDto.getMemo() != null) {
            productOptionEntity.setMemo(productOptionDto.getMemo());
        }
        if (productOptionDto.getImageUrl() != null) {
            productOptionEntity.setImageUrl(productOptionDto.getImageUrl());
        }
        if (productOptionDto.getImageFileName() != null) {
            productOptionEntity.setImageFileName(productOptionDto.getImageFileName());
        }
        if (productOptionDto.getColor() != null) {
            productOptionEntity.setColor(productOptionDto.getColor());
        }
        if (productOptionDto.getUnitCny() != null) {
            productOptionEntity.setUnitCny(productOptionDto.getUnitCny());
        }
        if (productOptionDto.getUnitKrw() != null) {
            productOptionEntity.setUnitKrw(productOptionDto.getUnitKrw());
        }
        if (productOptionDto.getProductCid() != null) {
            productOptionEntity.setProductCid(productOptionDto.getProductCid());
        }

        productOptionEntity.setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(userId);

        productOptionService.createOne(productOptionEntity);
    }
}
