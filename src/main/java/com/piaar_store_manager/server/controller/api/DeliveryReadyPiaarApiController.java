package com.piaar_store_manager.server.controller.api;

import java.util.List;

import com.piaar_store_manager.server.exception.ExcelFileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.delivery_ready.DeliveryReadyPiaarBusinessService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/delivery-ready/piaar")
public class DeliveryReadyPiaarApiController {
    private DeliveryReadyPiaarBusinessService deliveryReadyPiaarBusinessService;

    @Autowired
    public DeliveryReadyPiaarApiController(DeliveryReadyPiaarBusinessService deliveryReadyPiaarBusinessService) {
        this.deliveryReadyPiaarBusinessService = deliveryReadyPiaarBusinessService;
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

        // file extension check.
        deliveryReadyPiaarBusinessService.isExcelFile(file);
        try {
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

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Store excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/piaar/store</b>
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
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestBody List<DeliveryReadyPiaarItemDto> deliveryReadyPiaarItemDtos) {
        Message message = new Message();

        deliveryReadyPiaarBusinessService.createItemList(deliveryReadyPiaarItemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

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
    public ResponseEntity<?> getDeliveryReadyViewOrderDataByUserId() {
        Message message = new Message();

        message.setData(deliveryReadyPiaarBusinessService.getDeliveryReadyViewOrderDataByUserId());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/view/orderList/sold")
    public ResponseEntity<?> updateListToSold(@RequestBody List<DeliveryReadyPiaarItemDto> piaarItemDtos) {
        Message message = new Message();

        try {
            deliveryReadyPiaarBusinessService.updateListToSold(piaarItemDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/view/orderList/released")
    public ResponseEntity<?> updateListToReleased(@RequestBody List<DeliveryReadyPiaarItemDto> piaarItemDtos) {
        Message message = new Message();

        try {
            deliveryReadyPiaarBusinessService.updateListToReleased(piaarItemDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/view/orderList/combined")
    public ResponseEntity<?> getCombinedDelivery(@RequestBody List<DeliveryReadyPiaarItemDto> itemDtos) {
        Message message = new Message();

        try {
            message.setData(deliveryReadyPiaarBusinessService.getCombinedDelivery(itemDtos));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/view/orderList/combined-unit")
    public ResponseEntity<?> getUnitCombinedDelivery(@RequestBody List<DeliveryReadyPiaarItemDto> itemDtos) {
        Message message = new Message();

        try {
            message.setData(deliveryReadyPiaarBusinessService.getUnitCombinedDelivery(itemDtos));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
