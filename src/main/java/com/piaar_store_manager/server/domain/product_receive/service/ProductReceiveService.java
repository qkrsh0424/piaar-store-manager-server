package com.piaar_store_manager.server.domain.product_receive.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.domain.product_receive.repository.ProductReceiveCustomJdbc;
import com.piaar_store_manager.server.domain.product_receive.repository.ProductReceiveRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReceiveService {
    private final ProductReceiveRepository productReceiveRepository;
    private final ProductReceiveCustomJdbc productReceiveCustomJdbc;

    public ProductReceiveEntity searchOne(Integer productReceiveCid) {
        Optional<ProductReceiveEntity> receiveEntityOpt = productReceiveRepository.findById(productReceiveCid);

        if (receiveEntityOpt.isPresent()) {
            return receiveEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public List<ProductReceiveEntity> searchList() {
        return productReceiveRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productReceiveCid에 대응되는 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param productReceiveCid : Integer
     * @return ProductReceiveProj
     * @see ProductReceiveRepository#searchOneM2OJ
     */
    public ProductReceiveProj searchOneM2OJ(Integer productReceiveCid){
        Optional<ProductReceiveProj> productReceiveProjOpt = productReceiveRepository.searchOneM2OJ(productReceiveCid);

        if(productReceiveProjOpt.isPresent()) {
            return productReceiveProjOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid에 대응하는 receive 데이터를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveEntity::
     * @see ProductReceiveRepository#findByProductOptionCid
     */
    public List<ProductReceiveEntity> searchListByOptionCid(Integer productOptionCid) {
        return productReceiveRepository.findByProductOptionCid(productOptionCid);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 다중 cid값에 대응되는 receive 데이터를 모두 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReceiveEntity
     * @see ProductReceiveRepository#selectAllByCid
     */
    public List<ProductReceiveEntity> searchListByCid(List<Integer> cids) {
        return productReceiveRepository.selectAllByCid(cids);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @return List::ProductReceiveProj::
     * @see ProductReceiveRepository#searchListM2OJ
     */
    public List<ProductReceiveProj> searchListM2OJ() {
        return productReceiveRepository.searchListM2OJ();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate와 endDate기간 사이에 등록된 모든 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param startDate : LocalDateTime
     * @param endDate : LocalDateTime
     * @return List::ProductReceiveProj::
     * @see ProductReceiveRepository#searchListM2OJ
     */
    public List<ProductReceiveProj> searchListM2OJ(LocalDateTime startDate, LocalDateTime endDate) {
        return productReceiveRepository.searchListM2OJ(startDate, endDate);
    }

    public void saveAndModify(ProductReceiveEntity entity) {
        productReceiveRepository.save(entity);
    }

    @Transactional
    public List<ProductReceiveEntity> saveListAndModify(List<ProductReceiveEntity> entities) {
        return productReceiveRepository.saveAll(entities);
    }

    public void destroyOne(Integer productReceiveCid) {
        productReceiveRepository.findById(productReceiveCid).ifPresent(receive -> {
            productReceiveRepository.delete(receive);
        });
    }

    public void bulkInsert(List<ProductReceiveEntity> entities){
        // access check
        // userService.userLoginCheck();
        // userService.userManagerRoleCheck();
        
        productReceiveCustomJdbc.jdbcBulkInsert(entities);
    }

    public void deleteByErpOrderItemIds(List<UUID> erpOrderItemIds){
        // access check
        // userService.userLoginCheck();
        // userService.userManagerRoleCheck();
        
        productReceiveRepository.deleteByErpOrderItemIds(erpOrderItemIds);
    }
}
