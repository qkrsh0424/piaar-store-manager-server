package com.piaar_store_manager.server.service.expenditure_type;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.account_book.repository.AccountBookRepository;
import com.piaar_store_manager.server.model.expenditure_type.dto.ExpenditureTypeDto;
import com.piaar_store_manager.server.model.expenditure_type.entity.ExpenditureTypeEntity;
import com.piaar_store_manager.server.model.expenditure_type.repository.ExpenditureTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenditureTypeService {
    @Autowired
    ExpenditureTypeRepository expenditureTypeRepository;

    @Autowired
    AccountBookRepository accountBookRepository;

    public List<ExpenditureTypeDto> searchList(){
        // log.info("ExpenditureTypeService : searchList => touched"); 
        List<ExpenditureTypeEntity> entities = expenditureTypeRepository.findAll();

        return convEntitiesToDtos(entities);
    }

    public List<ExpenditureTypeDto> searchList(Map<String, Object> query){
        List<ExpenditureTypeDto> expenditureTypeDtos = this.searchList();
        List<AccountBookEntity> accountBookEntites = new ArrayList<>();

        String accountBookType = "expenditure";
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();

        accountBookEntites = accountBookRepository.selectList(accountBookType, startDate, endDate);

        for(ExpenditureTypeDto expenditureTypeDto : expenditureTypeDtos){
            Long sum = 0L;
            sum = accountBookEntites.stream()
                .filter(r->r.getExpenditureTypeId() == expenditureTypeDto.getExpenditureTypeId())
                .mapToLong(r->r.getMoney()).sum();
            expenditureTypeDto.setSumValue(sum);
        }

        return expenditureTypeDtos;
    }

    private List<ExpenditureTypeDto> convEntitiesToDtos(List<ExpenditureTypeEntity> entities){
        List<ExpenditureTypeDto> dtos = new ArrayList<>();
        for(ExpenditureTypeEntity entity : entities){
            ExpenditureTypeDto dto = new ExpenditureTypeDto();
            dto.setExpenditureTypeId(entity.getExpenditureTypeId());
            dto.setExpenditureType(entity.getExpenditureType());
            dtos.add(dto);
        }
        return dtos;
    }
}
