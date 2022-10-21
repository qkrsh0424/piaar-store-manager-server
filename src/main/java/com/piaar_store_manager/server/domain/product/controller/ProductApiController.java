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

import java.util.List;
import java.util.Map;
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
    @GetMapping("/one-m2oj/{productCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        message.setData(productBusinessService.searchOneM2OJ(productCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

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
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();

        message.setData(productBusinessService.searchListM2OJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

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

    // 22.10.11 FEAT
    // 재고관리 페이지에서 조회할 데이터
    @GetMapping("/batch/stock")
    public ResponseEntity<?> searchBatch(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productBusinessService.searchBatch(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // 22.10.17 FEAT
    // 재고관리 페이지에서 조회할 데이터 - 페이징처리
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
     * <p>
     * <b>POST : API URL => /api/v1/product/list</b>
     */
    // @PostMapping("/list")
    // @PermissionRole
    // public ResponseEntity<?> createList(@RequestBody List<ProductGetDto.CreateReq> productCreateReqDtos) {
    //     Message message = new Message();

    //     try {
    //         productBusinessService.createPAOList(productCreateReqDtos);
    //         message.setStatus(HttpStatus.OK);
    //         message.setMessage("success");
    //     } catch (DataIntegrityViolationException e) {
    //         message.setStatus(HttpStatus.BAD_REQUEST);
    //         message.setMessage("error");
    //         message.setMemo("입력된 옵션관리코드 값이 이미 존재합니다.");
    //     }

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/one</b>
     * <p>
     * 단일 product, product 하위의 다중 option, option 하위의 다중 package를 등록한다.
     */
    // @PostMapping("/one")
    // @PermissionRole
    // public ResponseEntity<?> createOne(@RequestBody ProductGetDto.CreateReq productCreateReqDto) {
    //     Message message = new Message();

    //     productBusinessService.createPAO(productCreateReqDto);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

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
    @PutMapping("/options")
    @PermissionRole
    public ResponseEntity<?> updateProductAndOptions(@RequestBody @Valid ProductGetDto.ProductAndOptions createDto) {
        Message message = new Message();

        productBusinessService.updateProductAndOptions(createDto);
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

    /**
     * Destroy( Delete or Remove ) one api for product.
     * <p>
     * <b>DELETE : API URL => /api/v1/product/one/{productId}</b>
     */
    // @PermissionRole(role = "ROLE_SUPERADMIN")
    // @DeleteMapping("/one/{productId}")
    // public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") Integer productId) {
    //     Message message = new Message();

    //     productBusinessService.destroyOne(productId);
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }
    // @PermissionRole(role = "ROLE_SUPERADMIN")
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
     * <b>PUT : API URL => /api/v1/product/one</b>
     */
    @PutMapping("/one")
    @PermissionRole
    public ResponseEntity<?> changePAO(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        productBusinessService.changePAO(productGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change list api for product
     * <p>
     * <b>PUT : API URL => /api/v1/product/list</b>
     */
    @PutMapping("/list")
    @PermissionRole
    public ResponseEntity<?> changePAOList(@RequestBody List<ProductGetDto.CreateReq> productCreateReqDto) {
        Message message = new Message();

        productBusinessService.changePAOList(productCreateReqDto);
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
