package com.piaar_store_manager.server.service.product_receive;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveJoinResDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReceiveBusinessService {
    private final ProductReceiveService productReceiveService;
    private final ProductOptionService productOptionService;

    @Autowired
    public ProductReceiveBusinessService(
        ProductReceiveService productReceiveService,
        ProductOptionService productOptionService
    ) {
        this.productReceiveService = productReceiveService;
        this.productOptionService = productOptionService;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productReceiveCid : Integer
     * @return ProductReceiveGetDto
     * @see ProductReceiveService#searchOne
     * @see ProductReceiveGetDto#toDto
     */
    public ProductReceiveGetDto searchOne(Integer productReceiveCid) {
        ProductReceiveEntity entity = productReceiveService.searchOne(productReceiveCid);
        ProductReceiveGetDto dto = ProductReceiveGetDto.toDto(entity);
        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productReceiveCid : Integer
     * @return ProductReceiveJoinResDto
     * @see ProductReceiveService#searchOneM2OJ
     * @see ProductReceiveJoinResDto#toDto
     */
    public ProductReceiveJoinResDto searchOneM2OJ(Integer productReceiveCid){
        ProductReceiveProj receiveProj = productReceiveService.searchOneM2OJ(productReceiveCid);
        ProductReceiveJoinResDto resDto = ProductReceiveJoinResDto.toDto(receiveProj);
        return resDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive 데이터를 모두 조회한다.
     * 
     * @return List::ProductReceiveGetDto::
     * @see ProductReceiveService#searchList
     * @see ProductReceiveGetDto#toDto
     */
    public List<ProductReceiveGetDto> searchList() {
        List<ProductReceiveEntity> entities = productReceiveService.searchList();
        List<ProductReceiveGetDto> dtos = entities.stream().map(r -> ProductReceiveGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 ProductReceive 데이터를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveGetDto
     * @see  ProductReceiveService#searchListByOptionCid
     * @see ProductReceiveGetDto#toDto
     */
    public List<ProductReceiveGetDto> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReceiveEntity> entities = productReceiveService.searchListByOptionCid(productOptionCid);
        List<ProductReceiveGetDto> dtos = entities.stream().map(r -> ProductReceiveGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive 데이터를 모두 조회한다.
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductReceiveJoinResDto::
     * @see ProductReceiveService#searchListM2OJ
     * @see ProductReceiveJoinResDto#toDto
     */
    public List<ProductReceiveJoinResDto> searchListM2OJ() {
        List<ProductReceiveProj> receiveProjs = productReceiveService.searchListM2OJ();
        List<ProductReceiveJoinResDto> resDtos = receiveProjs.stream().map(r -> ProductReceiveJoinResDto.toDto(r)).collect(Collectors.toList());
        return resDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 한개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveService#createPR
     * @see ProductOptionService#updateReceiveProductUnit
     * @see ProductReceiveGetDto#toDto
     */
    public ProductReceiveGetDto createPR(ProductReceiveGetDto productReceiveGetDto, UUID userId) {
        productReceiveGetDto.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(userId);

        // ProductReceive 데이터 생성
        ProductReceiveEntity entity = productReceiveService.createPR(ProductReceiveEntity.toEntity(productReceiveGetDto));
        ProductReceiveGetDto dto = ProductReceiveGetDto.toDto(entity);
        
        // ProductOption 재고 반영
        productOptionService.updateReceiveProductUnit(entity.getProductOptionCid(), userId, entity.getReceiveUnit());
        
        return dto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 여러개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDto : List::ProductReceiveGetDto::
     * @param userId : UUID
     * @see ProductReceiveService#createPRList
     * @see ProductOptionService#updateReceiveProductUnit
     * @see ProductReceiveGetDto#toDto
     */
    public List<ProductReceiveGetDto> createPRList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {
        List<ProductReceiveEntity> convertedEntities = productReceiveGetDtos.stream().map(r -> {
            r.setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(userId);
            return ProductReceiveEntity.toEntity(r);
        }).collect(Collectors.toList());

        // ProductReceive 데이터 생성
        List<ProductReceiveEntity> entities = productReceiveService.createPRList(convertedEntities);
        // ProductOption 재고 반영
        entities.forEach(r -> { productOptionService.updateReceiveProductUnit(r.getProductOptionCid(), userId, r.getReceiveUnit()); });

        List<ProductReceiveGetDto> dtos = entities.stream().map(r -> ProductReceiveGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }


    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productReceiveCid : Integer
     * @param userId : UUID
     * @see ProductReceiveService#destroyOne
     */
    public void destroyOne(Integer productReceiveCid, UUID userId) {
        productReceiveService.destroyOne(productReceiveCid, userId);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 ProductReceive cid 값과 상응되는 데이터를 업데이트한다.
     * ProductOption 의 재고수량을 변경한다.
     * 
     * @param receiveDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveService#searchOne
     * @see ProductReceiveService#createPR
     * @see ProductOptionService#updateReceiveProductUnit
     */
    public void changeOne(ProductReceiveGetDto receiveDto, UUID userId) {
        // 입고 데이터 조회
        ProductReceiveEntity entity = productReceiveService.searchOne(receiveDto.getCid());
        
        // 변경된 입고수량
        int changedReceiveUnit = receiveDto.getReceiveUnit() - entity.getReceiveUnit();
        // 변경된 입고 데이터
        entity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
        productReceiveService.createPR(entity);

        // 상품옵션의 재고수량 반영
        productOptionService.updateReceiveProductUnit(entity.getProductOptionCid(), userId, changedReceiveUnit);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 다중 ProductReceive cid 값과 상응되는 데이터를 업데이트한다.
     * ProductOption 의 재고수량을 변경한다.
     * 
     * @param receiveDtos : List::roductReceiveGetDto::
     * @param userId : UUID
     * @see ProductReceiveBusinessService#changeOne
     */
    public void changeList(List<ProductReceiveGetDto> receiveDtos, UUID userId) {
        receiveDtos.stream().forEach(r -> this.changeOne(r, userId));
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param receiveDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveService#searchOne
     * @see ProductReceiveService#createPR
     * @see ProductOptionService#updateReceiveProductUnit
     */
    public void patchOne(ProductReceiveGetDto receiveDto, UUID userId) {
        ProductReceiveEntity receiveEntity = productReceiveService.searchOne(receiveDto.getCid());

        if (receiveDto.getReceiveUnit() != null) {
            int storedReceiveUnit = receiveEntity.getReceiveUnit();
            // 변경된 입고 데이터
            receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
            productReceiveService.createPR(receiveEntity);
            productOptionService.updateReceiveProductUnit(receiveEntity.getProductOptionCid(), userId, receiveEntity.getReceiveUnit() - storedReceiveUnit);
        }
        if (receiveDto.getMemo() != null) {
            receiveEntity.setMemo(receiveDto.getMemo());
            productReceiveService.createPR(receiveEntity);
        }
    }
}
