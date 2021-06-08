package com.piaar_store_manager.server.service.account_book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.account_book.dto.AccountBookDefDto;
import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.account_book.repository.AccountBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBookService {
    @Autowired
    AccountBookRepository accountBookRepository;

    @Autowired
    DateHandler dateHandler; 

    public void createList(List<AccountBookDefDto> accountBookDefDtos) {
        List<AccountBookEntity> entities = getEntitiesByDtos(accountBookDefDtos);
        System.out.println(entities);
        accountBookRepository.saveAll(entities);
    }

    private AccountBookEntity getEntityByDto(AccountBookDefDto dto) {
        AccountBookEntity entity = new AccountBookEntity();
        entity.setId(UUID.randomUUID());
        entity.setAccountBookType(dto.getAccountBookType());
        entity.setBankType(dto.getBankType());
        entity.setDesc(dto.getDesc());
        entity.setMoney(dto.getMoney());
        entity.setRegDate(dto.getRegDate());
        entity.setCreatedAt(dateHandler.getCurrentDate());
        entity.setUpdatedAt(dateHandler.getCurrentDate());
        return entity;
    }

    private List<AccountBookEntity> getEntitiesByDtos(List<AccountBookDefDto> dtos) {
        List<AccountBookEntity> entities = new ArrayList<>();
        for (AccountBookDefDto dto : dtos) {
            AccountBookEntity entity = new AccountBookEntity();
            entity.setId(UUID.randomUUID());
            entity.setAccountBookType(dto.getAccountBookType());
            entity.setBankType(dto.getBankType());
            entity.setDesc(dto.getDesc());
            entity.setMoney(dto.getMoney());
            entity.setRegDate(dto.getRegDate());
            entity.setCreatedAt(dateHandler.getCurrentDate());
            entity.setUpdatedAt(dateHandler.getCurrentDate());
            entities.add(entity);
        }
        return entities;
    }
}
