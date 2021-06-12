package com.piaar_store_manager.server.controller.api;

import java.util.List;
import java.util.Map;

import com.piaar_store_manager.server.model.account_book.dto.AccountBookDefDto;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.account_book.AccountBookService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account-book")
public class AccountBookApiController {
    @Autowired
    AccountBookService accountBookService;

    @Autowired
    UserService userService;

    // /api/v1/account-book/list
    @GetMapping("/list")
    public ResponseEntity<?> searchList(@RequestParam Map<String, Object> query){

        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(accountBookService.searchList(query));
        message.setPagenation(accountBookService.searchPagenation(query));

        return new ResponseEntity<>(message, message.getStatus());
    }

    // /api/v1/account-book/list
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<AccountBookDefDto> accountBookDefDtos) {
        Message message = new Message();
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            
            accountBookService.createList(accountBookDefDtos, userService.getUserId());
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    // /api/v1/account-book/sum/income
    @GetMapping("/sum/income")
    public ResponseEntity<?> calcSumIncome(@RequestParam Map<String, Object> query){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(accountBookService.getSumIncome(query));

        return new ResponseEntity<>(message, message.getStatus());
    }

    // /api/v1/account-book/sum/expenditure
    @GetMapping("/sum/expenditure")
    public ResponseEntity<?> calcSumExpenditure(@RequestParam Map<String, Object> query){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(accountBookService.getSumExpenditure(query));

        return new ResponseEntity<>(message, message.getStatus());
    }
}
