package com.piaar_store_manager.server.domain.commute_record.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.commute_record.entity.CommuteRecordEntity;
import com.piaar_store_manager.server.domain.commute_record.repository.CommuteRecordRepository;

@Service
public class CommuteRecordService {
    private final CommuteRecordRepository commuteRecordRepository;

    @Autowired
    public CommuteRecordService(
            CommuteRecordRepository commuteRecordRepository
    ) {
        this.commuteRecordRepository = commuteRecordRepository;
    }

    public List<CommuteRecordEntity> searchByUserIdAndDateRange(UUID userId, Date startDate, Date endDate) {
        List<CommuteRecordEntity> entities = commuteRecordRepository.selectByUserIdAndDateRange(userId, startDate, endDate);
        return entities;
    }

    public Optional<CommuteRecordEntity> searchById(UUID id) {
        Optional<CommuteRecordEntity> entityOpt = commuteRecordRepository.selectById(id);
        return entityOpt;
    }

    public void createAndModify(CommuteRecordEntity entity) {
        commuteRecordRepository.save(entity);
    }
}
