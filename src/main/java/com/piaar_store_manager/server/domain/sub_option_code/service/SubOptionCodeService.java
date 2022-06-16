package com.piaar_store_manager.server.domain.sub_option_code.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;
import com.piaar_store_manager.server.domain.sub_option_code.repository.SubOptionCodeRepository;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubOptionCodeService {
    private final SubOptionCodeRepository subOptionCodeRepository;

    public List<SubOptionCodeEntity> searchListByProductOptionId(UUID optionId) {
        return subOptionCodeRepository.findByProductOptionId(optionId);
    }

    public List<SubOptionCodeEntity> findAll() {
        return subOptionCodeRepository.findAll();
    }

    public void destroyOne(UUID subOptionId) {
        subOptionCodeRepository.findById(subOptionId).ifPresent(subOptionCode -> {
            subOptionCodeRepository.delete(subOptionCode);
        });
    }

    public void saveAndModify(SubOptionCodeEntity entity) {
        subOptionCodeRepository.save(entity);
    }

    public SubOptionCodeEntity searchOne(UUID subOptionId) {
        Optional<SubOptionCodeEntity> entityOpt = subOptionCodeRepository.findById(subOptionId);

        if(entityOpt.isPresent()) {
            return entityOpt.get();
        }else {
            throw new NullPointerException();
        }
    }

    public void duplicationCodeCheck(String subOptionCode) {
        Optional<SubOptionCodeEntity> entityOpt = subOptionCodeRepository.findBySubOptionCode(subOptionCode);

        if(entityOpt.isPresent()) {
            throw new CustomInvalidDataException("이미 등록된 대체코드입니다. 다시 시도해주세요.");
        }
    }
}
