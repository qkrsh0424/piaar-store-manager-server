package com.piaar_store_manager.server.service.account_book;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.account_book.dto.AccountBookDefDto;
import com.piaar_store_manager.server.model.account_book.dto.AccountBookJUserDto;
import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.account_book.proj.AccountBookJUserProj;
import com.piaar_store_manager.server.model.account_book.repository.AccountBookRepository;
import com.piaar_store_manager.server.model.pagenation.PagenationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountBookService {
    private final Integer DEFAULT_ITEM_PER_PAGE = 30;
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
        entity.setDeleted(0);
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
            entity.setDeleted(0);
            entities.add(entity);
        }
        return entities;
    }

    public List<AccountBookJUserDto> searchList(Map<String, Object> query) {
        String accountBookType = query.get("accountBookType") != null ? query.get("accountBookType").toString() : "";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();
        
        Integer currPageParsed = query.get("currPage") != null ? Integer.parseInt(query.get("currPage").toString()) : 1;
        Integer currPage = (currPageParsed == null || (currPageParsed <1)) ? 0 : (currPageParsed - 1);

        Pageable pageable = PageRequest.of(currPage, DEFAULT_ITEM_PER_PAGE);
        List<AccountBookJUserDto> dtos = getJUserDtosByProj(
                accountBookRepository.selectListJUserByCond(accountBookType, bankType, startDate, endDate, pageable));
        return dtos;
    }

    public PagenationDto searchPagenation(Map<String, Object> query) {
        String accountBookType = query.get("accountBookType") != null ? query.get("accountBookType").toString() : "";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();
        Integer currPageParsed = query.get("currPage") != null ? Integer.parseInt(query.get("currPage").toString()) : 1;
        
        Integer itemSize = accountBookRepository.sizeJUserByCond(accountBookType, bankType, startDate, endDate);
        Integer itemPerPage = DEFAULT_ITEM_PER_PAGE;
        Integer pageSize = (int) Math.ceil(((double) itemSize) / itemPerPage);

        Integer currPage = (currPageParsed==null || currPageParsed <1) ? 1 : (currPageParsed >pageSize) ? pageSize : (currPageParsed);
        Integer nextPage = currPage >= (int) Math.ceil(((double) itemSize) / itemPerPage) ? currPage : currPage+1;
        Integer prevPage = currPage <= 1 ? 1 : currPage - 1;

        Integer firstPage = 1;
        Integer lastPage = pageSize;

        Integer pageGroupIdx = currPage <=10 ? 0 : ((currPage-1) / 10);

        PagenationDto pagenation = new PagenationDto();
        pagenation.setItemSize(itemSize);
        pagenation.setItemPerPage(itemPerPage);
        pagenation.setPageSize(pageSize);
        pagenation.setCurrPage(currPage);
        pagenation.setPrevPage(prevPage);
        pagenation.setNextPage(nextPage);
        pagenation.setFirstPage(firstPage);
        pagenation.setLastPage(lastPage);
        pagenation.setPageGroupIdx(pageGroupIdx);

        // System.out.println("================log=================");
        // log.info("AccountBookService : searchPagenation : itemSize => {}.", itemSize);
        // log.info("AccountBookService : searchPagenation : itemPerPage => {}.", itemPerPage);
        // log.info("AccountBookService : searchPagenation : pageSize => {}.", pageSize);
        // log.info("AccountBookService : searchPagenation : currPage => {}.", currPage);
        // log.info("AccountBookService : searchPagenation : nextPage => {}.", nextPage);
        // log.info("AccountBookService : searchPagenation : prevPage => {}.", prevPage);
        // log.info("AccountBookService : searchPagenation : firstPage => {}.", firstPage);
        // log.info("AccountBookService : searchPagenation : lastPage => {}.", lastPage);
        // log.info("AccountBookService : searchPagenation : pageGroupIdx => {}.", pageGroupIdx);
        // log.info("AccountBookService : searchPagenation : pagenation => {}.", pagenation);
        // System.out.println("================log=================");
        return pagenation;
    }

    private List<AccountBookJUserDto> getJUserDtosByProj(List<AccountBookJUserProj> projs) {
        List<AccountBookJUserDto> dtos = new ArrayList<>();
        for (AccountBookJUserProj proj : projs) {
            dtos.add(new AccountBookJUserDto(proj.getAccountBook(), proj.getUser()));
        }
        return dtos;
    }

    public Map<String, Object> getSumIncome(Map<String, Object> query) {
        if (query.get("accountBookType") != null && query.get("accountBookType").equals("expenditure")) {
            Map<String, Object> map = new HashMap<>();
            map.put("sum", 0);
            map.put("itemSize", 0);
            return map;
        }

        String accountBookType = "income";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();

        return accountBookRepository.sumIncomeOrExpenditureCond(accountBookType, bankType, startDate, endDate);
    }

    public Map<String, Object> getSumExpenditure(Map<String, Object> query) {
        if (query.get("accountBookType") != null && query.get("accountBookType").equals("income")) {
            Map<String, Object> map = new HashMap<>();
            map.put("sum", 0);
            map.put("itemSize", 0);
            return map;
        }

        String accountBookType = "expenditure";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();

        return accountBookRepository.sumIncomeOrExpenditureCond(accountBookType, bankType, startDate, endDate);
    }

    public void destroyOne(UUID id) {
        accountBookRepository.findById(id).ifPresent(r->{
            r.setDeleted(1);
            accountBookRepository.save(r);
        });
    }
}
