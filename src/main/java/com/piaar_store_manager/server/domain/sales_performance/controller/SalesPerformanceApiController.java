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

    @GetMapping("/total/pay-amount")
    public ResponseEntity<?> searchTotalPayAmount(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchTotalPayAmount(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/total/registration-and-unit")
    public ResponseEntity<?> searchTotalRegistrationAndUnit(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchTotalRegistrationAndUnit(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/total/pay-amount/day-of-week")
    public ResponseEntity<?> searchPayAmountDayOfWeek(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchPayAmountDayOfWeek(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/total/summary")
    public ResponseEntity<?> searchTotalSummary(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchTotalSummary(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/sales-channel/total/pay-amount")
    public ResponseEntity<?> searchSalesChannelPayAmount(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchSalesChannelPayAmount(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    } 

    @GetMapping("/sales-channel/pay-amount")
    public ResponseEntity<?> searchPayAmountByChannel(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchPayAmountByChannel(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/sales-channel/registration-and-unit")
    public ResponseEntity<?> searchRegistrationAndUnitByChannel(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchRegistrationAndUnitByChannel(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/sales-channel/pay-amount/day-of-week")
    public ResponseEntity<?> searchPayAmountDayOfWeekByChannel(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchPayAmountDayOfWeekByChannel(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/sales-channel/pay-amount/product")
    public ResponseEntity<?> searchProductPayAmountByChannel(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(salesPerformanceBusinessService.searchProductPayAmountByChannel(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
