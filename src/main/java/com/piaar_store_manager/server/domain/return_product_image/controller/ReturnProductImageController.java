package com.piaar_store_manager.server.domain.return_product_image.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.return_product_image.dto.ReturnProductImageDto;
import com.piaar_store_manager.server.domain.return_product_image.service.ReturnProductImageBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/return-product-images")
@RequiredLogin
@RequiredArgsConstructor
public class ReturnProductImageController {
    private final ReturnProductImageBusinessService returnProductImageBusinessService;

    @GetMapping("/batch/{erpReturnId}")
    public ResponseEntity<?> searchBatchByErpReturnId(@PathVariable(value = "erpReturnId") UUID erpReturnId) {
        Message message = new Message();

        message.setData(returnProductImageBusinessService.searchBatchByErpReturnId(erpReturnId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody List<ReturnProductImageDto> dtos) {
        Message message = new Message();

        returnProductImageBusinessService.createBatch(dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/{imageId}")
    // @PermissionRole(role = "ROLE_SUPERADMIN")
    @PermissionRole
    public ResponseEntity<?> deleteOne(@PathVariable UUID imageId) {
        Message message = new Message();

        returnProductImageBusinessService.deleteOne(imageId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
