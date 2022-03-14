package com.piaar_store_manager.server.domain.sales_analysis.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;

import javax.websocket.server.PathParam;

import com.piaar_store_manager.server.domain.sales_analysis.service.SalesAnalysisItemBusinessService;
import com.piaar_store_manager.server.model.message.Message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sales-analysis")
public class SalesAnalysisItemApi {
    private SalesAnalysisItemBusinessService salesAnalysisItemBusinessService;

    public SalesAnalysisItemApi(SalesAnalysisItemBusinessService salesAnalysisItemBusinessService) {
        this.salesAnalysisItemBusinessService = salesAnalysisItemBusinessService;
    }

    @GetMapping("")
    public ResponseEntity<?> searchAll(@RequestParam Map<String, Object> params){
        Message message = new Message();

        message.setData(salesAnalysisItemBusinessService.searchAll(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
