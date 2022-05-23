package com.piaar_store_manager.server.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@ToString
@Table(name = "product")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "code")
    private String code;

    @Setter
    @Column(name = "manufacturing_code")
    private String manufacturingCode;
    
    @Setter
    @Column(name = "naver_product_code")
    private String naverProductCode;

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
    @Column(name = "hs_code")
    private String hsCode;

    @Setter
    @Column(name = "style")
    private String style;

    @Setter
    @Column(name = "tariff_rate")
    private String tariffRate;

    @Setter
    @Column(name = "default_width")
    private Integer defaultWidth;

    @Setter
    @Column(name = "default_length")
    private Integer defaultLength;

    @Setter
    @Column(name = "default_height")
    private Integer defaultHeight;

    @Setter
    @Column(name = "default_quantity")
    private Integer defaultQuantity;

    @Setter
    @Column(name = "default_weight")
    private Integer defaultWeight;

    @Setter
    @Column(name = "default_total_purchase_price")
    private Integer defaultTotalPurchasePrice;

    @Setter
    @Column(name = "stock_management")
    private Boolean stockManagement;

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

    @Setter
    @Column(name = "product_category_cid")
    private Integer productCategoryCid;

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
            .manufacturingCode(productDto.getManufacturingCode())
            .naverProductCode(productDto.getNaverProductCode())
            .defaultName(productDto.getDefaultName())
            .managementName(productDto.getManagementName())
            .imageUrl(productDto.getImageUrl())
            .imageFileName(productDto.getImageFileName())
            .purchaseUrl(productDto.getPurchaseUrl())
            .memo(productDto.getMemo())
            .hsCode(productDto.getHsCode())
            .tariffRate(productDto.getTariffRate())
            .style(productDto.getStyle())
            .tariffRate(productDto.getTariffRate())
            .defaultWidth(productDto.getDefaultWidth())
            .defaultLength(productDto.getDefaultLength())
            .defaultHeight(productDto.getDefaultHeight())
            .defaultQuantity(productDto.getDefaultQuantity())
            .defaultWeight(productDto.getDefaultWeight())
            .defaultTotalPurchasePrice(productDto.getDefaultTotalPurchasePrice())
            .stockManagement(productDto.getStockManagement())
            .createdAt(productDto.getCreatedAt())
            .createdBy(productDto.getCreatedBy())
            .updatedAt(productDto.getUpdatedAt())
            .updatedBy(productDto.getUpdatedBy())
            .productCategoryCid(productDto.getProductCategoryCid())
            .build();

        return entity;
    }
}
