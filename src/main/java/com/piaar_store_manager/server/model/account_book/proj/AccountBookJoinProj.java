package com.piaar_store_manager.server.model.account_book.proj;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.user.entity.UserEntity;

public interface AccountBookJoinProj {
    AccountBookEntity getAccountBook();
    UserEntity getUser();
}
