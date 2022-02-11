package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.exception.ExcelFileUploadException;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.delivery_ready.DeliveryReadyPiaarBusinessService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/delivery-ready/piaar")
public class DeliveryReadyPiaarApiController {
    private DeliveryReadyPiaarBusinessService deliveryReadyPiaarBusinessService;
    private UserService userService;

    @Autowired
    public DeliveryReadyPiaarApiController(
        DeliveryReadyPiaarBusinessService deliveryReadyPiaarBusinessService,
        UserService userService
    ) {
        this.deliveryReadyPiaarBusinessService = deliveryReadyPiaarBusinessService;
        this.userService = userService;
    }

    /**
     * Upload excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/piaar/upload</b>
     * 
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws NullPointerException
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyPiaarBusinessService#isExcelFile
     * @see DeliveryReadyPiaarBusinessService#uploadDeliveryReadyExcelFile
     * @see UserService#isUserLogin
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            // file extension check.
            deliveryReadyPiaarBusinessService.isExcelFile(file);

            try{
                message.setData(deliveryReadyPiaarBusinessService.uploadDeliveryReadyExcelFile(file));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                throw new ExcelFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
            } catch (IllegalStateException e) {
                throw new ExcelFileUploadException("피아르 엑셀 양식과 데이터 타입이 다른 값이 존재합니다.\n올바른 엑셀 파일을 업로드해주세요");
            } catch (IllegalArgumentException e) {
                throw new ExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Store excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/store</b>
     * 
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyPiaarBusinessService#isExcelFile
     * @see DeliveryReadyPiaarBusinessService#storeDeliveryReadyExcelFile
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            // file extension check.
            deliveryReadyPiaarBusinessService.isExcelFile(file);

            message.setData(deliveryReadyPiaarBusinessService.storeDeliveryReadyExcelFile(file, userService.getUserId()));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search Order data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/piaar/view/order</b>
     * 
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyPiaarBusinessService#getDeliveryReadyViewOrderData
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @GetMapping("/view/orderList")
    public ResponseEntity<?> getDeliveryReadyViewUnreleasedData() {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            message.setData(deliveryReadyPiaarBusinessService.getDeliveryReadyViewOrderData(userService.getUserId()));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
