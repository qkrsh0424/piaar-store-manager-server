package com.piaar_store_manager.server.domain.product_option.service;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ReceiveReleaseSumOnlyDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.repository.ProductOptionRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;

    public ProductOptionEntity searchOne(Integer productOptionCid) {
        Optional<ProductOptionEntity> productOptionEntityOpt = productOptionRepository.findById(productOptionCid);

        if (productOptionEntityOpt.isPresent()) {
            return productOptionEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    public List<ProductOptionEntity> searchList() {
        return productOptionRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid에 대응하는 option, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, user, category를 함께 조회한다.
     *
     * @return ProductOptionProj
     * @see ProductOptionRepository#selectByCid
     */
    public ProductOptionProj searchOneM2OJ(Integer productOptionCid) {
        Optional<ProductOptionProj> productOptionProjOpt = productOptionRepository.searchOneM2OJ(productOptionCid);

        if(productOptionProjOpt.isPresent()) {
            return productOptionProjOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 option, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, user, category를 함께 조회한다.
     *
     * @return List::ProductOptionProj::
     * @see ProductOptionRepository#searchListM2OJ
     */
    public List<ProductOptionProj> searchListM2OJ() {
        return productOptionRepository.searchListM2OJ();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCid에 대응되는 option 데이터를 모두 조회한다.
     *
     * @param productCid : Integer
     * @return List::ProductOptionEntity::
     * @see ProductOptionRepository#findByProductCid
     */
    public List<ProductOptionEntity> searchListByProductCid(Integer productCid) {
        return productOptionRepository.findByProductCid(productCid);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * optionCodes에 대응되는 option 데이터를 모두 조회한다.
     * 
     * @param cids : List::Integer:;
     * @see ProductOptionRepository#findAllByCids
     */
    public List<ProductOptionEntity> searchListByCids(List<Integer> cids) {
        return productOptionRepository.findAllByCids(cids);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCids에 대응되는 option 데이터를 모두 조회한다.
     * 조회된 option의 release(출고)와 receive(입고) 데이터로 옵션의 재고수량을 구한다.
     *
     * @param productCids : List::Integer::
     * @return List::ProductOptionGetDto::
     * @see ProductOptionRepository#selectAllByProductCids
     * @see ProductOptionService#searchStockUnit
     */
    public List<ProductOptionGetDto> searchListByProductCids(List<Integer> productCids) {
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.searchListByProductCids(productCids);
        List<ProductOptionGetDto> productOptionGetDtos = this.searchStockUnit(productOptionEntities);
        return productOptionGetDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * optionCodes에 대응되는 option 데이터를 모두 조회한다.
     * 조회된 option의 release(출고)와 receive(입고) 데이터로 옵션의 재고수량을 구한다.
     *
     * @param productCids : List::Integer::
     * @return List::ProductOptionGetDto::
     * @see ProductOptionRepository#findAllByCode
     * @see ProductOptionService#searchStockUnit
     */
    public List<ProductOptionGetDto> searchListByOptionCodes(List<String> optionCodes) {
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.selectListByCodes(optionCodes);
        List<ProductOptionGetDto> productOptionGetDtos = this.searchStockUnit(productOptionEntities);
        return productOptionGetDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * option의 재고수량을 계산한다.
     * 입고수량과 출고수량을 이용해 ProductOption의 stockUnit을 세팅한다.
     * (receivedSumUnit, releasedSumUnit, stockSunUnit을 반환하기 위해 dto사용)
     *
     * @return List::ProductOptionGetDto::
     * @param productCid : Integer
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

    public void saveAndModify(ProductOptionEntity entity) {
        productOptionRepository.save(entity);
    }

    public void saveListAndModify(List<ProductOptionEntity> entities) {
        productOptionRepository.saveAll(entities);
    }

    public void destroyOne(Integer productOptionCid) {
        productOptionRepository.findById(productOptionCid).ifPresent(productOption -> {
            productOptionRepository.delete(productOption);
        });
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

    public void setReceivedAndReleasedAndStockSum(List<ProductOptionEntity> entities){
        List<Integer> productOptionCids = entities.stream().map(r -> r.getCid()).collect(Collectors.toList());

        List<Tuple> stockUnitTuple = productOptionRepository.sumStockUnitByOption(productOptionCids);
        stockUnitTuple.forEach(r -> {
            Integer cid = r.get("cid", Integer.class);
            Integer receivedSum = r.get("receivedSum", BigDecimal.class) != null ? r.get("receivedSum", BigDecimal.class).intValue() : 0;
            Integer releasedSum = r.get("releasedSum", BigDecimal.class) != null ? r.get("releasedSum", BigDecimal.class).intValue() : 0;

            entities.forEach(entity -> {
                if(entity.getCid().equals(cid)){
                    entity.setReceivedSum(receivedSum);
                    entity.setReleasedSum(releasedSum);
                    entity.setStockSumUnit(receivedSum - releasedSum);
                }
            });
        });
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 option, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, user, category를 함께 조회한다.
     *
     * @return List::ProductOptionProj::
     * @see ProductOptionRepository#qfindAllM2OJ
     */
    public List<ProductOptionProj> qfindAllM2OJ() {
        return productOptionRepository.qfindAllM2OJ();
    }
}
