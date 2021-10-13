package com.piaar_store_manager.server.model.product_option.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "product_option")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "nos_unique_code")
    private String nosUniqueCode;

    @Column(name = "default_name")
    private String defaultName;

    @Column(name = "management_name")
    private String managementName;

    @Column(name = "sales_price")
    private Integer salesPrice;

    @Column(name = "stock_unit")
    private Integer stockUnit;

    @Column(name = "status")
    private String status;

    @Column(name = "memo")
    private String memo;

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

    @Column(name = "product_cid")
    private Integer productCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionGetDto => ProductOptionEntity
     * 
     * @param productOptionDto : ProductOptionGetDto
     * @param userId : UUID
     * @param productCid : Integer
     * @return ProductOptionEntity
     */
    public static ProductOptionEntity toEntity(ProductOptionGetDto productOptionDto) {
        ProductOptionEntity productOptionEntity = ProductOptionEntity.builder()
                .id(UUID.randomUUID())
                .code(productOptionDto.getCode())
                .nosUniqueCode(productOptionDto.getNosUniqueCode())
                .defaultName(productOptionDto.getDefaultName())
                .managementName(productOptionDto.getManagementName())
                .salesPrice(productOptionDto.getSalesPrice())
                .stockUnit(productOptionDto.getStockUnit())
                .status(productOptionDto.getStatus())
                .memo(productOptionDto.getMemo())
                .createdAt(productOptionDto.getCreatedAt())
                .createdBy(productOptionDto.getCreatedBy())
                .updatedAt(productOptionDto.getUpdatedAt())
                .updatedBy(productOptionDto.getUpdatedBy())
                .productCid(productOptionDto.getProductCid())
                .build();

        return productOptionEntity;
    }
}
