package com.piaar_store_manager.server.model.product_release.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "product_release")
@Data
@Accessors(chain = true)
public class ProductReleaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "release_unit")
    private Integer releaseUnit;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at")
    private Date createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "product_option_cid")
    private Integer productOptionCid;
}
