package com.piaar_store_manager.server.model.product.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "Product")
@Accessors(chain = true)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "manufacturing_code")
    private String manufacturingCode;

    @Column(name = "n_product_code")
    private String nProductCode;

    @Column(name = "default_name")
    private String defaultName;

    @Column(name = "management_name")
    private String managementName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "product_category_cid")
    private Integer productCategoryCid;

}
