package com.piaar_store_manager.server.domain.sales_rate.controller;

import java.io.IOException;
import java.util.List;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.sales_rate.dto.SalesRateCommonGetDto;
import com.piaar_store_manager.server.domain.sales_rate.service.SalesRateService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// TODO : Code Refactoring All 안건들여도 됩니다~~
@RestController
@RequestMapping("/api/v1/sales-rate")
@RequiredArgsConstructor
@RequiredLogin
public class SalesRateApiController {

    private final SalesRateService salesRateService;

    // /api/v1/sales-rate/excel/naver/read
    @PostMapping("/excel/naver/read")
    public ResponseEntity<?> readSalesRate(@RequestParam("file") MultipartFile file) throws IOException {
        Message message = new Message();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            message.setMessage("extension_error");
            message.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(message, message.getStatus());
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);
        try {
            List<SalesRateCommonGetDto> salesRateCommonGetDtos = salesRateService.getSalesRateCommon(worksheet);
            message.setMessage("success");
            message.setStatus(HttpStatus.OK);
            message.setData(salesRateCommonGetDtos);
        } catch (Exception e) {
            message.setMessage("not_matched_file_error");
            message.setStatus(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
