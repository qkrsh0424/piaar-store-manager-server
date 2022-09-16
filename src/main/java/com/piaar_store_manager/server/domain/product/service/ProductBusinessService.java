package com.piaar_store_manager.server.domain.product.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.service.strategy.search.ProductSearchContext;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;

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

    private final ProductSearchContext productSearchContext;

    public <T> T searchOne(UUID productId, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productSearchContext.setSearchStrategy(objectType);

        T dto = productSearchContext.searchOne(productId);
        return dto;
    }

    public <T> List<T> searchAll(Map<String,Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productSearchContext.setSearchStrategy(objectType);

        List<T> dtos = productSearchContext.searchAll();
        return dtos;
    }

    public <T> List<T> searchBatchOfManagedStock(Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productSearchContext.setSearchStrategy(objectType);

        List<T> dtos = productSearchContext.searchBatchOfManagedStock();
        return dtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 product 생성, 해당 product에 포함된 option과 option에 포함된 package를 함께 생성한다.
     *
     * @see ProductService#saveAndGet
     * @see ProductOptionService#saveListAndModify
     * @see OptionPackageService#saveListAndModify
     */
    @Transactional
    public void createOne(ProductGetDto.CreateReq reqDto) {
        UUID USER_ID = userService.getUserId();

        // Save Product
        reqDto.getProductDto().setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

        ProductEntity savedEntity = productService.saveAndGet(ProductEntity.toEntity(reqDto.getProductDto()));
        ProductGetDto savedProductDto = ProductGetDto.toDto(savedEntity);

        List<ProductOptionEntity> entities = reqDto.getOptionDtos().stream().map(r -> {
            r.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID)
                    .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID).setProductCid(savedProductDto.getCid());

            // 패키지 상품 여부
            String packageYn = reqDto.getPackageDtos().size() > 0 ? "y" : "n";
            r.setPackageYn(packageYn);

            // 옵션에 totalPurchasePrice가 입력되지 않았다면 상품의 defaultTotalPurchasePrice로 setting
            if (r.getTotalPurchasePrice() == 0) {
                r.setTotalPurchasePrice(savedProductDto.getDefaultTotalPurchasePrice());
            }

            return ProductOptionEntity.toEntity(r);
        }).collect(Collectors.toList());

        // Save ProductOption
        productOptionService.saveListAndModify(entities);

        List<OptionPackageEntity> optionPackageEntities = reqDto.getPackageDtos().stream().map(r -> {
            r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
                    .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

            return OptionPackageEntity.toEntity(r);
        }).collect(Collectors.toList());

        // Save OptionPackage
        optionPackageService.saveListAndModify(optionPackageEntities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * createOne를 반복하여 실행한다.
     *
     * @see ProductBusinessService#createOne
     */
    @Transactional
    public void createBatch(List<ProductGetDto.CreateReq> productCreateReqDtos) {
        productCreateReqDtos.stream().forEach(r -> this.createOne(r));
    }

    @Transactional
    public void destroyOne(UUID productId) {
        ProductEntity entity = productService.searchOne(productId);
        productService.destroyOne(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 product수정, product의 매입총합계에 따라 하위 option들의 매입총합계를 함께 수정한다.
     *
     * @see ProductService#saveAndModify
     * @see ProductOptionService#saveListAndModify
     */
    @Transactional
    public void changeOne(ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        ProductEntity productEntity = productService.searchOne(productDto.getCid());
        productEntity.setCode(productDto.getCode()).setManufacturingCode(productDto.getManufacturingCode())
                .setManagementNumber(productDto.getManagementNumber()).setDefaultName(productDto.getDefaultName())
                .setManagementName(productDto.getManagementName()).setImageUrl(productDto.getImageUrl())
                .setImageFileName(productDto.getImageFileName()).setPurchaseUrl(productDto.getPurchaseUrl()).setMemo(productDto.getMemo())
                .setDefaultTotalPurchasePrice(productDto.getDefaultTotalPurchasePrice())
                .setProductDetailPageId(productDto.getProductDetailPageId())
                .setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID)
                .setStockManagement(productDto.getStockManagement())
                .setProductCategoryCid(productDto.getProductCategoryCid());

        productService.saveAndModify(productEntity);

        // 옵션들의 매입총합계를 변경한다.
        List<ProductOptionEntity> optionEntities = productOptionService.searchBatchByProductCid(productEntity.getCid());
        optionEntities.stream().forEach(r -> r.setTotalPurchasePrice(productEntity.getDefaultTotalPurchasePrice()));

        productOptionService.saveListAndModify(optionEntities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * changeOne을 반복하여 실행한다.
     *
     * @see ProductBusinessService#changeOne
     */
    @Transactional
    public void changeBatch(List<ProductGetDto.CreateReq> productCreateReqDtos) {
        productCreateReqDtos.stream().forEach(req -> {
            this.changeOne(req.getProductDto());
        });
    }

    @Transactional
    public void patchOne(ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        ProductEntity productEntity = productService.searchOne(productDto.getId());

        if (productDto.getCode() != null) {
            productEntity.setCode(productDto.getCode());
        }
        if (productDto.getManufacturingCode() != null) {
            productEntity.setManufacturingCode(productDto.getManufacturingCode());
        }
        if (productDto.getManagementNumber() != null) {
            productEntity.setManagementNumber(productDto.getManagementNumber());
        }
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
        if (productDto.getDefaultTotalPurchasePrice() != null) {
            productEntity.setDefaultTotalPurchasePrice(productDto.getDefaultTotalPurchasePrice());
        }
        if (productDto.getProductDetailPageId() != null) {
            productEntity.setProductDetailPageId(productDto.getProductDetailPageId());   
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
