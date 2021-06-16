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

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * AccountBook 내용을 여러개 등록한다. 
     * @param accountBookDefDtos
     * @param userId
     * @see AccountBookRepository
     */
    public void createList(List<AccountBookDefDto> accountBookDefDtos, UUID userId) {
        List<AccountBookEntity> entities = convEntitiesByDtos(accountBookDefDtos, userId);
        accountBookRepository.saveAll(entities);
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * AccountBookDefDto => AccountBookEntity
     * @param dto
     * @param userId
     * @return AccountBookEntity
     */
    private AccountBookEntity convEntityByDto(AccountBookDefDto dto, UUID userId) {
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

    /**
     * <b>Convert Method</b>
     * <p>
     * List::AccountBookDefDto:: => List::AccountBookEntity::
     * @param dtos
     * @param userId
     * @return List::AccountBookEntity::
     */
    private List<AccountBookEntity> convEntitiesByDtos(List<AccountBookDefDto> dtos, UUID userId) {
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

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 입/지출 건들을 조건에 맞게 조회한다. 
     * 
     * @param query : Map[accountBookType, bankType, startDate, endDate, currPage]
     * @return List::AccountBookJUserDto::
     * @see AccountBookRepository
     */
    public List<AccountBookJUserDto> searchList(Map<String, Object> query) {
        String accountBookType = query.get("accountBookType") != null ? query.get("accountBookType").toString() : "";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);

        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString()) : startDateCalendar.getTime(); /* 지정된 startDate 값이 있다면 해당 데이터로 조회, 없다면 1970년을 기준으로 조회 */
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date(); /* 지정된 endDate 값이 있다면 해당 데이터로 조회, 없다면 현재시간을 기준으로 조회 */
        
        Integer currPageParsed = query.get("currPage") != null ? Integer.parseInt(query.get("currPage").toString()) : 1;
        Integer currPage = (currPageParsed == null || (currPageParsed <1)) ? 0 : (currPageParsed - 1);

        Pageable pageable = PageRequest.of(currPage, DEFAULT_ITEM_PER_PAGE);
        List<AccountBookJUserDto> dtos = convJUserDtosByProj(
                accountBookRepository.selectListJUserByCond(accountBookType, bankType, startDate, endDate, pageable));
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * AccountBook 데이터의 페이지네이션 데이터를 얻는다.
     * @param query : Map[accountBookType, bankType, startDate, endDate, currPage]
     * @return PagenationDto
     * @see AccountBookRepository
     */
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

    /**
     * <b>Convert Method</b>
     * <p>
     * (AccountBook Join User) 프로젝션 리스트를 dto 리스트 형태로 변환
     * List::AccountBookJUserProj:: => List::AccountBookJUserDto::
     * @param projs
     * @return List ::AccountBookJUserDto::
     * 
     */
    private List<AccountBookJUserDto> convJUserDtosByProj(List<AccountBookJUserProj> projs) {
        List<AccountBookJUserDto> dtos = new ArrayList<>();
        for (AccountBookJUserProj proj : projs) {
            dtos.add(new AccountBookJUserDto(proj.getAccountBook(), proj.getUser()));
        }
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * accountBookType = "income" 인 데이터들의 money 값의 합을 구한다.
     * @param query : Map[accountBookType, bankType, startDate, endDate]
     * @return Map[sum, itemSize]
     * @see AccountBookRepository
     */
    public Map<String, Object> getSumIncome(Map<String, Object> query) {
        // accountBookType 이 expenditure의 조건 조회의 경우 0을 리턴한다.
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

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * accountBookType = "expenditure" 인 데이터들의 money 값의 합을 구한다.
     * @param query : Map[accountBookType, bankType, startDate, endDate]
     * @return Map[sum, itemSize]
     * @see AccountBookRepository
     */
    public Map<String, Object> getSumExpenditure(Map<String, Object> query) {
        // accountBookType 이 income 조건 조회의 경우 0을 리턴한다.
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

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * AccountBook id 값과 상응되는 데이터를 삭제한다.
     * @param id
     * @see AccountBookRepository
     */
    public void destroyOne(UUID id) {
        accountBookRepository.findById(id).ifPresent(r->{
            r.setDeleted(1);
            accountBookRepository.save(r);
        });
    }
}
