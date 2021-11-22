package com.piaar_store_manager.server.service.product_receive;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.model.product_receive.repository.ProductReceiveRepository;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReceiveService {
    private ProductReceiveRepository productReceiveRepository;
    private ProductOptionService productOptionService;

    @Autowired
    public ProductReceiveService(
        ProductReceiveRepository productReceiveRepository,
        ProductOptionService productOptionService
    ) {
        this.productReceiveRepository = productReceiveRepository;
        this.productOptionService = productOptionService;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productReceiveCid : Integer
     * @return ProductReceiveEntity
     * @see ProductReceiveRepository#findById
     */
    public ProductReceiveEntity searchOne(Integer productReceiveCid) {
        Optional<ProductReceiveEntity> receiveEntityOpt = productReceiveRepository.findById(productReceiveCid);

        if (receiveEntityOpt.isPresent()) {
            return receiveEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회하고,
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productReceiveCid : Integer
     * @return ProductReceiveProj
     * @see ProductReceiveRepository#selectByCid
     */
    public ProductReceiveProj searchOneM2OJ(Integer productReceiveCid){
        Optional<ProductReceiveProj> productReceiveProjOpt = productReceiveRepository.selectByCid(productReceiveCid);

        if(productReceiveProjOpt.isPresent()) {
            return productReceiveProjOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive 데이터를 모두 조회한다.
     * 
     * @return List::ProductReceiveEntity::
     * @see ProductReceiveRepository#findAll
     */
    public List<ProductReceiveEntity> searchList() {
        return productReceiveRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 ProductReceive 데이터를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveEntity
     * @see ProductReceiveRepository#findByProductOptionCid
     */
    public List<ProductReceiveEntity> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReceiveEntity> productEntities = productReceiveRepository.findByProductOptionCid(productOptionCid);
        return productEntities;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 다중 ProductReceive cid 값과 상응되는 데이터를 조회한다.
     *
     * @param cids : List::Integer::
     * @return List::ProductReceiveEntity
     * @see ProductReceiveRepository#selectAllByCid
     */
    public List<ProductReceiveEntity> searchListByCid(List<Integer> cids) {
        List<ProductReceiveEntity> entities = productReceiveRepository.selectAllByCid(cids);
        return entities;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive 데이터를 모두 조회하고,
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductReceiveProj::
     * @see ProductReceiveRepository#selectAll
     */
    public List<ProductReceiveProj> searchListM2OJ() {
        return productReceiveRepository.selectAll();
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 한개 등록한다.
     * 
     * @param entity : ProductReceiveEntity
     * @see ProductReceiveRepository#save
     */
    public ProductReceiveEntity createPR(ProductReceiveEntity entity) {
        return productReceiveRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 여러개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param entities : List::ProductReceiveEntity::
     * @see ProductReceiveRepository#saveAll
     */
    public List<ProductReceiveEntity> createPRList(List<ProductReceiveEntity> entities) {
        return productReceiveRepository.saveAll(entities);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productReceiveCid : Integer
     * @param userId : UUID
     * @see ProductReceiveRepository#findById
     * @see ProductOptionService#updateReleaseProductUnit
     * @see ProductReceiveRepository#delete
     */
    public void destroyOne(Integer productReceiveCid, UUID userId) {
        productReceiveRepository.findById(productReceiveCid).ifPresent(product -> {
            productOptionService.updateReleaseProductUnit(product.getProductOptionCid(), userId, product.getReceiveUnit());
            productReceiveRepository.delete(product);
        });
    }
}
