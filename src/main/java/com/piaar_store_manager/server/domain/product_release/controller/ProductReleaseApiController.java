package com.piaar_store_manager.server.domain.product_release.controller;

import java.util.List;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseBusinessService;
import com.piaar_store_manager.server.domain.user.service.UserService;

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
@RequestMapping("/api/v1/product-release")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReleaseApiController {
    private final ProductReleaseBusinessService productReleaseBusinessService;

    /**
     * Search one api for release.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/one/{productReleaseCid}</b>
     */
    @GetMapping("/one/{productReleaseCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        message.setData(productReleaseBusinessService.searchOne(productReleaseCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for release.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/one-m2oj/{productReleaseCid}</b>
     * <p>
     * productReleaseCid에 대응하는 release, release와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     */
    @GetMapping("/one-m2oj/{productReleaseCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        message.setData(productReleaseBusinessService.searchOneM2OJ(productReleaseCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for release.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/list</b>
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productReleaseBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for release.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/list/{productOptionCid}</b>
     */
    @GetMapping("/list/{productOptionCid}")
    public ResponseEntity<?> searchListByOptionCid(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        message.setData(productReleaseBusinessService.searchListByOptionCid(productOptionCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for release.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/list-m2oj</b>
     * <p>
     * 모든 release, release와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     */
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();

        message.setData(productReleaseBusinessService.searchListM2OJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for release.
     * <p>
     * <b>POST : API URL => /api/v1/product-release/one</b>
     */
    @PostMapping("/one")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        productReleaseBusinessService.createOne(productReleaseGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for release.
     * <p>
     * <b>POST : API URL => /api/v1/product-release/list</b>
     */
    @PostMapping("/list")
    @PermissionRole
    public ResponseEntity<?> createList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        productReleaseBusinessService.createList(productReleaseGetDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for release.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-release/one/{productReleaseCid}</b>
     */
    @DeleteMapping("/one/{productReleaseCid}")
    @PermissionRole
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        productReleaseBusinessService.destroyOne(productReleaseCid);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for release
     * <p>
     * <b>PUT : API URL => /api/v1/product-release/one</b>
     */
    @PutMapping("/one")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductReleaseGetDto releaseDto) {
        Message message = new Message();

        productReleaseBusinessService.changeOne(releaseDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change list api for release
     * <p>
     * <b>PUT : API URL => /api/v1/product-release/list</b>
     */
    @PutMapping("/list")
    @PermissionRole
    public ResponseEntity<?> changeList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        productReleaseBusinessService.changeList(productReleaseGetDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for release
     * <p>
     * <b>PATCH : API URL => /api/v1/product-release/one</b>
     */
    @PatchMapping("/one")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        productReleaseBusinessService.patchOne(productReleaseGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
