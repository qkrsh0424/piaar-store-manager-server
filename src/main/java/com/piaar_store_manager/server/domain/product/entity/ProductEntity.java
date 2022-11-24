package com.piaar_store_manager.server.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@ToString
@Builder
@Table(name = "product")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
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

    @Setter
    @Column(name = "default_name")
    private String defaultName;

    @Setter
    @Column(name = "management_name")
    private String managementName;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @Setter
    @Column(name = "image_file_name")
    private String imageFileName;

    @Setter
    @Column(name = "purchase_url")
    private String purchaseUrl;

    @Setter
    @Column(name = "memo")
    private String memo;

    @Setter
    @Column(name = "stock_management")
    private Boolean stockManagement;
    
    @Setter
    @Column(name = "product_category_cid")
    private Integer productCategoryCid;

    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Setter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "updated_by")
    private UUID updatedBy;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductGetDto => ProductEntity
     * 
     * @param productDto : ProductGetDto
     * @return ProductEntity
     */
    public static ProductEntity toEntity(ProductGetDto productDto) {
        ProductEntity entity = ProductEntity.builder()
            .id(UUID.randomUUID())
            .code(productDto.getCode())
            .defaultName(productDto.getDefaultName())
            .managementName(productDto.getManagementName())
            .imageUrl(productDto.getImageUrl())
            .imageFileName(productDto.getImageFileName())
            .purchaseUrl(productDto.getPurchaseUrl())
            .memo(productDto.getMemo())
            .stockManagement(productDto.getStockManagement())
            .productCategoryCid(productDto.getProductCategoryCid())
            .createdAt(productDto.getCreatedAt())
            .createdBy(productDto.getCreatedBy())
            .updatedAt(productDto.getUpdatedAt())
            .updatedBy(productDto.getUpdatedBy())
            .build();

        return entity;
    }
}
