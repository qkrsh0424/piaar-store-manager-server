package com.piaar_store_manager.server.domain.product_release.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseBusinessServiceV2;

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
@RequestMapping("/api/v2/product-release")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReleaseApiControllerV2 {
    private final ProductReleaseBusinessServiceV2 productReleaseBusinessService;

    /**
     * Create list api for release.
     * <p>
     * <b>POST : API URL => /api/v2/product-release/batch</b>
     */
    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody @Valid List<ProductReleaseGetDto> dtos) {
        Message message = new Message();

        Integer count = productReleaseBusinessService.createBatch(dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setMemo(count + " 건의 옵션 상품에 출고 처리가 완료되었습니다.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/batch/status")
    public ResponseEntity<?> searchBatchByOptionIds(@RequestParam Map<String, Object> params, @RequestBody List<UUID> optionIds) {
        Message message = new Message();
    
        message.setData(productReleaseBusinessService.searchBatchByOptionIds(optionIds, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Patch one api for release
     * <p>
     * <b>PATCH : API URL => /api/v2/product-release</b>
     */
    @PatchMapping("")
    @PermissionRole
    public ResponseEntity<?> patchOne(@RequestBody @Valid ProductReleaseGetDto productReleaseGetDto) {
        Message message = new Message();

        productReleaseBusinessService.patchOne(productReleaseGetDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
