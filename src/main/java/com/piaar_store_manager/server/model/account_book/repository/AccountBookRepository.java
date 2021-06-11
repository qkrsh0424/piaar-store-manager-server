package com.piaar_store_manager.server.model.account_book.repository;

import java.util.Date;
import java.util.List;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.account_book.proj.AccountBookJUserProj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBookEntity, Long>{

    @Query(
        "SELECT ab AS accountBook, u AS user FROM AccountBookEntity ab\n"+
        "LEFT OUTER JOIN UserEntity u ON ab.userId=u.id\n"+
        "WHERE ab.accountBookType LIKE %:accountBookType% AND ab.bankType LIKE %:bankType% AND ab.regDate BETWEEN :startDate AND :endDate\n"+
        "ORDER BY ab.regDate DESC"
    )
    List<AccountBookJUserProj> selectListJUserByCond(String accountBookType, String bankType, Date startDate, Date endDate);
    
}