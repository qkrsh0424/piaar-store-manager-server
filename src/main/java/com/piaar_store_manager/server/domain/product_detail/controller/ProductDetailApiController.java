package com.piaar_store_manager.server.domain.product_detail.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.domain.product_detail.service.ProductDetailBusinessService;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/product-detail")
@RequiredArgsConstructor
@RequiredLogin
public class ProductDetailApiController {
    private final ProductDetailBusinessService productDetailBusinessService;

    /**
     * Search one api for product detail.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/one/{detailCid}</b>
     */
    @GetMapping("/one/{detailCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "detailCid") Integer detailCid) {
        Message message = new Message();

        message.setData(productDetailBusinessService.searchOne(detailCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product detail.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/list/{optionCid}</b>
     */
    @GetMapping("/list/{optionCid}")
    public ResponseEntity<?> searchListByOptionCid(@PathVariable(value = "optionCid") Integer optionCid) {
        Message message = new Message();

        message.setData(productDetailBusinessService.searchList(optionCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search all api for product detail.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/list</b>
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(productDetailBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product detail.
     * <p>
     * <b>POST : API URL => /api/v1/product-detail/one</b>
     */
    @PostMapping("/one")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        productDetailBusinessService.createOne(productDetailGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for product detail.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-detail/one/{detailCid}</b>
     */
    @DeleteMapping("/one/{detailCid}")
    @PermissionRole
    public ResponseEntity<?> destroyOne(@PathVariable(value = "detailCid") Integer detailCid) {
        Message message = new Message();

        productDetailBusinessService.destroyOne(detailCid);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for product detail.
     * <p>
     * <b>PUT : API URL => /api/v1/product-detail/one</b>
     */
    @PutMapping("/one")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        productDetailBusinessService.changeOne(productDetailGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch( Delete or Remove ) one api for product detail.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-detail/one</b>
     */
    @PatchMapping("/one")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        productDetailBusinessService.patchOne(productDetailGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
