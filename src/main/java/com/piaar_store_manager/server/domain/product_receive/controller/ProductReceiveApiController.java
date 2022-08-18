package com.piaar_store_manager.server.domain.product_receive.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveBusinessService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-receive")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReceiveApiController {
    private final ProductReceiveBusinessService productReceiveBusinessService;

    /**
     * Search one api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/one/{productReceiveCid}</b>
     */
    // deprecated
    // @GetMapping("/one/{productReceiveCid}")
    // public ResponseEntity<?> searchOne(@PathVariable(value = "productReceiveCid") Integer productReceiveCid) {
    //     Message message = new Message();

    //     message.setData(productReceiveBusinessService.searchOne(productReceiveCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/one-m2oj/{productReceiveCid}</b>
     * <p>
     * productReceiveCid에 대응하는 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     */
    // deprecated
    // @GetMapping("/one-m2oj/{productReceiveCid}")
    // public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productReceiveCid") Integer productReceiveCid) {
    //     Message message = new Message();

    //     message.setData(productReceiveBusinessService.searchOneM2OJ(productReceiveCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search one api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/{productReceiveCid}</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @GetMapping("{productReceiveId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productReceiveId") UUID productReceiveId, @RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productReceiveBusinessService.searchOne(productReceiveId, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list</b>
     */
    // deprecated
    // @GetMapping("/list")
    // public ResponseEntity<?> searchList() {
    //     Message message = new Message();

    //     message.setData(productReceiveBusinessService.searchList());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list-m2oj</b>
     * <p>
     * 모든 receive, receive와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, option, category, user를 함께 조회한다.
     */
    // deprecated
    // @GetMapping("/list-m2oj")
    // public ResponseEntity<?> searchListM2OJ() {
    //     Message message = new Message();

    //     message.setData(productReceiveBusinessService.searchListM2OJ());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list-m2oj</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @GetMapping("/list")
    public ResponseEntity<?> searchList(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productReceiveBusinessService.searchList(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for receive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list/{productOptionCid}</b>
     */
    // deprecated
    // @GetMapping("/list/{productOptionCid}")
    // public ResponseEntity<?> searchListByOptionCid(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
    //     Message message = new Message();

    //     message.setData(productReceiveBusinessService.searchListByOptionCid(productOptionCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api for receive matching option cid.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list/{productOptionCid}</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @GetMapping("/list/{productOptionCid}")
    public ResponseEntity<?> searchListByOptionCid(@PathVariable(value = "productOptionCid") Integer productOptionCid, @RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productReceiveBusinessService.searchListByOptionCid(productOptionCid, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/one</b>
     */
    // deprecated
    // @PostMapping("/one")
    // @PermissionRole
    // public ResponseEntity<?> createOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
    //     Message message = new Message();

    //     productReceiveBusinessService.createOne(productReceiveGetDto);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Create one api for receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestParam Map<String, Object> params, @RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

        productReceiveBusinessService.createOne(params, productReceiveGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/list</b>
     */
    // @PostMapping("/list")
    // @PermissionRole
    // public ResponseEntity<?> createList(@RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
    //     Message message = new Message();

    //     productReceiveBusinessService.createList(productReceiveGetDtos);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Create list api for receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/list</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    @PostMapping("/list")
    @PermissionRole
    public ResponseEntity<?> createList(@RequestParam Map<String, Object> params, @RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
        Message message = new Message();

        productReceiveBusinessService.createList(params, productReceiveGetDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for receive.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-receive/one/{productReceiveCid}</b>
     */
    // deprecated
    // @DeleteMapping("/one/{productReceiveCid}")
    // @PermissionRole
    // public ResponseEntity<?> destroyOne(@PathVariable(value = "productReceiveCid") Integer productReceiveCid) {
    //     Message message = new Message();

    //     productReceiveBusinessService.destroyOne(productReceiveCid);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Destroy( Delete or Remove ) one api for receive.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-receive/{productReceiveCid}</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @DeleteMapping("{productReceiveId}")
    @PermissionRole
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReceiveId") UUID productReceiveId, @RequestParam Map<String, Object> params) {
        Message message = new Message();

        productReceiveBusinessService.destroyOne(productReceiveId, params);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for receive.
     * <p>
     * <b>PUT : API URL => /api/v1/product-receive/one</b>
     */
    // deprecated
    // @PutMapping("/one")
    // @PermissionRole
    // public ResponseEntity<?> changeOne(@RequestBody ProductReceiveGetDto receiveDto) {
    //     Message message = new Message();

    //     productReceiveBusinessService.changeOne(receiveDto);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Change one api for receive.
     * <p>
     * <b>PUT : API URL => /api/v1/product-receive</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestParam Map<String, Object> params, @RequestBody ProductReceiveGetDto receiveDto) {
        Message message = new Message();

        productReceiveBusinessService.changeOne(params, receiveDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change list api for receive.
     * <p>
     * <b>PUT : API URL => /api/v1/product-receive/list</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @PutMapping("/list")
    @PermissionRole
    public ResponseEntity<?> changeList(@RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
        Message message = new Message();

        productReceiveBusinessService.changeList(productReceiveGetDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for receive.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-receive/one</b>
     */
    // deprecated
    // @PatchMapping("/one")
    // @PermissionRole
    // public ResponseEntity<?> patchOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
    //     Message message = new Message();

    //     productReceiveBusinessService.patchOne(productReceiveGetDto);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Patch one api for receive.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-receive</b>
     * 
     * @param params : Map[String, Object] (objectType)
     */
    // Unused API
    @PatchMapping("")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestParam Map<String, Object> params, @RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

        productReceiveBusinessService.patchOne(params, productReceiveGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
