package com.piaar_store_manager.server.domain.return_type.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Entity
@Table(name = "return_reason_type")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReturnReasonTypeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;
}
