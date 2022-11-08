package com.piaar_store_manager.server.domain.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductManagementProj;
import com.piaar_store_manager.server.domain.product.proj.ProductProj;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductBusinessService {
    private final ProductService productService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    public ProductGetDto searchOne(Integer productCid) {
        // TODO :: cid -> id로 변경하자
        // ProductEntity entity = productService.searchOne(id);
        ProductEntity entity = productService.searchOne(productCid);
        return ProductGetDto.toDto(entity);
    }

    // 22.11.08 FEAT
    public ProductGetDto searchOne(UUID id) {
        ProductEntity entity = productService.searchOne(id);
        return ProductGetDto.toDto(entity);
    }

    public List<ProductGetDto> searchList() {
        List<ProductEntity> entities = productService.searchList();
        List<ProductGetDto> dtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCid에 대응하는 product, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @param productCid : Integer
     * @see ProductService#searchOneM2OJ
     */
    public ProductGetDto.ManyToOneJoin searchOneM2OJ(Integer productCid) {
        ProductProj productProj = productService.searchOneM2OJ(productCid);
        ProductGetDto.ManyToOneJoin productM2OJDto = ProductGetDto.ManyToOneJoin.toDto(productProj);
        return productM2OJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCid에 대응하는 product, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     *
     * @param productCid : Integer
     * @see ProductService#searchOneM2OJ
     * @see ProductOptionService#searchListByProductCid
     */
    public ProductGetDto.FullJoin searchOneFJ(Integer productCid) {
        ProductProj productProj = productService.searchOneM2OJ(productCid);
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByProductCid(productCid);
        List<ProductOptionGetDto> optionDtos = optionEntities.stream().map(r -> ProductOptionGetDto.toDto(r)).collect(Collectors.toList());

        ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(productProj);
        productFJDto.setOptions(optionDtos);
        return productFJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * categortyCid에 대응하는 product를 조회한다.
     *
     * @see ProductService#searchListByCategory
     */
    public List<ProductGetDto> searchListByCategory(Integer categoryCid) {
        List<ProductEntity> entities = productService.searchListByCategory(categoryCid);
        List<ProductGetDto> productDtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return productDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 product 조회, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @see ProductService#searchListM2OJ
     */
    public List<ProductGetDto.ManyToOneJoin> searchListM2OJ() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        List<ProductGetDto.ManyToOneJoin> productM2OJDto = productProjs.stream().map(proj -> ProductGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return productM2OJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 product 조회, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     *
     * @see ProductService#searchListM2OJ
     * @see ProductOptionService#searchListByProductCids
     */
    public List<ProductGetDto.FullJoin> searchListFJ() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        List<Integer> productCids = productProjs.stream().map(r -> r.getProduct().getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductCids(productCids);

        // option setting
        List<ProductGetDto.FullJoin> productFJDtos = productProjs.stream().map(r -> {
            List<ProductOptionGetDto> optionDtos = optionGetDtos.stream().filter(option -> r.getProduct().getCid().equals(option.getProductCid())).collect(Collectors.toList());

            ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(r);
            productFJDto.setOptions(optionDtos);
            return productFJDto;
        }).collect(Collectors.toList());
        return productFJDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 재고관리 여부가 true인 product 조회, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     *
     * @see ProductService#searchListM2OJ
     * @see ProductOptionService#searchListByProductCids
     */
    public List<ProductGetDto.FullJoin> searchStockListFJ() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        // 재고관리 상품 추출
        List<ProductProj> stockManagementProductProjs = productProjs.stream().filter(proj -> proj.getProduct().getStockManagement()).collect(Collectors.toList());
        List<Integer> productCids = stockManagementProductProjs.stream().map(proj -> proj.getProduct().getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductCids(productCids);

        // option setting
        List<ProductGetDto.FullJoin> productFJDtos = stockManagementProductProjs.stream().map(r -> {
            List<ProductOptionGetDto> optionDtos = optionGetDtos.stream().filter(option -> r.getProduct().getCid().equals(option.getProductCid())).collect(Collectors.toList());

            ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(r);
            productFJDto.setOptions(optionDtos);
            return productFJDto;
        }).collect(Collectors.toList());
        return productFJDtos;
    }

    // 22.10.11 NEW
    public List<ProductGetDto.FullJoin> searchBatch(Map<String, Object> params) {
        List<ProductManagementProj> projs = productService.findAllFJ(params);
        List<ProductGetDto.FullJoin> productDtos = projs.stream().map(proj -> ProductGetDto.FullJoin.toDto(proj)).collect(Collectors.toList());

        this.setOptionStockSumUnit(productDtos);
        return productDtos;
    }

    // 22.10.17 NEW
    public Page<ProductGetDto.FullJoin> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ProductManagementProj> pages = productService.findAllFJByPage(params, pageable);
        List<ProductManagementProj> projs = pages.getContent();
        List<ProductGetDto.FullJoin> productDtos = projs.stream().map(proj -> ProductGetDto.FullJoin.toDto(proj)).collect(Collectors.toList());

        // option setting
        this.setOptionStockSumUnit(productDtos);
        return new PageImpl<>(productDtos, pageable, pages.getTotalElements());
    }

    // 옵션 입출고 수량 계산. 옵션의 재고수량을 수정
    public void setOptionStockSumUnit(List<ProductGetDto.FullJoin> productDtos) {
         // option setting
         List<ProductOptionEntity> optionEntities = new ArrayList<>();
         productDtos.forEach(dto -> {
             if(dto.getOptions() != null) {
                 List<ProductOptionEntity> entities = dto.getOptions().stream().map(r -> ProductOptionEntity.toEntity(r)).collect(Collectors.toList());
                 optionEntities.addAll(entities);
             }
         });

         productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
 
         // option stockSumUnit setting
         productDtos.forEach(dto -> {
             dto.getOptions().forEach(optionDto -> {
                 optionEntities.forEach(optionEntity -> {
                     if(!optionDto.getCode().isEmpty() && optionDto.getCode().equals(optionEntity.getCode())) {
                         optionDto.setStockSumUnit(optionEntity.getStockSumUnit());
                         return;
                     }
                 });
             });
         });
    }

    @Transactional
    public void createOne(ProductGetDto productGetDto) {
        UUID USER_ID = userService.getUserId();

        productGetDto.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

        ProductEntity entity = ProductEntity.toEntity(productGetDto);
    }

    @Transactional
    public void createList(List<ProductGetDto> productGetDto) {
        UUID USER_ID = userService.getUserId();

        List<ProductEntity> productEntities = productGetDto.stream().map(r -> {
            r.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                    .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

            return ProductEntity.toEntity(r);
        }).collect(Collectors.toList());

        productService.saveListAndModify(productEntities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 product 등록, 해당 product에 포함된 option을 함께 등록하고
     * 해당 option에 포함된 option package를 함께 등록한다.
     *
     * @see ProductService#saveAndGet
     * @see ProductOptionService#saveListAndModify
     * @see OptionPackageService#saveListAndModify
     */
    // @Transactional
    // public void createPAO(ProductGetDto.CreateReq reqDto) {
    //     UUID USER_ID = userService.getUserId();

    //     // Save Product
    //     reqDto.getProductDto().setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
    //             .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

    //     ProductEntity savedEntity = productService.saveAndGet(ProductEntity.toEntity(reqDto.getProductDto()));
    //     ProductGetDto savedProductDto = ProductGetDto.toDto(savedEntity);

    //     List<ProductOptionEntity> entities = reqDto.getOptionDtos().stream().map(r -> {
    //         r.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
    //                 .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID).setProductCid(savedProductDto.getCid());

    //         // 패키지 상품 여부
    //         if (reqDto.getPackageDtos().size() > 0) {
    //             r.setPackageYn("y");
    //         } else {
    //             r.setPackageYn("n");
    //         }

    //         // 옵션에 totalPurchasePrice가 입력되지 않았다면 상품의 defaultTotalPurchasePrice로 setting
    //         if (r.getTotalPurchasePrice() == 0) {
    //             r.setTotalPurchasePrice(savedProductDto.getDefaultTotalPurchasePrice());
    //         }

    //         return ProductOptionEntity.toEntity(r);
    //     }).collect(Collectors.toList());

    //     // Save ProductOption
    //     productOptionService.saveListAndModify(entities);

    //     List<OptionPackageEntity> optionPackageEntities = reqDto.getPackageDtos().stream().map(r -> {
    //         r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
    //                 .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

    //         return OptionPackageEntity.toEntity(r);
    //     }).collect(Collectors.toList());

    //     // Save OptionPackage
    //     optionPackageService.saveListAndModify(optionPackageEntities);
    // }

    /**
     * [221005] FEAT
     */
    @Transactional
    public void createProductAndOptions(ProductGetDto.ProductAndOptions reqDto) {
        UUID USER_ID = userService.getUserId();

        // Save Product
        reqDto.getProduct()
                .setCode(CustomUniqueKeyUtils.generateKey())
                .setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

        ProductEntity savedEntity = productService.saveAndGet(ProductEntity.toEntity(reqDto.getProduct()));

        List<ProductOptionEntity> entities = reqDto.getOptions().stream().map(r -> {
            r.setCode(CustomUniqueKeyUtils.generateKey())
                    .setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                    .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID)
                    .setPackageYn("n")
                    .setProductCid(savedEntity.getCid())
                    .setProductId(savedEntity.getId());
            return ProductOptionEntity.toEntity(r);
        }).collect(Collectors.toList());

        // Save ProductOption
        productOptionService.saveListAndModify(entities);
    }

    /*
     * [221017] FEAT
     * TODO :: 제거
     */
    @Transactional
    public void updateProductAndOptions(ProductGetDto.ProductAndOptions reqDto) {
        UUID USER_ID = userService.getUserId();
        ProductGetDto PRODUCT = reqDto.getProduct();

        /*
         * Update Product
         */
        ProductEntity savedProductEntity = productService.searchOne(PRODUCT.getId());
        savedProductEntity.setDefaultName(PRODUCT.getDefaultName())
                    .setManagementName(PRODUCT.getManagementName())
                    .setImageUrl(PRODUCT.getImageUrl())
                    .setImageFileName(PRODUCT.getImageFileName())
                    .setPurchaseUrl(PRODUCT.getPurchaseUrl())
                    .setMemo(PRODUCT.getMemo())
                    .setStockManagement(PRODUCT.getStockManagement())
                    .setProductCategoryCid(PRODUCT.getProductCategoryCid())
                    .setUpdatedAt(LocalDateTime.now())
                    .setUpdatedBy(USER_ID);

        reqDto.getOptions().forEach(r -> r.setProductCid(savedProductEntity.getCid()).setProductId(savedProductEntity.getId()));
        this.updateProductOptions(reqDto);
    }

    // TODO :: 제거
    public void updateProductOptions(ProductGetDto.ProductAndOptions reqDto) {
        // 기존 저장된 옵션
        List<ProductOptionEntity> originOptions = productOptionService.searchListByProductId(reqDto.getProduct().getId());
        List<UUID> originOptionIds = originOptions.stream().map(r -> r.getId()).collect(Collectors.toList());
        
        List<UUID> reqOptionIds = reqDto.getOptions().stream().map(r -> r.getId()).collect(Collectors.toList());
        List<UUID> newOptionIds = reqOptionIds.stream().filter(option -> !originOptionIds.contains(option)).collect(Collectors.toList());
        
        // 변경된 옵션
        this.changeOriginOptions(reqDto.getOptions(), originOptions);
        // 새로 추가된 옵션
        this.createNewOptions(reqDto.getOptions(), newOptionIds);
        // 삭제된 옵션
        this.deleteSavedOptions(originOptions, reqOptionIds);
    }

    public void changeOriginOptions(List<ProductOptionGetDto> reqOptions, List<ProductOptionEntity> originOptions) {
        UUID USER_ID = userService.getUserId();

        reqOptions.forEach(reqOption -> {
            originOptions.forEach(entity -> {
                if (reqOption.getId().equals(entity.getId())) {
                    entity.setDefaultName(reqOption.getDefaultName())
                            .setManagementName(reqOption.getManagementName())
                            .setSalesPrice(reqOption.getSalesPrice())
                            .setTotalPurchasePrice(reqOption.getTotalPurchasePrice())
                            .setStatus(reqOption.getStatus())
                            .setMemo(reqOption.getMemo())
                            .setReleaseLocation(reqOption.getReleaseLocation())
                            .setSafetyStockUnit(reqOption.getSafetyStockUnit())
                            .setUpdatedAt(LocalDateTime.now())
                            .setUpdatedBy(USER_ID);
                }
            });
        });

        productOptionService.saveListAndModify(originOptions);
    }

    public void createNewOptions(List<ProductOptionGetDto> reqOptions, List<UUID> newOptionIds) {
        UUID USER_ID = userService.getUserId();

        List<ProductOptionEntity> newOptionEntities = reqOptions.stream()
            .filter(r -> newOptionIds.contains(r.getId()))
            .map(r -> {
                r.setCode(CustomUniqueKeyUtils.generateKey())
                    .setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
                    .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);
                
                return ProductOptionEntity.toEntity(r);
            }).collect(Collectors.toList());

        productOptionService.saveListAndModify(newOptionEntities);
    }

    public void deleteSavedOptions(List<ProductOptionEntity> savedOptions, List<UUID> reqOptionIds) {
        List<UUID> deletedIds = savedOptions.stream()
            .filter(r -> !reqOptionIds.contains(r.getId()))
            .map(r -> r.getId()).collect(Collectors.toList());

        if(deletedIds.size() != 0) {
            productOptionService.deleteBatch(deletedIds);
        }
    }

    @Transactional
    public void updateOne(ProductGetDto dto) {
        UUID USER_ID = userService.getUserId();

        /*
         * Update Product
         */
        ProductEntity savedProductEntity = productService.searchOne(dto.getId());
        savedProductEntity.setDefaultName(dto.getDefaultName())
                .setManagementName(dto.getManagementName())
                .setImageUrl(dto.getImageUrl())
                .setImageFileName(dto.getImageFileName())
                .setPurchaseUrl(dto.getPurchaseUrl())
                .setMemo(dto.getMemo())
                .setStockManagement(dto.getStockManagement())
                .setProductCategoryCid(dto.getProductCategoryCid())
                .setUpdatedAt(LocalDateTime.now())
                .setUpdatedBy(USER_ID);
    }

    /*
     * [221017] FEAT
     */
    public ProductGetDto.ProductAndOptions searchProductAndOptions(UUID productId) {
        ProductManagementProj proj = productService.qSelectProductAndOptions(productId);
        return ProductGetDto.ProductAndOptions.toDto(proj);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * createPAO를 반복하여 실행한다.
     *
     * @see ProductBusinessService#createPAO
     */

    public void destroyOne(UUID productId) {
       ProductEntity productEntity = productService.searchOne(productId);
       productService.destroyOne(productEntity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 product수정, product의 매입총합계에 따라 하위 option들의 매입총합계도 함께 수정한다.
     *
     * @see ProductService#saveAndModify
     * @see ProductOptionService#createList
     */
    // TODO :: 수정해야함
    @Transactional
    public void changePAO(ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        ProductEntity productEntity = productService.searchOne(productDto.getId());
        productEntity
                .setDefaultName(productDto.getDefaultName()).setManagementName(productDto.getManagementName())
                .setImageUrl(productDto.getImageUrl()).setImageFileName(productDto.getImageFileName())
                .setPurchaseUrl(productDto.getPurchaseUrl()).setMemo(productDto.getMemo())
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID)
                .setStockManagement(productDto.getStockManagement())
                .setProductCategoryCid(productDto.getProductCategoryCid());

        productService.saveAndModify(productEntity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * changeOne를 반복하여 실행한다.
     *
     * @see ProductBusinessService#changeOne
     */
    @Transactional
    public void changePAOList(List<ProductGetDto.CreateReq> productCreateReqDtos) {
        productCreateReqDtos.stream().forEach(req -> {
            this.changePAO(req.getProductDto());
        });
    }

    @Transactional
    public void patchOne(ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        ProductEntity productEntity = productService.searchOne(productDto.getId());

        if (productDto.getDefaultName() != null) {
            productEntity.setDefaultName(productDto.getDefaultName());
        }
        if (productDto.getManagementName() != null) {
            productEntity.setManagementName(productDto.getManagementName());
        }
        if (productDto.getImageUrl() != null) {
            productEntity.setImageUrl(productDto.getImageUrl());
        }
        if (productDto.getImageFileName() != null) {
            productEntity.setImageFileName(productDto.getImageFileName());
        }
        if (productDto.getPurchaseUrl() != null) {
            productEntity.setPurchaseUrl(productDto.getPurchaseUrl());
        }
        if (productDto.getMemo() != null) {
            productEntity.setMemo(productDto.getMemo());
        }
        if (productDto.getStockManagement() != null) {
            productEntity.setStockManagement(productDto.getStockManagement());
        }
        if (productDto.getProductCategoryCid() != null) {
            productEntity.setProductCategoryCid(productDto.getProductCategoryCid());
        }
        productEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);
    }
}
