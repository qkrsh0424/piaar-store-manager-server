package com.piaar_store_manager.server.service.product_release;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseJoinResDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.service.option_package.OptionPackageService;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.user.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseBusinessService {
    private final ProductReleaseService productReleaseService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productReleaseCid : Integer
     * @return ProductReleaseGetDto
     * @see ProductReleaseGetDto#toDto
     */
    public ProductReleaseGetDto searchOne(Integer productReleaseCid) {
        ProductReleaseEntity entity = productReleaseService.searchOne(productReleaseCid);
        ProductReleaseGetDto dto = ProductReleaseGetDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다. 해당 ProductRelease와 연관관계에 놓여있는 Many To
     * One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productReleaseCid : Integer
     * @return ProductReleaseJoinResDto
     * @see ProductReleaseService#searchOneM2OJ
     * @see ProductReleaseJoinResDto#toDto
     */
    public ProductReleaseJoinResDto searchOneM2OJ(Integer productReleaseCid) {
        ProductReleaseProj releaseProj = productReleaseService.searchOneM2OJ(productReleaseCid);
        ProductReleaseJoinResDto resDto = ProductReleaseJoinResDto.toDto(releaseProj);
        return resDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease 데이터를 모두 조회한다.
     * 
     * @return List::ProductReleaseGetDto::
     * @see ProductReleaseService#searchList
     * @see ProductReleaseGetDto#toDto
     */
    public List<ProductReleaseGetDto> searchList() {
        List<ProductReleaseEntity> entities = productReleaseService.searchList();
        List<ProductReleaseGetDto> dtos = entities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 ProductRelease 데이터를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReleaseGetDto
     * @see ProductReleaseService#searchListByOptionCid
     * @see ProductReleaseGetDto#toDto
     */
    public List<ProductReleaseGetDto> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReleaseEntity> entities = productReleaseService.searchListByOptionCid(productOptionCid);
        List<ProductReleaseGetDto> dtos = entities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease 데이터를 모두 조회한다. 해당 ProductRelease와 연관관계에 놓여있는 Many To One
     * JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductReleaseJoinResDto::
     * @see ProductReleaseService#searchListM2OJ
     * @see ProductReleaseJoinResDto#toDto
     */
    public List<ProductReleaseJoinResDto> searchListM2OJ() {
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ();
        List<ProductReleaseJoinResDto> resDtos = releaseProjs.stream().map(proj -> ProductReleaseJoinResDto.toDto(proj)).collect(Collectors.toList());
        return resDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 한개 등록한다. 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @see ProductReleaseService#createPL
     * @see ProductReleaseGetDto#toDto
     * @see ProductOptionService#updateReleaseProductUnit
     */
    @Transactional
    public void createPL(ProductReleaseGetDto productReleaseGetDto) {
        UUID USER_ID = userService.getUserId();
        productReleaseGetDto.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);

        // ProductRelease 데이터 생성
        productReleaseService.createPL(ProductReleaseEntity.toEntity(productReleaseGetDto));
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 여러개 등록한다. 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @see ProductReleaseService#createPLList
     * @see ProductOptionService#updateReleaseProductUnit
     * @see ProductReleaseGetDto#toDto
     */
//    @Transactional
//    public void createPLList(List<ProductReleaseGetDto> productReleaseGetDtos) {
//        UUID USER_ID = userService.getUserId();
//        List<ProductReleaseEntity> convertedEntities = productReleaseGetDtos.stream().map(r -> {
//            r.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);
//            return ProductReleaseEntity.toEntity(r);
//        }).collect(Collectors.toList());
//
//        // ProductRelease 데이터 생성
//        productReleaseService.createPLList(convertedEntities);
//    }

    @Transactional
    public void createPLList(List<ProductReleaseGetDto> productReleaseGetDtos) {
        // 1. release로 넘어온 productOptionCid로 option데이터를 찾는다.
        // 2. option 데이터의 packageYn이 n인 것과 y인 것을 분리
        // 3. 먼저 n인 애들 처리
        // 4. y인 애들 처리하기
        List<Integer> optionCids = productReleaseGetDtos.stream().map(r -> r.getProductOptionCid()).collect(Collectors.toList());
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByOptionCids(optionCids);

        // 1. 세트상품 X
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. 세트상품 O
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        this.createOfOriginOption(productReleaseGetDtos, originOptionEntities);

        if(parentOptionEntities.size() > 0) {
            this.createOfPackageOption(productReleaseGetDtos, parentOptionEntities);
        }

//        UUID USER_ID = userService.getUserId();
//        List<ProductReleaseEntity> convertedEntities = productReleaseGetDtos.stream().map(r -> {
//            r.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);
//            return ProductReleaseEntity.toEntity(r);
//        }).collect(Collectors.toList());
//
//        // ProductRelease 데이터 생성
//        productReleaseService.createPLList(convertedEntities);
    }

    public void createOfOriginOption(List<ProductReleaseGetDto> productReleaseGetDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();

        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();

        productReleaseGetDtos.forEach(dto -> {
            originOptionEntities.forEach(option -> {
                if(dto.getProductOptionCid().equals(option.getCid())){
                    ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                            .id(UUID.randomUUID())
                            .releaseUnit(dto.getReleaseUnit())
                            .memo(dto.getMemo())
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .productOptionCid(option.getCid())
                            .build();

                    productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
                }
            });
        });

        productReleaseService.createPLList(productReleaseEntities);
    }

    public void createOfPackageOption(List<ProductReleaseGetDto> productReleaseGetDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();

        productReleaseGetDtos.forEach(dto -> {
            parentOptionEntities.forEach(parentOption -> {
                if(dto.getProductOptionCid().equals(parentOption.getCid())) {
                    optionPackageEntities.forEach(option -> {
                        if(option.getParentOptionId().equals(parentOption.getId())) {
                            ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                                    .id(UUID.randomUUID())
                                    .releaseUnit(option.getPackageUnit() * dto.getReleaseUnit())
                                    .memo(dto.getMemo())
                                    .createdAt(CustomDateUtils.getCurrentDateTime())
                                    .createdBy(USER_ID)
                                    .productOptionCid(option.getOriginOptionCid())
                                    .build();

                            productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
                        }
                    });
                }
            });
        });
        productReleaseService.createPLList(productReleaseEntities);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productReleaseCid : Integer
     * @param userId            : UUID
     * @see ProductReleaseService#destroyOne
     */
    public void destroyOne(Integer productReleaseCid) {
        productReleaseService.destroyOne(productReleaseCid);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 ProductRelease cid 값과 상응되는 데이터를 업데이트한다. ProductOption 의 재고수량을 변경한다.
     * 
     * @param releaseDto : ProductReleaseGetDto
     * @param userId     : UUID
     * @see ProductReleaseService#searchOne
     * @see ProductReleaseService#createPL
     * @see ProductOptionService#updateReleaseProductUnit
     */
    @Transactional
    public void changeOne(ProductReleaseGetDto releaseDto) {
        // 출고 데이터 조회
        ProductReleaseEntity entity = productReleaseService.searchOne(releaseDto.getCid());

        // 변경된 출고수량
        int changedReleaseUnit = releaseDto.getReleaseUnit() - entity.getReleaseUnit();
        // 변경된 출고 데이터
        entity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        productReleaseService.createPL(entity);

        // 상품옵션의 재고수량 반영
        productOptionService.updateReleaseProductUnit(entity.getProductOptionCid(), changedReleaseUnit);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 다중 ProductRelease cid 값과 상응되는 데이터를 업데이트한다. ProductOption 의 재고수량을 변경한다.
     * 
     * @param releaseDtos : List::roductReleaseGetDto::
     * @param userId      : UUID
     * @see ProductReleaseBusinessService#changeOne
     */
    @Transactional
    public void changeList(List<ProductReleaseGetDto> releaseDtos) {
        releaseDtos.stream().forEach(r -> this.changeOne(r));
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param releaseDto : ProductReleaseGetDto
     * @param userId     : UUID
     * @see ProductReleaseService#searchOne
     * @see ProductOptionService#releaseProductUnit
     * @see ProductReleaseService#createPL
     * @see ProductOptionService#updateReleaseProductUnit
     */
    public void patchOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity releaseEntity = productReleaseService.searchOne(releaseDto.getCid());

        if (releaseDto.getReleaseUnit() != null) {
            int storedReleaseUnit = releaseEntity.getReleaseUnit();

            // 변경된 출고 데이터
            releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
            productOptionService.updateReleaseProductUnit(releaseEntity.getProductOptionCid(), releaseEntity.getReleaseUnit() - storedReleaseUnit);
        }
        if (releaseDto.getMemo() != null) {
            releaseEntity.setMemo(releaseDto.getMemo());
        }
        productReleaseService.createPL(releaseEntity);
    }
}
