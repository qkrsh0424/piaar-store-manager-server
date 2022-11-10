package com.piaar_store_manager.server.domain.product_option.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;

import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductOptionBusinessServiceV2 {
    private final ProductOptionService productOptionService;
    private final UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate와 endDate기간 사이에 등록된 모든 release(출고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * startDate와 endDate기간 사이에 등록된 모든 receive(입고) 데이터와 그에 대응하는 option, product, category, user 데이터를 모두 조회한다.
     * 입출고 데이터를 이용해 ProductOptionGetDto.JoinReceiveAndRelease 생성한다.
     *
     * @return ProductOptionGetDto.JoinReceiveAndRelease
     */
    public ProductOptionGetDto.RelatedProductReceiveAndProductRelease searchBatchStockStatus(List<UUID> optionIds, Map<String,Object> params) {
        ProductOptionProjection.RelatedProductReceiveAndProductRelease proj = productOptionService.qSearchBatchStockStatus(optionIds, params);
        ProductOptionGetDto.RelatedProductReceiveAndProductRelease stockStatusDto = ProductOptionGetDto.RelatedProductReceiveAndProductRelease.toDto(proj);
        return stockStatusDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 option 조회, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, category, user를 함께 조회한다.
     *
     * @return List::ProductOptionGetDto.ManyToOneJoin::
     * @see ProductOptionService#searchListM2OJ
     */
    public List<ProductOptionGetDto.ManyToOneJoin> searchAllM2OJ() {
        List<ProductOptionProj> productOptionProjs = productOptionService.searchListM2OJ();
        List<ProductOptionGetDto.ManyToOneJoin> optionM2OJDtos = productOptionProjs.stream().map(proj -> ProductOptionGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return optionM2OJDtos;
    }

    public List<ProductOptionGetDto> searchBatchByProductId(UUID productId) {
        List<ProductOptionEntity> entities = productOptionService.searchListByProductId(productId);
        List<ProductOptionGetDto> dtos = entities.stream().map(entity -> ProductOptionGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    public void updateBatch(UUID productId, List<ProductOptionGetDto> optionDtos) {
        // 기존 저장된 옵션
        List<ProductOptionEntity> originOptions = productOptionService.searchListByProductId(productId);
        List<UUID> originOptionIds = originOptions.stream().map(r -> r.getId()).collect(Collectors.toList());

        List<UUID> reqOptionIds = optionDtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        List<UUID> newOptionIds = reqOptionIds.stream().filter(option -> !originOptionIds.contains(option)).collect(Collectors.toList());

        // 변경된 옵션
        this.changeOriginOptions(optionDtos, originOptions);
        // 새로 추가된 옵션
        this.createNewOptions(optionDtos, newOptionIds);
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
                    r.setCode(CustomUniqueKeyUtils.generateOptionCode())
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
}
