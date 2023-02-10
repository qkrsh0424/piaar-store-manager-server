package com.piaar_store_manager.server.domain.sales_performance.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
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

    @PostMapping("/search/total")
    public ResponseEntity<?> searchSalesPerformance(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformance(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/channel")
    public ResponseEntity<?> searchSalesPerformanceByChannel(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByChannel(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/product/channel")
    public ResponseEntity<?> searchProductSalesPerformanceByChannel(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchProductSalesPerformanceByChannel(filter));
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
    public ResponseEntity<?> searchSalesProductPerformanceByCategory(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesProductPerformanceByCategory(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/product-option")
    public ResponseEntity<?> searchSalesPerformanceByProductOption(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByProductOption(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/product")
    public ResponseEntity<?> searchSalesPerformanceByProduct(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesPerformanceByProduct(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/product/best")
    public ResponseEntity<?> searchBestProductPerformanceByPaging(@RequestBody SalesPerformanceSearchFilter filter, @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 10) Pageable pageable) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchBestProductPerformance(filter, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/search/product/product-option")
    public ResponseEntity<?> searchOptionPerformanceByProduct(@RequestBody SalesPerformanceSearchFilter filter) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchProductOptionPerformanceByProduct(filter));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
