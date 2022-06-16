package com.piaar_store_manager.server.domain.stock_analysis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.stock_analysis.service.StockAnalysisBusinessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stock-analysis")
@RequiredArgsConstructor
public class StockAnalysisApi {
    private final StockAnalysisBusinessService stockAnalysisBusinessService;

    @GetMapping("")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(stockAnalysisBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
