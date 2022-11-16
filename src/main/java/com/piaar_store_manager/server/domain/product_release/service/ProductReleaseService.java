package com.piaar_store_manager.server.domain.product_release.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;
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

    public ProductReleaseEntity searchOne(Integer productReleaseCid) {
        Optional<ProductReleaseEntity> releaseEntityOpt = productReleaseRepository.findById(productReleaseCid);

        if (releaseEntityOpt.isPresent()) {
            return releaseEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public ProductReleaseEntity searchOne(UUID releaseId) {
        Optional<ProductReleaseEntity> entityOpt = productReleaseRepository.findById(releaseId);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public ProductReleaseEntity searchOneByErpOrderItemId(UUID erpOrderItemId) {
        List<ProductReleaseEntity> releaseEntities = productReleaseRepository.findByErpOrderItemId(erpOrderItemId);

        return releaseEntities.stream().findFirst().get();
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

    public List<ProductReleaseEntity> findByErpOrderItemIds(List<UUID> orderItemIds) {
        return productReleaseRepository.findByErpOrderItemIds(orderItemIds);
    }

    public List<ProductReleaseProjection.RelatedProductAndProductOption> qSearchBatchByOptionIds(List<UUID> optionIds, Map<String, Object> params) {
        return productReleaseRepository.qSearchBatchByOptionIds(optionIds, params);
    }
}
