package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_release.ProductReleaseBusinessService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductReleaseApiController {
    private ProductReleaseBusinessService productReleaseBusinessService;
    private UserService userService;

    @Autowired
    public ProductReleaseApiController(
        ProductReleaseBusinessService productReleaseBusinessService,
        UserService userService
    ) {
        this.productReleaseBusinessService = productReleaseBusinessService;
        this.userService = userService;
    }

    /**
     * Search one api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/one/{productReleaseCid}</b>
     *
     * @param productReleaseCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#searchOne
     */
    @GetMapping("/one/{productReleaseCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productReleaseBusinessService.searchOne(productReleaseCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productReleaseCid=" + productReleaseCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/one-m2oj/{productReleaseCid}</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductRelease와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     * 
     * @param productReleaseCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#searchOneM2OJ
     */
    @GetMapping("/one-m2oj/{productReleaseCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productReleaseBusinessService.searchOneM2OJ(productReleaseCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productReleaseCid=" + productReleaseCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productReleaseBusinessService.searchList());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/list/{productOptionCid}</b>
     *
     * @param productOptionCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseBusinessService#searchListByOptionCid
     */
    @GetMapping("/list/{productOptionCid}")
    public ResponseEntity<?> searchList(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productReleaseBusinessService.searchListByOptionCid(productOptionCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productOptionCid=" + productOptionCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/test/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#searchList
     */
    @GetMapping("/test/list")
    public ResponseEntity<?> searchReleaseList() {
        Message message = new Message();
        message.setData(productReleaseBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search test list api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/test/list/{productOptionCid}</b>
     *
     * @param productOptionCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseBusinessService#searchListByOptionCid
     */
    @GetMapping("/test/list/{productOptionCid}")
    public ResponseEntity<?> searchReleaseList(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        try {
            message.setData(productReleaseBusinessService.searchListByOptionCid(productOptionCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productOptionCid=" + productOptionCid + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/list-m2oj</b>
     * ProductRelease 데이터를 조회한다.
     * 해당 ProductRelease와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#searchListM2OJ
     */
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productReleaseBusinessService.searchListM2OJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for productRelease.
     * <p>
     * <b>POST : API URL => /api/v1/product-release/one</b>
     * 
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#createPL
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseBusinessService.createPL(productReleaseGetDto);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for productRelease.
     * <p>
     * <b>POST : API URL => /api/v1/product-release/list</b>
     * 
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#createPLList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseBusinessService.createPLList(productReleaseGetDtos);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one api for productRelease.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-release/one/{productReleaseCid}</b>
     *
     * @param productReleaseCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseBusinessService#destroyOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @DeleteMapping("/one/{productReleaseCid}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseBusinessService.destroyOne(productReleaseCid, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for productRelease
     * <p>
     * <b>PUT : API URL => /api/v1/product-release/one</b>
     *
     * @param releaseDto : ProductReleaseGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseBusinessService#changeOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody ProductReleaseGetDto releaseDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseBusinessService.changeOne(releaseDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change list api for productRelease
     * <p>
     * <b>PUT : API URL => /api/v1/product-release/list</b>
     *
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseBusinessService#changeList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/list")
    public ResponseEntity<?> changeList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseBusinessService.changeList(productReleaseGetDtos, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for productRelease
     * <p>
     * <b>PATCH : API URL => /api/v1/product-release/one</b>
     *
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseBusinessService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseBusinessService.patchOne(productReleaseGetDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        } else {
            userService.userDenyCheck(message);
        }
        
        return new ResponseEntity<>(message, message.getStatus());
    }
}
