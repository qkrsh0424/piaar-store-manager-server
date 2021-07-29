package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
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
    
    @Autowired
    private ProductReceiveService productReceiveService;

    @Autowired
    private UserService userService;

    @GetMapping("/one/{productReceiveId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productReceiveId") Integer productReceiveId) {
        Message message = new Message();

        try{
            message.setData(productReceiveService.searchOne(productReceiveId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productReceiveId=" + productReceiveId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productReceiveService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

         // 유저 로그인 상태체크.
         if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReceiveService.createRP(productReceiveGetDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/list")
    public ResponseEntity<?> createList(@RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReceiveService.createRPList(productReceiveGetDtos, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/one/{productReceiveId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReceiveId") Integer productReceiveId) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReceiveService.destroyOne(productReceiveId, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody ProductReceiveGetDto optionDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReceiveService.changeOne(optionDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/list")
    public ResponseEntity<?> changeList(@RequestBody List<ProductReceiveGetDto> productReceiveGetDtos) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReceiveService.changeList(productReceiveGetDtos, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/one")
    public ResponseEntity<?> patchOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReceiveService.patchOne(productReceiveGetDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/one-o2mj/{productReceiveId}")
    public ResponseEntity<?> searchOneOTMJ(@PathVariable(value = "productReceiveId") Integer productReceiveId) {
        Message message = new Message();

        try{
            message.setData(productReceiveService.searchOneOTMJ(productReceiveId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productReceiveId=" + productReceiveId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    @GetMapping("/list-o2mj")
    public ResponseEntity<?> searchListOTMJ() {
        Message message = new Message();

        try{
            message.setData(productReceiveService.searchListOTMJ());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("No Data.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }
}
