package com.piaar_store_manager.server.domain.erp_delivery_header.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_delivery_header.dto.ErpDeliveryHeaderDto;
import com.piaar_store_manager.server.domain.erp_delivery_header.service.ErpDeliveryHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/erp-delivery-headers")
@RequiredArgsConstructor
@RequiredLogin
public class ErpDeliveryHeaderApi {
    private final ErpDeliveryHeaderBusinessService erpDeliveryHeaderBusinessService;

    /**
     * Create one api for erp delivery header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-delivery-headers</b>
     * 
     * @param headerDto : ErpDeliveryHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDeliveryHeaderBusinessService#saveOne
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> saveOne(@RequestBody ErpDeliveryHeaderDto headerDto) {
        Message message = new Message();

        erpDeliveryHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for erp delivery header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-delivery-headers</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDeliveryHeaderBusinessService#searchOne
     */
    @GetMapping("")
    public ResponseEntity<?> searchOne() {
        Message message = new Message();

        message.setData(erpDeliveryHeaderBusinessService.searchOne());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-delivery-headers</b>
     * 
     * @param headerDto : ErpDeliveryHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDeliveryHeaderBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody ErpDeliveryHeaderDto headerDto) {
        Message message = new Message();

        erpDeliveryHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
