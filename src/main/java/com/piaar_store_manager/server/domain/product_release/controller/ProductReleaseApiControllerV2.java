package com.piaar_store_manager.server.domain.product_release.controller;

import java.util.List;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseBusinessServiceV2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/product-release")
@RequiredArgsConstructor
@RequiredLogin
public class ProductReleaseApiControllerV2 {
    private final ProductReleaseBusinessServiceV2 productReleaseBusinessService;

    /**
     * Create list api for release.
     * <p>
     * <b>POST : API URL => /api/v1/product-release/list</b>
     */
    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody List<ProductReleaseGetDto> dtos) {
        Message message = new Message();

        productReleaseBusinessService.createBatch(dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
