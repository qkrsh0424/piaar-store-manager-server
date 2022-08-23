package com.piaar_store_manager.server.domain.product_release.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.product_release.repository.ProductReleaseCustomJdbc;
import com.piaar_store_manager.server.domain.product_release.repository.ProductReleaseRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseService {
    private final ProductReleaseRepository productReleaseRepository;
    private final ProductReleaseCustomJdbc productReleaseCustomJdbc;

    public ProductReleaseEntity searchOne(UUID productReceiveId) {
        Optional<ProductReleaseEntity> receiveEntityOpt = productReleaseRepository.findById(productReceiveId);

        if (receiveEntityOpt.isPresent()) {
            return receiveEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public ProductReleaseProj searchOneM2OJ(UUID productReleaseId){
        Optional<ProductReleaseProj> productReleaseProjOpt = productReleaseRepository.searchOneM2OJ(productReleaseId);

        if(productReleaseProjOpt.isPresent()) {
            return productReleaseProjOpt.get();
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

    // deprecated
    public void destroyOne(Integer productReleaseCid) {
        productReleaseRepository.findById(productReleaseCid).ifPresent(product -> {
            productReleaseRepository.delete(product);
        });
    }

    public void destroyOne(UUID id) {
        productReleaseRepository.findById(id).ifPresent(release -> {
            productReleaseRepository.delete(release);
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
