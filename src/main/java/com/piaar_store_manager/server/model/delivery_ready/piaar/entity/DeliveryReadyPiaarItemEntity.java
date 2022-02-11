package com.piaar_store_manager.server.model.delivery_ready.piaar.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.PiaarUploadDetailDto;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Table(name = "delivery_ready_piaar_item")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class DeliveryReadyPiaarItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Type(type = "json")
    @Column(name = "upload_detail", columnDefinition = "json")
    private PiaarUploadDetailDto uploadDetail = new PiaarUploadDetailDto();

    @Column(name = "sold_yn", columnDefinition = "n")
    private String soldYn;

    @Column(name = "sold_at")
    private LocalDateTime soldAt;

    @Column(name = "released_yn", columnDefinition = "n")
    private String releasedYn;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "stock_reflected_yn", columnDefinition = "n")
    private String stockReflectedYn;

    @Column(name = "delivery_ready_file_cid")
    private Integer deliveryReadyFileCid;

    public static DeliveryReadyPiaarItemEntity toEntity(DeliveryReadyPiaarItemDto dto) {
        DeliveryReadyPiaarItemEntity entity = DeliveryReadyPiaarItemEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .uploadDetail(dto.getUploadDetail())
                .soldYn(dto.getSoldYn())
                .soldAt(dto.getSoldAt())
                .releasedYn(dto.getReleasedYn())
                .releasedAt(dto.getReleasedAt())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .stockReflectedYn(dto.getStockReflectedYn())
                .deliveryReadyFileCid(dto.getDeliveryReadyFileCid())
                .build();

        return entity;
    }
}
