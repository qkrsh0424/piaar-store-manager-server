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
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;
import com.piaar_store_manager.server.utils.DateHandler;

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
     * productOptionCid ???????????? option, option??? Many To One JOIN(m2oj) ??????????????? ???????????? product, category, user??? ?????? ????????????.
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
     * ?????? option ??????, option??? Many To One JOIN(m2oj) ??????????????? ???????????? product, category, user??? ?????? ????????????.
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
     * ProductOption ???????????? ?????? ????????????.
     * ProductOption??? ??????????????? ????????????.
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
     * optionCid??? ???????????? ????????? release(??????) ???????????? ?????? ????????????.
     * optionCid??? ???????????? ????????? receive(??????) ???????????? ?????? ????????????.
     * ????????? ???????????? ????????? ProductOptionStatusDto ????????????.
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
     * release(??????) ???????????? ?????? ???????????? option, product, category, user ???????????? ?????? ????????????.
     * receive(??????) ???????????? ?????? ???????????? option, product, category, user ???????????? ?????? ????????????.
     * ????????? ???????????? ????????? ProductOptionGetDto.JoinReceiveAndRelease ????????????.
     * TODO :: refactor?????? ???????????? api. ?????????????????? ?????????
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
     * startDate??? endDate?????? ????????? ????????? ?????? release(??????) ???????????? ?????? ???????????? option, product, category, user ???????????? ?????? ????????????.
     * startDate??? endDate?????? ????????? ????????? ?????? receive(??????) ???????????? ?????? ???????????? option, product, category, user ???????????? ?????? ????????????.
     * ????????? ???????????? ????????? ProductOptionGetDto.JoinReceiveAndRelease ????????????.
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

        optionGetDto.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
            .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        productOptionService.saveAndModify(ProductOptionEntity.toEntity(optionGetDto));
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ?????? option ??????, ?????? option??? ????????? package??? ?????? ????????????.
     * ?????? package??? size??? ?????? packageYn??? ????????????.
     *
     * @see ProductService#saveAndGet
     * @see ProductOptionService#createList
     * @see OptionPackageService#saveListAndModify
     */
    @Transactional
    public void createOAP(ProductOptionGetDto.CreateReq reqDto) {
        UUID USER_ID = userService.getUserId();

        ProductOptionGetDto optionGetDto = reqDto.getOptionDto()
                .setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
            .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        // ????????? ?????? ??????
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
                .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID)
                .setProductCid(productOptionDto.getProductCid());

        productOptionService.saveAndModify(optionEntity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ?????? option ??????, ?????? option??? ????????? package??? ?????? ????????????.
     * ?????? package??? size??? ?????? packageYn??? ????????????.
     * 
     * 1. ?????? option??? ????????? package ?????? ??????
     * 2. ?????? ???????????? ???????????? package ??????
     *
     * @see ProductOptionService#createList
     * @see OptionPackageService#deleteBatchByParentOptionId
     * @see OptionPackageService#saveListAndModify
     */
    @Transactional
    public void changeOAP(ProductOptionGetDto.CreateReq reqDto) {
        UUID USER_ID = userService.getUserId();

        ProductOptionGetDto productOptionGetDto = reqDto.getOptionDto();
        ProductOptionEntity productOptionEntity = productOptionService.searchOne(productOptionGetDto.getCid());
        /*
        ????????? ????????????
         */
        productOptionEntity
                .setCode(productOptionGetDto.getCode())
                .setNosUniqueCode(productOptionGetDto.getNosUniqueCode())
                .setDefaultName(productOptionGetDto.getDefaultName())
                .setManagementName(productOptionGetDto.getManagementName())
                .setNosUniqueCode(productOptionGetDto.getNosUniqueCode())
                .setSalesPrice(productOptionGetDto.getSalesPrice()).setStockUnit(productOptionGetDto.getStockUnit())
                .setTotalPurchasePrice(productOptionGetDto.getTotalPurchasePrice())
                .setStatus(productOptionGetDto.getStatus()).setMemo(productOptionGetDto.getMemo())
                .setImageUrl(productOptionGetDto.getImageUrl()).setImageFileName(productOptionGetDto.getImageFileName())
                .setColor(productOptionGetDto.getColor()).setUnitCny(productOptionGetDto.getUnitCny())
                .setUnitKrw(productOptionGetDto.getUnitKrw())
                .setPackageYn(productOptionGetDto.getPackageYn())
                .setUpdatedAt(DateHandler.getCurrentDate2())
                .setUpdatedBy(USER_ID)
                .setProductCid(productOptionGetDto.getProductCid());

        // ????????? ?????? ??????
        if(reqDto.getPackageDtos().size() > 0) {
            productOptionEntity.setPackageYn("y");
        }else {
            productOptionEntity.setPackageYn("n");
        }

        // ?????? ????????? ????????? ??????????????? ?????? ??????
        optionPackageService.deleteBatchByParentOptionId(productOptionEntity.getId());

        // ?????? ????????? ??????????????? ???????????? ????????? ??????
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
        productOptionEntity.setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);
        productOptionService.saveAndModify(productOptionEntity);
    }
}
