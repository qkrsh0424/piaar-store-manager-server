package com.piaar_store_manager.server.domain.product.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.service.ProductBusinessService;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RequiredLogin
public class ProductApiController {
    private final ProductBusinessService productBusinessService;

    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/products/{productId}</b>
     * 
     * @param params : Map::String, Object:: [objectType]
     */
    // Unused API
    @GetMapping("/{productId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productId") UUID productId, @RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productBusinessService.searchOne(productId, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search all api for product.
     * <p>
     * <b>GET : API URL => /api/v1/products/all</b>
     * 
     * @param params : Map::String, Object:: [objectType]
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAll(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productBusinessService.searchAll(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/products/batch/stock</b>
     * <p>
     * 재고관리 여부가 true인 product를 모두 조회한다.
     * 
     * @param params : Map::String, Object:: [objectType]
     */
    @GetMapping("/batch/stock")
    public ResponseEntity<?> searchBatchOfManagedStock(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productBusinessService.searchBatchOfManagedStock(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/one</b>
     * <p>
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductGetDto.CreateReq productCreateReqDto) {
        Message message = new Message();

        productBusinessService.createOne(productCreateReqDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create batch api for product.
     * <p>
     * <b>POST : API URL => /api/v1/products/batch</b>
     */
    // Unused API
    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody List<ProductGetDto.CreateReq> productCreateReqDtos) {
        Message message = new Message();

        try {
            productBusinessService.createBatch(productCreateReqDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (DataIntegrityViolationException e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
            message.setMemo("입력된 옵션관리코드 값이 이미 존재합니다.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for product.
     * <p>
     * <b>DELETE : API URL => /api/v1/products/{productId}</b>
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
     * Change one api for product
     * <p>
     * <b>PUT : API URL => /api/v1/products</b>
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        productBusinessService.changeOne(productGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change list api for product
     * <p>
     * <b>PUT : API URL => /api/v1/products/batch</b>
     */
    @PutMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> changeBatch(@RequestBody List<ProductGetDto.CreateReq> productCreateReqDto) {
        Message message = new Message();

        productBusinessService.changeBatch(productCreateReqDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for product
     * <p>
     * <b>PATCH : API URL => /api/v1/products</b>
     */
    @PatchMapping("")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        productBusinessService.patchOne(productGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
