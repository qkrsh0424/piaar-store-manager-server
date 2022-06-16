package com.piaar_store_manager.server.domain.product_detail.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.product_detail.dto.ProductDetailGetDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter @Setter
@Table(name = "product_detail")
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private Float detailCbm;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
