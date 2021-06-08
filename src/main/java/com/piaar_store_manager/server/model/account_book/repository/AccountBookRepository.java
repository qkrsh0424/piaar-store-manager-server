package com.piaar_store_manager.server.model.account_book.repository;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBookEntity, Long>{
    
}
