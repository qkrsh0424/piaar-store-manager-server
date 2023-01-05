package com.piaar_store_manager.server.domain.sales_performance.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.sales_performance.filter.ChannelPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.service.SalesPerformanceBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequiredLogin
@RequestMapping("/api/v1/sales-performance")
public class SalesPerformanceApiController {
    private final SalesPerformanceBusinessService salesPerformanceBusinessService;

    @PostMapping("/search/dashboard")
    public ResponseEntity<?> searchDashboard(@RequestBody DashboardPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchDashboard(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // @GetMapping("/total")
    // public ResponseEntity<?> searchSalesPerformance(@RequestParam Map<String, Object> params) {
    //     Message message = new Message();

    //     message.setData(salesPerformanceBusinessService.searchSalesPerformance(params));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    @PostMapping("/search/total")
    public ResponseEntity<?> searchSalesPerformance(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformance(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/channel")
    public ResponseEntity<?> searchSalesPerformanceByChannel(@RequestBody ChannelPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByChannel(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/category")
    public ResponseEntity<?> searchSalesPerformanceByCategory(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByCategory(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/category/product")
    public ResponseEntity<?> searchSalesProductPerformanceByCategory(@RequestParam SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesProductPerformanceByCategory(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
