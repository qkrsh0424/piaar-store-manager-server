package com.piaar_store_manager.server.service.product_receive;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.model.product_receive.repository.ProductReceiveRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReceiveService {
    private final ProductReceiveRepository productReceiveRepository;

    public ProductReceiveEntity searchOne(Integer productReceiveCid) {
        Optional<ProductReceiveEntity> receiveEntityOpt = productReceiveRepository.findById(productReceiveCid);

        if (receiveEntityOpt.isPresent()) {
            return receiveEntityOpt.get();
        } else {
            throw new NullPointerException();
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
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid에 대응하는 receive 데이터를 모두 조회한다.
     *
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
     * @see ProductReceiveRepository#selectAll
     */
    public List<ProductReceiveProj> searchListM2OJ() {
        return productReceiveRepository.selectAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * startDate와 endDate기간 사이에 등록된 모든 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     *
     * @param startDate : LocalDateTime
     * @param endDate : LocalDateTime
     * @return List::ProductReceiveProj::
     * @see ProductReceiveRepository#selectAll
     */
    public List<ProductReceiveProj> searchListM2OJ(LocalDateTime startDate, LocalDateTime endDate) {
        return productReceiveRepository.selectAll(startDate, endDate);
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
}
