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

    public ProductGetDto searchOne(UUID id) {
        ProductEntity entity = productService.searchOne(id);
        return ProductGetDto.toDto(entity);
    }

    public List<ProductGetDto> searchList() {
        List<ProductEntity> entities = productService.searchList();
        List<ProductGetDto> dtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /*
     * 다중 상품 조회. 상품과 관련있는 카테고리, 옵션을 함께 조회.
     * 옵션의 재고수량을 계산한다.
     */
    public List<ProductGetDto.RelatedCategoryAndOptions> searchBatch(Map<String, Object> params) {
        List<ProductProjection.RelatedCategoryAndOptions> projs = productService.qfindAllFJ(params);
        List<ProductGetDto.RelatedCategoryAndOptions> productDtos = projs.stream().map(proj -> ProductGetDto.RelatedCategoryAndOptions.toDto(proj)).collect(Collectors.toList());

        this.setOptionStockSumUnit(productDtos);
        return productDtos;
    }

    /*
     * 다중 상품 페이징조회. 상품과 관련있는 카테고리, 옵션을 함께 조회.
     * 옵션의 재고수량을 계산한다.
     */
    public Page<ProductGetDto.RelatedCategoryAndOptions> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ProductProjection.RelatedCategoryAndOptions> pages = productService.qfindAllFJByPage(params, pageable);
        List<ProductGetDto.RelatedCategoryAndOptions> productDtos = pages.getContent().stream().map(proj -> ProductGetDto.RelatedCategoryAndOptions.toDto(proj)).collect(Collectors.toList());

        // option setting
        this.setOptionStockSumUnit(productDtos);
        return new PageImpl<>(productDtos, pageable, pages.getTotalElements());
    }

    /*
     * 옵션의 입출고 수량을 계산해 옵션재고수량을 세팅
     */
    public void setOptionStockSumUnit(List<ProductGetDto.RelatedCategoryAndOptions> productDtos) {
        List<ProductOptionGetDto> optionDtos = new ArrayList<>();
        productDtos.forEach(dto -> {
            if(dto.getOptions() != null) {
                optionDtos.addAll(dto.getOptions());
            }
        });

        List<UUID> optionIds = optionDtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByIds(optionIds);

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

    /*
     * 단일상품, 상품의 하위 다중옵션 생성
     */
    @Transactional
    public void createProductAndOptions(ProductGetDto.RelatedOptions reqDto) {
        UUID USER_ID = userService.getUserId();
        ProductGetDto PRODUCT = reqDto.getProduct();
        List<ProductOptionGetDto> OPTIONS = reqDto.getOptions();
        
        // 앞뒤 공백제거
        ProductGetDto.removeBlank(PRODUCT);
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

    @Transactional
    public void updateOne(ProductGetDto dto) {
        UUID USER_ID = userService.getUserId();
        ProductGetDto.removeBlank(dto);

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

    public void destroyOne(UUID productId) {
       ProductEntity productEntity = productService.searchOne(productId);
       productService.destroyOne(productEntity);
    }
}
