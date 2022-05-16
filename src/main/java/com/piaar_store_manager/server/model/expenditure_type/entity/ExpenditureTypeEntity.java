package com.piaar_store_manager.server.model.expenditure_type.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expenditure_type")
public class ExpenditureTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenditure_type_id")
    private Integer expenditureTypeId;

    @Column(name = "expenditure_type")
    private String expenditureType;

    @Column(name = "expenditure_type_desc")
    private String expenditureTypeDesc;
}
