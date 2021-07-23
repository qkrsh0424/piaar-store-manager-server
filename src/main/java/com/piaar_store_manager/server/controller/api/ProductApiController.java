package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
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
    public ResponseEntity<?> searchOne(@PathVariable(value = "productId") Integer productId) {
        // TODO : 만약에 해당 productId의 자원이 없다면 404 에러를 반환해야 합니다. o
        // 현재 productId가 존재하지 않는 데이터임에도 불구하고 200 코드를 리턴하고 있습니다.
        // ========= 이러한 방법으로 널값을 NOT FOUNT 404 에러를 던저서 자원이 없다는것을 표시가능 =========
        // try {
        //     message.setData(productService.searchOne(productId));
        //     message.setStatus(HttpStatus.OK);
        //     message.setMessage("success");
        //     return new ResponseEntity<>(message, message.getStatus());
        // } catch (NullPointerException e) {
        //     message.setStatus(HttpStatus.NOT_FOUND);
        //     message.setMessage("not found productId=1 value.");
        //     return new ResponseEntity<>(message, message.getStatus());
        // }
        // ========= 이러한 방법으로 널값을 NOT FOUNT 404 에러를 던저서 자원이 없다는것을 표시가능 =========

        Message message = new Message();

        try{
            message.setData(productService.searchOne(productId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productId=" + productId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
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
    public ResponseEntity<?> searchList() {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productService.searchList());
        return new ResponseEntity<>(message, message.getStatus());

        // TODO : 데이터 리스트가 없는 경우에도 예외처리를 해줘야 하는 건가요 (,productCategory, productOption)
//        Message message = new Message();
//
//        try{
//            message.setData(productService.searchList());
//            message.setStatus(HttpStatus.OK);
//            message.setMessage("success");
//            return new ResponseEntity<>(message, message.getStatus());
//        } catch(NullPointerException e) {
//            message.setStatus(HttpStatus.NOT_FOUND);
//            message.setMessage("No Data");
//            return new ResponseEntity<>(message, message.getStatus());
//        }
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
    // @PostMapping("/one")
    // public ResponseEntity<?> createOne(@RequestBody ProductGetDto productGetDto){
    // Message message = new Message();
    //
    // // 유저 로그인 상태체크.
    // if(!userService.isUserLogin()){
    // message.setStatus(HttpStatus.FORBIDDEN);
    // message.setMessage("need_login");
    // message.setMemo("need login");
    // } else {
    // message.setStatus(HttpStatus.OK);
    // message.setMessage("success");
    //
    // productService.createOne(productGetDto, userService.getUserId());
    // }
    //
    // return new ResponseEntity<>(message, message.getStatus());
    // }

    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductCreateReqDto productCreateReqDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            // TODO : DB CREATE, UPDATE, DELETE 는 항상 Exception이 발생할수 있으므로 왠만하면 try catch로 감싸주는게 좋습니다 o
            try{
                productService.createPAO(productCreateReqDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
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
    // @PostMapping("/list")
    // public ResponseEntity<?> createList(@RequestBody List<ProductGetDto>
    // productGetDto){
    // Message message = new Message();
    //
    // // 유저 로그인 상태체크.
    // if (!userService.isUserLogin()) {
    // message.setStatus(HttpStatus.FORBIDDEN);
    // message.setMessage("need_login");
    // message.setMemo("need login");
    // } else {
    // message.setStatus(HttpStatus.OK);
    // message.setMessage("success");
    //
    // productService.createList(productGetDto, userService.getUserId());
    // }
    //
    // return new ResponseEntity<>(message, message.getStatus());
    // }

    // TODO : createOne 코드를 참고하여 다시한번 재작성 해보시기 바랍니다~~
    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<ProductCreateReqDto> productCreateReqDtos) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productService.createPAOList(productCreateReqDtos, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
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
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") Integer productId) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productService.destroyOne(productId);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
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
    public ResponseEntity<?> changeOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productService.changeOne(productGetDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
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
    public ResponseEntity<?> patchOne(@RequestBody ProductGetDto productGetDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productService.changeOne(productGetDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

}
