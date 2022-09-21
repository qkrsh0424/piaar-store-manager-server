package com.piaar_store_manager.server.domain.return_product_image.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Table(name = "return_product_image")
@Builder
@Getter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
 
    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Type(type = "uuid-char")
    @Column(name = "product_option_id")
    private UUID productOptionId;
    
    @Type(type = "uuid-char")
    @Column(name = "erp_return_item_id")
    private UUID erpReturnItemId;
}
