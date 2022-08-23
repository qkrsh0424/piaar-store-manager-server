package com.piaar_store_manager.server.domain.product_release.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseBusinessService {
    private final ProductReleaseService productReleaseService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    public ProductReleaseGetDto searchOne(Integer productReleaseCid) {
        ProductReleaseEntity entity = productReleaseService.searchOne(productReleaseCid);
        ProductReleaseGetDto dto = ProductReleaseGetDto.toDto(entity);
        return dto;
    }

    public List<ProductReleaseGetDto> searchList() {
        List<ProductReleaseEntity> entities = productReleaseService.searchList();
        List<ProductReleaseGetDto> dtos = entities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productReleaseCid 대응하는 release, release와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, otion, category, user를 함께 조회한다.
     *
     * @param productReleaseCid : Integer
     * @see ProductReleaseService#searchOneM2OJ
     */
    public ProductReleaseGetDto.ManyToOneJoin searchOneM2OJ(Integer productReleaseCid) {
        ProductReleaseProj releaseProj = productReleaseService.searchOneM2OJ(productReleaseCid);
        ProductReleaseGetDto.ManyToOneJoin resDto = ProductReleaseGetDto.ManyToOneJoin.toDto(releaseProj);
        return resDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 release 조회, release와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, otion, category, user를 함께 조회한다.
     *
     * @see ProductReleaseService#searchListM2OJ
     */
    public List<ProductReleaseGetDto.ManyToOneJoin> searchListM2OJ() {
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ();
        List<ProductReleaseGetDto.ManyToOneJoin> resDtos = releaseProjs.stream().map(proj -> ProductReleaseGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return resDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid에 대응하는 release를 조회한다.
     *
     * @param productOptionCid : Integer
     * @see ProductReleaseService#searchListByOptionCid
     */
    public List<ProductReleaseGetDto> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReleaseEntity> entities = productReleaseService.searchListByOptionCid(productOptionCid);
        List<ProductReleaseGetDto> dtos = entities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 release등록.
     * release로 넘어온 productOptionCid로 option 데이터를 조회한다.
     * 1) - option의 packageYn이 n인 상품은 release 데이터를 바로 생성하고,
     * 2) - option의 packageYn이 y인 상품은 package를 구성하는 option을 찾아 release 데이터 생성.
     *
     * FIX => 세트상품 여부와 관계없이 현재 선택된 옵션에 대한 출고 데이터 추가.
     * 
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @see ProductReleaseService#saveAndModify
     * @see ProductReleaseService#saveListAndModify
     * @see OptionPackageService#searchListByParentOptionId
     */
    @Transactional
    public void createOne(ProductReleaseGetDto productReleaseDto) {
        UUID USER_ID = userService.getUserId();

        ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                .id(UUID.randomUUID())
                .releaseUnit(productReleaseDto.getReleaseUnit())
                .memo(productReleaseDto.getMemo())
                .createdAt(CustomDateUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productOptionCid(productReleaseDto.getProductOptionCid())
                .build();

        productReleaseService.saveAndModify(ProductReleaseEntity.toEntity(releaseGetDto));
    }
    // @Transactional
    // public void createOne(ProductReleaseGetDto productReleaseGetDto) {
    //     UUID USER_ID = userService.getUserId();
    //     productReleaseGetDto.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);

    //     ProductOptionEntity optionEntity = productOptionService.searchOne(productReleaseGetDto.getProductOptionCid());
        
    //     if(optionEntity.getPackageYn().equals("n")) {
    //         // 1) 실행
    //         ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
    //                 .id(UUID.randomUUID())
    //                 .releaseUnit(productReleaseGetDto.getReleaseUnit())
    //                 .memo(productReleaseGetDto.getMemo())
    //                 .createdAt(CustomDateUtils.getCurrentDateTime())
    //                 .createdBy(USER_ID)
    //                 .productOptionCid(productReleaseGetDto.getCid())
    //                 .build();
    //          productReleaseService.saveAndModify(ProductReleaseEntity.toEntity(releaseGetDto));
    //     } else {
    //         // 2) 실행
    //         List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionId(optionEntity.getId());
            
    //         List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
    //         optionPackageEntities.forEach(option -> {
    //             ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
    //                     .id(UUID.randomUUID())
    //                     .releaseUnit(option.getPackageUnit() * productReleaseGetDto.getReleaseUnit())
    //                     .memo(productReleaseGetDto.getMemo())
    //                     .createdAt(CustomDateUtils.getCurrentDateTime())
    //                     .createdBy(USER_ID)
    //                     .productOptionCid(option.getOriginOptionCid())
    //                     .build();

    //             productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
    //         });
    //         productReleaseService.saveListAndModify(productReleaseEntities);
    //     }
    // }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 다중 release등록.
     * release로 넘어온 productOptionCid로 option 데이터를 조회한다.
     * 1) - option의 packageYn이 n인 상품은 release 데이터를 바로 생성,
     * 2) - option의 packageYn이 y인 상품은 package를 구성하는 option을 찾아 release 데이터 생성.
     *
     * FIX => 세트상품 여부와 관계없이 현재 선택된 옵션에 대한 출고 데이터 추가.
     * 
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @see ProductReleaseService#saveAndModify
     */
    @Transactional
    public void createList(List<ProductReleaseGetDto> productReleaseGetDtos) {
        UUID USER_ID = userService.getUserId();
        List<ProductReleaseEntity> productReleaseEntities = productReleaseGetDtos.stream().map(dto -> {
            ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                    .id(UUID.randomUUID())
                    .releaseUnit(dto.getReleaseUnit())
                    .memo(dto.getMemo())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .productOptionCid(dto.getProductOptionCid())
                    .build();

            return ProductReleaseEntity.toEntity(releaseGetDto);
        }).collect(Collectors.toList());

        productReleaseService.saveListAndModify(productReleaseEntities);
    }

    // @Transactional
    // public void createList(List<ProductReleaseGetDto> productReleaseGetDtos) {
    //     UUID USER_ID = userService.getUserId();
    //     List<Integer> optionCids = productReleaseGetDtos.stream().map(r -> r.getProductOptionCid()).collect(Collectors.toList());
    //     List<ProductOptionEntity> optionEntities = productOptionService.searchListByCids(optionCids);
    //     // 패키지 옵션 분류
    //     List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
    //     List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

    //     // 1) 실행
    //     List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
    //     productReleaseGetDtos.forEach(dto -> {
    //         originOptionEntities.forEach(option -> {
    //             if(dto.getProductOptionCid().equals(option.getCid())){
    //                 ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
    //                     .id(UUID.randomUUID())
    //                     .releaseUnit(dto.getReleaseUnit())
    //                     .memo(dto.getMemo())
    //                     .createdAt(CustomDateUtils.getCurrentDateTime())
    //                     .createdBy(USER_ID)
    //                     .productOptionCid(option.getCid())
    //                     .build();

    //                 productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
    //             }
    //         });
    //     });

    //     productReleaseService.saveListAndModify(productReleaseEntities);
    //     productReleaseEntities.clear();

    //     // 2) 실행
    //     if (parentOptionEntities.size() > 0) {
    //         List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
    //         List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

    //         productReleaseGetDtos.forEach(dto -> {
    //             parentOptionEntities.forEach(parentOption -> {
    //                 if (dto.getProductOptionCid().equals(parentOption.getCid())) {
    //                     optionPackageEntities.forEach(option -> {
    //                         if (option.getParentOptionId().equals(parentOption.getId())) {
    //                             ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
    //                                     .id(UUID.randomUUID())
    //                                     .releaseUnit(option.getPackageUnit() * dto.getReleaseUnit())
    //                                     .memo(dto.getMemo())
    //                                     .createdAt(CustomDateUtils.getCurrentDateTime())
    //                                     .createdBy(USER_ID)
    //                                     .productOptionCid(option.getOriginOptionCid())
    //                                     .build();

    //                             productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
    //                         }
    //                     });
    //                 }
    //             });
    //         });
    //         productReleaseService.saveListAndModify(productReleaseEntities);
    //     }
    // }

    public void destroyOne(Integer productReleaseCid) {
        productReleaseService.destroyOne(productReleaseCid);
    }

    @Transactional
    public void changeOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity entity = productReleaseService.searchOne(releaseDto.getCid());
        entity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
    }

    @Transactional
    public void changeList(List<ProductReleaseGetDto> releaseDtos) {
        releaseDtos.stream().forEach(r -> this.changeOne(r));
    }

    @Transactional
    public void patchOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity releaseEntity = productReleaseService.searchOne(releaseDto.getCid());

        if (releaseDto.getReleaseUnit() != null) {
            releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        }
        if (releaseDto.getMemo() != null) {
            releaseEntity.setMemo(releaseDto.getMemo());
        }
    }
}
