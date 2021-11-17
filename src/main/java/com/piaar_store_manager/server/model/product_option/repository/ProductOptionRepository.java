package com.piaar_store_manager.server.model.product_option.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

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

    /**
     * The sum of the number of receive unit and the number of release unit of product options.
     * Change the value of the ProductOptionGetDto to the returned value.
     * 
     * @param optionCids : List::Integer::
     * @return po.cid, sum(receive_unit), sum(release_unit)
     */
    @Query(value="SELECT po.cid AS cid, \n" +
                "(SELECT SUM(prl.release_unit) FROM product_release prl WHERE po.cid=prl.product_option_cid) AS releasedSum, \n" + 
                "(SELECT SUM(prc.receive_unit) FROM product_receive prc WHERE po.cid=prc.product_option_cid) AS receivedSum \n" +
                "FROM product_option po WHERE po.cid IN :optionCids", nativeQuery = true)
    List<Tuple> sumStockUnitByOption(List<Integer> optionCids);
}
