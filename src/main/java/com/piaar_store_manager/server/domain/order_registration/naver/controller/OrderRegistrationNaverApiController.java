package com.piaar_store_manager.server.domain.order_registration.naver.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.order_registration.naver.dto.OrderRegistrationHansanExcelFormDto;
import com.piaar_store_manager.server.domain.order_registration.naver.dto.OrderRegistrationNaverFormDto;
import com.piaar_store_manager.server.domain.order_registration.naver.dto.OrderRegistrationTailoExcelFormDto;
import com.piaar_store_manager.server.domain.order_registration.naver.service.OrderRegistrationNaverBusinessService;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/order-registration/naver")
@RequiredArgsConstructor
@RequiredLogin
public class OrderRegistrationNaverApiController {
    private final OrderRegistrationNaverBusinessService orderRegistrationNaverBusinessService;


    /**
     * Upload excel data for hansan order form.
     * <p>
     * <b>POST : API URL => /api/v1/order-registration/naver/upload/hansan</b>
     *
     * @param file
     * @throws CustomExcelFileUploadException
     * @see CustomExcelUtils#isExcelFile
     * @see OrderRegistrationNaverBusinessService#uploadHansanExcelFile
     */
    @PostMapping("/upload/hansan")
    public ResponseEntity<?> uploadHansanExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        try {
            message.setData(orderRegistrationNaverBusinessService.uploadHansanExcelFile(file));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("?????? ?????? ???????????? ???????????? ?????? ?????? ???????????????.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("?????????????????? ????????? ?????? ?????? ????????? ????????? ????????? ?????? ?????? ???????????????.\n????????? ?????? ????????? ?????????????????????");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("?????????????????? ????????? ?????? ?????? ????????? ????????????.\n????????? ?????? ????????? ?????????????????????");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download excel for order registration.
     * <p>
     * <b>POST : API URL => /api/v1/order-registration/naver/download/hansan</b>
     *
     * @param response : HttpServletResponse
     * @param hansanDto : List::OrderRegistrationHansanExcelFormDto::
     * @throws IllegalArgumentException
     * @see OrderRegistrationNaverBusinessService#changeNaverFormDtoByHansanFormDto
     */
    @PostMapping("/download/hansan")
    public void downloadHansanOrderRegistrationNaverExcel(HttpServletResponse response, @RequestBody List<OrderRegistrationHansanExcelFormDto> hansanDto) {

        // ?????? ?????? dto??? ????????? ???????????? ???????????? ??????
        List<OrderRegistrationNaverFormDto> dtos = orderRegistrationNaverBusinessService.changeNaverFormDtoByHansanFormDto(hansanDto);

        // ?????? ??????
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("????????????");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("??????????????????");
        cell = row.createCell(1);
        cell.setCellValue("????????????");
        cell = row.createCell(2);
        cell.setCellValue("?????????");
        cell = row.createCell(3);
        cell.setCellValue("????????????");

        for (int i = 0; i < dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(dtos.get(i).getProdOrderNumber());
            cell = row.createCell(1);
            cell.setCellValue(dtos.get(i).getTransportType());
            cell = row.createCell(2);
            cell.setCellValue(dtos.get(i).getDeliveryService());
            cell = row.createCell(3);
            cell.setCellValue(dtos.get(i).getTransportNumber());
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Upload excel data for tailo order form.
     * <p>
     * <b>POST : API URL => /api/v1/order-registration/naver/upload/tailo</b>
     *
     * @param file : MultipartFile
     * @throws CustomExcelFileUploadException
     * @see CustomExcelUtils#isExcelFile
     * @see OrderRegistrationNaverBusinessService#uploadTailoExcelFile
     */
    @PostMapping("/upload/tailo")
    public ResponseEntity<?> uploadTailoExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        try {
            message.setData(orderRegistrationNaverBusinessService.uploadTailoExcelFile(file));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("?????? ?????? ???????????? ???????????? ?????? ?????? ???????????????.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("?????????????????? ????????? ????????? ?????? ????????? ????????? ????????? ?????? ?????? ???????????????.\n????????? ?????? ????????? ?????????????????????");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("?????????????????? ????????? ????????? ?????? ????????? ????????????.\n????????? ?????? ????????? ?????????????????????");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download excel for order registration.
     * <p>
     * <b>POST : API URL => /api/v1/order-registration/naver/download/tailo</b>
     *
     * @param response : HttpServletResponse
     * @param tailoDtos : List::OrderRegistrationTailoExcelFormDto::
     * @throws IllegalArgumentException
     * @see OrderRegistrationNaverBusinessService#changeNaverFormDtoByTailoFormDto
     */
    @PostMapping("/download/tailo")
    public void downloadTailoOrderRegistrationNaverExcel(HttpServletResponse response, @RequestBody List<OrderRegistrationTailoExcelFormDto> tailoDtos) {

        // ????????? ?????? dto??? ????????? ???????????? ???????????? ??????
        List<OrderRegistrationNaverFormDto> dtos = orderRegistrationNaverBusinessService.changeNaverFormDtoByTailoFormDto(tailoDtos);

        // ?????? ??????
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("????????????");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("??????????????????");
        cell = row.createCell(1);
        cell.setCellValue("????????????");
        cell = row.createCell(2);
        cell.setCellValue("?????????");
        cell = row.createCell(3);
        cell.setCellValue("????????????");

        for (int i = 0; i < dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(dtos.get(i).getProdOrderNumber());
            cell = row.createCell(1);
            cell.setCellValue(dtos.get(i).getTransportType());
            cell = row.createCell(2);
            cell.setCellValue(dtos.get(i).getDeliveryService());
            cell = row.createCell(3);
            cell.setCellValue(dtos.get(i).getTransportNumber());
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}
