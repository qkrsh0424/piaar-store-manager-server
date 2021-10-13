package com.piaar_store_manager.server.model.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import com.piaar_store_manager.server.model.product.dto.ProductGetDto;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
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

    @Column(name = "code")
    private String code;

    @Column(name = "manufacturing_code")
    private String manufacturingCode;

    @Column(name = "naver_product_code")
    private String naverProductCode;

    @Column(name = "default_name")
    private String defaultName;

    @Column(name = "management_name")
    private String managementName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_file_name")
    private String imageFileName;

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

    @Column(name = "product_category_cid")
    private Integer productCategoryCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductGetDto => ProductEntity
     * 
     * @param productDto : ProductGetDto
     * @param userId : UUID
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
            .memo(productDto.getMemo())
            .createdAt(productDto.getCreatedAt())
            .createdBy(productDto.getCreatedBy())
            .updatedAt(productDto.getUpdatedAt())
            .updatedBy(productDto.getUpdatedBy())
            .productCategoryCid(productDto.getProductCategoryCid())
            .build();

        return entity;
    }
}
