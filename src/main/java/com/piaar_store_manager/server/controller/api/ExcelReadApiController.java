package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.util.List;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.order_confirm.dto.OrderConfirmGetDto;
import com.piaar_store_manager.server.service.order_confirm.OrderConfirmService;

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

@RestController
@RequestMapping("/api/excel")
public class ExcelReadApiController {
    @Autowired
    OrderConfirmService orderConfirmService;

    // /api/excel/order-confirm/read
    @PostMapping("/order-confirm/read")
    public ResponseEntity<?> readOrderConfirm(@RequestParam("file") MultipartFile file) throws IOException{
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
        List<OrderConfirmGetDto> orderManageDtos = orderConfirmService.getReadExcel(worksheet);
        message.setMessage("success");
        message.setStatus(HttpStatus.OK);
        message.setData(orderManageDtos);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
