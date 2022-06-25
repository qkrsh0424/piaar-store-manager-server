package com.piaar_store_manager.server.domain.user_erp_default_header.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.piaar_store_manager.server.domain.user_erp_default_header.dto.UserErpDefaultHeaderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Builder
@Table(name = "user_erp_default_header")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserErpDefaultHeaderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Type(type = "uuid-char")
    @Column(name = "user_id")
    private UUID userId;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "order_header_id")
    private UUID orderHeaderId;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "sales_header_id")
    private UUID salesHeaderId;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "release_complete_header_id")
    private UUID releaseCompleteHeaderId;

    public static UserErpDefaultHeaderEntity toEntity(UserErpDefaultHeaderDto dto) {
        UserErpDefaultHeaderEntity entity = UserErpDefaultHeaderEntity.builder()
            .id(dto.getId())
            .userId(dto.getUserId())
            .orderHeaderId(dto.getOrderHeaderId())
            .salesHeaderId(dto.getSalesHeaderId())
            .releaseCompleteHeaderId(dto.getReleaseCompleteHeaderId())
            .build();
        return entity;
    }
}
