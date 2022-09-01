package com.piaar_store_manager.server.domain.product_option.controller;

import java.util.Map;
import java.util.UUID;

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
@RequestMapping("/api/v1/product-options")
@RequiredArgsConstructor
@RequiredLogin
public class ProductOptionApiController {
    private final ProductOptionBusinessService productOptionBusinessService;

    /**
     * Search one api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-options/one/{productOptionCid}</b>
     */
    // Deprecated
    // @GetMapping("/one/{productOptionCid}")
    // public ResponseEntity<?> searchOne(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchOne(productOptionCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search one api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/one-m2oj/{productOptionCId}</b>
     * <p>
     * productOptionCid에 대응하는 option, option와 Many To One JOIN(m2oj) 연관관계에 놓여있는 product, user, category를 함께 조회한다.
     */
    // Deprecated
    // @GetMapping("/one-m2oj/{productOptionCid}")
    // public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchOneM2OJ(productOptionCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search one api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-options/{productOptionId}</b>
     * 
     * @param params : Map::String, Object:: [objectType]
     */
    // Unused API
    @GetMapping("/{productOptionId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productOptionId") UUID optionId, @RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchOne(optionId, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list</b>
     */
    // Deprecated
    // @GetMapping("/list")
    // public ResponseEntity<?> searchList() {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchList());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    // /**
    //  * Search list api for product option.
    //  * <p>
    //  * <b>GET : API URL => /api/v1/product-option/list-m2oj</b>
    //  * <p>
    //  * 모든 option, option과 Many To One Join(m2oj) 연관관계에 놓여있는 product, user, category을 함께 조회한다.
    //  */
    // Deprecated
    // @GetMapping("/list-m2oj")
    // public ResponseEntity<?> searchListM2OJ() {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchListM2OJ());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search all api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-options/all</b>
     * 
     * @param params : Map::String, Object:: [objectType]
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAll(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchAll(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-options/list/{productCid}</b>
     */
    // Deprecated
    // @GetMapping("/list/{productCid}")
    // public ResponseEntity<?> searchListByProduct(@PathVariable(value = "productCid") Integer productCid) {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchListByProductCid(productCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }
    
    /**
     * Search api for option matching product cid.
     * <p>
     * <b>GET : API URL => /api/v1/product-options/batch/{productCid}</b>
     * 
     * @param params : Map::String, Object:: [objectType]
     */
    // Unused API
    @GetMapping("/batch/{productCid}")
    public ResponseEntity<?> searchBatchByProductCid(@PathVariable(value = "productCid") Integer productCid, @RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchBatchByProductCid(productCid, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api of status(release & receive) for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/status/{optionCid}</b>
     * <p>
     * optionCid에 대응하는 option의 모든 receive(입고), release(출고) 데이터를 조회한다.
     */
    // @GetMapping("/stock/status/{optionCid}")
    // public ResponseEntity<?> searchStockStatus(@PathVariable(value = "optionCid") Integer optionCid) {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchStockStatus(optionCid));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    // @GetMapping("/stock/status/{optionCid}")
    // public ResponseEntity<?> searchStockStatus(@PathVariable(value = "optionCid") Integer optionCid, @RequestParam Map<String, Object> params) {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchStockStatus(optionCid, params));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api of status(release & receive) for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/status/list</b>
     * <p>
     * 모든 option 조회, 해당 option의 모든 receive(입고), release(출고) 데이터를 조회한다.
     */
    // @GetMapping("/stock/status/list")
    // public ResponseEntity<?> searchAllStockStatus() {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchAllStockStatus());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search list api of status(release & receive) for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/status</b>
     * <p>
     * 모든 option 조회, 해당 option의 startDate와 endDate기간 사이에 등록된 receive(입고), release(출고) 데이터를 조회한다.
     */
    
    // @GetMapping("/stock/status")
    // public ResponseEntity<?> searchStockStatus(@RequestParam Map<String, Object> params) {
    //     Message message = new Message();

    //     message.setData(productOptionBusinessService.searchStockStatus(params));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Search api of status(release & receive) for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-options/stock/status</b>
     * <p>
     * option의 모든 receive(입고), release(출고) 데이터를 조회한다.
     * 
     * @param params : Map::String, Object:: [objectType, (optionCid)]
     */
    @GetMapping("/stock/status")
    public ResponseEntity<?> searchForStockStatus(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchForStockStatus(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product option.
     * <p>
     * <b>POST : API URL => /api/v1/product-options/one</b>
     */
    // @PostMapping("/one")
    // @PermissionRole
    // public ResponseEntity<?> createOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
    //     Message message = new Message();

    //     productOptionBusinessService.createOne(productOptionGetDto);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Create one api for product option.
     * <p>
     * <b>POST : API URL => /api/v1/product-options</b>
     */
    @PostMapping("")
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
     * <b>POST : API URL => /api/v1/product-options/option-packages</b>
     * <p>
     * 단일 option, option 하위의 다중 package를 등록한다.
     */
    @PostMapping("/option-packages")
    @PermissionRole
    public ResponseEntity<?> createOptionAndPackages(@RequestBody ProductOptionGetDto.CreateReq createReqDto) {
        Message message = new Message();

        productOptionBusinessService.createOptionAndPackages(createReqDto);
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
    // @DeleteMapping("/one/{productOptionCid}")
    // @PermissionRole(role = "ROLE_SUPERADMIN")
    // public ResponseEntity<?> destroyOne(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
    //     Message message = new Message();

    //     productOptionBusinessService.destroyOne(productOptionCid);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Destroy( Delete or Remove ) one api for product option.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-option/{optionId}</b>
     * <p>
     */
    @DeleteMapping("/{optionId}")
    @PermissionRole(role = "ROLE_SUPERADMIN")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "optionId") UUID optionId) {
        Message message = new Message();

        productOptionBusinessService.destroyOne(optionId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for product option.
     * <p>
     * <b>PUT : API URL => /api/v1/product-options/one</b>
     * <p>
     */
    // @PutMapping("/one")
    // @PermissionRole
    // public ResponseEntity<?> changeOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
    //     Message message = new Message();

    //     productOptionBusinessService.changeOne(productOptionGetDto);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Change one api for product option.
     * <p>
     * <b>PUT : API URL => /api/v1/product-options</b>
     * <p>
     */
    @PutMapping("")
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
     * <b>PUT : API URL => /api/v1/product-options/option-packages</b>
     * <p>
     * 단일 option, option 하위의 다중 package를 수정한다.
     */
    // TODO :: 옵션과 옵션패키지 분리해야 할 듯
    @PutMapping("/option-packages")
    @PermissionRole
    public ResponseEntity<?> changeOptionAndPackages(@RequestBody ProductOptionGetDto.CreateReq reqDto) {
        Message message = new Message();

        productOptionBusinessService.changeOptionAndPackages(reqDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for product option.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-options</b>
     * <p>
     */
    // Unused API
    @PatchMapping("")
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
     * <b>GET : API URL => /api/v1/product-options/release-location</b>
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
     * <b>GET : API URL => /api/v1/product-options/stock-cycle</b>
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
