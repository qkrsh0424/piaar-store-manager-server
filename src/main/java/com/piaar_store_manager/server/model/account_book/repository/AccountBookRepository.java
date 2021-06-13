package com.piaar_store_manager.server.model.account_book.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.account_book.proj.AccountBookJUserProj;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBookEntity, Long>{

    @Query(
        "SELECT ab AS accountBook, u AS user FROM AccountBookEntity ab\n"+
        "LEFT OUTER JOIN UserEntity u ON ab.userId=u.id\n"+
        "WHERE ab.accountBookType LIKE %:accountBookType% AND ab.bankType LIKE %:bankType% AND ab.regDate BETWEEN :startDate AND :endDate AND ab.deleted=0\n"+
        "ORDER BY ab.regDate DESC"
    )
    List<AccountBookJUserProj> selectListJUserByCond(String accountBookType, String bankType, Date startDate, Date endDate, Pageable pageable);

    @Query(
        "SELECT count(ab) FROM AccountBookEntity ab\n"+
        "LEFT OUTER JOIN UserEntity u ON ab.userId=u.id\n"+
        "WHERE ab.accountBookType LIKE %:accountBookType% AND ab.bankType LIKE %:bankType% AND ab.regDate BETWEEN :startDate AND :endDate AND ab.deleted=0\n"+
        "ORDER BY ab.regDate DESC"
    )
    Integer sizeJUserByCond(String accountBookType, String bankType, Date startDate, Date endDate);
 
    @Query(
        "SELECT COALESCE(sum(ab.money),0) AS sum, count(ab) AS itemSize FROM AccountBookEntity ab\n"+
        "WHERE ab.accountBookType LIKE %:accountBookType% AND ab.bankType LIKE %:bankType% AND (ab.regDate BETWEEN :startDate AND :endDate) AND ab.deleted=0"
    )
    Map<String, Object> sumIncomeOrExpenditureCond(String accountBookType, String bankType, Date startDate, Date endDate);

    Optional<AccountBookEntity> findById(UUID id);

}