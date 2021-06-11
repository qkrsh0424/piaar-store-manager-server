package com.piaar_store_manager.server.service.account_book;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.account_book.dto.AccountBookDefDto;
import com.piaar_store_manager.server.model.account_book.dto.AccountBookJUserDto;
import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.account_book.proj.AccountBookJUserProj;
import com.piaar_store_manager.server.model.account_book.repository.AccountBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountBookService {
    @Autowired
    AccountBookRepository accountBookRepository;

    @Autowired
    DateHandler dateHandler; 

    public void createList(List<AccountBookDefDto> accountBookDefDtos, UUID userId) {
        List<AccountBookEntity> entities = getEntitiesByDtos(accountBookDefDtos, userId);
        accountBookRepository.saveAll(entities);
    }

    private AccountBookEntity getEntityByDto(AccountBookDefDto dto, UUID userId) {
        AccountBookEntity entity = new AccountBookEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId(userId);
        entity.setAccountBookType(dto.getAccountBookType());
        entity.setBankType(dto.getBankType());
        entity.setDesc(dto.getDesc());
        entity.setMoney(dto.getMoney());
        entity.setRegDate(dto.getRegDate());
        entity.setCreatedAt(dateHandler.getCurrentDate());
        entity.setUpdatedAt(dateHandler.getCurrentDate());
        return entity;
    }

    private List<AccountBookEntity> getEntitiesByDtos(List<AccountBookDefDto> dtos, UUID userId) {
        List<AccountBookEntity> entities = new ArrayList<>();
        for (AccountBookDefDto dto : dtos) {
            AccountBookEntity entity = new AccountBookEntity();
            entity.setId(UUID.randomUUID());
            entity.setUserId(userId);
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

    public List<AccountBookJUserDto> searchList(Map<String, Object> query){
        String accountBookType = query.get("accountBookType") != null ? query.get("accountBookType").toString() : "";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null? new Date(query.get("startDate").toString()) : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();

        List<AccountBookJUserDto> dtos = getJUserDtosByProj(accountBookRepository.selectListJUserByCond(accountBookType, bankType, startDate, endDate));
        return dtos;
    }

    private List<AccountBookJUserDto> getJUserDtosByProj(List<AccountBookJUserProj> projs){
        List<AccountBookJUserDto> dtos = new ArrayList<>();
        for(AccountBookJUserProj proj: projs){
            dtos.add(new AccountBookJUserDto(proj.getAccountBook(), proj.getUser()));
        }
        return dtos;
    }
}
