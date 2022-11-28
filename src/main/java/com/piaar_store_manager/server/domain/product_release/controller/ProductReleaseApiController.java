package com.piaar_store_manager.server.domain.product_release.controller;

import java.util.UUID;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseBusinessService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-release")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReleaseApiController {
    private final ProductReleaseBusinessService productReleaseBusinessService;

    @GetMapping("/erp-order-item/{erpOrderItemId}")
    public ResponseEntity<?> searchOneByErpOrderItemId(@PathVariable(value = "erpOrderItemId") UUID erpOrderItemId) {
        Message message = new Message();

        message.setData(productReleaseBusinessService.searchOneByErpOrderItemId(erpOrderItemId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
