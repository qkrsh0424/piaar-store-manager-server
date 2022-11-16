package com.piaar_store_manager.server.domain.product_category.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "product_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name ="code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    @Type(type = "uuid-char")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by")
    @Type(type = "uuid-char")
    private UUID updatedBy;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductCategoryGetDto => ProductCategoryEntity
     * 
     * @param reqDto : ProductCategoryGetDto
     * @return ProductCategoryEntity
     */
    public static ProductCategoryEntity toEntity(ProductCategoryGetDto reqDto) {
        ProductCategoryEntity entity = ProductCategoryEntity.builder()
            .id(reqDto.getId())
            .code(reqDto.getCode())
            .name(reqDto.getName())
            .createdAt(reqDto.getCreatedAt())
            .createdBy(reqDto.getCreatedBy())
            .updatedAt(reqDto.getUpdatedAt())
            .updatedBy(reqDto.getUpdatedBy())
            .build();

        return entity;
    }
}
