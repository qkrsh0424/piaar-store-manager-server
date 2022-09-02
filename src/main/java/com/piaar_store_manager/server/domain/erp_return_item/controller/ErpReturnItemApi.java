package com.piaar_store_manager.server.domain.erp_return_item.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_return_item.dto.ErpReturnItemDto;
import com.piaar_store_manager.server.domain.erp_return_item.service.ErpReturnItemBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/erp-return-items")
@RequiredArgsConstructor
@RequiredLogin
public class ErpReturnItemApi {
    private final ErpReturnItemBusinessService erpReturnItemBusinessService;

    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody List<ErpReturnItemDto> erpReturnItemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.createBatch(erpReturnItemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
