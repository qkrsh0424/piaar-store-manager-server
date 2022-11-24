package com.piaar_store_manager.server.domain.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductProjection;
import com.piaar_store_manager.server.domain.product.proj.ProductProjection.RelatedCategoryAndOptions;
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
     * categortyCid에 대응하는 product를 조회한다.
     *
     * @see ProductService#searchListByCategory
     */
    public List<ProductGetDto> searchListByCategory(Integer categoryCid) {
        List<ProductEntity> entities = productService.searchListByCategory(categoryCid);
        List<ProductGetDto> productDtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return productDtos;
    }

    // 22.10.11 NEW
    public List<ProductGetDto.RelatedCategoryAndOptions> searchBatch(Map<String, Object> params) {
        List<ProductProjection.RelatedCategoryAndOptions> projs = productService.findAllFJ(params);
        List<ProductGetDto.RelatedCategoryAndOptions> productDtos = projs.stream().map(proj -> ProductGetDto.RelatedCategoryAndOptions.toDto(proj)).collect(Collectors.toList());

        this.setOptionStockSumUnit(productDtos);
        return productDtos;
    }

    // 22.10.17 NEW
    public Page<ProductGetDto.RelatedCategoryAndOptions> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ProductProjection.RelatedCategoryAndOptions> pages = productService.findAllFJByPage(params, pageable);
        List<ProductProjection.RelatedCategoryAndOptions> projs = pages.getContent();
        List<ProductGetDto.RelatedCategoryAndOptions> productDtos = projs.stream().map(proj -> ProductGetDto.RelatedCategoryAndOptions.toDto(proj)).collect(Collectors.toList());

        // option setting
        this.setOptionStockSumUnit(productDtos);
        return new PageImpl<>(productDtos, pageable, pages.getTotalElements());
    }

    // 옵션 입출고 수량 계산. 옵션의 재고수량을 수정
    public void setOptionStockSumUnit(List<ProductGetDto.RelatedCategoryAndOptions> productDtos) {
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

        productGetDto.setCode(CustomUniqueKeyUtils.generateCode18()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

        ProductEntity entity = ProductEntity.toEntity(productGetDto);
    }

    @Transactional
    public void createList(List<ProductGetDto> productGetDto) {
        UUID USER_ID = userService.getUserId();

        List<ProductEntity> productEntities = productGetDto.stream().map(r -> {
            r.setCode(CustomUniqueKeyUtils.generateCode18()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                    .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

            return ProductEntity.toEntity(r);
        }).collect(Collectors.toList());

        productService.saveListAndModify(productEntities);
    }

    /**
     * [221005] FEAT
     * 20221117 strip() 설정
     * 20221122 공백제거 메서드 생성 후 적용
     */
    @Transactional
    public void createProductAndOptions(ProductGetDto.RelatedOptions reqDto) {
        UUID USER_ID = userService.getUserId();
        ProductGetDto PRODUCT = reqDto.getProduct();
        List<ProductOptionGetDto> OPTIONS = reqDto.getOptions();
        ProductGetDto.removeBlank(PRODUCT);     // 앞뒤 공백제거
        OPTIONS.forEach(option -> ProductOptionGetDto.removeBlank(option));

        ProductEntity productEntity = ProductEntity.builder()
            .id(UUID.randomUUID())
            .code(CustomUniqueKeyUtils.generateCode18())
            .defaultName(PRODUCT.getDefaultName())
            .managementName(PRODUCT.getManagementName())
            .imageUrl(PRODUCT.getImageUrl())
            .imageFileName(PRODUCT.getImageFileName())
            .purchaseUrl(PRODUCT.getPurchaseUrl())
            .memo(PRODUCT.getMemo())
            .stockManagement(PRODUCT.getStockManagement())
            .createdAt(CustomDateUtils.getCurrentDateTime())
            .createdBy(USER_ID)
            .updatedAt(CustomDateUtils.getCurrentDateTime())
            .updatedBy(USER_ID)
            .productCategoryCid(PRODUCT.getProductCategoryCid())
            .build();

        ProductEntity savedProductEntity = productService.saveAndGet(productEntity);

        List<ProductOptionEntity> optionEntities = OPTIONS.stream().map(optionDto -> {
            ProductOptionEntity entity = ProductOptionEntity.builder()
                .id(UUID.randomUUID())
                .code(CustomUniqueKeyUtils.generateCode18())
                .defaultName(optionDto.getDefaultName())
                .managementName(optionDto.getManagementName())
                .salesPrice(optionDto.getSalesPrice())
                .safetyStockUnit(optionDto.getSafetyStockUnit())
                .status(optionDto.getStatus())
                .memo(optionDto.getMemo())
                .releaseLocation(optionDto.getReleaseLocation())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .updatedAt(CustomDateUtils.getCurrentDateTime())
                .updatedBy(USER_ID)
                .productCid(savedProductEntity.getCid())
                .productId(savedProductEntity.getId())
                .totalPurchasePrice(optionDto.getTotalPurchasePrice())
                .packageYn("n")
                .build();

            return entity;
        }).collect(Collectors.toList());

        // Save ProductOption
        productOptionService.saveListAndModify(optionEntities);
    }

    public void createNewOptions(List<ProductOptionGetDto> reqOptions, List<UUID> newOptionIds) {
        UUID USER_ID = userService.getUserId();

        List<ProductOptionEntity> newOptionEntities = reqOptions.stream()
            .filter(r -> newOptionIds.contains(r.getId()))
            .map(r -> {
                r.setCode(CustomUniqueKeyUtils.generateCode18())
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
        ProductGetDto.removeBlank(dto);

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
    public ProductGetDto.RelatedOptions searchProductAndOptions(UUID productId) {
        ProductProjection.RelatedCategoryAndOptions proj = productService.qSelectProductAndOptions(productId);
        return ProductGetDto.RelatedOptions.toDto(proj);
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
    // @Transactional
    // public void changePAO(ProductGetDto productDto) {
    //     UUID USER_ID = userService.getUserId();

    //     ProductEntity productEntity = productService.searchOne(productDto.getId());
    //     productEntity
    //             .setDefaultName(productDto.getDefaultName()).setManagementName(productDto.getManagementName())
    //             .setImageUrl(productDto.getImageUrl()).setImageFileName(productDto.getImageFileName())
    //             .setPurchaseUrl(productDto.getPurchaseUrl()).setMemo(productDto.getMemo())
    //             .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID)
    //             .setStockManagement(productDto.getStockManagement())
    //             .setProductCategoryCid(productDto.getProductCategoryCid());

    //     productService.saveAndModify(productEntity);
    // }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * changeOne를 반복하여 실행한다.
     *
     * @see ProductBusinessService#changeOne
     */
    // @Transactional
    // public void changePAOList(List<ProductGetDto.CreateReq> productCreateReqDtos) {
    //     productCreateReqDtos.stream().forEach(req -> {
    //         this.changePAO(req.getProductDto());
    //     });
    // }

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
