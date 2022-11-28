package com.piaar_store_manager.server.domain.product_receive.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveBusinessServiceV2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v2/product-receive")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReceiveApiControllerV2 {
    private final ProductReceiveBusinessServiceV2 productReceiveBusinessService;

    /**
     * Create list api for product receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/batch</b>
     */
    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody @Valid List<ProductReceiveGetDto> dtos) {
        Message message = new Message();

        Integer count = productReceiveBusinessService.createBatch(dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setMemo(count + " 건의 옵션 상품에 입고 처리가 완료되었습니다.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product receive.
     * <p>
     * <b>POST : API URL => /api/v1/product-receive/batch/status</b>
     */
    @PostMapping("/batch/status")
    public ResponseEntity<?> searchBatchByOptionIds(@RequestParam Map<String, Object> params, @RequestBody List<UUID> optionIds) {
        Message message = new Message();
    
        message.setData(productReceiveBusinessService.searchBatchByOptionIds(optionIds, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for product receive.
     * <p>
     * <b>PATCH : API URL => /api/v1/product-receive</b>
     */
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
