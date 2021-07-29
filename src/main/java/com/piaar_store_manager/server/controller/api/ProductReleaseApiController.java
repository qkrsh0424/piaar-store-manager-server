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

    @GetMapping("/one/{productReleaseId}")
    public ResponseEntity<?> searchOne(@PathVariable(value = "productReleaseId") Integer productReleaseId) {
        Message message = new Message();

        try{
            message.setData(productReleaseService.searchOne(productReleaseId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productReleaseId=" + productReleaseId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productReleaseService.searchList());
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

         // 유저 로그인 상태체크.
         if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReleaseService.createRP(productReleaseGetDto, userService.getUserId());
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
    public ResponseEntity<?> createList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReleaseService.createRPList(productReleaseGetDtos, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/one/{productReleaseId}")
    public ResponseEntity<?> destroyOne(@PathVariable(value = "productReleaseId") Integer productReleaseId) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReleaseService.destroyOne(productReleaseId, userService.getUserId());
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
    public ResponseEntity<?> changeOne(@RequestBody ProductReleaseGetDto releaseDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReleaseService.changeOne(releaseDto, userService.getUserId());
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
    public ResponseEntity<?> changeList(@RequestBody List<ProductReleaseGetDto> productReleaseGetDtos) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReleaseService.changeList(productReleaseGetDtos, userService.getUserId());
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
    public ResponseEntity<?> patchOne(@RequestBody ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        // 유저 로그인 상태체크.
        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            try{
                productReleaseService.patchOne(productReleaseGetDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(Exception e) {
                message.setStatus(HttpStatus.BAD_REQUEST);
                message.setMessage("error");
            }
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/one-o2mj/{productReleaseId}")
    public ResponseEntity<?> searchOneOTMJ(@PathVariable(value = "productReleaseId") Integer productReleaseId) {
        Message message = new Message();

        try{
            message.setData(productReleaseService.searchOneOTMJ(productReleaseId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            return new ResponseEntity<>(message, message.getStatus());
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found productReleaseId=" + productReleaseId + " value.");
            return new ResponseEntity<>(message, message.getStatus());
        }
    }

    @GetMapping("/list-o2mj")
    public ResponseEntity<?> searchListOTMJ() {
        Message message = new Message();

        try{
            message.setData(productReleaseService.searchListOTMJ());
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
