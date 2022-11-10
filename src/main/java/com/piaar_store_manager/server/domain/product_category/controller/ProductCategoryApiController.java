package com.piaar_store_manager.server.domain.product_category.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_category.service.ProductCategoryBusinessService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-category")
@RequiredArgsConstructor
@RequiredLogin
public class ProductCategoryApiController {
    private final ProductCategoryBusinessService productCategoryBusinessService;

    /**
     * Search list api for product category.
     * <p>
     * <b>GET : API URL => /api/v1/product-category/list</b>
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productCategoryBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // 221109 FEAT
    @PermissionRole
    @PostMapping("")
    public ResponseEntity<?> createOne(@RequestBody ProductCategoryGetDto dto) {
        Message message = new Message();

        productCategoryBusinessService.createOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PermissionRole
    @PatchMapping("")
    public ResponseEntity<?> changeOne(@RequestBody ProductCategoryGetDto dto) {
        Message message = new Message();

        productCategoryBusinessService.changeOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PermissionRole
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteOne(@PathVariable(value = "categoryId") UUID id) {
        Message message = new Message();

        productCategoryBusinessService.deleteOne(id);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
