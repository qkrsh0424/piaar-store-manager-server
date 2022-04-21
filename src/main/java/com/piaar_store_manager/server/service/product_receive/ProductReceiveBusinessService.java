package com.piaar_store_manager.server.service.product_receive;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveJoinResDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.service.option_package.OptionPackageService;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.user.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReceiveBusinessService {
    private final ProductReceiveService productReceiveService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;


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
        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
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
        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
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
        List<ProductReceiveJoinResDto> resDtos = receiveProjs.stream().map(proj -> ProductReceiveJoinResDto.toDto(proj)).collect(Collectors.toList());
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
    @Transactional
    public ProductReceiveGetDto createPR(ProductReceiveGetDto productReceiveGetDto) {
        UUID USER_ID = userService.getUserId();

        productReceiveGetDto.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);

        // ProductReceive 데이터 생성
        ProductReceiveEntity entity = productReceiveService.createPR(ProductReceiveEntity.toEntity(productReceiveGetDto));
        ProductReceiveGetDto dto = ProductReceiveGetDto.toDto(entity);
        
        // ProductOption 재고 반영
        productOptionService.updateReceiveProductUnit(entity.getProductOptionCid(), entity.getReceiveUnit());
        
        return dto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 여러개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDtos : List::ProductReceiveGetDto::
     * @param userId : UUID
     * @see ProductReceiveService#createPRList
     * @see ProductOptionService#updateReceiveProductUnit
     * @see ProductReceiveGetDto#toDto
     */
//    @Transactional
//    public List<ProductReceiveGetDto> createPRList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {
//        List<ProductReceiveEntity> convertedEntities = productReceiveGetDtos.stream().map(r -> {
//            r.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(userId);
//            return ProductReceiveEntity.toEntity(r);
//        }).collect(Collectors.toList());
//
//        // ProductReceive 데이터 생성
//        List<ProductReceiveEntity> entities = productReceiveService.createPRList(convertedEntities);
//        // ProductOption 재고 반영
//        entities.forEach(r -> { productOptionService.updateReceiveProductUnit(r.getProductOptionCid(), r.getReceiveUnit()); });
//
//        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
//        return dtos;
//    }

    @Transactional
    public void createPRList(List<ProductReceiveGetDto> productReceiveGetDtos) {
        // 1. receive로 넘어온 productOptionCid로 option데이터를 찾는다.
        // 2. option 데이터의 packageYn이 n인 것과 y인 것을 분리
        // 3. 먼저 n인 애들 처리
        // 4. y인 애들 처리하기

        List<Integer> optionCids = productReceiveGetDtos.stream().map(r -> r.getProductOptionCid()).collect(Collectors.toList());
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByOptionCids(optionCids);
        // 1. 세트상품 X
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. 세트상품 O
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        this.createOfOriginOption(productReceiveGetDtos, originOptionEntities);

        if(parentOptionEntities.size() > 0) {
            this.createOfPackageOption(productReceiveGetDtos, parentOptionEntities);
        }

//        List<ProductReceiveEntity> convertedEntities = productReceiveGetDtos.stream().map(r -> {
//            r.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(userId);
//            return ProductReceiveEntity.toEntity(r);
//        }).collect(Collectors.toList());
//
//        // ProductReceive 데이터 생성
//        List<ProductReceiveEntity> entities = productReceiveService.createPRList(convertedEntities);
//        // ProductOption 재고 반영
//        entities.forEach(r -> { productOptionService.updateReceiveProductUnit(r.getProductOptionCid(), r.getReceiveUnit()); });
//
//        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> ProductReceiveGetDto.toDto(entity)).collect(Collectors.toList());
//        return dtos;
    }

    public void createOfOriginOption(List<ProductReceiveGetDto> productReceiveGetDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();

        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();

        productReceiveGetDtos.forEach(dto -> {
            originOptionEntities.forEach(option -> {
                if(dto.getProductOptionCid().equals(option.getCid())){
                    ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                        .id(UUID.randomUUID())
                        .receiveUnit(dto.getReceiveUnit())
                        .memo(dto.getMemo())
                        .createdAt(CustomDateUtils.getCurrentDateTime())
                        .createdBy(USER_ID)
                        .productOptionCid(option.getCid())
                        .build();

                productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
                }
            });
        });

        productReceiveService.createPRList(productReceiveEntities);
    }

    public void createOfPackageOption(List<ProductReceiveGetDto> productReceiveGetDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();

        productReceiveGetDtos.forEach(dto -> {
            parentOptionEntities.forEach(parentOption -> {
                if(dto.getProductOptionCid().equals(parentOption.getCid())) {
                    optionPackageEntities.forEach(option -> {
                        if(option.getParentOptionId().equals(parentOption.getId())) {
                            ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                                    .id(UUID.randomUUID())
                                    .receiveUnit(option.getPackageUnit() * dto.getReceiveUnit())
                                    .memo(dto.getMemo())
                                    .createdAt(CustomDateUtils.getCurrentDateTime())
                                    .createdBy(USER_ID)
                                    .productOptionCid(option.getOriginOptionCid())
                                    .build();

                            productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
                        }
                    });
                }
            });
        });
        productReceiveService.createPRList(productReceiveEntities);
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
    public void destroyOne(Integer productReceiveCid) {
        UUID USER_ID = userService.getUserId();
        productReceiveService.destroyOne(productReceiveCid, USER_ID);
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
    @Transactional
    public void changeOne(ProductReceiveGetDto receiveDto) {
        // 입고 데이터 조회
        ProductReceiveEntity entity = productReceiveService.searchOne(receiveDto.getCid());
        
        // 변경된 입고수량
        int changedReceiveUnit = receiveDto.getReceiveUnit() - entity.getReceiveUnit();
        // 변경된 입고 데이터
        entity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
        productReceiveService.createPR(entity);

        // 상품옵션의 재고수량 반영
        productOptionService.updateReceiveProductUnit(entity.getProductOptionCid(), changedReceiveUnit);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 다중 ProductReceive cid 값과 상응되는 데이터를 업데이트한다.
     * ProductOption 의 재고수량을 변경한다.
     * 
     * @param receiveDtos : List::roductReceiveGetDto::
     * @see ProductReceiveBusinessService#changeOne
     */
    @Transactional
    public void changeList(List<ProductReceiveGetDto> receiveDtos) {
        receiveDtos.stream().forEach(r -> this.changeOne(r));
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param receiveDto : ProductReceiveGetDto
     * @see ProductReceiveService#searchOne
     * @see ProductReceiveService#createPR
     * @see ProductOptionService#updateReceiveProductUnit
     */
    public void patchOne(ProductReceiveGetDto receiveDto) {
        ProductReceiveEntity receiveEntity = productReceiveService.searchOne(receiveDto.getCid());

        if (receiveDto.getReceiveUnit() != null) {
            int storedReceiveUnit = receiveEntity.getReceiveUnit();
            // 변경된 입고 데이터
            receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit()).setMemo(receiveDto.getMemo());
            productOptionService.updateReceiveProductUnit(receiveEntity.getProductOptionCid(), receiveEntity.getReceiveUnit() - storedReceiveUnit);
        }
        if (receiveDto.getMemo() != null) {
            receiveEntity.setMemo(receiveDto.getMemo());
        }
        productReceiveService.createPR(receiveEntity);

    }
}
