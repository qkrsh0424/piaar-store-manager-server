package com.piaar_store_manager.server.domain.product_receive.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "product_receive")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductReceiveEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "receive_unit")
    private Integer receiveUnit;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "product_option_cid")
    private Integer productOptionCid;

    @Type(type = "uuid-char")
    @Column(name = "product_option_id")
    private UUID productOptionId;

    @Type(type ="uuid-char")
    @Column(name = "erp_order_item_id")
    private UUID erpOrderItemId;


    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReceiveGetDto => ProductReceiveEntity
     * 
     * @param dto : ProductReceiveGetDto
     * @param userId : UUID
     * @return ProductReceiveEntity
     */
    public static ProductReceiveEntity toEntity(ProductReceiveGetDto dto) {
        ProductReceiveEntity entity = ProductReceiveEntity.builder()
            .id(UUID.randomUUID())
            .receiveUnit(dto.getReceiveUnit())
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