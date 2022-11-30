package com.piaar_store_manager.server.domain.erp_return_item.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_order_item.service.ErpOrderItemService;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.erp_return_item.dto.ErpReturnItemDto;
import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
import com.piaar_store_manager.server.domain.erp_return_item.vo.ErpReturnItemVo;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveService;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.domain.return_product_image.dto.ReturnProductImageDto;
import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;
import com.piaar_store_manager.server.domain.return_product_image.service.ReturnProductImageService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@RequiredLogin
public class ErpReturnItemBusinessService {
    private final ErpReturnItemService erpReturnItemService;
    private final ProductOptionService productOptionService;
    private final ProductReleaseService productReleaseService;
    private final ErpOrderItemService erpOrderItemService;
    private final ProductReceiveService productReceiveService;
    private final OptionPackageService optionPackageService;
    private final ReturnProductImageService returnProductImageService;
    private final UserService userService;

    /*
     * 반품 데이터는 직접 등록이 존재하지 않는다. erp 출고데이터 -> 반품데이터 생성.
     */
    @Transactional
    public void createErpReturnItemAndReturnProductImage(ErpReturnItemDto.CreateReq returnItemReqDto) {
        ErpReturnItemDto returnItemDto = returnItemReqDto.getErpReturnItem();
        List<ReturnProductImageDto> returnProductImageDtos = returnItemReqDto.getReturnImages();
        List<ReturnProductImageEntity> imageEntities = new ArrayList<>();

        UUID USER_ID = userService.getUserId();
        ErpOrderItemEntity orderItemEntity = erpOrderItemService.searchOne(returnItemDto.getErpOrderItemId());

        // erpOrderItem 조회, 재고 미반영데이터 검사
        if (orderItemEntity.getStockReflectYn().equals("n")) {
            throw new CustomInvalidDataException("재고 미반영 데이터는 반품데이터로 설정할 수 없습니다.");
        }

        if (orderItemEntity.getReturnYn().equals("y")) {
            throw new CustomInvalidDataException("이미 반품접수된 상품입니다.");
        }

        // 반품 데이터 생성
        ErpReturnItemEntity entity = ErpReturnItemEntity.builder()
                .id(UUID.randomUUID())
                .waybillNumber(returnItemDto.getWaybillNumber())
                .courier(returnItemDto.getCourier())
                .transportType(returnItemDto.getTransportType())
                .deliveryChargeReturnType(returnItemDto.getDeliveryChargeReturnType())
                .deliveryChargeReturnYn(returnItemDto.getDeliveryChargeReturnYn())
                .receiveLocation(returnItemDto.getReceiveLocation())
                .returnReasonType(returnItemDto.getReturnReasonType())
                .returnReasonDetail(returnItemDto.getReturnReasonDetail())
                .managementMemo1(returnItemDto.getManagementMemo1())
                .managementMemo2(returnItemDto.getManagementMemo2())
                .managementMemo3(returnItemDto.getManagementMemo3())
                .managementMemo4(returnItemDto.getManagementMemo4())
                .managementMemo5(returnItemDto.getManagementMemo5())
                .createdAt(LocalDateTime.now())
                .createdBy(USER_ID)
                .collectYn("n")
                .collectAt(returnItemDto.getCollectAt())
                .collectCompleteYn("n")
                .collectCompleteAt(returnItemDto.getCollectCompleteAt())
                .returnCompleteYn("n")
                .returnCompleteAt(returnItemDto.getReturnCompleteAt())
                .returnRejectYn("n")
                .returnRejectAt(returnItemDto.getReturnRejectAt())
                .defectiveYn("n")
                .stockReflectYn("n")
                .erpOrderItemId(returnItemDto.getErpOrderItemId())
                .build();

        if(returnProductImageDtos != null) {
            imageEntities.addAll(this.createProductReturnImages(returnProductImageDtos, entity));
        }

        erpReturnItemService.saveAndModify(entity);
        returnProductImageService.saveListAndModify(imageEntities);
    }

    /*
     * 반품 데이터는 직접 등록이 존재하지 않는다. erp 출고데이터 -> 반품데이터 생성.
     * 반품 데이터 다중 생성
     */
    @Transactional
    public void createBatchErpReturnItemAndReturnProductImage(List<ErpReturnItemDto.CreateReq> returnItemReqDtos) {
        UUID USER_ID = userService.getUserId();
        // 최종 저장될 erp return item과 return product image
        List<ErpReturnItemEntity> returnItemEntities = new ArrayList<>();
        List<ReturnProductImageEntity> imageEntities = new ArrayList<>();

        // erpOrderItem 조회, 재고 미반영데이터 & 반품 여부 검사
        this.checkErpOrderItemField(returnItemReqDtos);

        returnItemReqDtos.forEach(reqDto -> {
            // 요청된 erp return item과 return product image
            ErpReturnItemDto returnItemDto = reqDto.getErpReturnItem();
            List<ReturnProductImageDto> returnProductImageDtos = reqDto.getReturnImages();

            // 반품 데이터 생성
            ErpReturnItemEntity entity = ErpReturnItemEntity.builder()
                .id(UUID.randomUUID())
                .waybillNumber(returnItemDto.getWaybillNumber())
                .courier(returnItemDto.getCourier())
                .transportType(returnItemDto.getTransportType())
                .deliveryChargeReturnType(returnItemDto.getDeliveryChargeReturnType())
                .deliveryChargeReturnYn(returnItemDto.getDeliveryChargeReturnYn())
                .receiveLocation(returnItemDto.getReceiveLocation())
                .returnReasonType(returnItemDto.getReturnReasonType())
                .returnReasonDetail(returnItemDto.getReturnReasonDetail())
                .managementMemo1(returnItemDto.getManagementMemo1())
                .managementMemo2(returnItemDto.getManagementMemo2())
                .managementMemo3(returnItemDto.getManagementMemo3())
                .managementMemo4(returnItemDto.getManagementMemo4())
                .managementMemo5(returnItemDto.getManagementMemo5())
                .createdAt(LocalDateTime.now())
                .createdBy(USER_ID)
                .collectYn("n")
                .collectAt(returnItemDto.getCollectAt())
                .collectCompleteYn("n")
                .collectCompleteAt(returnItemDto.getCollectCompleteAt())
                .returnCompleteYn("n")
                .returnCompleteAt(returnItemDto.getReturnCompleteAt())
                .returnRejectYn("n")
                .returnRejectAt(returnItemDto.getReturnRejectAt())
                .defectiveYn("n")
                .stockReflectYn("n")
                .erpOrderItemId(returnItemDto.getErpOrderItemId())
                .build();

            returnItemEntities.add(entity);
            // 반품 이미지 생성
            if(returnProductImageDtos != null) {
                imageEntities.addAll(this.createProductReturnImages(returnProductImageDtos, entity));
            }
        });

        erpReturnItemService.bulkInsert(returnItemEntities);
        returnProductImageService.bulkInsert(imageEntities);
    }

    private void checkErpOrderItemField(List<ErpReturnItemDto.CreateReq> returnItemReqDtos) {
        List<UUID> orderItemIds = returnItemReqDtos.stream().map(reqDto -> reqDto.getErpReturnItem().getErpOrderItemId()).collect(Collectors.toList());
        List<ErpOrderItemEntity> orderItemEntities = erpOrderItemService.searchBatchByIds(orderItemIds);
        
        // erpOrderItem 조회, 재고 미반영데이터, 반품 검사
        orderItemEntities.forEach(entity -> {
            if (entity.getStockReflectYn().equals("n")) {
                throw new CustomInvalidDataException("재고 미반영 데이터는 반품데이터로 설정할 수 없습니다.");
            }
            if (entity.getReturnYn().equals("y")) {
                throw new CustomInvalidDataException("이미 반품접수된 상품입니다.");
            }
        });
    }
    
    // 이걸 returnProductImage에 빼야하나?
    private List<ReturnProductImageEntity> createProductReturnImages(List<ReturnProductImageDto> imageDtos, ErpReturnItemEntity returnItemEntity) {
        UUID USER_ID = userService.getUserId();

        // 반품 상품 이미지 등록
        List<ReturnProductImageEntity> imageEntities = imageDtos.stream().map(dto -> {
            ReturnProductImageEntity imageEntity = ReturnProductImageEntity.builder()
                    .id(UUID.randomUUID())
                    .imageUrl(dto.getImageUrl())
                    .imageFileName(dto.getImageFileName())
                    .createdAt(LocalDateTime.now())
                    .createdBy(USER_ID)
                    .productOptionId(dto.getProductOptionId())
                    .erpReturnItemId(returnItemEntity.getId())
                    .build();

            return imageEntity;
        }).collect(Collectors.toList());

        return imageEntities;
    }

    public Page<ErpReturnItemVo> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ErpReturnItemProj> itemPages = erpReturnItemService.findAllM2OJByPage(params, pageable);

        List<ErpReturnItemProj> itemProjs = itemPages.getContent();

        List<ErpReturnItemVo> erpReturnItemVos = this.setReleaseOptionStockUnitAndToVos(itemProjs);
        return new PageImpl<>(erpReturnItemVos, pageable, itemPages.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ErpReturnItemVo> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpReturnItemProj> itemProjs = erpReturnItemService.findAllM2OJByReleasedItem(ids, params); // 페이징 처리 x
        List<ErpReturnItemVo> itemVos = this.setReleaseOptionStockUnitAndToVos(itemProjs);
        return itemVos;
    }

    private List<ErpReturnItemVo> setReleaseOptionStockUnitAndToVos(List<ErpReturnItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistListByReturnItem(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
        /*
        ErpOrderItemProjs -> ErpOrderItemVos 변환
         */
        List<ErpReturnItemVo> erpReturnItemVos = itemProjs.stream().map(ErpReturnItemVo::toVo).collect(Collectors.toList());
        /*
        ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
         */
        List<ErpOrderItemVo> erpOrderItemVos = erpReturnItemVos.stream().map(vo -> vo.getErpOrderItem()).collect(Collectors.toList());
        ErpOrderItemVo.setOptionStockUnitForList(erpOrderItemVos, optionEntities);
        return erpReturnItemVos;
    }
    
    @Transactional
    public void updateOne(ErpReturnItemDto dto) {
        ErpReturnItemEntity entity = erpReturnItemService.searchOne(dto.getId());

        entity
            .setWaybillNumber(dto.getWaybillNumber())
            .setCourier(dto.getCourier())
            .setTransportType(dto.getTransportType())
            .setDeliveryChargeReturnType(dto.getDeliveryChargeReturnType())
            .setReceiveLocation(dto.getReceiveLocation())
            .setReturnReasonType(dto.getReturnReasonType())
            .setReturnReasonDetail(dto.getReturnReasonDetail())
            .setManagementMemo1(dto.getManagementMemo1())
            .setManagementMemo2(dto.getManagementMemo2())
            .setManagementMemo3(dto.getManagementMemo3())
            .setManagementMemo4(dto.getManagementMemo4())
            .setManagementMemo5(dto.getManagementMemo5());
    }

    @Transactional
    public void changeBatchForCollectYn(List<ErpReturnItemDto> itemDtos) {
        List<UUID> idList = itemDtos.stream().map(ErpReturnItemDto::getId).collect(Collectors.toList());
        List<ErpReturnItemEntity> entities = erpReturnItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {

                if (dto.getCollectYn().equals("n")) {
                    entity.setCollectYn("n");
                    entity.setCollectAt(null);
                    
                    // TODO :: 수거중, 수거완료, 처리완료는 각 상태별로 존재하는 데이터가 중복되지 않는다. 따라서 수거중처리만 하면됨.
                    // entity.setReleaseYn("n");
                    // entity.setReleaseAt(null);
                    return;
                }

                entity.setCollectYn("y");
                entity.setCollectAt(CustomDateUtils.getCurrentDateTime());
            }
        }));

        erpReturnItemService.saveListAndModify(entities);
    }

    @Transactional
    public void changeBatchForCollectCompleteYn(List<ErpReturnItemDto> itemDtos) {
        List<UUID> idList = itemDtos.stream().map(ErpReturnItemDto::getId).collect(Collectors.toList());
        List<ErpReturnItemEntity> entities = erpReturnItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            // 불량상품 및 재고반영 데이터 제한
            if(entity.getReturnRejectYn().equals("y")) {
                throw new CustomInvalidDataException("번퓸거절 데이터는 상태를 변경할 수 없습니다.");
            }

            if (entity.getId().equals(dto.getId())) {
                if (dto.getCollectCompleteYn().equals("n")) {
                    entity.setCollectCompleteYn("n");
                    entity.setCollectCompleteAt(null);
                    
                    // TODO :: 수거중, 수거완료, 처리완료는 각 상태별로 존재하는 데이터가 중복되지 않는다. 따라서 수거완료처리만 하면됨.
                    // entity.setReleaseYn("n");
                    // entity.setReleaseAt(null);
                    return;
                }

                entity.setCollectCompleteYn("y");
                entity.setCollectCompleteAt(CustomDateUtils.getCurrentDateTime());
            }
        }));

        erpReturnItemService.saveListAndModify(entities);
    }

    @Transactional
    public void changeBatchForReturnCompleteYn(List<ErpReturnItemDto> itemDtos) {
        List<UUID> idList = itemDtos.stream().map(ErpReturnItemDto::getId).collect(Collectors.toList());
        List<ErpReturnItemEntity> entities = erpReturnItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            // 반품거절 데이터 제한
            if(entity.getReturnRejectYn().equals("y")) {
                throw new CustomInvalidDataException("반품거절 데이터는 상태를 변경할 수 없습니다.");
            }
            // 불량상품 및 재고반영 데이터 제한
            if(entity.getDefectiveYn().equals("y") || entity.getStockReflectYn().equals("y")) {
                throw new CustomInvalidDataException("불량상품 및 재고반영된 데이터는 상태를 변경할 수 없습니다.");
            }

            if (entity.getId().equals(dto.getId())) {

                if (dto.getReturnCompleteYn().equals("n")) {
                    entity.setReturnCompleteYn("n");
                    entity.setReturnCompleteAt(null);
                    
                    // TODO :: 수거중, 수거완료, 처리완료는 각 상태별로 존재하는 데이터가 중복되지 않는다. 따라서 수거완료처리만 하면됨.
                    // entity.setReleaseYn("n");
                    // entity.setReleaseAt(null);
                    return;
                }

                entity.setReturnCompleteYn("y");
                entity.setReturnCompleteAt(CustomDateUtils.getCurrentDateTime());
            }
        }));

        erpReturnItemService.saveListAndModify(entities);
    }

    @Transactional
    public void changeForReturnRejectYn(ErpReturnItemDto itemDto) {
        ErpReturnItemEntity entity = erpReturnItemService.searchOne(itemDto.getId());
        
        if(itemDto.getReturnRejectYn().equals("n")) {
            entity.setReturnRejectYn("n").setReturnRejectAt(null);
        }else {
            entity.setReturnRejectYn("y").setReturnRejectAt(CustomDateUtils.getCurrentDateTime());
        }
        erpReturnItemService.saveAndModify(entity);
    }

    /**
     * Erp Order Item이 제거될 때, Erp Return Item도 함께 제거되어야 한다.
     */
    @Transactional
    public void deleteBatch(List<ErpReturnItemDto> itemDtos) {
        List<UUID> itemIds = itemDtos.stream().map(ErpReturnItemDto::getId).collect(Collectors.toList());
        erpReturnItemService.deleteBatch(itemIds);

        // erpReturnItem에 대응되는 product receive 데이터 batchDelete
        List<UUID> orderItemIds = itemDtos.stream().map(ErpReturnItemDto::getErpOrderItemId).collect(Collectors.toList());
        productReceiveService.deleteByErpOrderItemIds(orderItemIds);

        // erpOrderItem의 returnYn을 n으로 변경한다.
        List<UUID> idList = itemDtos.stream().map(ErpReturnItemDto::getErpOrderItemId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);

        entities.forEach(entity -> {
            entity.setReturnYn("n");
        });

        erpOrderItemService.saveListAndModify(entities);
    }

    @Transactional
    public void changeForReturnReason(ErpReturnItemDto itemDto) {
        ErpReturnItemEntity entity = erpReturnItemService.searchOne(itemDto.getId());

        entity.setReturnReasonType(itemDto.getReturnReasonType())
            .setReturnReasonDetail(itemDto.getReturnReasonDetail());

        erpReturnItemService.saveAndModify(entity);
    }

    @Transactional
    public void actionReflectDefective(ErpReturnItemDto itemDto, Map<String, Object> params) {
        ErpReturnItemEntity returnItemEntity = erpReturnItemService.searchOne(itemDto.getId());

        if(returnItemEntity.getStockReflectYn().equals("y")) {
            throw new CustomInvalidDataException("재고반영된 데이터는 불량상품으로 등록할 수 없습니다.");
        }

        returnItemEntity.setDefectiveYn("y");

        String memo = params.get("memo") == null ? "" : params.get("memo").toString();
        List<ProductReleaseEntity> releaseEntities = productReleaseService.findByErpOrderItemIds(Arrays.asList(itemDto.getErpOrderItemId()));
        releaseEntities.forEach(entity -> entity.setMemo(memo));
    }

    @Transactional
    public void actionCancelDefective(ErpReturnItemDto itemDto, Map<String, Object> params) {
        ErpReturnItemEntity returnItemEntity = erpReturnItemService.searchOne(itemDto.getId());
        returnItemEntity.setDefectiveYn("n");

        String memo = params.get("memo") == null ? "" : params.get("memo").toString();
        List<ProductReleaseEntity> releaseEntities = productReleaseService.findByErpOrderItemIds(Arrays.asList(itemDto.getErpOrderItemId()));
        releaseEntities.forEach(entity -> entity.setMemo(memo));
    }

    // 단일 재고 반영 (입고 등록)
    @Transactional
    public void actionReflectStock(ErpReturnItemDto returnItem, Map<String, Object> params) {
        ErpReturnItemEntity erpReturnItemEntity = erpReturnItemService.searchOne(returnItem.getId());
        ErpOrderItemEntity erpOrderItemEntity = erpOrderItemService.searchOne(erpReturnItemEntity.getErpOrderItemId());
        List<ProductReceiveEntity> receiveEntities = new ArrayList<>();

        if(erpOrderItemEntity.getReleaseOptionCode().isEmpty()) {
            return;    
        }
        if(erpReturnItemEntity.getDefectiveYn().equals("y")) {
            throw new CustomInvalidDataException("불량상품으로 등록된 데이터는 재고반영할 수 없습니다.");
        }

        ProductOptionEntity optionEntity = productOptionService.findOneByCode(erpOrderItemEntity.getReleaseOptionCode());
        
        if(optionEntity == null) {
            return;
        }

        if(optionEntity.getPackageYn().equals("n")) {
            receiveEntities.add(this.reflectStockUnit(erpReturnItemEntity, optionEntity, params));
        }else if(optionEntity.getPackageYn().equals("y")) {
            receiveEntities.addAll(this.reflectStockUnitOfPackageOption(erpReturnItemEntity, optionEntity, params));
        }

        productReceiveService.bulkInsert(receiveEntities);
    }

    public ProductReceiveEntity reflectStockUnit(ErpReturnItemEntity returnItem, ProductOptionEntity option, Map<String, Object> params) {
        UUID USER_ID = userService.getUserId();
        
        String memo = params.get("memo") == null ? "" : params.get("memo").toString();
        Integer unit = params.get("unit") == null ? 0 : Integer.parseInt(params.get("unit").toString());
        ProductReceiveEntity receiveEntity = ProductReceiveEntity.builder()
            .id(UUID.randomUUID())
            .receiveUnit(unit)
            .memo(memo)
            .createdAt(LocalDateTime.now())
            .createdBy(USER_ID)
            .productOptionCid(option.getCid())
            .productOptionId(option.getId())
            .erpOrderItemId(returnItem.getErpOrderItemId())
            .build();

        returnItem.setStockReflectYn("y");
        return receiveEntity;
    }

    // public List<ProductReceiveEntity> reflectStockUnitOfPackageOption(ErpReturnItemEntity returnItem, ProductOptionEntity optionEntity, Map<String, Object> params) {
    //     UUID USER_ID = userService.getUserId();

    //     String memo = params.get("memo") == null ? "" : params.get("memo").toString();
    //     Integer unit = params.get("unit") == null ? 0 : Integer.parseInt(params.get("unit").toString());
        
    //     List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionId(optionEntity.getId());
    //     List<ProductReceiveEntity> receiveEntities = new ArrayList<>();

    //     optionPackageEntities.forEach(option -> {
    //         ProductReceiveEntity receiveEntity = ProductReceiveEntity.builder()
    //             .id(UUID.randomUUID())
    //             .receiveUnit(unit * option.getPackageUnit())
    //             .memo(memo)
    //             .createdAt(LocalDateTime.now())
    //             .createdBy(USER_ID)
    //             // .productOptionCid(option.getOriginOptionCid())
    //             .productOptionId(option.getOriginOptionId())
    //             .erpOrderItemId(returnItem.getErpOrderItemId())
    //             .build();

    //         receiveEntities.add(receiveEntity);
    //     });

    //     returnItem.setStockReflectYn("y");
    //     return receiveEntities;
    // }

    public List<ProductReceiveEntity> reflectStockUnitOfPackageOption(ErpReturnItemEntity returnItem, ProductOptionEntity optionEntity, Map<String, Object> params) {
        UUID USER_ID = userService.getUserId();

        String memo = params.get("memo") == null ? "" : params.get("memo").toString();
        Integer unit = params.get("unit") == null ? 0 : Integer.parseInt(params.get("unit").toString());
        
        List<OptionPackageProjection.RelatedProductAndOption> optionPackageEntities = optionPackageService.searchBatchByParentOptionId(optionEntity.getId());
        List<ProductReceiveEntity> receiveEntities = new ArrayList<>();

        optionPackageEntities.forEach(option -> {
            ProductReceiveEntity receiveEntity = ProductReceiveEntity.builder()
                .id(UUID.randomUUID())
                .receiveUnit(unit * option.getOptionPackage().getPackageUnit())
                .memo(memo)
                .createdAt(LocalDateTime.now())
                .createdBy(USER_ID)
                .productOptionCid(option.getProductOption().getCid())
                .productOptionId(option.getProductOption().getId())
                .erpOrderItemId(returnItem.getErpOrderItemId())
                .build();

            receiveEntities.add(receiveEntity);
        });

        returnItem.setStockReflectYn("y");
        return receiveEntities;
    }

    @Transactional
    public void actionCancelStock(ErpReturnItemDto itemDto) {
        if(itemDto.getStockReflectYn().equals("n")) {
            return;
        }

        ErpReturnItemEntity erpReturnItemEntity = erpReturnItemService.searchOne(itemDto.getId());
        erpReturnItemEntity.setStockReflectYn("n");
    

        productReceiveService.deleteByErpOrderItemIds(Arrays.asList(erpReturnItemEntity.getErpOrderItemId()));
    }

    @Transactional
    public void changeForDeliveryChargeReturnYn(ErpReturnItemDto itemDto) {
        ErpReturnItemEntity entity = erpReturnItemService.searchOne(itemDto.getId());
        
        entity.setDeliveryChargeReturnYn(itemDto.getDeliveryChargeReturnYn());
        erpReturnItemService.saveAndModify(entity);
    }

    @Transactional
    public void changeForDeliveryChargeReturnTypeAndReturnYn(ErpReturnItemDto itemDto) {
        ErpReturnItemEntity entity = erpReturnItemService.searchOne(itemDto.getId());
        
        entity.setDeliveryChargeReturnType(itemDto.getDeliveryChargeReturnType()).setDeliveryChargeReturnYn(itemDto.getDeliveryChargeReturnYn());
        erpReturnItemService.saveAndModify(entity);
    }
}
