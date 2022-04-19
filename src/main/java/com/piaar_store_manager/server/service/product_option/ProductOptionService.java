package com.piaar_store_manager.server.service.product_option;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ReceiveReleaseSumOnlyDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 조회한다.
     * 
     * @param productOptionCid : Integer
     * @return ProductOptionEntity
     * @see ProductOptionRepository#findById
     */
    public ProductOptionEntity searchOne(Integer productOptionCid) {
        Optional<ProductOptionEntity> productOptionEntityOpt = productOptionRepository.findById(productOptionCid);

        if (productOptionEntityOpt.isPresent()) {
            return productOptionEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductOption와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return ProductOptionProj
     * @see ProductOptionRepository#selectByCid
     */
    public ProductOptionProj searchOneM2OJ(Integer productOptionCid) {
        Optional<ProductOptionProj> productOptionProjOpt = productOptionRepository.selectByCid(productOptionCid);

        if(productOptionProjOpt.isPresent()) {
            return productOptionProjOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     *
     * @return List::ProductOptionEntity::
     * @see ProductOptionRepository#findAll
     */
    public List<ProductOptionEntity> searchList() {
        return productOptionRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     * 해당 ProductOption와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductOptionProj::
     * @see ProductOptionRepository#selectAll
     */
    public List<ProductOptionProj> searchListM2OJ() {
        return productOptionRepository.selectAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product Cid에 대응되는 ProductOption 데이터를 모두 조회한다.
     * ProductOption의 입고수량과 출고수량을 통해 재고수량을 계산한다.
     *
     * @return List::ProductOptionEntity::
     * @param productCid : Integer
     * @see ProductOptionRepository#findByProductCid
     */
    public List<ProductOptionEntity> searchListByProduct(Integer productCid) {
        return productOptionRepository.findByProductCid(productCid);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product Cid들에 대응되는 ProductOption 데이터를 모두 조회한다.
     * ProductOption의 입고수량과 출고수량을 통해 재고수량을 계산한다.
     *
     * @return List::ProductOptionGetDto::
     * @param productCid : List::Integer::
     * @see ProductOptionRepository#selectAllByProductCids
     * @see ProductOptionService#searchStockUnit
     */
    public List<ProductOptionGetDto> searchListByProductList(List<Integer> productCids) {
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.selectAllByProductCids(productCids);
        List<ProductOptionGetDto> productOptionGetDtos = this.searchStockUnit(productOptionEntities);
        return productOptionGetDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Option Code들에 대응되는 ProductOption 데이터를 모두 조회한다.
     * ProductOption의 입고수량과 출고수량을 통해 재고수량을 계산한다.
     *
     * @return List::ProductOptionGetDto::
     * @param optionCodes : List::String::
     * @see ProductOptionRepository#findAllByCode
     * @see ProductOptionService#searchStockUnit
     */
    public List<ProductOptionGetDto> searchListByOptionCodes(List<String> optionCodes) {
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.findAllByCode(optionCodes);
        List<ProductOptionGetDto> productOptionGetDtos = this.searchStockUnit(productOptionEntities);
        return productOptionGetDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption의 재고수량을 계산한다.
     * 입고수량과 출고수량을 이용해 ProductOption의 stockUnit을 세팅한다.
     * (receivedSumUnit, releasedSumUnit, stockSunUnit을 반환하기 위해 dto사용)
     *
     * @return List::ProductOptionGetDto::
     * @param productCid : Integer
     * @see ProductOptionGetDto#toDtos
     * @see ProductOptionBusinessService#sumStockUnit
     */
    public List<ProductOptionGetDto> searchStockUnit(List<ProductOptionEntity> entities) {
        List<Integer> productOptionCids = entities.stream().map(r -> r.getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> productOptionGetDtos = entities.stream().map(entity -> ProductOptionGetDto.toDto(entity)).collect(Collectors.toList());

        List<ReceiveReleaseSumOnlyDto> stockUnitByOption = this.sumStockUnit(productOptionCids);

        for(ProductOptionGetDto dto : productOptionGetDtos) {
            stockUnitByOption.stream().forEach(r -> {
                if(dto.getCid().equals(r.getOptionCid())){
                    dto.setReceivedSumUnit(r.getReceivedSum()).setReleasedSumUnit(r.getReleasedSum())
                        .setStockSumUnit(r.getReceivedSum() - r.getReleasedSum());
                }
            });
        }

        return productOptionGetDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 한개 등록한다.
     * 
     * @param entity : ProductOptionEntity
     * @see ProductOptionRepository#save
     */
    public ProductOptionEntity createOne(ProductOptionEntity entity) {
        return productOptionRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 여러개 등록한다.
     * 
     * @param entities : List::ProductOptionEntity::
     * @see ProductOptionRepository#saveAll
     */
    public List<ProductOptionEntity> createList(List<ProductOptionEntity> entities) {
        return productOptionRepository.saveAll(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productOptionCid : Integer
     * @see ProductOptionRepository#findById
     * @see ProductOptionRepository#delete
     */
    public void destroyOne(Integer productOptionCid) {
        productOptionRepository.findById(productOptionCid).ifPresent(productOption -> {
            productOptionRepository.delete(productOption);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productOptionDto : ProductOptionGetDto
     * @param userId : UUID
     * @see ProductOptionRepository#findById
     * @see ProductOptionRepository#save
     */
    public void changeOne(ProductOptionGetDto productOptionDto, UUID userId) {
        productOptionRepository.findById(productOptionDto.getCid()).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setCode(productOptionDto.getCode())
                    .setNosUniqueCode(productOptionDto.getNosUniqueCode())
                    .setDefaultName(productOptionDto.getDefaultName())
                    .setManagementName(productOptionDto.getManagementName())
                    .setNosUniqueCode(productOptionDto.getNosUniqueCode())
                    .setSalesPrice(productOptionDto.getSalesPrice()).setStockUnit(productOptionDto.getStockUnit())
                    .setTotalPurchasePrice(productOptionDto.getTotalPurchasePrice())
                    .setStatus(productOptionDto.getStatus()).setMemo(productOptionDto.getMemo())
                    .setImageUrl(productOptionDto.getImageUrl()).setImageFileName(productOptionDto.getImageFileName())
                    .setColor(productOptionDto.getColor()).setUnitCny(productOptionDto.getUnitCny())
                    .setUnitKrw(productOptionDto.getUnitKrw())
                    .setPackageYn(productOptionDto.getPackageYn())
                    .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(userId)
                    .setProductCid(productOptionDto.getProductCid());

            productOptionRepository.save(productOptionEntity);
        }, null);

    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 입고되는 상품의 ProductOption cid 값과 상응되는 데이터의 내용을 업데이트한다.
     * 
     * @param optionCid : Integer
     * @param userId : UUID
     * @param receiveUnit : Integer
     * @see ProductOptionRepository#findById
     * @see ProductOptionRepository#save
     */
    public void updateReceiveProductUnit(Integer optionCid, Integer receiveUnit){
        UUID USER_ID = userService.getUserId();
        productOptionRepository.findById(optionCid).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setStockUnit(productOptionEntity.getStockUnit() + receiveUnit)
                               .setUpdatedAt(DateHandler.getCurrentDate2())
                               .setUpdatedBy(USER_ID);

            productOptionRepository.save(productOptionEntity);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 출고되는 상품의 ProductOption cid 값과 상응되는 데이터의 내용을 업데이트한다.
     * 
     * @param optionCid : Integer
     * @param userId : UUID
     * @param releaseUnit : Integer
     * @see ProductOptionRepository#findById
     * @see ProductOptionRepository#save
     */
    public void updateReleaseProductUnit(Integer optionCid, Integer releaseUnit){
        UUID USER_ID = userService.getUserId();
        productOptionRepository.findById(optionCid).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setStockUnit(productOptionEntity.getStockUnit() - releaseUnit)
                               .setUpdatedAt(DateHandler.getCurrentDate2())
                               .setUpdatedBy(USER_ID);

            productOptionRepository.save(productOptionEntity);
        }, null);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 입고데이터와 출고데이터의 합을 모두 조회한다.
     * 입고데이터와 출고데이터를 이용해 재고수량을 계산한다.
     * 
     * @param cids : List::Integer::
     * @see ProductOptionRepository#sumStockUnitByOption
     */
    public List<ReceiveReleaseSumOnlyDto> sumStockUnit(List<Integer> cids) {
        List<Tuple> stockUnitTuple = productOptionRepository.sumStockUnitByOption(cids);
        List<ReceiveReleaseSumOnlyDto> stockUnitByOption = stockUnitTuple.stream().map(r -> {
            ReceiveReleaseSumOnlyDto dto = ReceiveReleaseSumOnlyDto.builder()
                    .optionCid(r.get("cid", Integer.class))
                    .receivedSum(r.get("receivedSum", BigDecimal.class) != null ? r.get("receivedSum", BigDecimal.class).intValue() : 0)
                    .releasedSum(r.get("releasedSum", BigDecimal.class) != null ? r.get("releasedSum", BigDecimal.class).intValue() : 0)
                    .build();

            return dto;
        }).collect(Collectors.toList());

        return stockUnitByOption;
    }

    public Integer findOptionCidByCode(String optionCode) {
        return productOptionRepository.findCidByCode(optionCode);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다.
     * 해당 ProductOption와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductOptionProj::
     * @see ProductOptionRepository#qfindAllM2OJ
     */
    public List<ProductOptionProj> qfindAllM2OJ() {
        return productOptionRepository.qfindAllM2OJ();
    }

    public List<ProductOptionEntity> searchListByOptionCids(List<Integer> cids) {
        return productOptionRepository.findAllByCids(cids);
    }
}
