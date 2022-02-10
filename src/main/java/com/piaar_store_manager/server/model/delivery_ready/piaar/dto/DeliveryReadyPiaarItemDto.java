package com.piaar_store_manager.server.model.delivery_ready.piaar.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReadyPiaarItemDto {
    private Integer cid;
    private UUID id;
    private PiaarUploadDetailDto uploadDetail = new PiaarUploadDetailDto();
    private Boolean sold;
    private LocalDateTime soldAt;
    private Boolean released;
    private LocalDateTime releasedAt;
    private Boolean stockReflected;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer deliveryReadyFileCid;

    public static DeliveryReadyPiaarItemDto toDto(DeliveryReadyPiaarItemEntity entity) {
        DeliveryReadyPiaarItemDto dto = DeliveryReadyPiaarItemDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .uploadDetail(entity.getUploadDetail())
                .sold(entity.getSold())
                .soldAt(entity.getSoldAt())
                .released(entity.getReleased())
                .releasedAt(entity.getReleasedAt())
                .stockReflected(entity.getStockReflected())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .deliveryReadyFileCid(entity.getDeliveryReadyFileCid())
                .build();

        return dto;
    }
}
