package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationHansanExcelFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationNaverFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationTailoExcelFormDto;
import com.piaar_store_manager.server.service.order_registration.OrderRegistrationNaverBusinessService;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return ResponseEntity(message, HttpStatus)
     * @throws FileUploadException
     * @throws NullPointerException
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @see Message
     * @see HttpStatus
     * @see CustomExcelUtils#isExcelFile
     * @see OrderRegistrationNaverBusinessService#uploadHansanExcelFile
     * @see UserService#isUserLogin
     */
    @PostMapping("/upload/hansan")
    public ResponseEntity<?> uploadHansanExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new FileUploadException("This is not an excel file.");
        }

        try {
            message.setData(orderRegistrationNaverBusinessService.uploadHansanExcelFile(file));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("운송장번호가 기입된 한산 엑셀 파일과 데이터 타입이 다른 값이 존재합니다.\n올바른 엑셀 파일을 업로드해주세요");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("운송장번호가 기입된 한산 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download data for order registration.
     * <p>
     * <b>POST : API URL => /api/v1/order-registration/naver/download/hansan</b>
     *
     * @param response : HttpServletResponse
     * @param viewDtos : List::DeliveryReadyItemHansanExcelFormDto::
     * @throws IOException
     * @see Message
     * @see HttpStatus
     * @see OrderRegistrationNaverBusinessService#changeNaverFormDtoByHansanFormDto
     */
    @PostMapping("/download/hansan")
    public void downloadHansanOrderRegistrationNaverExcel(HttpServletResponse response, @RequestBody List<OrderRegistrationHansanExcelFormDto> hansanDto) {

        // 한산 엑셀 dto를 네이버 대량등록 양식으로 변환
        List<OrderRegistrationNaverFormDto> dtos = orderRegistrationNaverBusinessService.changeNaverFormDtoByHansanFormDto(hansanDto);

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("발송처리");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("상품주문번호");
        cell = row.createCell(1);
        cell.setCellValue("배송방법");
        cell = row.createCell(2);
        cell.setCellValue("택배사");
        cell = row.createCell(3);
        cell.setCellValue("송장번호");

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
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws FileUploadException
     * @throws NullPointerException
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @see Message
     * @see HttpStatus
     * @see CustomExcelUtils#isExcelFile
     * @see OrderRegistrationNaverBusinessService#uploadTailoExcelFile
     * @see UserService#isUserLogin
     */
    @PostMapping("/upload/tailo")
    public ResponseEntity<?> uploadTailoExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new FileUploadException("This is not an excel file.");
        }

        try {
            message.setData(orderRegistrationNaverBusinessService.uploadTailoExcelFile(file));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("운송장번호가 기입된 테일로 엑셀 파일과 데이터 타입이 다른 값이 존재합니다.\n올바른 엑셀 파일을 업로드해주세요");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("운송장번호가 기입된 테일로 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download data for order registration.
     * <p>
     * <b>POST : API URL => /api/v1/order-registration/naver/download/tailo</b>
     *
     * @param response : HttpServletResponse
     * @param viewDtos : List::DeliveryReadyItemHansanExcelFormDto::
     * @throws IOException
     * @see Message
     * @see HttpStatus
     * @see OrderRegistrationNaverBusinessService#changeNaverFormDtoByTailoFormDto
     */
    @PostMapping("/download/tailo")
    public void downloadTailoOrderRegistrationNaverExcel(HttpServletResponse response, @RequestBody List<OrderRegistrationTailoExcelFormDto> tailoDto) {

        // 테일로 엑셀 dto를 네이버 대량등록 양식으로 변환
        List<OrderRegistrationNaverFormDto> dtos = orderRegistrationNaverBusinessService.changeNaverFormDtoByTailoFormDto(tailoDto);

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("발송처리");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("상품주문번호");
        cell = row.createCell(1);
        cell.setCellValue("배송방법");
        cell = row.createCell(2);
        cell.setCellValue("택배사");
        cell = row.createCell(3);
        cell.setCellValue("송장번호");

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
