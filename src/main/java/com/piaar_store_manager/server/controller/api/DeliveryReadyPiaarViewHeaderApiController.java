package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto.DeliveryReadyPiaarViewHeaderDto;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.delivery_ready_view_header.DeliveryReadyPiaarViewHeaderBusinessService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery-ready/piaar/view")
public class DeliveryReadyPiaarViewHeaderApiController {
    private DeliveryReadyPiaarViewHeaderBusinessService deliveryReadyPiaarViewHeaderBusinessService;
    private UserService userService;

    @Autowired
    public DeliveryReadyPiaarViewHeaderApiController(
        DeliveryReadyPiaarViewHeaderBusinessService deliveryReadyPiaarViewHeaderBusinessService,
        UserService userService
    ) {
        this.deliveryReadyPiaarViewHeaderBusinessService = deliveryReadyPiaarViewHeaderBusinessService;
        this.userService = userService;
    }

    /**
     * Create one api for product.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/piaar/view/one</b>
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
    public ResponseEntity<?> createOne(@RequestBody DeliveryReadyPiaarViewHeaderDto viewHeaderDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                deliveryReadyPiaarViewHeaderBusinessService.createOne(viewHeaderDto, userService.getUserId());
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
     * Search one api for product.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/piaar/view/searchOne</b>
     *
     * @param productCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see UserService#isUserLogin
     * @see DeliveryReadyPiaarViewHeaderBusinessService#searchOneByUser
     */
    @GetMapping("/searchOne")
    public ResponseEntity<?> searchOne() {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else{
            try {
                message.setData(deliveryReadyPiaarViewHeaderBusinessService.searchOneByUser(userService.getUserId()));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("not found");
                message.setMemo("Not found value.");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/piaar/view/one</b>
     * 
     * @param productCreateReqDto : ProductCreateReqDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see ProductBusinessService#createPAO
     * @see UserService#getUserId
     * @see UserService#userDenyCheck
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeOne(@RequestBody DeliveryReadyPiaarViewHeaderDto viewHeaderDto) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            try{
                deliveryReadyPiaarViewHeaderBusinessService.changeOne(viewHeaderDto, userService.getUserId());
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch(NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("not found");
                message.setMemo("user not found.");
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
