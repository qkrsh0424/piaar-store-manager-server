package com.piaar_store_manager.server.service.product_release;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.model.product_release.repository.ProductReleaseRepository;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReleaseService {

    @Autowired
    private ProductReleaseRepository productReleaseRepository;

    @Autowired
    private ProductOptionService productOptionService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productReleaseCid : Integer
     * @return ProductReleaseEntity
     * @see ProductReleaseRepository#findById
     */
    public ProductReleaseEntity searchOne(Integer productReleaseCid) {
        Optional<ProductReleaseEntity> releaseEntityOpt = productReleaseRepository.findById(productReleaseCid);
        ProductReleaseEntity entity = new ProductReleaseEntity();

        if (releaseEntityOpt.isPresent()) {
            entity = releaseEntityOpt.get();
        } else {
            throw new NullPointerException();
        }

        return entity;
    }


    public ProductReleaseProj searchOneM2OJ(Integer productReleaseCid){
        Optional<ProductReleaseProj> productReleaseProjOpt = productReleaseRepository.selectByCid(productReleaseCid);

        if(productReleaseProjOpt.isPresent()) {
            return productReleaseProjOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease 데이터를 모두 조회한다.
     * 
     * @return List::ProductReleaseEntity::
     * @see ProductReleaseRepository#findAll
     */
    public List<ProductReleaseEntity> searchList() {
        return productReleaseRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 ProductRelease 데이터를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReleaseEntity
     * @see ProductReleaseRepository#findByProductOptionCid
     */
    public List<ProductReleaseEntity> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReleaseEntity> productEntities = productReleaseRepository.findByProductOptionCid(productOptionCid);

        return productEntities;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 다중 ProductRelease cid 값과 상응되는 데이터를 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReleaseEntity
     * @see ProductReleaseRepository#selectAllByCid
     */
    public List<ProductReleaseEntity> searchListByCid(List<Integer> cids) {
        List<ProductReleaseEntity> entities = productReleaseRepository.selectAllByCid(cids);

        return entities;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease 데이터를 모두 조회하고,
     * 해당 ProductRelease와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductReleaseProj::
     * @see ProductReleaseRepository#selectAll
     */
    public List<ProductReleaseProj> searchListM2OJ() {
        return productReleaseRepository.selectAll();
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 한개 등록한다.
     * 
     * @param entity : ProductReleaseEntity
     * @see ProductReleaseRepository#save
     */
    public ProductReleaseEntity createPL(ProductReleaseEntity entity) {
        return productReleaseRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 여러개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param entities : List::ProductReleaseEntity::
     * @see ProductReleaseRepository#saveAll
     */
    public List<ProductReleaseEntity> createPLList(List<ProductReleaseEntity> entities) {
        return productReleaseRepository.saveAll(entities);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productReleaseCid : Integer
     * @param userId : UUID
     * @see ProductReleaseRepository#findById
     * @see ProductOptionService#releaseProductUnit
     * @see ProductReleaseRepository#delete
     */
    public void destroyOne(Integer productReleaseCid, UUID userId) {
        productReleaseRepository.findById(productReleaseCid).ifPresent(product -> {
            productOptionService.updateReceiveProductUnit(product.getProductOptionCid(), userId, product.getReleaseUnit());

            productReleaseRepository.delete(product);
        });
    }
}
