package com.piaar_store_manager.server.model.account_book.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class AccountBookDefDto {
    private UUID id;
    private UUID userId;
    private String accountBookType;
    private String bankType;
    private String desc;
    private Long money;
    private Integer expenditureTypeId;
    private Date regDate;
    private Date createdAt;
    private Date updatedAt;
}
