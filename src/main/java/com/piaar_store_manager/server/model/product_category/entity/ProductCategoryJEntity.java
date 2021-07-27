package com.piaar_store_manager.server.model.product_category.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

import javax.persistence.*;

import com.piaar_store_manager.server.model.product.entity.ProductJEntity;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "product_category")
@ToString(exclude = {"productList"})
public class ProductCategoryJEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productCategory")
    private List<ProductJEntity> productList;
}
