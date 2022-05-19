package com.piaar_store_manager.server.service.account_book;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AccountBookServiceTest {
    @Autowired
    AccountBookService accountBookService;

    @Test
    public void searchPagenation(){
        // given
        Map<String, Object> query = new HashMap<>();
        query.put("currPage", 11);

        // when
        accountBookService.searchPagenation(query);
    }
    
}
