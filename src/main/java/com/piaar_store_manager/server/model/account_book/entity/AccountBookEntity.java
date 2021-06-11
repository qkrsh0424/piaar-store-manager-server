package com.piaar_store_manager.server.model.account_book.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "account_book")
public class AccountBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_book_pk")
    private Long accountBookPk;
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;
    @Type(type = "uuid-char")
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "account_book_type")
    private String accountBookType;
    @Column(name = "bank_type")
    private String bankType;
    @Column(name = "\"desc\"")
    private String desc;
    @Column(name = "money")
    private Long money;
    @Column(name = "reg_date")
    private Date regDate;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
}
