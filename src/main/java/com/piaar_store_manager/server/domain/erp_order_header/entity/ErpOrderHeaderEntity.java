package com.piaar_store_manager.server.domain.erp_order_header.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.erp_order_header.dto.ErpOrderHeaderDetailDto;
import com.piaar_store_manager.server.domain.erp_order_header.dto.ErpOrderHeaderDto;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Builder
@Table(name = "erp_order_header")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ErpOrderHeaderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "header_title")
    private String headerTitle;

    @Type(type = "json")
    @Column(name = "header_detail", columnDefinition = "json")
    private ErpOrderHeaderDetailDto headerDetail;

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

    public static ErpOrderHeaderEntity toEntity(ErpOrderHeaderDto dto) {
        if(dto == null) return null;

        ErpOrderHeaderEntity entity = ErpOrderHeaderEntity.builder()
            .cid(dto.getCid())
            .id(dto.getId())
            .headerTitle(dto.getHeaderTitle())
            .headerDetail(dto.getHeaderDetail())
            .createdAt(dto.getCreatedAt())
            .createdBy(dto.getCreatedBy())
            .updatedAt(dto.getUpdatedAt())
            .build();

        return entity;
    }
}
