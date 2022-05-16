package com.piaar_store_manager.server.domain.account_book.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.account_book.dto.AccountBookDto;
import com.piaar_store_manager.server.domain.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.domain.account_book.proj.AccountBookJoinProj;
import com.piaar_store_manager.server.domain.account_book.repository.AccountBookRepository;
import com.piaar_store_manager.server.domain.pagenation.PagenationDto;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final Integer DEFAULT_ITEM_PER_PAGE = 30;
    private final AccountBookRepository accountBookRepository;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * AccountBook 내용을 여러개 등록한다.
     * 
     * @param accountBookDtos : List::AccountBookDto::
     * @see AccountBookRepository#saveAll
     */
    public void createList(List<AccountBookDto> accountBookDtos) {
        UUID USER_ID = userService.getUserId();
        List<AccountBookEntity> entities = accountBookDtos.stream().map(dto -> {
            dto.setUserId(USER_ID).setRegDate(CustomDateUtils.getCurrentDateTime()).setCreatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedAt(CustomDateUtils.getCurrentDateTime());
            return AccountBookEntity.toEntity(dto);
        }).collect(Collectors.toList());
        accountBookRepository.saveAll(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 입/지출 건들을 조건에 맞게 조회한다. 
     * 
     * @param query : Map[accountBookType, bankType, startDate, endDate, currPage]
     * @return List::AccountBookDto.JoinDto::
     * @see AccountBookRepository#selectListJUserByCond
     */
    public List<AccountBookDto.JoinDto> searchList(Map<String, Object> query) {
        String accountBookType = query.get("accountBookType") != null ? query.get("accountBookType").toString() : "";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);  /* 지정된 startDate 값이 있다면 해당 데이터로 조회, 없다면 1970년을 기준으로 조회 */
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();   /* 지정된 endDate 값이 있다면 해당 데이터로 조회, 없다면 현재시간을 기준으로 조회 */

        Integer currPageParsed = query.get("currPage") != null ? Integer.parseInt(query.get("currPage").toString()) : 1;
        Integer currPage = (currPageParsed == null || (currPageParsed <1)) ? 0 : (currPageParsed - 1);

        Pageable pageable = PageRequest.of(currPage, DEFAULT_ITEM_PER_PAGE);
        List<AccountBookJoinProj> projs = accountBookRepository.selectListJUserByCond(accountBookType, bankType, startDate, endDate, pageable);
        List<AccountBookDto.JoinDto> dtos = projs.stream().map(proj -> AccountBookDto.JoinDto.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * AccountBook 데이터의 페이지네이션 데이터를 얻는다.
     * @param query : Map[accountBookType, bankType, startDate, endDate, currPage]
     * @return PagenationDto
     * @see AccountBookRepository#sizeJUserByCond
     */
    public PagenationDto searchPagenation(Map<String, Object> query) {
        String accountBookType = query.get("accountBookType") != null ? query.get("accountBookType").toString() : "";
        String bankType = query.get("bankType") != null ? query.get("bankType").toString() : "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();

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
     * <b>DB Select Related Method</b>
     * <p>
     * accountBookType = "income" 인 데이터들의 money 값의 합을 구한다.
     * @param query : Map[accountBookType, bankType, startDate, endDate]
     * @return Map[sum, itemSize]
     * @see AccountBookRepository#sumIncomeOrExpenditureCond
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();

        return accountBookRepository.sumIncomeOrExpenditureCond(accountBookType, bankType, startDate, endDate);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * accountBookType = "expenditure" 인 데이터들의 money 값의 합을 구한다.
     * 
     * @param query : Map[accountBookType, bankType, startDate, endDate]
     * @return Map[sum, itemSize]
     * @see AccountBookRepository#sumIncomeOrExpenditureCond
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();

        return accountBookRepository.sumIncomeOrExpenditureCond(accountBookType, bankType, startDate, endDate);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * AccountBook id 값과 상응되는 데이터를 삭제한다.
     * 
     * @param id : UUID
     * @see AccountBookRepository#save
     */
    public void destroyOne(UUID id) {
        accountBookRepository.findById(id).ifPresent(r->{
            r.setDeleted(1);
            accountBookRepository.save(r);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * AccountBook id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * @param accountBookId : UUID
     * @param accountBookDto : AccountBookDto
     * @see AccountBookRepository#save
     */
    public void patchOne(UUID accountBookId, AccountBookDto accountBookDto) {
        accountBookRepository.findById(accountBookId).ifPresentOrElse(r->{
            if(accountBookDto.getAccountBookType() != null){
                r.setAccountBookType(accountBookDto.getAccountBookType());
            }
            if(accountBookDto.getBankType() != null){
                r.setBankType(accountBookDto.getBankType());
            }
            if(accountBookDto.getUserId() != null){
                r.setUserId(accountBookDto.getUserId());
            }
            if(accountBookDto.getDesc() != null){
                r.setDesc(accountBookDto.getDesc());
            }
            if(accountBookDto.getMoney() != null){
                r.setMoney(accountBookDto.getMoney());
            }
            if(accountBookDto.getExpenditureTypeId() != null){
                r.setExpenditureTypeId(accountBookDto.getExpenditureTypeId());
            }
            if(accountBookDto.getRegDate() != null){
                r.setRegDate(accountBookDto.getRegDate());
            }
            if(accountBookDto.getCreatedAt() != null){
                r.setCreatedAt(accountBookDto.getCreatedAt());
            }
            r.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
            accountBookRepository.save(r);
        }, null);
    }

    
}
