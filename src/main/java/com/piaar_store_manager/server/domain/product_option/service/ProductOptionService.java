package com.piaar_store_manager.server.domain.product_option.service;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockCycleDto;
import com.piaar_store_manager.server.domain.product_option.dto.ReceiveReleaseSumOnlyDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;
import com.piaar_store_manager.server.domain.product_option.repository.ProductOptionCustomJdbc;
import com.piaar_store_manager.server.domain.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;
import com.piaar_store_manager.server.domain.stock_analysis.proj.StockAnalysisProj;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionCustomJdbc productOptionCustomJdbc;

    public ProductOptionEntity searchOne(Integer productOptionCid) {
        Optional<ProductOptionEntity> productOptionEntityOpt = productOptionRepository.findById(productOptionCid);

        if (productOptionEntityOpt.isPresent()) {
            return productOptionEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
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
     * @param productOptionCid : Integer
     * @return ProductOptionProj
     * @see ProductOptionRepository#searchOneM2OJ
     */
    public ProductOptionProj searchOneM2OJ(Integer productOptionCid) {
        Optional<ProductOptionProj> productOptionProjOpt = productOptionRepository.searchOneM2OJ(productOptionCid);

        if(productOptionProjOpt.isPresent()) {
            return productOptionProjOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 option, option과 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, category, user를 함께 조회한다.
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
     * @return List::ProductOptionGetDto::
     * @see ProductOptionRepository#findByProductCid
     */
    public List<ProductOptionEntity> searchListByProductCid(Integer productCid) {
        return productOptionRepository.findByProductCid(productCid);
    }

    // [221021] FEAT
    public List<ProductOptionEntity> searchListByProductId(UUID productId) {
        return productOptionRepository.findByProductId(productId);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * cids에 대응되는 option 데이터를 모두 조회한다.
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
     * @see ProductOptionRepository#searchListByProductCids
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
     * @param optionCodes : List::String::
     * @return List::ProductOptionGetDto::
     * @see ProductOptionRepository#selectListByCodes
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
     * @param entities : List::ProductOptionEntity::
     * @return List::ProductOptionGetDto::
     * @see ProductOptionService#sumStockUnit
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

    public void deleteBatch(List<UUID> ids) {
        productOptionRepository.deleteBatch(ids);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * cids에 대응되는 옵션 조회,
     * receive(입고)와 release(출고) 데이터를 이용해 재고수량을 계산한다.
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

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 
     * @param optionCode : String
     * @return Integer
     */
    public ProductOptionEntity findOneByCode(String optionCode) {
        Optional<ProductOptionEntity> optionEntityOpt = productOptionRepository.findByCode(optionCode);
        
        if(optionEntityOpt.isPresent()) {
            return optionEntityOpt.get();
        }else{
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCids값에 대응되는 총 옵션수량, 출고수량, 재고수량을 조회한다.
     * 
     * @param entities List::ProductOptionEntity::
     * @see ProductOptionRepository#sumStockUnitByOption
     */
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

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate, endDate 날짜 사이의 판매랭킹 데이터를 모두 조회한다.
     * (네이버, 쿠팡, 피아르 erp)
     * 
     * @param startDate : LocalDateTime
     * @param endDate : LocalDateTime
     * @return List::SalesAnalysisItemProj::
     */
    public List<SalesAnalysisItemProj> findSalesAnalysisItem(LocalDateTime startDate, LocalDateTime endDate) {
        return productOptionRepository.findSalesAnalysisItem(startDate, endDate);
    }

    public List<StockAnalysisProj> qfindStockAnalysis() {
        return productOptionRepository.qfindStockAnalysis();
    }

    public List<ProductOptionStockCycleDto> searchStockStatusByWeek(LocalDateTime searchEndDate, Integer productCid) {
        return productOptionCustomJdbc.searchStockStatusByWeek(searchEndDate, productCid);
    }

    public ProductOptionProjection.RelatedProductReceiveAndProductRelease qSearchBatchStockStatus(List<UUID> optionIds, Map<String, Object> params) {
        return productOptionRepository.qSearchBatchStockStatus(optionIds, params);
    }
}
