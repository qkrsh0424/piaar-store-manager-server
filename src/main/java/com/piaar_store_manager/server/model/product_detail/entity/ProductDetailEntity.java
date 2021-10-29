package com.piaar_store_manager.server.model.product_detail.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.model.product_detail.dto.ProductDetailGetDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "product_detail")
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "detail_width")
    private Integer detailWidth;

    @Column(name = "detail_length")
    private Integer detailLength;

    @Column(name = "detail_height")
    private Integer detailHeight;

    @Column(name = "detail_quantity")
    private Integer detailQuantity;

    @Column(name = "detail_Weight")
    private Integer detailWeight;

    @Column(name = "detail_cbm")
    private String detailCbm;

    @Column(name = "created_at")
    private Date createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Type(type = "uuid-char")
    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "product_option_cid")
    private Integer productOptionCid;

    public static ProductDetailEntity toEntity(ProductDetailGetDto dto) {
        ProductDetailEntity entity = ProductDetailEntity.builder()
            .id(UUID.randomUUID())
            .detailWidth(dto.getDetailWidth())
            .detailLength(dto.getDetailLength())
            .detailHeight(dto.getDetailHeight())
            .detailQuantity(dto.getDetailQuantity())
            .detailWeight(dto.getDetailWeight())
            .detailCbm(dto.getDetailCbm())
            .createdAt(dto.getCreatedAt())
            .createdBy(dto.getCreatedBy())
            .updatedAt(dto.getUpdatedAt())
            .updatedBy(dto.getUpdatedBy())
            .productOptionCid(dto.getProductOptionCid())
            .build();

        return entity;
    }
}
