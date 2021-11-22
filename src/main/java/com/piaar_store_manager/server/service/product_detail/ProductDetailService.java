package com.piaar_store_manager.server.service.product_detail;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;
import com.piaar_store_manager.server.model.product_detail.repository.ProductDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {
    private ProductDetailRepository productDetailRepository;

    @Autowired
    public ProductDetailService(
        ProductDetailRepository productDetailRepository
    ) {
        this.productDetailRepository = productDetailRepository;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 조회한다.
     *
     * @param detailCid : Integer
     * @return ProductDetailGetDto
     * @see ProductDetailRepository#findById
     */
    public ProductDetailEntity searchOne(Integer detailCid) {
        Optional<ProductDetailEntity> detailEntityOpt = productDetailRepository.findById(detailCid);

        if (detailEntityOpt.isPresent()) {
            return detailEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductDetail 데이터를 모두 조회한다.
     * 
     * @return List::ProductDetailEntity::
     * @see ProductDetailRepository#findByProductOptionCid
     */
    public List<ProductDetailEntity> searchList(Integer optionCid) {
        return productDetailRepository.findByProductOptionCid(optionCid);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductDetail 내용을 한개 등록한다.
     * 
     * @param productDetailEntity : ProductDetailEntity
     * @see ProductDetailRepository#save
     */
    public ProductDetailEntity createOne(ProductDetailEntity productDetailEntity) {
        return productDetailRepository.save(productDetailEntity);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductDetail cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param detailCid : Integer
     * @see ProductDetailRepository#findById
     * @see ProductDetailRepository#delete
     */
    public void destroyOne(Integer detailCid) {
        productDetailRepository.findById(detailCid).ifPresent(product -> {
            productDetailRepository.delete(product);
        });
    }
}
