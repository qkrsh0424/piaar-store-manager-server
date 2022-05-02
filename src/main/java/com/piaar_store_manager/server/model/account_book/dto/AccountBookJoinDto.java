package com.piaar_store_manager.server.model.account_book.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaar_store_manager.server.domain.user.dto.UserDefDto;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;
import com.piaar_store_manager.server.domain.user.entity.UserEntity;
import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.expenditure_type.dto.ExpenditureTypeDto;
import com.piaar_store_manager.server.model.expenditure_type.entity.ExpenditureTypeEntity;

import lombok.Data;

@Data
public class AccountBookJoinDto {
    // private AccountBookDefDto accountBook;
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

    private UserGetDto user;
    private ExpenditureTypeDto expenditureType;

    public AccountBookJoinDto(AccountBookEntity accountBookEntity, UserEntity userEntity,
            ExpenditureTypeEntity expenditureTypeEntity) {
        // this.accountBook = convAccountBookEntityToDto(accountBookEntity);
        this.id=accountBookEntity.getId();
        this.userId=accountBookEntity.getUserId();
        this.accountBookType=accountBookEntity.getAccountBookType();
        this.bankType=accountBookEntity.getBankType();
        this.desc=accountBookEntity.getDesc();
        this.money=accountBookEntity.getMoney();
        this.expenditureTypeId=accountBookEntity.getExpenditureTypeId();
        this.regDate=accountBookEntity.getRegDate();
        this.createdAt=accountBookEntity.getCreatedAt();
        this.updatedAt=accountBookEntity.getUpdatedAt();

        this.user = convUserEntityToDto(userEntity);
        this.expenditureType = convExpenditureEntityToDto(expenditureTypeEntity);
    }

    private AccountBookDefDto convAccountBookEntityToDto(AccountBookEntity entity) {
        AccountBookDefDto dto = new AccountBookDefDto();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setAccountBookType(entity.getAccountBookType());
        dto.setBankType(entity.getBankType());
        dto.setDesc(entity.getDesc());
        dto.setMoney(entity.getMoney());
        dto.setExpenditureTypeId(entity.getExpenditureTypeId());
        dto.setRegDate(entity.getRegDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    private UserGetDto convUserEntityToDto(UserEntity entity) {
        UserGetDto dto = new UserGetDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setRoles(entity.getRoles());
        dto.setUsername(entity.getUsername());

        return dto;
    }

    private ExpenditureTypeDto convExpenditureEntityToDto(ExpenditureTypeEntity entity) {
        ExpenditureTypeDto dto = new ExpenditureTypeDto();

        if (entity != null) {
            dto.setExpenditureTypeId(entity.getExpenditureTypeId());
            dto.setExpenditureType(entity.getExpenditureType());
        }

        return dto;
    }
}
