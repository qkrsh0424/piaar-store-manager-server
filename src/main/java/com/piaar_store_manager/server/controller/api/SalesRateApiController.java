package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.sales_rate.dto.OrderSearchExcelFormDto;
import com.piaar_store_manager.server.model.sales_rate.dto.SalesRateCommonGetDto;
import com.piaar_store_manager.server.model.sales_rate.dto.SalesRateCommonGetDto;
import com.piaar_store_manager.server.service.sales_rate.SalesRateService;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// TODO : Code Refactoring All
@RestController
@RequestMapping("/api/v1/sales-rate")
public class SalesRateApiController {
    @Autowired
    SalesRateService salesRateService;

    // /api/v1/sales-rate/excel/naver/read
    @PostMapping("/excel/naver/read")
    public ResponseEntity<?> readSalesRate(@RequestParam("file") MultipartFile file) throws IOException {
        Message message = new Message();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);
        List<SalesRateCommonGetDto> salesRateCommonGetDtos = salesRateService.getSalesRateCommon(worksheet);
        message.setMessage("success");
        message.setStatus(HttpStatus.OK);
        message.setData(salesRateCommonGetDtos);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
