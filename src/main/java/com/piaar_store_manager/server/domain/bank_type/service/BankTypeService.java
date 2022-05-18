package com.piaar_store_manager.server.domain.bank_type.service;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.domain.bank_type.dto.BankTypeDefDto;
import com.piaar_store_manager.server.domain.bank_type.entity.BankTypeEntity;
import com.piaar_store_manager.server.domain.bank_type.repository.BankTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BankTypeService {
    @Autowired
    BankTypeRepository bankTypeRepo;

    public List<BankTypeDefDto> searchList(){
        Sort sort = Sort.by(Sort.Direction.ASC, "bankTypeId");
        List<BankTypeEntity> bankTypeEntities = bankTypeRepo.findAll(sort);
        List<BankTypeDefDto> bankTypeDtos = getDtosByEntities(bankTypeEntities);
        return bankTypeDtos;
    }

    private List<BankTypeDefDto> getDtosByEntities(List<BankTypeEntity> bankTypeEntities){
        List<BankTypeDefDto> bankTypeDtos = new ArrayList<>();

        for(BankTypeEntity bankTypeEntity : bankTypeEntities){
            BankTypeDefDto bankTypeDto = new BankTypeDefDto();
            bankTypeDto.setBankTypeId(bankTypeEntity.getBankTypeId());
            bankTypeDto.setBankType(bankTypeEntity.getBankType());
            bankTypeDtos.add(bankTypeDto);
        }
        return bankTypeDtos;
    }
}
