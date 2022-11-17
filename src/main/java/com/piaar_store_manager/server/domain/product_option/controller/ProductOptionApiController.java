package com.piaar_store_manager.server.domain.product_option.controller;

import java.util.Map;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionBusinessService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product-option")
@RequiredArgsConstructor
@RequiredLogin
public class ProductOptionApiController {
    private final ProductOptionBusinessService productOptionBusinessService;

    /**
     * Search one api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/one/{productOptionCid}</b>
     */
    @GetMapping("/one/{productOptionCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchOne(productOptionCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/one-m2oj/{productOptionCId}</b>
     * <p>
     * productOptionCid에 대응하는 option, option와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, user, category를 함께 조회한다.
     */
    @GetMapping("/one-m2oj/{productOptionCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchOneM2OJ(productOptionCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list</b>
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list/{productCid}</b>
     */
    @GetMapping("/list/{productCid}")
    public ResponseEntity<?> searchListByProduct(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchListByProductCid(productCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list-m2oj</b>
     * <p>
     * 모든 option, option과 Many To One Join(m2oj) 연관관계에 놓여있는 product, user, category을 함께 조회한다.
     */
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchListM2OJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product option.
     * <p>
     * <b>POST : API URL => /api/v1/product-option/one</b>
     */
    @PostMapping("/one")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
        Message message = new Message();

        productOptionBusinessService.createOne(productOptionGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product option.
     * <p>
     * <b>POST : API URL => /api/v1/product-option/option-packages</b>
     * <p>
     * 단일 option, option 하위의 다중 package를 등록한다.
     */
    @PostMapping("/option-packages")
    @PermissionRole
    public ResponseEntity<?> createOAP(@RequestBody ProductOptionGetDto.CreateReq createReqDto) {
        Message message = new Message();

        productOptionBusinessService.createOAP(createReqDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for product option.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-option/one/{productOptionCid}</b>
     * <p>
     */
    @DeleteMapping("/one/{productOptionCid}")
    @PermissionRole(role = "ROLE_SUPERADMIN")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        productOptionBusinessService.destroyOne(productOptionCid);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for product option.
     * <p>
     * <b>PUT : API URL => /api/v1/product-option/one</b>
     * <p>
     */
    @PutMapping("/one")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
        Message message = new Message();

        productOptionBusinessService.changeOne(productOptionGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for product option.
     * <p>
     * <b>PUT : API URL => /api/v1/product-option/option-packages</b>
     * <p>
     * 단일 option, option 하위의 다중 package를 수정한다.
     */
    @PutMapping("/option-packages")
    @PermissionRole
    public ResponseEntity<?> changeOAP(@RequestBody ProductOptionGetDto.CreateReq reqDto) {
        Message message = new Message();

        productOptionBusinessService.changeOAP(reqDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for product option.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-option/one</b>
     * <p>
     */
    @PatchMapping("/one")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
        Message message = new Message();

        productOptionBusinessService.patchOne(productOptionGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search api for release location of produc option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/release-location</b>
     * <p>
     */
    @GetMapping("/release-location")
    public ResponseEntity<?> searchRelaseLocation() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchReleaseLocation());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search api for stock status by week.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock-cycle</b>
     * <p>
     * @param params Map::String, Object:: [searchEndDate, categoryCid]
     */
    @GetMapping("/stock-cycle")
    public ResponseEntity<?> searchStockCycle(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchStockCycle(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
