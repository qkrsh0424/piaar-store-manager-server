package com.piaar_store_manager.server.controller.api;

import java.text.ParseException;
import java.util.Map;

import com.piaar_store_manager.server.exception.DeliveryReadyFileUploadException;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.delivery_ready.DeliveryReadyCoupangService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/delivery-ready/coupang")
public class DeliveryReadyCoupangApiController {
    
    @Autowired
    private DeliveryReadyCoupangService deliveryReadyService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) throws ParseException {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            // file extension check.
            deliveryReadyService.isExcelFile(file);

            try{
                message.setData(deliveryReadyService.uploadDeliveryReadyExcelFile(file));
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                throw new DeliveryReadyFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
            } catch (IllegalStateException e) {
                throw new DeliveryReadyFileUploadException("쿠팡 배송 준비 엑셀 파일이 아닙니다. 올바른 배송 준비 엑셀 파일을 업로드해주세요");
            }
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Store excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/store</b>
     * 
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws IOException
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverService#isExcelFile
     * @see DeliveryReadyNaverService#storeDeliveryReadyExcelFile
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) throws ParseException {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            // file extension check.
            deliveryReadyService.isExcelFile(file);

            message.setData(deliveryReadyService.storeDeliveryReadyExcelFile(file, userService.getUserId()));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search unreleased data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/view/unreleased</b>
     * 
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverService#getDeliveryReadyViewUnreleasedData
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @GetMapping("/view/unreleased")
    public ResponseEntity<?> getDeliveryReadyViewUnreleasedData() {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            message.setData(deliveryReadyService.getDeliveryReadyViewUnreleasedData());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search released data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/view/released</b>
     * 
     * @return ResponseEntity(message, HttpStatus)
     * @param query : Map[startDate, endDate]
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverService#getDeliveryReadyViewReleased
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @GetMapping("/view/released")
    public ResponseEntity<?> getDeliveryReadyViewReleased(@RequestParam Map<String, Object> query) throws ParseException {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            message.setData(deliveryReadyService.getDeliveryReadyViewReleased(query));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
