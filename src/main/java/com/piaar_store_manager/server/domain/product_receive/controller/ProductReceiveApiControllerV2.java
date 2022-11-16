package com.piaar_store_manager.server.domain.product_receive.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveBusinessServiceV2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/product-receive")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReceiveApiControllerV2 {
    private final ProductReceiveBusinessServiceV2 productReceiveBusinessService;

    /**
     * Create list api for receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/list</b>
     */
    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody List<ProductReceiveGetDto> dtos) {
        Message message = new Message();

        productReceiveBusinessService.createBatch(dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/batch/status")
    public ResponseEntity<?> searchBatchByOptionIds(@RequestParam Map<String, Object> params, @RequestBody List<UUID> optionIds) {
        Message message = new Message();
    
        message.setData(productReceiveBusinessService.searchBatchByOptionIds(optionIds, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody ProductReceiveGetDto productReceiveGetDto) {
        Message message = new Message();

        productReceiveBusinessService.patchOne(productReceiveGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
