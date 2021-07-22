package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.service.account_book.AccountBookService;
import com.piaar_store_manager.server.service.product.ProductService;
import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     *
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/{productId}</b>
     *
     * @param productId
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchOne
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productId") Integer productId){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productService.searchOne(productId));
        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     *
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchOne
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList(){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/one</b>
     *
     * @param productGetDto : ProductGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductGetDto
     * @see ProductService#createOne
     */
//    @PostMapping("/one")
//    public ResponseEntity<?> createOne(@RequestBody ProductGetDto productGetDto){
//        Message message = new Message();
//
//        // 유저 로그인 상태체크.
//        if(!userService.isUserLogin()){
//            message.setStatus(HttpStatus.FORBIDDEN);
//            message.setMessage("need_login");
//            message.setMemo("need login");
//        } else {
//            message.setStatus(HttpStatus.OK);
//            message.setMessage("success");
//
//            productService.createOne(productGetDto, userService.getUserId());
//        }
//
//        return new ResponseEntity<>(message, message.getStatus());
//    }

    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductCreateReqDto productCreateReqDto){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");

            productService.createPAO(productCreateReqDto, userService.getUserId());
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create list api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/list</b>
     *
     * @param productGetDto : ProductGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductGetDto
     * @see ProductService#createList
     */
//    @PostMapping("/list")
//    public ResponseEntity<?> createList(@RequestBody List<ProductGetDto> productGetDto){
//        Message message = new Message();
//
//        // 유저 로그인 상태체크.
//        if (!userService.isUserLogin()) {
//            message.setStatus(HttpStatus.FORBIDDEN);
//            message.setMessage("need_login");
//            message.setMemo("need login");
//        } else {
//            message.setStatus(HttpStatus.OK);
//            message.setMessage("success");
//
//            productService.createList(productGetDto, userService.getUserId());
//        }
//
//        return new ResponseEntity<>(message, message.getStatus());
//    }
    
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<ProductCreateReqDto> productCreateReqDtos){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");

            productService.createPAOList(productCreateReqDtos, userService.getUserId());
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) one for product.
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>DELETE : API URL => /api/v1/product/{productId}</b>
     *
     * @param productId : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#destroyOne
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") Integer productId){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");

            productService.destroyOne(productId);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change product
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>PUT : API URL => /api/v1/product</b>
     *
     * @param productGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#patchOne
     */
    @PutMapping("")
    public ResponseEntity<?> changeOne(@RequestBody ProductGetDto productGetDto){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");

            productService.changeOne(productGetDto, userService.getUserId());
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch( Delete or Remove ) product
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>PATCH : API URL => /api/v1/product</b>
     *
     * @param productGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#patchOne
     */
    @PatchMapping("")
    public ResponseEntity<?> patchOne(@RequestBody ProductGetDto productGetDto){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");

            productService.changeOne(productGetDto, userService.getUserId());
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

}
