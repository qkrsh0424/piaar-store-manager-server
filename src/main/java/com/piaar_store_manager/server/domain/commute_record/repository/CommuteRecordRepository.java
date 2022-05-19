package com.piaar_store_manager.server.domain.commute_record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.commute_record.entity.CommuteRecordEntity;

@Repository
public interface CommuteRecordRepository extends JpaRepository<CommuteRecordEntity, Integer> {
    @Query("SELECT cr FROM CommuteRecordEntity cr\n" +
            "JOIN UserEntity u ON cr.userId=u.id\n" +
            "WHERE cr.userId=:userId AND cr.createdAt BETWEEN :startDate AND :endDate")
    List<CommuteRecordEntity> selectByUserIdAndDateRange(UUID userId, Date startDate, Date endDate);

    @Query("SELECT cr FROM CommuteRecordEntity cr\n" +
            "WHERE cr.id=:id")
    Optional<CommuteRecordEntity> selectById(UUID id);
}
