package com.piaar_store_manager.server.model.option_package.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import com.piaar_store_manager.server.model.option_package.dto.OptionPackageDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "option_package")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionPackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "package_unit")
    private Integer packageUnit;

    @Column(name = "origin_option_code")
    private String originOptionCode;

    @Column(name = "origin_option_id")
    private String originOptionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Type(type = "uuid-char")
    @Column(name = "updated_by")
    private UUID updatedBy;

    @Type(type = "uuid-char")
    @Column(name = "parent_option_id")
    private UUID parentOptionId;

    public static OptionPackageEntity toEntity(OptionPackageDto dto) {
        OptionPackageEntity entity = OptionPackageEntity.builder()
            .id(dto.getId())
            .packageUnit(dto.getPackageUnit())
            .originOptionCode(dto.getOriginOptionCode())
            .originOptionId(dto.getOriginOptionId())
            .createdAt(dto.getCreatedAt())
            .createdBy(dto.getCreatedBy())
            .updatedAt(dto.getUpdatedAt())
            .updatedBy(dto.getUpdatedBy())
            .parentOptionId(dto.getParentOptionId())
            .build();

        return entity;
    }
}
