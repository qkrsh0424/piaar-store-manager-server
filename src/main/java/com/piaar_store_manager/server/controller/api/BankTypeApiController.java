package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.bank_type.BankTypeService;

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
    @RequiredLogin
    @GetMapping("/list")
    public ResponseEntity<?> searchList(){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(bankTypeService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }
}
