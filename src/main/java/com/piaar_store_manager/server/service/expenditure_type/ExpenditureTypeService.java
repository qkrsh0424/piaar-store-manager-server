package com.piaar_store_manager.server.service.expenditure_type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.domain.account_book.repository.AccountBookRepository;
import com.piaar_store_manager.server.model.expenditure_type.dto.ExpenditureTypeDto;
import com.piaar_store_manager.server.model.expenditure_type.entity.ExpenditureTypeEntity;
import com.piaar_store_manager.server.model.expenditure_type.repository.ExpenditureTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenditureTypeService {
    @Autowired
    ExpenditureTypeRepository expenditureTypeRepository;

    @Autowired
    AccountBookRepository accountBookRepository;

    public List<ExpenditureTypeDto> searchList(){
        // log.info("ExpenditureTypeService : searchList => touched"); 
        List<ExpenditureTypeEntity> entities = expenditureTypeRepository.findAll();
        List<ExpenditureTypeDto> dtos = entities.stream().map(entity -> ExpenditureTypeDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    public List<ExpenditureTypeDto> searchList(Map<String, Object> query){
        List<ExpenditureTypeDto> expenditureTypeDtos = this.searchList();
        List<AccountBookEntity> accountBookEntites = new ArrayList<>();

        String accountBookType = "expenditure";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();

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
}
