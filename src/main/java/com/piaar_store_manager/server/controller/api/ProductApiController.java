package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.service.product.ProductBusinessService;
import com.piaar_store_manager.server.service.user.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     * @param productCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductBusinessService#searchOne
     */
    @GetMapping("/one/{productCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        try {
            message.setData(productBusinessService.searchOne(productCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productCid=" + productCid + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one-m2oj/{productCid}</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductBusinessService#searchOneM2OJ
     */
    @GetMapping("/one-m2oj/{productCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        try {
            message.setData(productBusinessService.searchOneM2OJ(productCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productCid=" + productCid + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one-fj/{productId}</b>
     * <p>
     * Product id 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Full JOIN(fj) 상태를 조회한다.
     *
     * @param productCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductBusinessService#searchOneFJ
     */
    @GetMapping("/one-fj/{productCid}")
    public ResponseEntity<?> searchOneFJ(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        try {
            message.setData(productBusinessService.searchOneFJ(productCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productCid=" + productCid + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserServie#isUserLogin
     * @see ProductBusinessService#searchList
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
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserServie#isUserLogin
     * @see productBusinessService#searchListByCategory
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
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductBusinessService#searchListM2OJ
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
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductBusinessService#searchListFJ
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
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#searchListFJ
     */
    @GetMapping("/list-fj/stock")
    public ResponseEntity<?> searchStockListFJ() {
        Message message = new Message();

        message.setData(productBusinessService.searchStockListFJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/one</b>
     *
     * @param productCreateReqDto : ProductCreateReqDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#createPAO
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/one")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductCreateReqDto productCreateReqDto) {
        Message message = new Message();

        try {
            productBusinessService.createPAO(productCreateReqDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/list</b>
     *
     * @param productCreateReqDto : List::ProductCreateReqDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#createPAOList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/list")
    @PermissionRole
    public ResponseEntity<?> createList(@RequestBody List<ProductCreateReqDto> productCreateReqDtos) {
        Message message = new Message();

        try {
            productBusinessService.createPAOList(productCreateReqDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (DataIntegrityViolationException e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
            message.setMemo("입력된 옵션관리코드 값이 이미 존재합니다.");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for product.
     * <p>
     * <b>DELETE : API URL => /api/v1/product/one/{productId}</b>
     *
     * @param productId : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isSuperAdmin
     * @see ProductBusinessService#destroyOne
     * @see UserService#userDenyCheck
     */
    @PermissionRole(role = "ROLE_SUPERADMIN")
    @DeleteMapping("/one/{productId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") Integer productId) {
        Message message = new Message();

        try {
            productBusinessService.destroyOne(productId);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for product
     * <p>
     * <b>PUT : API URL => /api/v1/product/one</b>
     *
     * @param productGetDto : ProductGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isManager
     * @see ProductBusinessService#changeOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        try {
            productBusinessService.changeOne(productGetDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change list api for product
     * <p>
     * <b>PUT : API URL => /api/v1/product/list</b>
     *
     * @param productGetDto : List::ProductCreateReqDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#changePAOList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/list")
    @PermissionRole
    public ResponseEntity<?> changePAOList(@RequestBody List<ProductCreateReqDto> productCreateReqDto) {
        Message message = new Message();

        try {
            productBusinessService.changePAOList(productCreateReqDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for product
     * <p>
     * <b>PATCH : API URL => /api/v1/product/one</b>
     *
     * @param productGetDto : ProductGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        try {
            productBusinessService.patchOne(productGetDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
