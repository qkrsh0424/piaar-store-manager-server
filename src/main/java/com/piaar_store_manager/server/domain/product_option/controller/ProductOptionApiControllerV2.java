package com.piaar_store_manager.server.domain.product_option.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionBusinessServiceV2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/product-options")
@RequiredArgsConstructor
@RequiredLogin
public class ProductOptionApiControllerV2 {
    private final ProductOptionBusinessServiceV2 productOptionBusinessService;

    /**
     * Search list api of status(release & receive) for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock/status</b>
     * <p>
     * 모든 option 조회, 해당 option의 startDate와 endDate기간 사이에 등록된 receive(입고), release(출고) 데이터를 조회한다.
     * 
     * @params Map::String, Object:: [startDate, endDate]
     */
    @PostMapping("/batch/stock/status")
    public ResponseEntity<?> searchBatchStockStatus(@RequestParam Map<String, Object> params, @RequestBody List<UUID> optionIds) {
        Message message = new Message();
    
        message.setData(productOptionBusinessService.searchBatchStockStatus(optionIds, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list-m2oj</b>
     * <p>
     * 모든 option, option과 Many To One Join(m2oj) 연관관계에 놓여있는 product, user, category을 함께 조회한다.
     */
    @GetMapping("/all-m2oj")
    public ResponseEntity<?> searchAllM2OJ() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchAllM2OJ());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // 221108 FEAT
    @GetMapping("/batch/{productId}")
    public ResponseEntity<?> searchBatchByProductId(@PathVariable UUID productId) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchBatchByProductId(productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // 221108 FEAT
    @PutMapping("/batch/{productId}")
    public ResponseEntity<?> updateBatch(@PathVariable UUID productId, @RequestBody @Valid List<ProductOptionGetDto> optionDtos) {
        Message message = new Message();

        productOptionBusinessService.updateBatch(productId, optionDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
