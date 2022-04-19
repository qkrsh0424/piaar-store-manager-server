package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveBusinessService;
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
@RequestMapping("/api/v1/product-receive")
public class ProductReceiveApiController {
    private ProductReceiveBusinessService productReceiveBusinessService;
    private UserService userService;

    @Autowired
    public ProductReceiveApiController(
        ProductReceiveBusinessService productReceiveBusinessService,
        UserService userService
    ) {
        this.productReceiveBusinessService = productReceiveBusinessService;
        this.userService = userService;
    }

    /**
     * Search one api for productReceive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/one/{productReceiveCid}</b>
     *
     * @param productReceiveCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#searchOne
     */
    @GetMapping("/one/{productReceiveCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productReceiveCid") Integer productReceiveCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productReceiveBusinessService.searchOne(productReceiveCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productReceiveCid=" + productReceiveCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for productReceive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/one-m2oj/{productReceiveCid}</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     * 
     * @param productReceiveCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#searchOneM2OJ
     */
    @GetMapping("/one-m2oj/{productReceiveCid}")
    public ResponseEntity<?> searchOneM2OJ(@PathVariable(value = "productReceiveCid") Integer productReceiveCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productReceiveBusinessService.searchOneM2OJ(productReceiveCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productReceiveCid=" + productReceiveCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productReceive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productReceiveBusinessService.searchList());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for productReceive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list/{productOptionCid}</b>
     *
     * @param productOptionCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#searchList
     */
    @GetMapping("/list/{productOptionCid}")
    public ResponseEntity<?> searchListByOptionCid(@PathVariable(value = "productOptionCid") Integer productOptionCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productReceiveBusinessService.searchListByOptionCid(productOptionCid));
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
     * Search list api for productReceive.
     * <p>
     * <b>GET : API URL => /api/v1/product-receive/list-m2oj</b>
     * ProductReceive 데이터를 조회한다.
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#searchListM2OJ
     */
    @GetMapping("/list-m2oj")
    public ResponseEntity<?> searchListM2OJ() {
        Message message = new Message();
        
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productReceiveBusinessService.searchListM2OJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for productReceive.
     * And Update list api for productOption.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/one</b>
     * 
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#createPR
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReceiveBusinessService.createPR(productReceiveGetDto, userService.getUserId());
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
     * Create list api for productReceive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/list</b>
     * 
     * @param productReceiveGetDtos : List::ProductReceiveGetDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#createPRList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReceiveBusinessService.createPRList(productReceiveGetDtos);
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
     * Destroy( Delete or Remove ) one api for productReceive.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-receive/one/{productReceiveCid}</b>
     *
     * @param productReceiveCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#destroyOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @DeleteMapping("/one/{productReceiveCid}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReceiveCid") Integer productReceiveCid) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReceiveBusinessService.destroyOne(productReceiveCid, userService.getUserId());
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
     * Change one api for productReceive
     * <p>
     * <b>PUT : API URL => /api/v1/product-receive/one</b>
     *
     * @param receiveDto : ProductReceiveGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#changeOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody ProductReceiveGetDto receiveDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReceiveBusinessService.changeOne(receiveDto, userService.getUserId());
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
     * Change list api for productReceive
     * <p>
     * <b>PUT : API URL => /api/v1/product-receive/list</b>
     *
     * @param productReceiveGetDtos : List::ProductReceiveGetDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see productReceiveBusinessService#changeList
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/list")
    public ResponseEntity<?> changeList(@RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReceiveBusinessService.changeList(productReceiveGetDtos, userService.getUserId());
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
     * Patch one api for productReceive
     * <p>
     * <b>PATCH : API URL => /api/v1/product-receive/one</b>
     *
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductReceiveBusinessService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productReceiveBusinessService.patchOne(productReceiveGetDto, userService.getUserId());
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
