package com.piaar_store_manager.server.domain.erp_sales_header.controller;


import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_sales_header.dto.ErpSalesHeaderDto;
import com.piaar_store_manager.server.domain.erp_sales_header.service.ErpSalesHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/erp-sales-headers")
@RequiredArgsConstructor
@RequiredLogin
public class ErpSalesHeaderApi {
    private final ErpSalesHeaderBusinessService erpSalesHeaderBusinessService;

    /**
     * Create one api for erp sales header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-sales-headers</b>
     * 
     * @param headerDto : ErpSalesHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSalesHeaderBusinessService#saveOne
     */
    // Unused API
    @PostMapping("")
    public ResponseEntity<?> saveOne(@RequestBody ErpSalesHeaderDto headerDto) {
        Message message = new Message();

        erpSalesHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for erp sales header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-sales-headers/all</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSalesHeaderBusinessService#searchAll
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(erpSalesHeaderBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for product.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-sales-headers</b>
     * 
     * @param headerDto : ErpSalesHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSalesHeaderBusinessService#updateOne
     */
    // Unused API
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody ErpSalesHeaderDto headerDto) {
        Message message = new Message();

        erpSalesHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for erp sales header.
     * <p>
     * <b>DELETE : API URL => /api/v1/erp-sales-headers</b>
     * 
     * @param headerId : UUID
     * @see ErpSalesHeaderBusinessService#deleteOne
     */
    @DeleteMapping("/{headerId}")
    @PermissionRole
    public ResponseEntity<?> deleteOne(@PathVariable UUID headerId) {
        Message message = new Message();

        erpSalesHeaderBusinessService.deleteOne(headerId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
