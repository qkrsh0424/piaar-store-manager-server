package com.piaar_store_manager.server.domain.sales_analysis.controller;

import java.util.Map;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.sales_analysis.service.SalesAnalysisItemBusinessService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sales-analysis")
@RequiredLogin
@RequiredArgsConstructor
public class SalesAnalysisItemApi {
    private final SalesAnalysisItemBusinessService salesAnalysisItemBusinessService;

    @GetMapping("")
    public ResponseEntity<?> searchAll(@RequestParam Map<String, Object> params){
        Message message = new Message();

        message.setData(salesAnalysisItemBusinessService.searchAll(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
