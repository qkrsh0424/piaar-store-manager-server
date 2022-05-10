package com.piaar_store_manager.server.domain.product_release.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "product_release")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "product_option_cid")
    private Integer productOptionCid;

    @Column(name = "product_option_id")
    @Type(type ="uuid-char")
    private UUID productOptionId;

    @Column(name = "erp_order_item_id")
    @Type(type ="uuid-char")
    private UUID erpOrderItemId;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseGetDto => ProductReleaseEntity
     * 
     * @param dto : ProductReleaseGetDto
     * @param userId : UUID
     * @return ProductReleaseEntity
     */
    public static ProductReleaseEntity toEntity(ProductReleaseGetDto dto) {
        ProductReleaseEntity entity = ProductReleaseEntity.builder()
              .id(UUID.randomUUID())
              .releaseUnit(dto.getReleaseUnit())
              .memo(dto.getMemo())
              .createdAt(dto.getCreatedAt())
              .createdBy(dto.getCreatedBy())
              .productOptionCid(dto.getProductOptionCid())
              .productOptionId(dto.getProductOptionId())
              .erpOrderItemId(dto.getErpOrderItemId())
              .build();

        return entity;
    }
}
