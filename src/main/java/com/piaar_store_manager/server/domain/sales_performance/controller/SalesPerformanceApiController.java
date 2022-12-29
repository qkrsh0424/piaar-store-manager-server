package com.piaar_store_manager.server.domain.sales_performance.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.sales_performance.service.SalesPerformanceBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequiredLogin
@RequestMapping("/api/v1/sales-performance")
public class SalesPerformanceApiController {
    private final SalesPerformanceBusinessService salesPerformanceBusinessService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> searchDashboard(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchDashboard(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/total")
    public ResponseEntity<?> searchSalesPerformance(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformance(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/channel")
    public ResponseEntity<?> searchSalesPerformanceByChannel(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByChannel(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/category")
    public ResponseEntity<?> searchSalesPerformanceByCategory(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByCategory(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/category/product")
    public ResponseEntity<?> searchSalesProductPerformanceByCategory(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesProductPerformanceByCategory(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
