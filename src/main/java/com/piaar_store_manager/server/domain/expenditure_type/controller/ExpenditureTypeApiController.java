package com.piaar_store_manager.server.domain.expenditure_type.controller;

import java.util.Map;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.expenditure_type.service.ExpenditureTypeService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expenditure-type")
@RequiredArgsConstructor
@RequiredLogin
public class ExpenditureTypeApiController {

    private final ExpenditureTypeService expenditureTypeService;
     
    // /**
    //  * Search all list for expenditureType
    //  * <p>
    //  * <b>GET : API URL => /api/v1/expenditure-type/list</b>
    //  * @return ResponseEntity(message, HttpStatus)
    //  * @see Message
    //  * @see HttpStatus
    //  * @see BankTypeService#searchList
    //  */
    // @GetMapping("/list")
    // public ResponseEntity<?> searchList(){
    //     Message message = new Message();
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");
    //     message.setData(expenditureTypeService.searchList());
    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search all list for expenditureType
     * <p>
     * <b>GET : API URL => /api/v1/expenditure-type/list</b>
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see BankTypeService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList(@RequestParam Map<String,Object> query){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(expenditureTypeService.searchList(query));
        return new ResponseEntity<>(message, message.getStatus());
    }
}
