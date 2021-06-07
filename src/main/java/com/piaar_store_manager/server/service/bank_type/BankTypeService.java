package com.piaar_store_manager.server.service.bank_type;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.bank_type.dto.BankTypeDefDto;
import com.piaar_store_manager.server.model.bank_type.entity.BankTypeEntity;
import com.piaar_store_manager.server.model.bank_type.repository.BankTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankTypeService {
    @Autowired
    BankTypeRepository bankTypeRepo;

    public List<BankTypeDefDto> searchList(){
        List<BankTypeEntity> bankTypeEntities = bankTypeRepo.findAll();
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
