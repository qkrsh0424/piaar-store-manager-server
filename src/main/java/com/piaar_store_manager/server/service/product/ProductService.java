package com.piaar_store_manager.server.service.product;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.proj.ProductProj;
import com.piaar_store_manager.server.model.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(
        ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productCid : Integer
     * @return ProductGetDto
     * @see ProductRepository#findById
     */
    public ProductEntity searchOne(Integer productCid) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productCid);

        if (productEntityOpt.isPresent()) {
            return productEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productCid : Integer
     * @return ProductProj
     * @see ProductRepository#selectByCid
     */
    public ProductProj searchProjOne(Integer productCid) {
        Optional<ProductProj> productProjOpt = productRepository.selectByCid(productCid);

        if(productProjOpt.isPresent()) {
            return productProjOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 
     * @return List::ProductEntity::
     * @see ProductRepository#findAll
     */
    public List<ProductEntity> searchList() {
        return productRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Category cid에 대응하는 Product 데이터를 모두 조회한다.
     * 
     * @return List::ProductEntity::
     * @see ProductRepository#findByProductCategoryCid
     */
    public List<ProductEntity> searchListByCategory(Integer categoryCid) {
        return productRepository.findByProductCategoryCid(categoryCid);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductProj::
     * @see ProductRepository#selectAll
     */
    public List<ProductProj> searchProjList(){
        return productRepository.selectAll();
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 한개 등록한다.
     * 
     * @param entity : ProductEntity
     * @see ProductRepository#save
     */
    public ProductEntity createOne(ProductEntity entity) {
        return productRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 여러개 등록한다.
     * 
     * @param entities : List::ProductEntity::
     * @see ProductRepository#saveAll
     */
    public List<ProductEntity> createList(List<ProductEntity> entities) {
        return productRepository.saveAll(entities);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productCid : Integer
     * @see ProductRepository#findById
     * @see ProductRepository#delete
     */
    public void destroyOne(Integer productCid) {
        productRepository.findById(productCid).ifPresent(product -> {
            productRepository.delete(product);
        });
    }
}
