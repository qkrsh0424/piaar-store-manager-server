package com.piaar_store_manager.server.domain.product_option.entity;

import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Column(name = "total_purchase_price")
    private Integer totalPurchasePrice;

    @Column(name = "stock_unit")
    private Integer stockUnit;

    @Column(name = "status")
    private String status;

    @Column(name = "memo")
    private String memo;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "color")
    private String color;

    @Column(name = "unit_cny")
    private String unitCny;

    @Column(name = "unit_krw")
    private String unitKrw;

    @Column(name = "package_yn", columnDefinition = "n")
    private String packageYn;

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

    @Transient
    private Integer receivedSum;
    @Transient
    private Integer releasedSum;
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
                // .id(UUID.randomUUID())
                .id(productOptionDto.getId())
                .code(productOptionDto.getCode())
                .nosUniqueCode(productOptionDto.getNosUniqueCode())
                .defaultName(productOptionDto.getDefaultName())
                .managementName(productOptionDto.getManagementName())
                .salesPrice(productOptionDto.getSalesPrice())
                .totalPurchasePrice(productOptionDto.getTotalPurchasePrice())
                .stockUnit(productOptionDto.getStockUnit())
                .status(productOptionDto.getStatus())
                .memo(productOptionDto.getMemo())
                .imageUrl(productOptionDto.getImageUrl())
                .imageFileName(productOptionDto.getImageFileName())
                .color(productOptionDto.getColor())
                .unitCny(productOptionDto.getUnitCny())
                .unitKrw(productOptionDto.getUnitKrw())
                .packageYn(productOptionDto.getPackageYn())
                .createdAt(productOptionDto.getCreatedAt())
                .createdBy(productOptionDto.getCreatedBy())
                .updatedAt(productOptionDto.getUpdatedAt())
                .updatedBy(productOptionDto.getUpdatedBy())
                .productCid(productOptionDto.getProductCid())
                .build();

        return productOptionEntity;
    }

    public static List<ProductOptionEntity> getExistList(List<ErpOrderItemProj> itemProjs) {
        return itemProjs.stream()
                .filter(r -> r.getProductOption() != null)
                .map(ErpOrderItemProj::getProductOption)
                .collect(Collectors.toList());
    }
}
