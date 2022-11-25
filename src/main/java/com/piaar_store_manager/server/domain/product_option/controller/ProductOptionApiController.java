package com.piaar_store_manager.server.domain.product_option.controller;

import java.util.Map;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionBusinessService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product-option")
@RequiredArgsConstructor
@RequiredLogin
public class ProductOptionApiController {
    private final ProductOptionBusinessService productOptionBusinessService;

    /**
     * Search list api for product option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/list</b>
     */
    @GetMapping("/batch")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search api for release location of produc option.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/release-location</b>
     * <p>
     */
    @GetMapping("/release-location")
    public ResponseEntity<?> searchRelaseLocation() {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchReleaseLocation());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search api for stock status by week.
     * <p>
     * <b>GET : API URL => /api/v1/product-option/stock-cycle</b>
     * <p>
     * @param params Map::String, Object:: [searchEndDate, categoryCid]
     */
    @GetMapping("/stock-cycle")
    public ResponseEntity<?> searchStockCycle(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(productOptionBusinessService.searchStockCycle(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
