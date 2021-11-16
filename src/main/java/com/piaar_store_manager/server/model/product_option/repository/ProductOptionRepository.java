package com.piaar_store_manager.server.model.product_option.repository;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;

import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Integer> {

    @Query(
        "SELECT po AS productOption, p AS product, u AS user, pc AS category FROM ProductOptionEntity po\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN UserEntity u ON po.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid = p.productCategoryCid\n"+
        "WHERE po.cid=:cid"
    )
    Optional<ProductOptionProj> selectByCid(Integer cid);

    // FIX : Added "ORDER BY" query for product_option.created_at ASC
    @Query(
        "SELECT po AS productOption, p AS product, u AS user, pc AS category FROM ProductOptionEntity po\n"+
        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
        "JOIN UserEntity u ON po.createdBy = u.id\n"+
        "JOIN ProductCategoryEntity pc ON pc.cid = p.productCategoryCid\n"+
        "ORDER BY po.createdAt ASC"

    )
    List<ProductOptionProj> selectAll();

    Optional<ProductOptionEntity> findByCode(String code);

    @Query(
        "SELECT po.cid FROM ProductOptionEntity po\n" +
        "WHERE po.code=:code"
    )
    Integer findCidByCode(String code);

    List<ProductOptionEntity> findByProductCid(Integer productCid);

    @Query(
        "SELECT po FROM ProductOptionEntity po\n" +
        "WHERE po.productCid IN :productCids"
    )
    List<ProductOptionEntity> selectAllByProductCids(List<Integer> productCids);
    // @Query(value = "SELECT receive.receive_stock - release.release_stock\n" + 
    //                "FROM (SELECT SUM(rc.receive_unit) AS receive_stock FROM product_receive rc WHERE rc.product_option_cid=:optionCid) receive, \n" +
    //                "(SELECT SUM(rl.release_unit) AS release_stock FROM product_release rl WHERE rl.product_option_cid=:optionCid) release"
    // , nativeQuery = true)
    // Integer findStockStatus(Integer optionCid);

    // @Query(
    //     "SELECT SUM(rc.receiveUnit)-SUM(rl.releaseUnit) FROM (SELECT * FROM ProductReceiveEntity rc WHERE rc.productOptionCid=:optionCid) rc, (SELECT * FROM ProductReleaseEntity rl WHERE rl.productOptionCid=:optionCid) rl"
    // )
    // Integer sumByStockUnit(Integer optionCid);

    // @Query(
    //     "SELECT SUM(rc.receiveUnit) FROM SELECT * (FROM ProductReceiveEntity rc WHERE rc.productOptionCid=:optionCid)"
    // )
    // Integer sumByStockUnit(Integer optionCid);
}
