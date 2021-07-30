package com.piaar_store_manager.server.model.product.proj;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.model.user.entity.UserEntity;

/**
 * 자기 자신과 Many To One 관계에 놓여있는 값들과 JOIN
 */
public interface ProductProj {
    ProductEntity getProduct();
    ProductCategoryEntity getCategory();
    UserEntity getUser();
}
