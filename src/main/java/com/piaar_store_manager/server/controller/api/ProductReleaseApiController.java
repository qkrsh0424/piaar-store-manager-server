package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;
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
    
    @Autowired
    private ProductReleaseService productReleaseService;

    @Autowired
    private UserService userService;

    /**
     * Search one api for productRelease.
     * <p>
     * <b>GET : API URL => /api/v1/product-release/one/{productReleaseCid}</b>
     *
     * @param productReleaseCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseService#searchOne
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
                message.setData(productReleaseService.searchOne(productReleaseCid));
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
     * <b>GET : API URL => /api/v1/product-release/one-m2oj/{productCid}</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductRelease와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     * 
     * @param productReleaseCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReleaseService#searchOneM2OJ
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
                message.setData(productReleaseService.searchOneM2OJ(productReleaseCid));
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
     * @see productReleaseService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productReleaseService.searchList());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
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
     * @see productReleaseService#searchListM2OJ
     */
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productReleaseService.searchListM2OJ());
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
     * @see ProductReleaseService#createPR
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseService.createPR(productReleaseGetDto, userService.getUserId());
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
     * @see ProductReleaseService#createPRList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseService.createPRList(productReleaseGetDtos, userService.getUserId());
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
     * <b>DELETE : API URL => /api/v1/product-release/one/{productreleaseCid}</b>
     *
     * @param productReleaseCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseService#destroyOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @DeleteMapping("/one/{productReleaseCid}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReleaseCid") Integer productReleaseCid) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseService.destroyOne(productReleaseCid, userService.getUserId());
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
     * @see ProductReleaseService#changeOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody ProductReleaseGetDto releaseDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseService.changeOne(releaseDto, userService.getUserId());
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
     * @see productReleaseService#changeList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/list")
    public ResponseEntity<?> changeList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseService.changeList(productReleaseGetDtos, userService.getUserId());
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
     * Patch( Delete or Remove ) one api for productRelease
     * <p>
     * <b>PATCH : API URL => /api/v1/product-release/one</b>
     *
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReleaseService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReleaseService.patchOne(productReleaseGetDto, userService.getUserId());
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
