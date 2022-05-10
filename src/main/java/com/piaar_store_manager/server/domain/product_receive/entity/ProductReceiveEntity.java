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

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "product_receive")
@Data
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
        ProductReceiveEntity entity = new ProductReceiveEntity();

        entity.setId(UUID.randomUUID())
              .setReceiveUnit(dto.getReceiveUnit())
              .setMemo(dto.getMemo())
              .setCreatedAt(dto.getCreatedAt())
              .setCreatedBy(dto.getCreatedBy())
              .setProductOptionCid(dto.getProductOptionCid());

        return entity;
    }
}