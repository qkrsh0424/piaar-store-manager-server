package com.piaar_store_manager.server.model.product_detail.repository;

import java.util.List;

import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;
import com.piaar_store_manager.server.model.product_detail.proj.ProductDetailProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Integer> {
    @Query(
        "SELECT pd AS detail FROM ProductDetailEntity pd\n"+
        "WHERE pd.productOptionCid=:cid"
    )
    List<ProductDetailProj> findAllByCid(Integer cid);
}
