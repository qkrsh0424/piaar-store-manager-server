package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-option")
public class ProductOptionApiController {

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private UserService userService;

    /**
     *
     * Search one api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/{productOptionId}</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#searchOne
     */
    @GetMapping("/one/{productOptionId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productOptionId") Integer productOptionId){
        Message message = new Message();

        try{
            message.setData(productOptionService.searchOne(productOptionId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productOptionId=" + productOptionId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    /**
     *
     * Search one api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/one-o2mj/{productOptionId}</b>
     *
     * @param productOptionId
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#searchOneOTMJ
     */
    @GetMapping("/one-o2mj/{productOptionId}")
    public ResponseEntity<?> searchOneOTMJ(@PathVariable(value = "productOptionId") Integer productOptionId) {
        Message message = new Message();

        try{
            message.setData(productOptionService.searchOneO2MJ(productOptionId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productOptionId=" + productOptionId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    /**
     *
     * Search list api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#searchList
     */
    @GetMapping("/list")
    public ResponseEntity<?> searchList(){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productOptionService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     *
     * Search list api for productOption.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list-o2mj</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#searchListO2MJ
     */
    @GetMapping("/list-o2mj")
    public ResponseEntity<?> searchListOTMJ() {
        Message message = new Message();

        try{
            message.setData(productOptionService.searchListO2MJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("No Data.");
            return new ResponseEntity<>(message, message.getStatus());
        }
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
     * @see ProductGetDto
     * @see ProductOptionService#createOne
     */
    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductOptionGetDto productOptionGetDto){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productOptionService.createOne(productOptionGetDto, userService.getUserId());
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
     * Destroy( Delete or Remove ) one for productOption.
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>DELETE : API URL => /api/v1/product-option/{productOptionId}</b>
     *
     * @param productOptionId : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#destroyOne
     */
    @DeleteMapping("/one/{productOptionId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productOptionId") Integer productOptionId){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productOptionService.destroyOne(productOptionId);
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
     * Change productOption
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>PUT : API URL => /api/v1/product-option</b>
     *
     * @param productOptionGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#patchOne
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody ProductOptionGetDto productOptionGetDto){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productOptionService.changeOne(productOptionGetDto, userService.getUserId());
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
     * Patch( Delete or Remove ) productOption
     * <p>
     * <b>Permission level => Manager</b>
     * <p>
     * <b>PATCH : API URL => /api/v1/product-option</b>
     *
     * @param productOptionGetDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductOptionService#patchOne
     */
    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductOptionGetDto productOptionGetDto){
        Message message = new Message();

        // 유저 로그인 상태체크.
        if(!userService.isUserLogin()){
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productOptionService.changeOne(productOptionGetDto, userService.getUserId());
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
