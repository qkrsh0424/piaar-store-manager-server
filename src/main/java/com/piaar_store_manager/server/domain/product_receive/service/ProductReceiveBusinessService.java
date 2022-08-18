package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.create.CreateContext;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.delete.DeleteContext;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.search.SearchContext;
import com.piaar_store_manager.server.domain.product_receive.service.strategy.update.UpdateContext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReceiveBusinessService {
    // private final ProductReceiveService productReceiveService;
    // private final ProductOptionService productOptionService;
    // private final OptionPackageService optionPackageService;
    // private final UserService userService;
    private final SearchContext productReceiveSearchContext;
    private final CreateContext productReceiveCreateContext;
    private final DeleteContext productReceiveDeleteContext;
    private final UpdateContext productReceiveUpdateContext;

    // deprecated
    // public ProductReceiveGetDto searchOne(Integer productReceiveCid) {
    //     ProductReceiveEntity entity = productReceiveService.searchOne(productReceiveCid);
    //     ProductReceiveGetDto dto = ProductReceiveGetDto.toDto(entity);
    //     return dto;
    // }

    // deprecated
    // public List<ProductReceiveGetDto> searchList() {
    //     List<ProductReceiveEntity> entities = productReceiveService.searchList();
    //     List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
    //     return dtos;
    // }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productReceiveCid 대응하는 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, otion, category, user를 함께 조회한다.
     *
     * @param productReceiveCid : Integer
     * @see ProductReceiveService#searchOneM2OJ
     */
    // deprecated
    // public ProductReceiveGetDto.ManyToOneJoin searchOneM2OJ(Integer productReceiveCid){
    //     ProductReceiveProj receiveProj = productReceiveService.searchOneM2OJ(productReceiveCid);
    //     ProductReceiveGetDto.ManyToOneJoin resDto = ProductReceiveGetDto.ManyToOneJoin.toDto(receiveProj);
    //     return resDto;
    // }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 receive 조회, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, otion, category, user를 함께 조회한다.
     *
     * @see ProductReceiveService#searchListM2OJ
     */
    // deprecated
    // public List<ProductReceiveGetDto.ManyToOneJoin> searchListM2OJ() {
    //     List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ();
    //     List<ProductReceiveGetDto.ManyToOneJoin> resDtos = receiveProjs.stream().map(proj -> ProductReceiveGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
    //     return resDtos;
    // }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid에 대응하는 receive를 조회한다.
     *
     * @param productOptionCid : Integer
     * @see ProductReceiveService#searchListByOptionCid
     */
    // deprecated
    // public List<ProductReceiveGetDto> searchListByOptionCid(Integer productOptionCid) {
    //     List<ProductReceiveEntity> entities = productReceiveService.searchListByOptionCid(productOptionCid);
    //     List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
    //     return dtos;
    // }
    
    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 receive등록.
     * receive로 넘어온 productOptionCid로 option 데이터를 조회한다.
     * 1) - option의 packageYn이 n인 상품은 receive 데이터를 바로 생성하고,
     * 2) - option의 packageYn이 y인 상품은 package를 구성하는 option을 찾아 receive 데이터 생성.
     *
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @see ProductReceiveService#saveAndModify
     * @see ProductReceiveService#saveListAndModify
     * @see OptionPackageService#searchListByParentOptionId
     */
    // deprecated
    // @Transactional
    // public void createOne(ProductReceiveGetDto productReceiveGetDto) {
    //     UUID USER_ID = userService.getUserId();
    //     productReceiveGetDto.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);

    //     ProductOptionEntity optionEntity = productOptionService.searchOne(productReceiveGetDto.getProductOptionCid());

    //     if(optionEntity.getPackageYn().equals("n")) {
    //         // 1) 실행
    //         ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
    //                 .id(UUID.randomUUID())
    //                 .receiveUnit(productReceiveGetDto.getReceiveUnit())
    //                 .memo(productReceiveGetDto.getMemo())
    //                 .createdAt(CustomDateUtils.getCurrentDateTime())
    //                 .createdBy(USER_ID)
    //                 .productOptionCid(productReceiveGetDto.getCid())
    //                 .build();
    //         productReceiveService.saveAndModify(ProductReceiveEntity.toEntity(receiveGetDto));
    //     } else {
    //         // 2) 실행
    //         List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionId(optionEntity.getId());
            
    //         List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
    //         optionPackageEntities.forEach(option -> {
    //             ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
    //                     .id(UUID.randomUUID())
    //                     .receiveUnit(option.getPackageUnit() * productReceiveGetDto.getReceiveUnit())
    //                     .memo(productReceiveGetDto.getMemo())
    //                     .createdAt(CustomDateUtils.getCurrentDateTime())
    //                     .createdBy(USER_ID)
    //                     .productOptionCid(option.getOriginOptionCid())
    //                     .build();

    //             productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
    //         });
    //         productReceiveService.saveListAndModify(productReceiveEntities);
    //     }
    // }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 다중 receive등록.
     * receive로 넘어온 productOptionCid로 option 데이터를 조회한다.
     * 1) - option의 packageYn이 n인 상품은 receive 데이터를 바로 생성,
     * 2) - option의 packageYn이 y인 상품은 package를 구성하는 option을 찾아 receive 데이터 생성.
     *
     * @param productReceiveGetDtos : List::ProductReceiveGetDto::
     * @see ProductReceiveService#saveAndModify
     */
    // deprecated
    // @Transactional
    // public void createList(List<ProductReceiveGetDto> productReceiveGetDtos) {
    //     UUID USER_ID = userService.getUserId();
    //     List<Integer> optionCids = productReceiveGetDtos.stream().map(r -> r.getProductOptionCid()).collect(Collectors.toList());
    //     List<ProductOptionEntity> optionEntities = productOptionService.searchListByCids(optionCids);
    //     // 패키지 옵션 분류
    //     List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
    //     List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

    //     // 1) 실행
    //     List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
    //     productReceiveGetDtos.forEach(dto -> {
    //         originOptionEntities.forEach(option -> {
    //             if(dto.getProductOptionCid().equals(option.getCid())){
    //                 ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
    //                     .id(UUID.randomUUID())
    //                     .receiveUnit(dto.getReceiveUnit())
    //                     .memo(dto.getMemo())
    //                     .createdAt(CustomDateUtils.getCurrentDateTime())
    //                     .createdBy(USER_ID)
    //                     .productOptionCid(option.getCid())
    //                     .build();

    //                 productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
    //             }
    //         });
    //     });

    //     productReceiveService.saveListAndModify(productReceiveEntities);
    //     productReceiveEntities.clear();

    //     // 2) 실행
    //     if (parentOptionEntities.size() > 0) {
    //         List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
    //         List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

    //         productReceiveGetDtos.forEach(dto -> {
    //             parentOptionEntities.forEach(parentOption -> {
    //                 if (dto.getProductOptionCid().equals(parentOption.getCid())) {
    //                     optionPackageEntities.forEach(option -> {
    //                         if (option.getParentOptionId().equals(parentOption.getId())) {
    //                             ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
    //                                     .id(UUID.randomUUID())
    //                                     .receiveUnit(option.getPackageUnit() * dto.getReceiveUnit())
    //                                     .memo(dto.getMemo())
    //                                     .createdAt(CustomDateUtils.getCurrentDateTime())
    //                                     .createdBy(USER_ID)
    //                                     .productOptionCid(option.getOriginOptionCid())
    //                                     .build();

    //                             productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
    //                         }
    //                     });
    //                 }
    //             });
    //         });
    //         productReceiveService.saveListAndModify(productReceiveEntities);
    //     }
    // }

    // deprecated
    // public void destroyOne(Integer productReceiveCid) {
    //     productReceiveService.destroyOne(productReceiveCid);
    // }

    // deprecated
    // @Transactional
    // public void changeOne(ProductReceiveGetDto receiveDto) {
    //     ProductReceiveEntity entity = productReceiveService.searchOne(receiveDto.getCid());
    //     entity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
    // }

    // deprecated
    // @Transactional
    // public void changeList(List<ProductReceiveGetDto> receiveDtos) {
    //     receiveDtos.stream().forEach(r -> this.changeOne(r));
    // }

    // deprecated
    // @Transactional
    // public void patchOne(ProductReceiveGetDto receiveDto) {
    //     ProductReceiveEntity receiveEntity = productReceiveService.searchOne(receiveDto.getCid());

    //     if (receiveDto.getReceiveUnit() != null) {
    //         receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
    //     }
    //     if (receiveDto.getMemo() != null) {
    //         receiveEntity.setMemo(receiveDto.getMemo());
    //     }
    // }

    public <T> T searchOne(UUID productReceiveId, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveSearchContext.setSearchStrategy(objectType);

        T dto = productReceiveSearchContext.searchOne(productReceiveId);
        return dto;
    }

    public <T> List<T> searchList(Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveSearchContext.setSearchStrategy(objectType);
        
        List<T> dto = productReceiveSearchContext.searchList();
        return dto;
    }

    public <T> List<T> searchListByOptionCid(Integer productOptionCid, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveSearchContext.setSearchStrategy(objectType);

        List<T> dtos = productReceiveSearchContext.searchListByOptionCid(productOptionCid);
        return dtos;
    }

    public void createOne(Map<String, Object> params, ProductReceiveGetDto productReceiveDto) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveCreateContext.setCreateStrategy(objectType);

        productReceiveCreateContext.createOne(productReceiveDto);
    }

    public void createList(Map<String, Object> params, List<ProductReceiveGetDto> productReceiveGetDtos) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveCreateContext.setCreateStrategy(objectType);
        
        productReceiveCreateContext.createList(productReceiveGetDtos);
    }

    public void destroyOne(UUID productReceiveId, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveDeleteContext.setDeleteStrategy(objectType);

        productReceiveDeleteContext.destroyOne(productReceiveId);
    }

    public void changeOne(Map<String, Object> params, ProductReceiveGetDto receiveDto) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveUpdateContext.setUpdateStrategy(objectType);

        productReceiveUpdateContext.changeOne(receiveDto);
    }

    public void changeList(Map<String, Object> params, List<ProductReceiveGetDto> receiveDtos) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveUpdateContext.setUpdateStrategy(objectType);

        productReceiveUpdateContext.changeList(receiveDtos);
    }

    public void patchOne(Map<String, Object> params, ProductReceiveGetDto receiveDto) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        productReceiveUpdateContext.setUpdateStrategy(objectType);

        productReceiveUpdateContext.patchOne(receiveDto);
    }
}
