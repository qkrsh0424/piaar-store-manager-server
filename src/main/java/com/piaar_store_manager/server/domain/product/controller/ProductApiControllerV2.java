package com.piaar_store_manager.server.domain.product.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.service.ProductBusinessService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v2/product")
@RequiredArgsConstructor
@RequiredLogin
public class ProductApiControllerV2 {
    private final ProductBusinessService productBusinessService;

    // 22.10.11 FEAT
    // 재고관리 페이지에서 조회할 데이터
    @GetMapping("/batch/stock")
    public ResponseEntity<?> searchBatch(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productBusinessService.searchBatch(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // 22.10.17 FEAT
    // 재고관리 페이지에서 조회할 데이터 - 페이징처리
    @GetMapping("/batch/stock/page")
    public ResponseEntity<?> searchBatchByPaging(@RequestParam Map<String, Object> params, @PageableDefault(sort = "cid", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Message message = new Message();

        message.setData(productBusinessService.searchBatchByPaging(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    //  [221005] FEAT
    @PostMapping("/options")
    @PermissionRole
    public ResponseEntity<?> createProductAndOptions(@RequestBody @Valid ProductGetDto.ProductAndOptions createDto) {
        Message message = new Message();

        productBusinessService.createProductAndOptions(createDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // [221021] FEAT
    @PutMapping("/options")
    @PermissionRole
    public ResponseEntity<?> updateProductAndOptions(@RequestBody @Valid ProductGetDto.ProductAndOptions createDto) {
        Message message = new Message();

        productBusinessService.updateProductAndOptions(createDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // [221021] FEAT
    @GetMapping("/{productId}")
    public ResponseEntity<?> searchProductAndOptions(@PathVariable(value = "productId") UUID productId) {
        Message message = new Message();

        message.setData(productBusinessService.searchProductAndOptions(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    //  [221025] FEAT
    @PermissionRole(role = "ROLE_SUPERADMIN")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") UUID productId) {
        Message message = new Message();

        productBusinessService.destroyOne(productId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
