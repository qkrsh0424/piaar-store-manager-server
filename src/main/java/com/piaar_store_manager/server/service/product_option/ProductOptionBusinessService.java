package com.piaar_store_manager.server.service.product_option;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionCreateReqDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionJoinReceiveAndReleaseDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionJoinResDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionStatusDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveJoinOptionDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseJoinOptionDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.service.option_package.OptionPackageService;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;

import com.piaar_store_manager.server.service.user.UserService;
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
     * @see ProductOptionGetDto#toDtos
     */
    public List<ProductOptionGetDto> searchList() {
        List<ProductOptionEntity> entities = productOptionService.searchList();
        List<ProductOptionGetDto> dtos = entities.stream().map(entity -> ProductOptionGetDto.toDto(entity)).collect(Collectors.toList());
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
     * @see ProductOptionJoinResDto#toDtos
     */
    public List<ProductOptionJoinResDto> searchListM2OJ() {
        List<ProductOptionProj> productOptionProjs = productOptionService.searchListM2OJ();
        List<ProductOptionJoinResDto> resDtos = productOptionProjs.stream().map(proj -> ProductOptionJoinResDto.toDto(proj)).collect(Collectors.toList());
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
        List<ProductOptionEntity> entities = productOptionService.searchListByProduct(productCid);
        List<ProductOptionGetDto> dtos = entities.stream().map(r -> ProductOptionGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
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
     * @see ProductReceiveGetDto#toDto
     */
    public ProductOptionStatusDto searchStockStatus(Integer optionCid) {
        // 1. 출고데이터 조회
        List<ProductReleaseEntity> releaseEntities = productReleaseService.searchListByOptionCid(optionCid);
        List<ProductReleaseGetDto> releaseDtos = releaseEntities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        
        // 2. 입고데이터 조회
        List<ProductReceiveEntity> receiveEntities = productReceiveService.searchListByOptionCid(optionCid);
        List<ProductReceiveGetDto> receiveDtos = receiveEntities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());

        // 3. 합쳐서 ProductOptionStatusDto 생성
        ProductOptionStatusDto statusDto = ProductOptionStatusDto.builder()
            .productRelease(releaseDtos)
            .productReceive(receiveDtos)
            .build();

        return statusDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 출고데이터와 그에 대응하는 옵션 및 상품 데이터를 모두 조회한다.
     * 입고데이터와 그에 대응하는 옵션 및 상품 데이터를 모두 조회한다.
     * 입, 출고 데이터를 이용해 ProductOptionStatusDto 생성한다.
     *
     * @return ProductOptionJoinReceiveAndReleaseDto
     * @see ProductReleaseService#searchListM2OJ
     * @see ProductReleaseGetDto#toDto
     * @see ProductReceiveService#searchListM2OJ
     * @see ProductReceiveGetDto#toDto
     */
    public ProductOptionJoinReceiveAndReleaseDto searchAllStockStatus() {
        // 1. 출고데이터 조회
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ();
        List<ProductReleaseJoinOptionDto> releaseDtos = releaseProjs.stream().map(proj -> ProductReleaseJoinOptionDto.toDto(proj)).collect(Collectors.toList());
        
        // // 2. 입고데이터 조회
        List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ();
        List<ProductReceiveJoinOptionDto> receiveDtos = receiveProjs.stream().map(proj -> ProductReceiveJoinOptionDto.toDto(proj)).collect(Collectors.toList());


        // 3. 합쳐서 ProductOptionStatusDto 생성
        ProductOptionJoinReceiveAndReleaseDto statusDto = ProductOptionJoinReceiveAndReleaseDto.builder()
            .productRelease(releaseDtos)
            .productReceive(receiveDtos)
            .build();

        return statusDto;
    }

    public ProductOptionJoinReceiveAndReleaseDto searchAllStockStatus(Map<String,Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        
        // 1. 출고데이터 조회
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ(startDate, endDate);
        List<ProductReleaseJoinOptionDto> releaseDtos = releaseProjs.stream().map(proj -> ProductReleaseJoinOptionDto.toDto(proj)).collect(Collectors.toList());
        
        // // 2. 입고데이터 조회
        List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ(startDate, endDate);
        List<ProductReceiveJoinOptionDto> receiveDtos = receiveProjs.stream().map(proj -> ProductReceiveJoinOptionDto.toDto(proj)).collect(Collectors.toList());

        // 3. 합쳐서 ProductOptionStatusDto 생성
        ProductOptionJoinReceiveAndReleaseDto statusDto = ProductOptionJoinReceiveAndReleaseDto.builder()
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
     * @see ProductOptionEntity#toEntity
     * @see ProductOptionService#createOne
     * @see ProductOptionGetDto#toDto
     */
    public ProductOptionGetDto createOne(ProductOptionGetDto optionGetDto) {
        UUID USER_ID = userService.getUserId();

        optionGetDto.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
            .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        ProductOptionEntity entity = productOptionService.createOne(ProductOptionEntity.toEntity(optionGetDto));
        ProductOptionGetDto dto = ProductOptionGetDto.toDto(entity);
        return dto;
    }

    public ProductOptionGetDto createOAP(ProductOptionCreateReqDto reqDto) {
        UUID USER_ID = userService.getUserId();

        ProductOptionGetDto optionGetDto = reqDto.getOptionDto().setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
            .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        // option save
        ProductOptionEntity entity = productOptionService.createOne(ProductOptionEntity.toEntity(optionGetDto));

        List<OptionPackageEntity> optionPackageEntities = reqDto.getPackageDtos().stream().map(r -> {
            r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
                .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

            return OptionPackageEntity.toEntity(r);
        }).collect(Collectors.toList());

        // option package save
        optionPackageService.saveListAndModify(optionPackageEntities);

        ProductOptionGetDto dto = ProductOptionGetDto.toDto(entity);
        return dto;
    }

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
     * @see ProductOptionService#changeOne
     */
    public void changeOne(ProductOptionGetDto productOptionDto) {
        productOptionService.changeOne(productOptionDto);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 상품 옵션과 옵션 패키지를 함께 업데이트한다.
     */
    @Transactional
    public void changeOAP(ProductOptionCreateReqDto reqDto) {
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
                .setStatus(productOptionGetDto.getStatus()).setMemo(productOptionGetDto.getMemo())
                .setImageUrl(productOptionGetDto.getImageUrl()).setImageFileName(productOptionGetDto.getImageFileName())
                .setColor(productOptionGetDto.getColor()).setUnitCny(productOptionGetDto.getUnitCny())
                .setUnitKrw(productOptionGetDto.getUnitKrw())
                .setPackageYn(productOptionGetDto.getPackageYn())
                .setUpdatedAt(DateHandler.getCurrentDate2())
                .setUpdatedBy(USER_ID)
                .setProductCid(productOptionGetDto.getProductCid());
        this.changeOptionPackage(reqDto);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 옵션 패키지 데이터가 업데이트되는 경우를 나눠 처리한다.
     * (새로운 데이터가 추가된 경우, 기존 데이터가 변경된 경우, 기존 데이터가 제거된 경우)
     */
    public void changeOptionPackage(ProductOptionCreateReqDto reqDto) {
        // 기존에 저장된 옵션 패키지
        List<OptionPackageEntity> savedOptionPackages = optionPackageService.searchListByParentOptionId(reqDto.getOptionDto().getId());
        List<UUID> savedPackageIdList = savedOptionPackages.stream().map(r -> r.getId()).collect(Collectors.toList());
        
        // 기존 데이터가 변경된 경우
        this.changeOriginOptionPackage(reqDto, savedOptionPackages);

        List<UUID> packageIdList = reqDto.getPackageDtos().stream().map(r -> r.getId()).collect(Collectors.toList());

        // 새로 추가된 데이터
        List<UUID> newPackageIdList = packageIdList.stream().filter(optionPackage -> !savedPackageIdList.contains(optionPackage)).collect(Collectors.toList());
        
        this.saveAddOptionPackage(reqDto, newPackageIdList);
        this.deleteOriginOptionPackage(savedOptionPackages, packageIdList);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 옵션 패키지 데이터 중 새로 추가된 데이터들을 저장한다.
     */
    public void saveAddOptionPackage(ProductOptionCreateReqDto reqDto, List<UUID> newPackageIdList) {
        UUID USER_ID = userService.getUserId();

        List<OptionPackageEntity> newOptionPackageEntities = new ArrayList<>();
        reqDto.getPackageDtos().stream().forEach(r -> {
            if (newPackageIdList.contains(r.getId())) {
                r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
                        .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

                newOptionPackageEntities.add(OptionPackageEntity.toEntity(r));
            }
        });

        optionPackageService.saveListAndModify(newOptionPackageEntities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 옵션 패키지 데이터 중 기존에 저장된 데이터들의 내용을 업데이트한다.
     */
    public void changeOriginOptionPackage(ProductOptionCreateReqDto reqDto, List<OptionPackageEntity> savedOptionPackages) {
        UUID USER_ID = userService.getUserId();

        // 변경된 데이터
        reqDto.getPackageDtos().stream().forEach(optionPackage -> {
            savedOptionPackages.stream().forEach(entity -> {
                if (optionPackage.getId().equals(entity.getId())) {
                    entity.setPackageUnit(optionPackage.getPackageUnit())
                            .setOriginOptionCode(optionPackage.getOriginOptionCode())
                            .setOriginOptionId(optionPackage.getOriginOptionId())
                            .setUpdatedAt(LocalDateTime.now())
                            .setUpdatedBy(USER_ID);
                }
            });
        });

        optionPackageService.saveListAndModify(savedOptionPackages);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 옵션 패키지 데이터 중 제거된 데이터들을 제거한다.
     */
    public void deleteOriginOptionPackage(List<OptionPackageEntity> savedOptionPackages, List<UUID> packageIdList) {
        // 삭제된 데이터
        List<UUID> deletedIdList = new ArrayList<>();
        savedOptionPackages.stream().forEach(entity -> {
            if (!packageIdList.contains(entity.getId())) {
                deletedIdList.add(entity.getId());
            }
        });

        if (deletedIdList.size() != 0) {
            optionPackageService.deleteBatch(deletedIdList);
        }
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
        productOptionService.createOne(productOptionEntity);
    }
}
