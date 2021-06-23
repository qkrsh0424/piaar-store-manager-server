package com.piaar_store_manager.server.model.account_book.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.user.dto.UserDefDto;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.model.user.entity.UserEntity;

import lombok.Data;

@Data
public class AccountBookJoinDto {
    private AccountBookDefDto accountBook;
    private UserGetDto user;

    public AccountBookJoinDto(AccountBookEntity accountBookEntity, UserEntity userEntity){
        this.accountBook = convAccountBookEntityToDto(accountBookEntity);
        this.user = convUserEntityToDto(userEntity);
    }

    private AccountBookDefDto convAccountBookEntityToDto(AccountBookEntity entity){
        AccountBookDefDto dto = new AccountBookDefDto();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setAccountBookType(entity.getAccountBookType());
        dto.setBankType(entity.getBankType());
        dto.setDesc(entity.getDesc());
        dto.setMoney(entity.getMoney());
        dto.setRegDate(entity.getRegDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    private UserGetDto convUserEntityToDto(UserEntity entity){
        UserGetDto dto = new UserGetDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setRoles(entity.getRoles());
        dto.setUsername(entity.getUsername());

        return dto;
    }
}
