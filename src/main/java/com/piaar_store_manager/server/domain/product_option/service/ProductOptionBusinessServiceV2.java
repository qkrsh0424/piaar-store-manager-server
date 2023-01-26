package com.piaar_store_manager.server.domain.product_option.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.service.ProductService;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductOptionBusinessServiceV2 {
    private final ProductOptionService productOptionService;
    private final ProductService productService;
    private final UserService userService;

    /*
     * 모든 상품옵션을 조회한다
     * 옵션이 속한 상품도 함께 조회한다
     */
    public List<ProductOptionGetDto.RelatedProduct> searchAllRelatedProduct() {
        List<ProductOptionProjection.RelatedProduct> projs = productOptionService.qfindAllRelatedProduct();
        List<ProductOptionGetDto.RelatedProduct> dtos = projs.stream().map(proj -> ProductOptionGetDto.RelatedProduct.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    /*
     * 모든 상품옵션을 조회한다
     * 옵션이 속한 상품과 상품의 카테고리도 함께 조회한다
     */
    public List<ProductOptionGetDto.RelatedProductAndProductCategory> searchAllRelatedProductAndProductCategory() {
        List<ProductOptionProjection.RelatedProductAndProductCategory> projs = productOptionService.qfindAllRelatedProductAndProductCategory();
        List<ProductOptionGetDto.RelatedProductAndProductCategory> dtos = projs.stream().map(proj -> ProductOptionGetDto.RelatedProductAndProductCategory.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    public List<ProductOptionGetDto> searchBatchByProductId(UUID productId) {
        List<ProductOptionEntity> entities = productOptionService.searchListByProductId(productId);
        List<ProductOptionGetDto> dtos = entities.stream().map(entity -> ProductOptionGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /*
     * 상품 옵션을 업데이트
     * 기존에 저장된 옵션과 현재 요청된 옵션을 비교해
     * update, create, delete 를 실행한다
     */
    public void updateBatch(UUID productId, List<ProductOptionGetDto> optionDtos) {
        optionDtos.forEach(option -> ProductOptionGetDto.removeBlank(option));
        ProductGetDto productDto = ProductGetDto.toDto(productService.searchOne(productId));
        
        // 기존 저장된 옵션
        List<ProductOptionEntity> originOptions = productOptionService.searchListByProductId(productId);
        List<UUID> originOptionIds = originOptions.stream().map(r -> r.getId()).collect(Collectors.toList());

        // 현재 생성 요청된 옵션
        List<UUID> reqOptionIds = optionDtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        List<UUID> newOptionIds = reqOptionIds.stream().filter(option -> !originOptionIds.contains(option)).collect(Collectors.toList());

        // 변경된 옵션
        this.changeOriginOptions(optionDtos, originOptions);
        // 새로 추가된 옵션
        this.createNewOptions(optionDtos, newOptionIds, productDto);
        // 삭제된 옵션
        this.deleteSavedOptions(originOptions, reqOptionIds);
    }

    // 변경된 옵션
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
                            .setUpdatedAt(CustomDateUtils.getCurrentDateTime())
                            .setUpdatedBy(USER_ID);
                }
            });
        });
        productOptionService.saveListAndModify(originOptions);
    }

    // 새로 추가된 옵션
    public void createNewOptions(List<ProductOptionGetDto> reqOptions, List<UUID> newOptionIds, ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        List<ProductOptionEntity> newOptionEntities = reqOptions.stream()
                .filter(r -> newOptionIds.contains(r.getId()))
                .map(r -> {
                    ProductOptionEntity option = ProductOptionEntity.builder()
                            .id(UUID.randomUUID())
                            .code(CustomUniqueKeyUtils.generateCode18())
                            .defaultName(r.getDefaultName())
                            .managementName(r.getManagementName())
                            .salesPrice(r.getSalesPrice())
                            .safetyStockUnit(r.getSafetyStockUnit())
                            .status(r.getStatus())
                            .memo(r.getMemo())
                            .releaseLocation(r.getReleaseLocation())
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .updatedAt(CustomDateUtils.getCurrentDateTime())
                            .updatedBy(USER_ID)
                            .totalPurchasePrice(r.getTotalPurchasePrice())
                            .productCid(productDto.getCid())
                            .productId(productDto.getId())
                            .packageYn("n")
                            .build();

                    return option;
                }).collect(Collectors.toList());
        productOptionService.saveListAndModify(newOptionEntities);
    }

    // 삭제된 옵션
    public void deleteSavedOptions(List<ProductOptionEntity> savedOptions, List<UUID> reqOptionIds) {
        List<UUID> deletedIds = savedOptions.stream()
                .filter(r -> !reqOptionIds.contains(r.getId()))
                .map(r -> r.getId()).collect(Collectors.toList());

        if(deletedIds.size() != 0) {
            productOptionService.deleteBatch(deletedIds);
        }
    }

    public void deleteOne(UUID optionId) {
        ProductOptionEntity optionEntity = productOptionService.searchOne(optionId);
        productOptionService.deleteOne(optionEntity);
    }
}
