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
@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
@RequiredLogin
public class ProductApiControllerV2 {
    private final ProductBusinessService productBusinessService;

    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v2/products/{productId}</b>
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productId") UUID productId) {
        Message message = new Message();

        message.setData(productBusinessService.searchOne(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product.
     * product related category and options.
     * <p>
     * <b>GET : API URL => /api/v2/products/batch/stock</b>
     */
    @GetMapping("/batch/stock")
    public ResponseEntity<?> searchBatch(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productBusinessService.searchBatch(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search paging list api for product.
     * product related category and options.
     * <p>
     * <b>GET : API URL => /api/v2/products/batch/stock/page</b>
     */
    @GetMapping("/batch/stock/page")
    public ResponseEntity<?> searchBatchByPaging(@RequestParam Map<String, Object> params, @PageableDefault(sort = "cid", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Message message = new Message();

        message.setData(productBusinessService.searchBatchByPaging(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for product.
     * product related options.
     * <p>
     * <b>POST : API URL => /api/v2/products/options</b>
     */
    @PostMapping("/options")
    @PermissionRole
    public ResponseEntity<?> createProductAndOptions(@RequestBody @Valid ProductGetDto.RelatedOptions createDto) {
        Message message = new Message();

        productBusinessService.createProductAndOptions(createDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for product.
     * <p>
     * <b>DELETE : API URL => /api/v2/products/{productId}</b>
     */
    @PermissionRole(role = "ROLE_SUPERADMIN")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") UUID productId) {
        Message message = new Message();

        productBusinessService.destroyOne(productId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for product.
     * <p>
     * <b>PUT : API URL => /api/v2/products</b>
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody @Valid ProductGetDto dto) {
        Message message = new Message();

        productBusinessService.updateOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
