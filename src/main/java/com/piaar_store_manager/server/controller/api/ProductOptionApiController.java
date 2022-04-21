package com.piaar_store_manager.server.controller.api;

import java.util.Map;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionCreateReqDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionBusinessService;
import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product-option")
@RequiredArgsConstructor
public class ProductOptionApiController {
    private final ProductOptionBusinessService productOptionBusinessService;

    /**
     * Search one api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/one/{productOptionCid}</b>
     *
     * @param productOptionCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionBusinessService#searchOne
     */
    @RequiredLogin
    @GetMapping("/one/{productOptionCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        try {
            message.setData(productOptionBusinessService.searchOne(productOptionCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productOptionCid=" + productOptionCid + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/one-m2oj/{productOptionCId}</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionBusinessService#searchOneM2OJ
     */
    @RequiredLogin
    @GetMapping("/one-m2oj/{productOptionCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        try {
            message.setData(productOptionBusinessService.searchOneM2OJ(productOptionCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productOptionCid=" + productOptionCid + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionBusinessService#searchList
     */
    @RequiredLogin
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list/{productCid}</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductOptionBusinessService#searchListByProduct
     */
    @RequiredLogin
    @GetMapping("/list/{productCid}")
    public ResponseEntity<?> searchListByProduct(@PathVariable(value = "productCid") Integer productCid) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchListByProduct(productCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list-m2oj</b>
     * <p>
     * ProductOption 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductOptionBusinessService#searchListM2OJ
     */
    @RequiredLogin
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchListM2OJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api of status(release & receive) for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/status/{optionCid}</b>
     * <p>
     * ProductOption 데이터의 입출고 현황을 조회한다.
     * 입출고 현황(출고 + 입고 데이터)를 날짜순서로 조회한다.
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductOptionBusinessService#searchStockStatus
     */
    @RequiredLogin
    @GetMapping("/stock/status/{optionCid}")
    public ResponseEntity<?> searchStockStatus(@PathVariable(value = "optionCid") Integer optionCid) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchStockStatus(optionCid));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api of status(release & receive) for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/statusList</b>
     * <p>
     * ProductOption 데이터의 입출고 현황을 조회한다.
     * 입출고 현황(출고 + 입고 데이터)를 날짜순서로 조회한다.
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see ProductOptionBusinessService#searchAllStockStatus
     */
    @RequiredLogin
    @GetMapping("/stock/statusList")
    public ResponseEntity<?> searchAllStockStatus() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchAllStockStatus());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api of status(release & receive) for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/status</b>
     * ProductOption 데이터의 입출고 현황을 조회한다.
     * 입출고 현황(출고 + 입고 데이터)을 파라미터로 넘어온 기간별로 조회한다.
     *
     * @param params : Map::String, Object::
     * @return ResponseEntity(message, HttpStatus)
     * @see ProductOptionBusinessService#searchAllStockStatus
     */
    @RequiredLogin
    @GetMapping("/stock/status")
    public ResponseEntity<?> searchAllStockStatus(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchAllStockStatus(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for productOption.
     * <p>
     * <b>POST : API URL => /api/v1/product/one</b>
     *
     * @param productOptionGetDto : ProductOptionGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isManager
     * @see ProductOptionBusinessService#createOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/one")
    @RequiredLogin
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
        Message message = new Message();

        try {
            productOptionBusinessService.createOne(productOptionGetDto);
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

    @PostMapping("/option-packages")
    @RequiredLogin
    @PermissionRole
    public ResponseEntity<?> createOAP(@RequestBody ProductOptionCreateReqDto createReqDto) {
        Message message = new Message();

        try {
            productOptionBusinessService.createOAP(createReqDto);
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
     * Destroy( Delete or Remove ) one api for productOption.
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>DELETE : API URL => /api/v1/product-option/{productOptionId}</b>
     *
     * @param productOptionCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isSuperAdmin
     * @see ProductOptionBusinessService#destroyOne
     * @see UserService#userDenyCheck
     */
    @DeleteMapping("/one/{productOptionCid}")
    @RequiredLogin
    @PermissionRole(role = "ROLE_SUPERADMIN")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        try {
            productOptionBusinessService.destroyOne(productOptionCid);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for productOption
     * <p>
     * <b>PUT : API URL => /api/v1/product-option/one</b>
     *
     * @param productOptionGetDto : ProductOptionGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isManager
     * @see ProductOptionBusinessService#changeOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    @RequiredLogin
    @PermissionRole
    public ResponseEntity<?> changeOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
        Message message = new Message();

        try {
            productOptionBusinessService.changeOne(productOptionGetDto);
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

    @PutMapping("/option-packages")
    @RequiredLogin
    @PermissionRole
    public ResponseEntity<?> changeOAP(@RequestBody ProductOptionCreateReqDto reqDto) {
        Message message = new Message();

        try {
            productOptionBusinessService.changeOAP(reqDto);
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
     * Patch one api for productOption
     * <p>
     * <b>PATCH : API URL => /api/v1/product-option/one</b>
     *
     * @param productOptionGetDto : ProductOptionGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isManager
     * @see ProductOptionBusinessService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    @RequiredLogin
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductOptionGetDto productOptionGetDto) {
        Message message = new Message();

        try {
            productOptionBusinessService.patchOne(productOptionGetDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (Exception e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage("error");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

}
