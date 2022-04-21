package com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.entity.DeliveryReadyPiaarViewHeaderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReadyPiaarViewHeaderDto {
    private Integer cid;
    private UUID id;
    private PiaarExcelViewHeaderDetailDto viewHeaderDetail = new PiaarExcelViewHeaderDetailDto();

    @Setter
    private LocalDateTime createdAt;

    @Setter
    private UUID createdBy;

    @Setter
    private LocalDateTime updatedAt;

    public static DeliveryReadyPiaarViewHeaderDto toDto(DeliveryReadyPiaarViewHeaderEntity entity) {
        DeliveryReadyPiaarViewHeaderDto dto = DeliveryReadyPiaarViewHeaderDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .viewHeaderDetail(entity.getViewHeaderDetail())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .build();

        return dto;
    }
}
