package com.piaar_store_manager.server.domain.sub_option_code.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Entity
@Table(name = "sub_option_code")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubOptionCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "sub_option_code")
    private String subOptionCode;

    @Column(name = "memo")
    private String memo;

    @Type(type = "uuid-char")
    @Column(name = "product_option_id")
    private UUID productOptionId;

    // @Column(name = "product_option_code")
    // private String productOptionCode;

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
}
