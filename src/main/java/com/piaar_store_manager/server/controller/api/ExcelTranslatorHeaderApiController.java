package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.excel_translator_data.dto.DownloadExcelDataGetDto;
import com.piaar_store_manager.server.model.excel_translator_data.dto.UploadedDetailDto;
import com.piaar_store_manager.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.excel_translator.ExcelTranslatorHeaderBusinessService;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/excel-translator")
@RequiredArgsConstructor
@RequiredLogin
public class ExcelTranslatorHeaderApiController {

    private final ExcelTranslatorHeaderBusinessService excelTranslatorHeaderBusinessService;

    /**
     * Create one api for excel translator header.
     */
    @PostMapping("/one")
    public ResponseEntity<?> createExcelTranslatorHeaderTitle(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderBusinessService.createTitle(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for excel translator header.
     * 
     * @see ExcelTranslatorHeaderBusinessService#searchList
     */
    @GetMapping("/list")
    @PermissionRole
    public ResponseEntity<?> searchExcelTranslatorHeader() {
        Message message = new Message();

        message.setData(excelTranslatorHeaderBusinessService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for excel translator header.
     * 
     * @see ExcelTranslatorHeaderBusinessService#changeOne
     */
    @PutMapping("/one")
    public ResponseEntity<?> changeExcelTranslatorHeader(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderBusinessService.changeOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for excel translator header.
     * 
     * @query query : Map::String, Object::
     * @see ExcelTranslatorHeaderBusinessService#deleteOne
     */
    @DeleteMapping("/one")
    public ResponseEntity<?> deleteExcelTranslatorHeader(@RequestParam Map<String, Object> query) {
        Message message = new Message();

        excelTranslatorHeaderBusinessService.deleteOne(query);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Upload a free-form excel file.
     * 
     * @param file : MultipartFile
     * @param dto : ExcelTranslatorHeaderGetDto
     * @see ExcelTranslatorHeaderBusinessService#uploadExcelFile
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file, @RequestPart ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        try{
            message.setData(excelTranslatorHeaderBusinessService.uploadExcelFile(file, dto));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("설정된 양식과 동일한 엑셀 파일을 업로드해주세요.");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("설정된 양식과 동일한 엑셀 파일을 업로드해주세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for upload detail of excel translator header.
     * 
     * @param dto : ExcelTranslatorHeaderGetDto
     * @see ExcelTranslatorHeaderBusinessService#updateUploadHeaderDetailOfExcelTranslator
     */
    @PutMapping("/header/upload/one")
    public ResponseEntity<?> updateUploadHeaderDetailOfExcelTranslator(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        try {
            excelTranslatorHeaderBusinessService.updateUploadHeaderDetailOfExcelTranslator(dto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("올바르지 않은 값이 존재합니다. 다시 등록해주세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change one api for download detail of excel translator header.
     * 
     * @param dto : ExcelTranslatorHeaderGetDto
     * @see ExcelTranslatorHeaderBusinessService#updateDownloadHeaderDetailOfExcelTranslator
     */
    @PutMapping("/header/download/one")
    public ResponseEntity<?> updateDownloadHeaderDetailOfExcelTranslator(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderBusinessService.updateDownloadHeaderDetailOfExcelTranslator(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download excel file.
     * 
     * @param response : HttpServletResponse
     * @param dtos : List::DownloadExcelDataGetDto::
     */
    @PostMapping("/download")
    public void downloadExcelFile(HttpServletResponse response, @RequestBody List<DownloadExcelDataGetDto> dtos) {

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        // 날짜 변환 형식 지정
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.KOREA);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);

        for(int i = 0; i < dtos.size(); i++) {
            for(int j = 0; j < dtos.get(i).getTranslatedData().getDetails().size(); j++) {
                // 엑셀 데이터는 header의 다음 row부터 기입
                row = sheet.getRow(j);
                if(row == null) {
                    row = sheet.createRow(j);
                }
                cell = row.createCell(i);
                
                // 데이터 타입에 맞춰 엑셀 항목 작성.
                UploadedDetailDto detailDto = dtos.get(i).getTranslatedData().getDetails().get(j);
                try{
                    if(detailDto.getCellType() == null || detailDto.getCellType().isBlank()) {
                        cell.setCellValue("");
                    }else if(detailDto.getCellType().equals("String")) {
                        cell.setCellValue(detailDto.getColData().toString());
                    }else if(detailDto.getCellType().equals("Date")) {
                        Date data = format.parse(detailDto.getColData().toString());
                        cell.setCellValue(outputFormat.format(DateHandler.getKstDate(data)));
                    }else if(detailDto.getCellType().equals("Double")) {
                        cell.setCellValue((int)detailDto.getColData());
                    }
                } catch(ParseException e) {
                    throw new CustomExcelFileUploadException("데이터 변환에 오류가 생겼습니다. 다시 시도해주세요.");
                }
            }
        }

        for(int i = 0; i < dtos.size(); i++){
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Download excel file.
     * 
     * @param response : HttpServletResponse
     * @param dtos : List::DownloadExcelDataGetDto::
     */
    @PostMapping("/header/upload/download")
    public void downloadUploadedDetails(HttpServletResponse response, @RequestBody List<UploadedDetailDto> dtos) {

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;

        row = sheet.createRow(0);

        for(int i = 0; i < dtos.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(dtos.get(i).getColData().toString());
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}
