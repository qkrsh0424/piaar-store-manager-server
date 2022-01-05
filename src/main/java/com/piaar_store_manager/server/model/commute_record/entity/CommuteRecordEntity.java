package com.piaar_store_manager.server.model.commute_record.entity;

import com.piaar_store_manager.server.model.commute_record.dto.CommuteRecordGetDto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "commute_record")
public class CommuteRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;
    @Column(name = "id")
    @Type(type="uuid-char")
    private UUID id;
    @Column(name = "user_id")
    @Type(type="uuid-char")
    private UUID userId;
    @Column(name = "work_start_date")
    private Date workStartDate;
    @Column(name = "work_end_date")
    private Date workEndDate;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private Date createdAt;

    public static CommuteRecordEntity toEntity(CommuteRecordGetDto dto) {
        CommuteRecordEntity entity = CommuteRecordEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .userId(dto.getUserId())
                .workStartDate(dto.getWorkStartDate())
                .workEndDate(dto.getWorkEndDate())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .build();
        return entity;
    }
}
