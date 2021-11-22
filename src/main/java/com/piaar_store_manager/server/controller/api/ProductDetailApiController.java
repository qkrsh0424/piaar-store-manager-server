package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product_detail.dto.ProductDetailGetDto;
import com.piaar_store_manager.server.service.product_detail.ProductDetailBusinessService;
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
@RequestMapping("/api/v1/product-detail")
public class ProductDetailApiController {
    private ProductDetailBusinessService productDetailBusinessService;
    private UserService userService;

    @Autowired
    public ProductDetailApiController(
        ProductDetailBusinessService productDetailBusinessService,
        UserService userService
    ) {
        this.productDetailBusinessService = productDetailBusinessService;
        this.userService = userService;
    }



    /**
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/one/{detailCid}</b>
     *
     * @param detailCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductDetailBusinessService#searchOne
     */
    @GetMapping("/one/{detailCid}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "detailCid") Integer detailCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productDetailBusinessService.searchOne(detailCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found detailCid=" + detailCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
    
    /**
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product-detail/list/{optionCid}</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductDetailBusinessService#searchList
     */
    @GetMapping("/list/{optionCid}")
    public ResponseEntity<?> searchListByOptionCid(@PathVariable(value = "optionCid") Integer optionCid) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productDetailBusinessService.searchList(optionCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found optionCid=" + optionCid + " value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product-detail/one</b>
     * 
     * @param productDetailGetDto : ProductDetailGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductDetailBusinessService#createOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productDetailBusinessService.createOne(productDetailGetDto, userService.getUserId());
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
     * Destroy( Delete or Remove ) one api for product detail.
     * <p>
     * <b>DELETE : API URL => /api/v1/product-detail/one/{detailCid}</b>
     *
     * @param detailCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductDetailBusinessService#destroyOne
     * @see UserService#userDenyCheck
     */
    @DeleteMapping("/one/{detailCid}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "detailCid") Integer detailCid) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productDetailBusinessService.destroyOne(detailCid);
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
     * Change one api for product
     * <p>
     * <b>PUT : API URL => /api/v1/product-detail/one</b>
     *
     * @param productDetailGetDto : ProductDetailGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductDetailBusinessService#changeOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productDetailBusinessService.changeOne(productDetailGetDto, userService.getUserId());
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
     * Patch( Delete or Remove ) one api for product
     * <p>
     * <b>PATCH : API URL => /api/v1/product/one</b>
     *
     * @param productDetailGetDto : ProductDetailGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductDetailBusinessService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductDetailGetDto productDetailGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productDetailBusinessService.patchOne(productDetailGetDto, userService.getUserId());
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
