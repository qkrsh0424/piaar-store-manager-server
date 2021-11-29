package com.piaar_store_manager.server.service.product_release;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseJoinResDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReleaseBusinessService {
    private final ProductReleaseService productReleaseService;
    private final ProductOptionService productOptionService;

    @Autowired
    public ProductReleaseBusinessService(
        ProductReleaseService productReleaseService,
        ProductOptionService productOptionService
    ) {
        this.productReleaseService = productReleaseService;
        this.productOptionService = productOptionService;
    }

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
     * @see ProductReleaseGetDto#toDtos
     */
    public List<ProductReleaseGetDto> searchList() {
        List<ProductReleaseEntity> entities = productReleaseService.searchList();
        List<ProductReleaseGetDto> dtos = ProductReleaseGetDto.toDtos(entities);
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
     * @see ProductReleaseGetDto#toDtos
     */
    public List<ProductReleaseGetDto> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReleaseEntity> entities = productReleaseService.searchListByOptionCid(productOptionCid);
        List<ProductReleaseGetDto> dtos = ProductReleaseGetDto.toDtos(entities);
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
     * @see ProductReleaseJoinResDto#toDtos
     */
    public List<ProductReleaseJoinResDto> searchListM2OJ() {
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ();
        List<ProductReleaseJoinResDto> resDtos = ProductReleaseJoinResDto.toDtos(releaseProjs);
        return resDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 한개 등록한다. 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @param userId               : UUID
     * @see ProductReleaseService#createPL
     * @see ProductReleaseGetDto#toDto
     * @see ProductOptionService#updateReleaseProductUnit
     */
    public ProductReleaseGetDto createPL(ProductReleaseGetDto productReleaseGetDto, UUID userId) {
        productReleaseGetDto.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(userId);

        // ProductRelease 데이터 생성
        ProductReleaseEntity entity = productReleaseService.createPL(ProductReleaseEntity.toEntity(productReleaseGetDto));
        ProductReleaseGetDto dto = ProductReleaseGetDto.toDto(entity);
        
        // ProductOption 재고 반영
        productOptionService.updateReleaseProductUnit(entity.getProductOptionCid(), userId, entity.getReleaseUnit());
        
        return dto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 여러개 등록한다. 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDto : List::ProductReleaseGetDto::
     * @param userId               : UUID
     * @see ProductReleaseService#createPLList
     * @see ProductOptionService#updateReleaseProductUnit
     * @see ProductReleaseGetDto#toDtos
     */
    public List<ProductReleaseGetDto> createPLList(List<ProductReleaseGetDto> productReleaseGetDtos, UUID userId) {
        List<ProductReleaseEntity> convertedEntities = productReleaseGetDtos.stream().map(r -> {
            r.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(userId);
            return ProductReleaseEntity.toEntity(r);
        }).collect(Collectors.toList());

        // ProductRelease 데이터 생성
        List<ProductReleaseEntity> entities = productReleaseService.createPLList(convertedEntities);
        // ProductOption 재고 반영
        entities.forEach(r -> { productOptionService.updateReleaseProductUnit(r.getProductOptionCid(), userId, r.getReleaseUnit()); });

        List<ProductReleaseGetDto> dtos = ProductReleaseGetDto.toDtos(entities);
        return dtos;
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
    public void destroyOne(Integer productReleaseCid, UUID userId) {
        productReleaseService.destroyOne(productReleaseCid, userId);
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
    public void changeOne(ProductReleaseGetDto releaseDto, UUID userId) {
        // 출고 데이터 조회
        ProductReleaseEntity entity = productReleaseService.searchOne(releaseDto.getCid());

        // 변경된 출고수량
        int changedReleaseUnit = releaseDto.getReleaseUnit() - entity.getReleaseUnit();
        // 변경된 출고 데이터
        entity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        productReleaseService.createPL(entity);

        // 상품옵션의 재고수량 반영
        productOptionService.updateReleaseProductUnit(entity.getProductOptionCid(), userId, changedReleaseUnit);
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
    public void changeList(List<ProductReleaseGetDto> releaseDtos, UUID userId) {
        releaseDtos.stream().forEach(r -> this.changeOne(r, userId));
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
    public void patchOne(ProductReleaseGetDto releaseDto, UUID userId) {
        ProductReleaseEntity releaseEntity = productReleaseService.searchOne(releaseDto.getCid());

        if (releaseDto.getReleaseUnit() != null) {
            int storedReleaseUnit = releaseEntity.getReleaseUnit();

            // 변경된 출고 데이터
            releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
            productOptionService.updateReleaseProductUnit(releaseEntity.getProductOptionCid(), userId, releaseEntity.getReleaseUnit() - storedReleaseUnit);
        }
        if (releaseDto.getMemo() != null) {
            releaseEntity.setMemo(releaseDto.getMemo());
        }
        productReleaseService.createPL(releaseEntity);
    }
}
