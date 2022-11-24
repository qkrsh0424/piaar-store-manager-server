package com.piaar_store_manager.server.domain.product.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product.service.ProductBusinessService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@RequiredLogin
public class ProductApiController {
    private final ProductBusinessService productBusinessService;

    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one/{productCid}</b>
     */
    @GetMapping("/one/{productCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        message.setData(productBusinessService.searchOne(productCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list</b>
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list/{categoryCid}</b>
     * <p>
     * categoryCid에 대응하는 product를 모두 조회한다.
     */
    @GetMapping("/list/{categoryCid}")
    public ResponseEntity<?> searchListByCategory(@PathVariable(value = "categoryCid") Integer categoryCid) {
        Message message = new Message();

        message.setData(productBusinessService.searchListByCategory(categoryCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    //  [221005] FEAT
    @PostMapping("/options")
    @PermissionRole
    public ResponseEntity<?> createProductAndOptions(@RequestBody @Valid ProductGetDto.RelatedOptions createDto) {
        Message message = new Message();

        productBusinessService.createProductAndOptions(createDto);
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
     * Patch one api for product
     * <p>
     * <b>PATCH : API URL => /api/v1/product/one</b>
     */
    @PatchMapping("/one")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        productBusinessService.patchOne(productGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
