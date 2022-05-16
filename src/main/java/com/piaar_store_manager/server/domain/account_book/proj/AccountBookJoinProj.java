package com.piaar_store_manager.server.domain.account_book.proj;

import com.piaar_store_manager.server.domain.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.domain.user.entity.UserEntity;
import com.piaar_store_manager.server.model.expenditure_type.entity.ExpenditureTypeEntity;

public interface AccountBookJoinProj {
    AccountBookEntity getAccountBook();
    UserEntity getUser();
    ExpenditureTypeEntity getExpenditureType();
}
