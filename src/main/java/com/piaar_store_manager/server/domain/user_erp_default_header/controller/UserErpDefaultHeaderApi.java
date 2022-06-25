package com.piaar_store_manager.server.domain.user_erp_default_header.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.user_erp_default_header.dto.UserErpDefaultHeaderDto;
import com.piaar_store_manager.server.domain.user_erp_default_header.service.UserErpDefaultHeaderBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-erp-default-headers")
@RequiredLogin
@RequiredArgsConstructor
public class UserErpDefaultHeaderApi {
    private final UserErpDefaultHeaderBusinessService userErpDefaultHeaderBusinessService;

    @GetMapping("")
    public ResponseEntity<?> searchOne() {
        Message message = new Message();

        message.setData(userErpDefaultHeaderBusinessService.searchOne());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("")
    public ResponseEntity<?> createOne(@RequestBody UserErpDefaultHeaderDto dto) {
        Message message = new Message();

        userErpDefaultHeaderBusinessService.createOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("")
    public ResponseEntity<?> changeOne(@RequestBody UserErpDefaultHeaderDto dto) {
        Message message = new Message();

        userErpDefaultHeaderBusinessService.changeOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
