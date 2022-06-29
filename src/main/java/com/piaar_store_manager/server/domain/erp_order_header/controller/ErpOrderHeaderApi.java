package com.piaar_store_manager.server.domain.erp_order_header.controller;


import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_order_header.dto.ErpOrderHeaderDto;
import com.piaar_store_manager.server.domain.erp_order_header.service.ErpOrderHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/erp-order-headers")
@RequiredLogin
@RequiredArgsConstructor
public class ErpOrderHeaderApi {
    private final ErpOrderHeaderBusinessService erpOrderHeaderBusinessService;

    /**
     * Create one api for erp order header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-order-headers</b>
     * 
     * @param headerDto : ErpOrderHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderHeaderBusinessService#saveOne
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> saveOne(@RequestBody ErpOrderHeaderDto headerDto) {
        Message message = new Message();

        erpOrderHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search api for erp order header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-order-headers</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderHeaderBusinessService#searchList
     */
    @GetMapping("")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(erpOrderHeaderBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp order header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-order-headers</b>
     * 
     * @param headerDto : ErpOrderHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderHeaderBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody ErpOrderHeaderDto headerDto) {
        Message message = new Message();

        erpOrderHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for erp order header.
     * <p>
     * <b>DELETE : API URL => /api/v1/erp-order-headers</b>
     * 
     * @param headerId : UUID
     * @see ErpOrderHeaderBusinessService#deleteOne
     */
    @DeleteMapping("/{headerId}")
    @PermissionRole
    public ResponseEntity<?> deleteOne(@PathVariable UUID headerId) {
        Message message = new Message();

        erpOrderHeaderBusinessService.deleteOne(headerId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
