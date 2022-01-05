package com.piaar_store_manager.server.model.commute_record.dto;

import com.piaar_store_manager.server.model.commute_record.entity.CommuteRecordEntity;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommuteRecordGetDto {
    private Integer cid;
    private UUID id;
    private UUID userId;
    private Date workStartDate;
    private Date workEndDate;
    private String status;
    private Date createdAt;

    public static CommuteRecordGetDto toDto(CommuteRecordEntity entity) {
        if(entity == null){
            return null;
        }
        CommuteRecordGetDto dto = CommuteRecordGetDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .userId(entity.getUserId())
                .workStartDate(entity.getWorkStartDate())
                .workEndDate(entity.getWorkEndDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
        return dto;
    }
}
