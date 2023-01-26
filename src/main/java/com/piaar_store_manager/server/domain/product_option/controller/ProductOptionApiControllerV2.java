package com.piaar_store_manager.server.domain.product_option.controller;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionBusinessServiceV2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v2/product-options")
@RequiredArgsConstructor
@RequiredLogin
public class ProductOptionApiControllerV2 {
    private final ProductOptionBusinessServiceV2 productOptionBusinessService;

    /**
     * Search all api for product option.
     * product option related product.
     * <p>
     * <b>GET : API URL => /api/v2/product-options/all/product</b>
     * <p>
     */
    @GetMapping("/all/product")
    public ResponseEntity<?> searchAllRelatedProduct() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchAllRelatedProduct());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/all/product/product-category")
    public ResponseEntity<?> searchAllRelatedProductAndProductCategory() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchAllRelatedProductAndProductCategory());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v2/product-options/batch/{productId}</b>
     * <p>
     */
    @GetMapping("/batch/{productId}")
    public ResponseEntity<?> searchBatchByProductId(@PathVariable UUID productId) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchBatchByProductId(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update list api for product option.
     * <p>
     * <b>GET : API URL => /api/v2/product-options/batch/{productId}</b>
     * <p>
     */
    @PutMapping("/batch/{productId}")
    public ResponseEntity<?> updateBatch(@PathVariable UUID productId, @RequestBody @Valid List<ProductOptionGetDto> optionDtos) {
        Message message = new Message();

        productOptionBusinessService.updateBatch(productId, optionDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for product option.
     * <p>
     * <b>GET : API URL => /api/v2/product-options/{optionId}</b>
     * <p>
     */
    @DeleteMapping("/{optionId}")
    @PermissionRole(role = "ROLE_SUPERADMIN")
    public ResponseEntity<?> deleteOne(@PathVariable(value = "optionId") UUID optionId) {
        Message message = new Message();

        productOptionBusinessService.deleteOne(optionId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
