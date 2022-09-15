package com.piaar_store_manager.server.domain.product_category.controller;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_category.service.ProductCategoryBusinessService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-category")
@RequiredArgsConstructor
@RequiredLogin
public class ProductCategoryApiController {
    private final ProductCategoryBusinessService productCategoryBusinessService;

    /**
     * Search all api for product category.
     * <p>
     * <b>GET : API URL => /api/v1/product-category/all</b>
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(productCategoryBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
