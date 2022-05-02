package com.piaar_store_manager.server.domain.product_release.proj;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.user.entity.UserEntity;

/**
 * 자기 자신과 Many To One 관계에 놓여있는 값들과 JOIN
 */
public interface ProductReleaseProj {
    ProductReleaseEntity getProductRelease();
    ProductOptionEntity getProductOption();
    ProductEntity getProduct();
    ProductCategoryEntity getCategory();
    UserEntity getUser();
}
