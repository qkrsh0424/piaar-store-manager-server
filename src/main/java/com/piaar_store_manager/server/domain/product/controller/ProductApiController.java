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
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one-m2oj/{productCid}</b>
     * <p>
     * productCid에 대응하는 product, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     */
    // @GetMapping("/one-m2oj/{productCid}")
    // public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productCid") Integer productCid) {
    //     Message message = new Message();

    //     message.setData(productBusinessService.searchOneM2OJ(productCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one-fj/{productId}</b>
     * <p>
     * productCid에 대응하는 product, product와 Full Join(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     */
    @GetMapping("/one-fj/{productCid}")
    public ResponseEntity<?> searchOneFJ(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        message.setData(productBusinessService.searchOneFJ(productCid));
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

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list-m2oj</b>
     * <p>
     * 모든 product, product와 Many To One Join(m2oj) 연관관계에 놓여있는 user, category을 함께 조회한다.
     */
    // @GetMapping("/list-m2oj")
    // public ResponseEntity<?> searchListM2OJ() {
    //     Message message = new Message();

    //     message.setData(productBusinessService.searchListM2OJ());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list-fj</b>
     * <p>
     * 모든 product, product와 Full Join(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     */
    @GetMapping("/list-fj")
    public ResponseEntity<?> searchListFJ() {
        Message message = new Message();

        message.setData(productBusinessService.searchListFJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list-fj</b>
     * <p>
     * 재고관리 여부가 true인 product 조회, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     */
    @GetMapping("/list-fj/stock")
    public ResponseEntity<?> searchStockListFJ() {
        Message message = new Message();

        message.setData(productBusinessService.searchStockListFJ());
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
