package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.service.product.ProductBusinessService;
import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductApiController {
    private ProductBusinessService productBusinessService;
    private UserService userService;

    @Autowired
    public ProductApiController(
        ProductBusinessService productBusinessService,
        UserService userService
    ) {
        this.productBusinessService = productBusinessService;
        this.userService = userService;
    }

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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productBusinessService.searchOne(productCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productCid=" + productCid + " value.");
            }
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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productBusinessService.searchOneM2OJ(productCid));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productCid=" + productCid + " value.");
            }
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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(productBusinessService.searchOneFJ(productCid));
                message.setStatus(HttpStatus.OK);
                 message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("Not found productCid=" + productCid + " value.");
            }
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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productBusinessService.searchList());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productBusinessService.searchListByCategory(categoryCid));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productBusinessService.searchListM2OJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productBusinessService.searchListFJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

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

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            message.setData(productBusinessService.searchStockListFJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

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
    public ResponseEntity<?> createOne(@RequestBody ProductCreateReqDto productCreateReqDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productBusinessService.createPAO(productCreateReqDto, userService.getUserId());
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
    public ResponseEntity<?> createList(@RequestBody List<ProductCreateReqDto> productCreateReqDtos) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productBusinessService.createPAOList(productCreateReqDtos, userService.getUserId());
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
    @DeleteMapping("/one/{productId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") Integer productId) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isSuperAdmin()) {
            try{
                productBusinessService.destroyOne(productId);
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
    public ResponseEntity<?> changeOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productBusinessService.changeOne(productGetDto, userService.getUserId());
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
    public ResponseEntity<?> changePAOList(@RequestBody List<ProductCreateReqDto> productCreateReqDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productBusinessService.changePAOList(productCreateReqDto, userService.getUserId());
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
     * @param productGetDto : ProductGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#patchOne
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        //유저의 권한을 체크한다.
        if (userService.isManager()) {
            try{
                productBusinessService.patchOne(productGetDto, userService.getUserId());
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
