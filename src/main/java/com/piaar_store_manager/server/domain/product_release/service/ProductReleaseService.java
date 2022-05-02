package com.piaar_store_manager.server.domain.product_release.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.product_release.repository.ProductReleaseCustomJdbc;
import com.piaar_store_manager.server.domain.product_release.repository.ProductReleaseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseService {
    private final ProductReleaseRepository productReleaseRepository;
    private final ProductOptionService productOptionService;
    private final ProductReleaseCustomJdbc productReleaseCustomJdbc;

    public ProductReleaseEntity searchOne(Integer productReleaseCid) {
        Optional<ProductReleaseEntity> releaseEntityOpt = productReleaseRepository.findById(productReleaseCid);

        if (releaseEntityOpt.isPresent()) {
            return releaseEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    public List<ProductReleaseEntity> searchList() {
        return productReleaseRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productReleaseCid에 대응되는 release, release와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param productReleaseCid : Integer
     * @return ProductReleaseProj
     * @see ProductReleaseRepository#searchOneM2OJ
     */
    public ProductReleaseProj searchOneM2OJ(Integer productReleaseCid){
        Optional<ProductReleaseProj> productReleaseProjOpt = productReleaseRepository.searchOneM2OJ(productReleaseCid);

        if(productReleaseProjOpt.isPresent()) {
            return productReleaseProjOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOptionCid 값과 상응되는 release 데이터를 모두 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReleaseEntity
     * @see ProductReleaseRepository#findByProductOptionCid
     */
    public List<ProductReleaseEntity> searchListByOptionCid(Integer productOptionCid) {
        return productReleaseRepository.findByProductOptionCid(productOptionCid);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 다중 cid값에 대응되는 release 데이터를 모두 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReleaseEntity
     * @see ProductReleaseRepository#selectAllByCid
     */
    public List<ProductReleaseEntity> searchListByCid(List<Integer> cids) {
        return productReleaseRepository.selectAllByCid(cids);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 release, release와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @return List::ProductReleaseProj::
     * @see ProductReleaseRepository#searchListM2OJ
     */
    public List<ProductReleaseProj> searchListM2OJ() {
        return productReleaseRepository.searchListM2OJ();
    }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate와 endDate기간 사이에 등록된 모든 releaes, releaes와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param startDate : LocalDateTime
     * @param endDate : LocalDateTime
     * @return List::ProductReleaseProj::
     * @see ProductReleaseRepository#searchListM2OJ
     */
    public List<ProductReleaseProj> searchListM2OJ(LocalDateTime startDate, LocalDateTime endDate) {
        return productReleaseRepository.searchListM2OJ(startDate, endDate);
    }

    public void saveAndModify(ProductReleaseEntity entity) {
        productReleaseRepository.save(entity);
    }

    @Transactional
    public void saveListAndModify(List<ProductReleaseEntity> entities) {
        productReleaseRepository.saveAll(entities);
    }

    public void destroyOne(Integer productReleaseCid) {
        productReleaseRepository.findById(productReleaseCid).ifPresent(product -> {
            productReleaseRepository.delete(product);
        });
    }

    public void bulkInsert(List<ProductReleaseEntity> entities){
        // access check
        // userService.userLoginCheck();
        // userService.userManagerRoleCheck();
        
        productReleaseCustomJdbc.jdbcBulkInsert(entities);
    }

    public void deleteByErpOrderItemIds(List<UUID> erpOrderItemIds){
        // access check
        // userService.userLoginCheck();
        // userService.userManagerRoleCheck();
        
        productReleaseRepository.deleteByErpOrderItemIds(erpOrderItemIds);
    }
}
