package com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto.DeliveryReadyPiaarViewHeaderDto;
import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto.PiaarExcelViewHeaderDetailDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Table(name = "delivery_ready_piaar_view_header")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReadyPiaarViewHeaderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Type(type = "json")
    @Column(name = "view_header_detail", columnDefinition = "json")
    private PiaarExcelViewHeaderDetailDto viewHeaderDetail = new PiaarExcelViewHeaderDetailDto();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static DeliveryReadyPiaarViewHeaderEntity toEntity(DeliveryReadyPiaarViewHeaderDto dto) {
        DeliveryReadyPiaarViewHeaderEntity entity = DeliveryReadyPiaarViewHeaderEntity.builder()
            .cid(dto.getCid())
            .id(dto.getId())
            .viewHeaderDetail(dto.getViewHeaderDetail())
            .createdAt(dto.getCreatedAt())
            .createdBy(dto.getCreatedBy())
            .updatedAt(dto.getUpdatedAt())
            .build();

        return entity;
    }
}
