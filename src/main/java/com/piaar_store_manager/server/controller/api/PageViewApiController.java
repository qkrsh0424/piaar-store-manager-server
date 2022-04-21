package com.piaar_store_manager.server.controller.api;

import java.io.IOException;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.page_view.naver_analytics.NAPageViewService;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/page-view")
@RequiredArgsConstructor
public class PageViewApiController {

    private final NAPageViewService naPageViewService;

    @RequiredLogin
    @PostMapping("/na/popular-page/excel/read")
    public ResponseEntity<?> readPageViewNAPopularPageExcel(@RequestParam("file") MultipartFile file)
            throws IOException {
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
            message.setMessage("success");
            message.setStatus(HttpStatus.OK);
            message.setData(naPageViewService.getProductPageViews(worksheet));
        } catch (Exception e) {
            message.setMessage("not_matched_file_error");
            message.setStatus(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, message.getStatus());
    }
}
