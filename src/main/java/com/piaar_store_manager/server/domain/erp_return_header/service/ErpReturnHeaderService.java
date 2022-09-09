package com.piaar_store_manager.server.domain.erp_return_header.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.erp_return_header.entity.ErpReturnHeaderEntity;
import com.piaar_store_manager.server.domain.erp_return_header.repository.ErpReturnHeaderRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReturnHeaderService {
    private final ErpReturnHeaderRepository erpReturnHeaderRepository;
    
    public ErpReturnHeaderEntity searchOne(UUID id) {
        Optional<ErpReturnHeaderEntity> entityOpt = erpReturnHeaderRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    public List<ErpReturnHeaderEntity> findAll() {
        return erpReturnHeaderRepository.findAll();
    }

    public void saveAndModify(ErpReturnHeaderEntity entity) {
        erpReturnHeaderRepository.save(entity);
    }

    public void deleteOne(ErpReturnHeaderEntity entity) {
        erpReturnHeaderRepository.delete(entity);
    }
}
