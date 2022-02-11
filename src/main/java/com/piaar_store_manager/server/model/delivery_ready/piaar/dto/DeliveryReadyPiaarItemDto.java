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
    private String soldYn;
    private LocalDateTime soldAt;
    private String releasedYn;
    private LocalDateTime releasedAt;
    private String stockReflectedYn;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer deliveryReadyFileCid;

    public static DeliveryReadyPiaarItemDto toDto(DeliveryReadyPiaarItemEntity entity) {
        DeliveryReadyPiaarItemDto dto = DeliveryReadyPiaarItemDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .uploadDetail(entity.getUploadDetail())
                .soldYn(entity.getSoldYn())
                .soldAt(entity.getSoldAt())
                .releasedYn(entity.getReleasedYn())
                .releasedAt(entity.getReleasedAt())
                .stockReflectedYn(entity.getStockReflectedYn())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .deliveryReadyFileCid(entity.getDeliveryReadyFileCid())
                .build();

        return dto;
    }
}
