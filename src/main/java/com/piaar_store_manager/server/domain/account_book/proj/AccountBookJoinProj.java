package com.piaar_store_manager.server.domain.account_book.proj;

import com.piaar_store_manager.server.domain.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.domain.expenditure_type.entity.ExpenditureTypeEntity;
import com.piaar_store_manager.server.domain.user.entity.UserEntity;

public interface AccountBookJoinProj {
    AccountBookEntity getAccountBook();
    UserEntity getUser();
    ExpenditureTypeEntity getExpenditureType();
}
