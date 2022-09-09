package com.piaar_store_manager.server.domain.return_type.dto;

import com.piaar_store_manager.server.domain.return_type.entity.ReturnReasonTypeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnReasonTypeDto {
    private Integer cid;
    private String type;
    private String description;

    public static ReturnReasonTypeDto toDto(ReturnReasonTypeEntity entity) {
        ReturnReasonTypeDto dto = ReturnReasonTypeDto.builder()
            .cid(entity.getCid())
            .type(entity.getType())
            .description(entity.getDescription())
            .build();

        return dto;
    }
}
