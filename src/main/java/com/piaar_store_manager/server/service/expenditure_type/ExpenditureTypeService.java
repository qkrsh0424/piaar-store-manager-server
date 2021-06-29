package com.piaar_store_manager.server.service.expenditure_type;

import java.util.ArrayList;
import java.util.List;

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

    public List<ExpenditureTypeDto> searchList(){
        // log.info("ExpenditureTypeService : searchList => touched"); 
        List<ExpenditureTypeEntity> entities = expenditureTypeRepository.findAll();

        return convEntitiesToDtos(entities);
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
