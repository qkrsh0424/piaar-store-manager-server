package com.piaar_store_manager.server.domain.return_type.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.return_type.service.ReturnReasonTypeBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/return-reason-types")
@RequiredLogin
public class ReturnReasonTypeApi {
    private final ReturnReasonTypeBusinessService returnReasonTypeBusinessService;
    
    @GetMapping("/all")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(returnReasonTypeBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
