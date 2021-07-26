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
     * <b>GET : API URL => /api/v1/product/one/{productId}</b>
     *
     * @param productId
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchOne
     */
    @GetMapping("/one/{productId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productId") Integer productId) {
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
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one-o2mj/{productId}</b>
     *
     * @param productId
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchOneOTMJ
     */
    @GetMapping("/one-o2mj/{productId}")
    public ResponseEntity<?> searchOneOTMJ(@PathVariable(value = "productId") Integer productId) {
        Message message = new Message();

        try{
            message.setData(productService.searchOneO2MJ(productId));
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
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/one-fj/{productId}</b>
     *
     * @param productId
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchOneFullJoin
     */
    @GetMapping("/one-fj/{productId}")
    public ResponseEntity<?> searchOneFullJoin(@PathVariable(value = "productId") Integer productId) {
        Message message = new Message();

        try{
            message.setData(productService.searchOneFullJoin(productId));
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
     * @see ProductService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     *
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list-o2mj</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchListO2MJ
     */
    @GetMapping("/list-o2mj")
    public ResponseEntity<?> searchListOTMJ() {
        Message message = new Message();

        try{
            message.setData(productService.searchListO2MJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("No Data");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    /**
     *
     * Search list api for product.
     * <p>
     * <b>GET : API URL => /api/v1/product/list-fj</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#searchListFullJoin
     */
    @GetMapping("/list-fj")
    public ResponseEntity<?> searchListFullJoin() {
        Message message = new Message();

        try{
            message.setData(productService.searchListFullJoin());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("No Data");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/product/one</b>
     * 
     * @param ProductCreateReqDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductCreateReqDto
     * @see ProductService#createOne
     */
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductCreateReqDto productCreateReqDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
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
     * @param ProductCreateReqDto : List
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductCreateReqDto
     * @see ProductService#createList
     */
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
     * <b>DELETE : API URL => /api/v1/product/one/{productId}</b>
     *
     * @param productId : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#destroyOne
     */
    @DeleteMapping("/one/{productId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productId") Integer productId) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            // TODO : 권한이 ROLE_MANAGER 이상인 유저만 삭제 가능
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
     * <b>PUT : API URL => /api/v1/product/one</b>
     *
     * @param productGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#patchOne
     */
    @PutMapping("/one")
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
     * <b>PATCH : API URL => /api/v1/product/one</b>
     *
     * @param productGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductService#patchOne
     */
    @PatchMapping("/one")
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
