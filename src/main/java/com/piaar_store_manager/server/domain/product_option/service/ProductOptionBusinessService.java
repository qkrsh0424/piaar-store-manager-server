package com.piaar_store_manager.server.domain.product_option.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockStatusDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveService;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductOptionBusinessService {
    private final ProductReleaseService productReleaseService;
    private final ProductReceiveService productReceiveService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    public ProductOptionGetDto searchOne(Integer productOptionCid) {
        ProductOptionEntity entity = productOptionService.searchOne(productOptionCid);
        ProductOptionGetDto dto = ProductOptionGetDto.toDto(entity);
        return dto;
    }
    
    public List<ProductOptionGetDto> searchList() {
        List<ProductOptionEntity> entities = productOptionService.searchList();
        List<ProductOptionGetDto> dtos = entities.stream().map(entity -> ProductOptionGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid 대응하는 option, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, category, user를 함께 조회한다.
     *
     * @param productOptionCid : Integer
     * @see ProductOptionService#searchOneM2OJ
     */
    public ProductOptionGetDto.ManyToOneJoin searchOneM2OJ(Integer productOptionCid) {
        ProductOptionProj optionProj = productOptionService.searchOneM2OJ(productOptionCid);
        ProductOptionGetDto.ManyToOneJoin optionM2OJDto = ProductOptionGetDto.ManyToOneJoin.toDto(optionProj);
        return optionM2OJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 option 조회, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, category, user를 함께 조회한다.
     *
     * @return List::ProductOptionGetDto.ManyToOneJoin::
     * @see ProductOptionService#searchListM2OJ
     */
    public List<ProductOptionGetDto.ManyToOneJoin> searchListM2OJ() {
        List<ProductOptionProj> productOptionProjs = productOptionService.searchListM2OJ();
        List<ProductOptionGetDto.ManyToOneJoin> optionM2OJDtos = productOptionProjs.stream().map(proj -> ProductOptionGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return optionM2OJDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     * ProductOption의 재고수량을 계산한다.
     *
     * @param productCid : Integer
     * @return List::ProductOptionGetDto::
     * @see ProductOptionService#searchListByProductCid
     */
    public List<ProductOptionGetDto> searchListByProductCid(Integer productCid) {
        List<ProductOptionEntity> entities = productOptionService.searchListByProductCid(productCid);
        List<ProductOptionGetDto> dtos = entities.stream().map(r -> ProductOptionGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * optionCid에 대응하는 옵션의 release(출고) 데이터를 모두 조회한다.
     * optionCid에 대응하는 옵션의 receive(입고) 데이터를 모두 조회한다.
     * 입출고 데이터를 이용해 ProductOptionStatusDto 생성한다.
     *
     * @param optionCid : Integer
     * @return ProductOptionStatusDto
     * @see ProductReleaseService#searchListByOptionCid
     * @see ProductReceiveService#searchListByOptionCid
     */
    public ProductOptionStockStatusDto searchStockStatus(Integer optionCid) {
        List<ProductReleaseEntity> releaseEntities = productReleaseService.searchListByOptionCid(optionCid);
        List<ProductReceiveEntity> receiveEntities = productReceiveService.searchListByOptionCid(optionCid);
        
        List<ProductReleaseGetDto> releaseDtos = releaseEntities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        List<ProductReceiveGetDto> receiveDtos = receiveEntities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());

        ProductOptionStockStatusDto statusDto = ProductOptionStockStatusDto.builder()
            .productRelease(releaseDtos)
            .productReceive(receiveDtos)
            .build();

        return statusDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * release(출고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * receive(입고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * 입출고 데이터를 이용해 ProductOptionGetDto.JoinReceiveAndRelease 생성한다.
     * TODO :: refactor전에 사용하던 api. 제거해야하는 메서드
     *
     * @return ProductOptionGetDto.JoinReceiveAndRelease
     * @see ProductReleaseService#searchListM2OJ
     * @see ProductReceiveService#searchListM2OJ
     */
    public ProductOptionStockStatusDto.JoinReceiveAndRelease searchAllStockStatus() {
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ();
        List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ();
        
        List<ProductReleaseGetDto.JoinProdAndOption> releaseDtos = releaseProjs.stream().map(proj -> ProductReleaseGetDto.JoinProdAndOption.toDto(proj)).collect(Collectors.toList());
        List<ProductReceiveGetDto.JoinProdAndOption> receiveDtos = receiveProjs.stream().map(proj -> ProductReceiveGetDto.JoinProdAndOption.toDto(proj)).collect(Collectors.toList());

        ProductOptionStockStatusDto.JoinReceiveAndRelease statusDto = ProductOptionStockStatusDto.JoinReceiveAndRelease.builder()
            .productReceive(receiveDtos)
            .productRelease(releaseDtos)
            .build();

        return statusDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate와 endDate기간 사이에 등록된 모든 release(출고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * startDate와 endDate기간 사이에 등록된 모든 receive(입고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * 입출고 데이터를 이용해 ProductOptionGetDto.JoinReceiveAndRelease 생성한다.
     *
     * @return ProductOptionGetDto.JoinReceiveAndRelease
     * @see ProductReleaseService#searchListM2OJ
     * @see ProductReceiveService#searchListM2OJ
     */
    public ProductOptionStockStatusDto.JoinReceiveAndRelease searchAllStockStatus(Map<String,Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ(startDate, endDate);
        List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ(startDate, endDate);
        
        List<ProductReceiveGetDto.JoinProdAndOption> receiveDtos = receiveProjs.stream().map(proj -> ProductReceiveGetDto.JoinProdAndOption.toDto(proj)).collect(Collectors.toList());
        List<ProductReleaseGetDto.JoinProdAndOption> releaseDtos = releaseProjs.stream().map(proj -> ProductReleaseGetDto.JoinProdAndOption.toDto(proj)).collect(Collectors.toList());

        ProductOptionStockStatusDto.JoinReceiveAndRelease statusDto = ProductOptionStockStatusDto.JoinReceiveAndRelease.builder()
            .productReceive(receiveDtos)
            .productRelease(releaseDtos)
            .build();

        return statusDto;
    }

    public void createOne(ProductOptionGetDto optionGetDto) {
        UUID USER_ID = userService.getUserId();

        optionGetDto.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
            .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

        productOptionService.saveAndModify(ProductOptionEntity.toEntity(optionGetDto));
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 option 등록, 해당 option에 포함된 package을 함께 등록한다.
     * 이때 package의 size에 따라 packageYn을 세팅한다.
     *
     * @param reqDto : ProductOptionGetDto.CreateReq
     * @see ProductOptionService#saveAndModify
     * @see OptionPackageService#saveListAndModify
     */
    @Transactional
    public void createOAP(ProductOptionGetDto.CreateReq reqDto) {
        UUID USER_ID = userService.getUserId();

        ProductOptionGetDto optionGetDto = reqDto.getOptionDto()
                .setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
            .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

        // 패키지 상품 여부
        if(reqDto.getPackageDtos().size() > 0) {
            optionGetDto.setPackageYn("y");
        } else {
            optionGetDto.setPackageYn("n");
        }

        // option save
        productOptionService.saveAndModify(ProductOptionEntity.toEntity(optionGetDto));

        List<OptionPackageEntity> optionPackageEntities = reqDto.getPackageDtos().stream().map(r -> {
            r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
                .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

            return OptionPackageEntity.toEntity(r);
        }).collect(Collectors.toList());

        // option package save
        optionPackageService.saveListAndModify(optionPackageEntities);
    }

    public void destroyOne(Integer productOptionCid) {
        productOptionService.destroyOne(productOptionCid);
    }

    public void changeOne(ProductOptionGetDto productOptionDto) {
        UUID USER_ID = userService.getUserId();
        ProductOptionEntity optionEntity = productOptionService.searchOne(productOptionDto.getCid());

        optionEntity.setCode(productOptionDto.getCode())
                .setNosUniqueCode(productOptionDto.getNosUniqueCode())
                .setDefaultName(productOptionDto.getDefaultName())
                .setManagementName(productOptionDto.getManagementName())
                .setNosUniqueCode(productOptionDto.getNosUniqueCode())
                .setSalesPrice(productOptionDto.getSalesPrice()).setStockUnit(productOptionDto.getStockUnit())
                .setTotalPurchasePrice(productOptionDto.getTotalPurchasePrice())
                .setStatus(productOptionDto.getStatus()).setMemo(productOptionDto.getMemo())
                .setImageUrl(productOptionDto.getImageUrl()).setImageFileName(productOptionDto.getImageFileName())
                .setColor(productOptionDto.getColor()).setUnitCny(productOptionDto.getUnitCny())
                .setUnitKrw(productOptionDto.getUnitKrw())
                .setPackageYn(productOptionDto.getPackageYn())
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID)
                .setProductCid(productOptionDto.getProductCid());

        productOptionService.saveAndModify(optionEntity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 option 수정, 해당 option에 포함된 package을 함께 수정한다.
     * 이때 package의 size에 따라 packageYn을 세팅한다.
     * 
     * 1. 해당 option에 등록된 package 모두 제거
     * 2. 현재 수정되는 데이터로 package 세팅
     *
     * @see ProductOptionService#searchOne
     * @see OptionPackageService#deleteBatchByParentOptionId
     * @see OptionPackageService#saveListAndModify
     */
    @Transactional
    public void changeOAP(ProductOptionGetDto.CreateReq reqDto) {
        UUID USER_ID = userService.getUserId();

        ProductOptionGetDto productOptionGetDto = reqDto.getOptionDto();
        ProductOptionEntity productOptionEntity = productOptionService.searchOne(productOptionGetDto.getCid());
        /*
        영속성 업데이트
         */
        productOptionEntity
                .setCode(productOptionGetDto.getCode())
                .setNosUniqueCode(productOptionGetDto.getNosUniqueCode())
                .setDefaultName(productOptionGetDto.getDefaultName())
                .setManagementName(productOptionGetDto.getManagementName())
                .setNosUniqueCode(productOptionGetDto.getNosUniqueCode())
                .setSalesPrice(productOptionGetDto.getSalesPrice()).setStockUnit(productOptionGetDto.getStockUnit())
                .setTotalPurchasePrice(productOptionGetDto.getTotalPurchasePrice())
                .setStatus(productOptionGetDto.getStatus())
                .setReleaseLocation(productOptionGetDto.getReleaseLocation())
                .setMemo(productOptionGetDto.getMemo())
                .setImageUrl(productOptionGetDto.getImageUrl()).setImageFileName(productOptionGetDto.getImageFileName())
                .setColor(productOptionGetDto.getColor()).setUnitCny(productOptionGetDto.getUnitCny())
                .setUnitKrw(productOptionGetDto.getUnitKrw())
                .setPackageYn(productOptionGetDto.getPackageYn())
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime())
                .setUpdatedBy(USER_ID)
                .setProductCid(productOptionGetDto.getProductCid());

        // 패키지 상품 여부
        if(reqDto.getPackageDtos().size() > 0) {
            productOptionEntity.setPackageYn("y");
        }else {
            productOptionEntity.setPackageYn("n");
        }

        // 해당 옵션에 등록된 옵션패키지 전체 제거
        optionPackageService.deleteBatchByParentOptionId(productOptionEntity.getId());

        // 현재 넘어온 옵션패키지 데이터로 패지지 세팅
        List<OptionPackageEntity> newOptionPackageEntities = reqDto.getPackageDtos().stream().map(r -> {
            r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID).setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);
            return OptionPackageEntity.toEntity(r);
        }).collect(Collectors.toList());

        optionPackageService.saveListAndModify(newOptionPackageEntities);
    }

    public void patchOne(ProductOptionGetDto productOptionDto) {
        UUID USER_ID = userService.getUserId();
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
        if (productOptionDto.getTotalPurchasePrice() != null) {
            productOptionEntity.setTotalPurchasePrice(productOptionDto.getTotalPurchasePrice());
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
        productOptionEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);
        productOptionService.saveAndModify(productOptionEntity);
    }
}
