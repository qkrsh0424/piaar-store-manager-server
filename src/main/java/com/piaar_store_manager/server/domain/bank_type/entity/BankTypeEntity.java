package com.piaar_store_manager.server.domain.bank_type.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bank_type")
public class BankTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_type_id")
    private Integer bankTypeId;

    @Column(name = "bank_type")
    private String bankType;
}
