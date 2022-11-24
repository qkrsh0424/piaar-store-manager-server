package com.piaar_store_manager.server.domain.product_option.entity;

import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString
@Table(name = "product_option")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
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

    // @Column(name = "nos_unique_code")
    // private String nosUniqueCode;

    @Setter
    @Column(name = "default_name")
    private String defaultName;

    @Setter
    @Column(name = "management_name")
    private String managementName;

    @Setter
    @Column(name = "sales_price")
    private Integer salesPrice;

    @Setter
    @Column(name = "total_purchase_price")
    private Integer totalPurchasePrice;

    // @Column(name = "stock_unit")
    // private Integer stockUnit;

    @Setter
    @Column(name = "status")
    private String status;

    @Setter
    @Column(name = "memo")
    private String memo;

    @Setter
    @Column(name = "release_location")
    private String releaseLocation;

    // @Column(name = "image_url")
    // private String imageUrl;

    // @Column(name = "image_file_name")
    // private String imageFileName;

    // @Column(name = "color")
    // private String color;

    // @Column(name = "unit_cny")
    // private String unitCny;

    // @Column(name = "unit_krw")
    // private String unitKrw;

    @Setter
    @Column(name = "package_yn", columnDefinition = "n")
    private String packageYn;

    @Setter
    @Column(name = "safety_stock_unit")
    private Integer safetyStockUnit;

    @Column(name = "product_cid")
    private Integer productCid;

    @Type(type = "uuid-char")
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

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
    @Transient
    private Integer receivedSum;
    @Setter
    @Transient
    private Integer releasedSum;
    @Setter
    @Transient
    private Integer stockSumUnit;


    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionGetDto => ProductOptionEntity
     *
     * @param productOptionDto : ProductOptionGetDto
     * @param userId           : UUID
     * @param productCid       : Integer
     * @return ProductOptionEntity
     */
    public static ProductOptionEntity toEntity(ProductOptionGetDto productOptionDto) {
        ProductOptionEntity productOptionEntity = ProductOptionEntity.builder()
                .cid(productOptionDto.getCid())
                .id(productOptionDto.getId())
                .code(productOptionDto.getCode())
                .defaultName(productOptionDto.getDefaultName())
                .managementName(productOptionDto.getManagementName())
                .salesPrice(productOptionDto.getSalesPrice())
                .totalPurchasePrice(productOptionDto.getTotalPurchasePrice())
                .status(productOptionDto.getStatus())
                .memo(productOptionDto.getMemo())
                .releaseLocation(productOptionDto.getReleaseLocation())
                .packageYn(productOptionDto.getPackageYn())
                .safetyStockUnit(productOptionDto.getSafetyStockUnit())
                .createdAt(productOptionDto.getCreatedAt())
                .createdBy(productOptionDto.getCreatedBy())
                .updatedAt(productOptionDto.getUpdatedAt())
                .updatedBy(productOptionDto.getUpdatedBy())
                .productCid(productOptionDto.getProductCid())
                .productId(productOptionDto.getProductId())
                .build();

        return productOptionEntity;
    }

    public static List<ProductOptionEntity> getExistList(List<ErpOrderItemProj> itemProjs) {
        return itemProjs.stream()
                .filter(r -> r.getProductOption() != null)
                .map(ErpOrderItemProj::getProductOption)
                .collect(Collectors.toList());
    }

    public static List<ProductOptionEntity> getExistListByReturnItem(List<ErpReturnItemProj> itemProjs) {
        return itemProjs.stream()
                .filter(r -> r.getProductOption() != null)
                .map(ErpReturnItemProj::getProductOption)
                .collect(Collectors.toList());
    }
}
