package com.piaar_store_manager.server.domain.bank_type.controller;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.bank_type.service.BankTypeService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bank-type")
@RequiredArgsConstructor
@RequiredLogin
public class BankTypeApiController {

    private final BankTypeService bankTypeService;

    /**
     * Search all list for bankType
     * <p>
     * <b>GET : API URL => /api/v1/bank-type/list</b>
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see BankTypeService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList(){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(bankTypeService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }
}
