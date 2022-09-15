package com.piaar_store_manager.server.domain.product_detail.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.domain.product_detail.service.ProductDetailBusinessService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-details")
@RequiredArgsConstructor
@RequiredLogin
public class ProductDetailApiController {
    private final ProductDetailBusinessService productDetailBusinessService;

    /**
     * Search one api for product detail.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/{detailCid}</b>
     */
    // deprecated
    // @GetMapping("{detailCid}")
    // public ResponseEntity<?> searchOne(@PathVariable(value = "detailCid") Integer detailCid) {
    //     Message message = new Message();

    //     message.setData(productDetailBusinessService.searchOne(detailCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search one api for detail.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/{detailId}</b>
     */
    // Unused API
    @GetMapping("/{detailId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "detailId") UUID detailId) {
        Message message = new Message();

        message.setData(productDetailBusinessService.searchOne(detailId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search all api for detail.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/all</b>
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(productDetailBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search api for detail matching option cid.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/batch/{productOptionCid}</b>
     * 
     * @param productOptionCid : Integer
     */
    // Unused API
    @GetMapping("/batch/{productOptionCid}")
    public ResponseEntity<?> searchBatchByOptionCid(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        message.setData(productDetailBusinessService.searchBatchByOptionCid(productOptionCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for detail.
     * <p>
     * <b>POST : API URL => /api/v1/product-detail</b>
     * 
     * @param productDetailGetDto : ProductDetailGetDto
     */
    // Unused API
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        productDetailBusinessService.createOne(productDetailGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for detail.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-detail/{detailCid}</b>
     */
    // @DeleteMapping("{detailCid}")
    // @PermissionRole
    // public ResponseEntity<?> destroyOne(@PathVariable(value = "detailCid") Integer detailCid) {
    //     Message message = new Message();

    //     productDetailBusinessService.destroyOne(detailCid);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Change one api for detail.
     * <p>
     * <b>PUT : API URL => /api/v1/product-detail</b>
     * 
     * @param productDetailGetDto : ProductDetailGetDto
     */
    // Unused API
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        productDetailBusinessService.changeOne(productDetailGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for detail.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-detail</b>
     * 
     * @param productDetailGetDto : ProductDetailGetDto
     */
    // Unused API
    @PatchMapping("")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        productDetailBusinessService.patchOne(productDetailGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for detail.
     * <p>
     * <b>DELETE : API UTL => /api/v1/product-detail/{detailId}</b>
     * 
     * @param detailId : UUID
     */
    @DeleteMapping("/{detailId}")
    @PermissionRole
    public ResponseEntity<?> destroyOne(@PathVariable(value = "detailId") UUID detailId) {
        Message message = new Message();

        productDetailBusinessService.destroyOne(detailId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
