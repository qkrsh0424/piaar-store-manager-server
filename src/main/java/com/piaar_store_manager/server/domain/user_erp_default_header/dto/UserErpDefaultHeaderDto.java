package com.piaar_store_manager.server.domain.user_erp_default_header.dto;

import java.util.UUID;

import com.piaar_store_manager.server.domain.user_erp_default_header.entity.UserErpDefaultHeaderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@Getter
@ToString
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class UserErpDefaultHeaderDto {
    private UUID id;
    @Setter
    private UUID userId;
    @Setter
    private UUID orderHeaderId;
    @Setter
    private UUID salesHeaderId;
    @Setter
    private UUID releaseCompleteHeaderId;

    public static UserErpDefaultHeaderDto toDto(UserErpDefaultHeaderEntity entity) {
        UserErpDefaultHeaderDto dto = UserErpDefaultHeaderDto.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .orderHeaderId(entity.getOrderHeaderId())
            .salesHeaderId(entity.getSalesHeaderId())
            .releaseCompleteHeaderId(entity.getReleaseCompleteHeaderId())
            .build();
        return dto;
    }
}
