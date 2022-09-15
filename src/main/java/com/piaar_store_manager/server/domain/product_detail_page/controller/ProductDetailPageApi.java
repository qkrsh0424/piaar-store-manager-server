package com.piaar_store_manager.server.domain.product_detail_page.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_detail_page.dto.ProductDetailPageDto;
import com.piaar_store_manager.server.domain.product_detail_page.service.ProductDetailPageBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product-detail-pages")
@RequiredLogin
@RequiredArgsConstructor
public class ProductDetailPageApi {
    private final ProductDetailPageBusinessService productDetailPageBusinessService;

    @GetMapping("/batch/{productId}")
    public ResponseEntity<?> searchBatch(@PathVariable(value = "productId") UUID productId) {
        Message message = new Message();

        message.setData(productDetailPageBusinessService.searchBatch(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("")
    public ResponseEntity<?> createOne(@RequestBody ProductDetailPageDto dto) {
        Message message = new Message();

        productDetailPageBusinessService.createOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/{pageId}")
    public ResponseEntity<?> deleteOne(@PathVariable(value = "pageId") UUID pageId) {
        Message message = new Message();

        productDetailPageBusinessService.deleteOne(pageId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("")
    public ResponseEntity<?> updateOne(@RequestBody ProductDetailPageDto dto) {
        Message message = new Message();

        productDetailPageBusinessService.updateOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
