package com.piaar_store_manager.server.domain.product_detail_page.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.piaar_store_manager.server.domain.product_detail_page.dto.ProductDetailPageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Table(name = "product_detail_page")
@Entity
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailPageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "title")
    private String title;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @Setter
    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Type(type = "uuid-char")
    @Column(name = "product_id")
    private UUID productId;

    public static ProductDetailPageEntity toEntity(ProductDetailPageDto dto) {
        ProductDetailPageEntity entity = ProductDetailPageEntity.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .imageUrl(dto.getImageUrl())
            .imageFileName(dto.getImageFileName())
            .createdAt(dto.getCreatedAt())
            .createdBy(dto.getCreatedBy())
            .productId(dto.getProductId())
            .build();

        return entity;
    }
}
