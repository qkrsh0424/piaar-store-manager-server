package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.model.account_book.dto.AccountBookDefDto;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.account_book.AccountBookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-book")
public class AccountBookApiController {
    @Autowired
    AccountBookService accountBookService;
    
    // /api/v1/account-book/list
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<AccountBookDefDto> accountBookDefDtos){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        accountBookService.createList(accountBookDefDtos);
        return new ResponseEntity<>(message, message.getStatus());
    }    
}
