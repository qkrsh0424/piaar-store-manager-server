package com.piaar_store_manager.server.domain.erp_download_excel_header.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.domain.erp_download_excel_header.dto.ErpDownloadExcelHeaderDto;
import com.piaar_store_manager.server.domain.erp_download_excel_header.service.ErpDownloadExcelHeaderBusinessService;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpDownloadOrderItemDto;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.utils.StaticErpItemDataUtils;

@RestController
@RequestMapping("/api/v1/erp-download-excel-headers")
@RequiredArgsConstructor
@RequiredLogin
public class ErpDownloadExcelHeaderApi {
    private final ErpDownloadExcelHeaderBusinessService erpDownloadExcelHeaderBusinessService;

    /**
     * Create one api for erp download excel header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-download-excel-headers</b>
     *
     * @param headerDto : ErpDownloadExcelHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDownloadExcelHeaderBusinessService#saveOne
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> saveOne(@RequestBody ErpDownloadExcelHeaderDto headerDto) {
        Message message = new Message();

        erpDownloadExcelHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for erp download excel header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-download-excel-headers</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDownloadExcelHeaderBusinessService#searchAll
     */
    @GetMapping("")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(erpDownloadExcelHeaderBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp download excel header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-download-excel-headers</b>
     *
     * @param headerDto : ErpDownloadExcelHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDownloadExcelHeaderBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody ErpDownloadExcelHeaderDto headerDto) {
        Message message = new Message();

        erpDownloadExcelHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp download excel header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-download-excel-headers/{id}</b>
     *
     * @param id : UUID
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpDownloadExcelHeaderBusinessService#deleteOne
     */
    @DeleteMapping("/{id}")
    @PermissionRole
    public ResponseEntity<?> deleteOne(@PathVariable(value = "id") UUID id) {
        Message message = new Message();

        erpDownloadExcelHeaderBusinessService.deleteOne(id);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download merge excel file by erp download excel header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-download-excel-headers/{id}/download-order-items/action-download</b>
     *
     * @param response                 : HttpServletResponse
     * @param id                       : UUID
     * @param erpDownloadOrderItemDtos : List::ErpDownloadOrderItemDto:;
     * @see ErpDownloadExcelHeaderBusinessService#searchErpDownloadExcelHeader
     * @see ErpDownloadExcelHeaderBusinessService#downloadByErpDownloadExcelHeader
     */
    // @PostMapping("/{id}/download-order-items/action-download")
    // public void downloadForDownloadOrderItems(HttpServletResponse response, @PathVariable(value = "id") UUID id, @RequestBody List<ErpDownloadOrderItemDto> erpDownloadOrderItemDtos) {
    //     ErpDownloadExcelHeaderDto headerDto = erpDownloadExcelHeaderBusinessService.searchErpDownloadExcelHeader(id);
    //     List<ErpDownloadItemVo> vos = erpDownloadExcelHeaderBusinessService.downloadByErpDownloadExcelHeader(headerDto, erpDownloadOrderItemDtos);

    //     // 엑셀 생성
    //     Workbook workbook = new XSSFWorkbook();
    //     Sheet sheet = workbook.createSheet("Sheet1");
    //     Row row = null;
    //     Cell cell = null;
    //     int rowNum = 0;

    //     row = sheet.createRow(rowNum++);
    //     int HEADER_COLUMN_SIZE = headerDto.getHeaderDetail().getDetails().size();
    //     for (int i = 0; i < HEADER_COLUMN_SIZE; i++) {
    //         cell = row.createCell(i);
    //         cell.setCellValue(headerDto.getHeaderDetail().getDetails().get(i).getCustomCellName());
    //     }

    //     for(int i = 0; i < vos.size(); i++) {
    //         row = sheet.createRow(rowNum++);
    //         for (int j = 0; j < HEADER_COLUMN_SIZE; j++) {
    //             String fieldType = headerDto.getHeaderDetail().getDetails().get(j).getFieldType();
                
    //             String cellValue = "";
    //             switch(fieldType) {
    //                 case "운송코드":
    //                     cellValue = erpDownloadOrderItemDtos.get(i).getCombinedFreightCode();
    //                     break;
    //                 case "고정값":
    //                     cellValue = headerDto.getHeaderDetail().getDetails().get(j).getFixedValue();
    //                     break;
    //                 default:
    //                     cellValue = vos.get(i).getCellValue().get(j).toString();
    //             }
    //             cell = row.createCell(j);
    //             cell.setCellValue(cellValue);
    //         }
    //     }

    //     for (int i = 0; i < HEADER_COLUMN_SIZE; i++) {
    //         sheet.autoSizeColumn(i);
    //     }

    //     response.setContentType("ms-vnd/excel");
    //     response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

    //     try {
    //         workbook.write(response.getOutputStream());
    //         workbook.close();
    //     } catch (IOException e) {
    //         throw new IllegalArgumentException();
    //     }
    // }

    /*
    TEST 2
    downloadForDownloadOrderItems OR downloadForDownloadOrderItems2 둘중 하나 주석후 사용
     */
    @PostMapping("/{id}/download-order-items/action-download")
    @PermissionRole
    public void downloadForDownloadOrderItems2(HttpServletResponse response, @PathVariable(value = "id") UUID id, @RequestBody List<ErpDownloadOrderItemDto> erpDownloadOrderItemDtos) {

        ErpDownloadExcelHeaderDto headerDto = erpDownloadExcelHeaderBusinessService.searchErpDownloadExcelHeader(id);
        List<List<String>> matrix = erpDownloadExcelHeaderBusinessService.downloadByErpDownloadExcelHeader2(id, headerDto, erpDownloadOrderItemDtos);

//         엑셀 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        for (int i = 0; i < matrix.size(); i++) {
            row = sheet.createRow(rowNum++);
            for (int j = 0; j < matrix.get(i).size(); j++) {
                cell = row.createCell(j);
                cell.setCellStyle(cs);
                cell.setCellValue(matrix.get(i).get(j));
            }
        }

        for (int i = 0; i < headerDto.getHeaderDetail().getDetails().size(); i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i))+(short)1024);
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

    @PostMapping("/upload-excel-sample/action-download")
    @PermissionRole
    public void downloadSample(HttpServletResponse response) {
        List<Object> dataList = StaticErpItemDataUtils.getUploadHeaderExcelSample();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        CellStyle blankStyle = workbook.createCellStyle();
        CellStyle requiredStyle = workbook.createCellStyle();

        Font blankStyleHeaderFont = workbook.createFont();
        Font requiredStyleHeaderFont = workbook.createFont();

        blankStyleHeaderFont.setColor(IndexedColors.GREY_25_PERCENT.index);
        requiredStyleHeaderFont.setColor(IndexedColors.RED.index);

        blankStyle.setFont(blankStyleHeaderFont);
        requiredStyle.setFont(requiredStyleHeaderFont);

        row = sheet.createRow(rowNum++);
        for (int i = 0; i < dataList.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue((String) dataList.get(i));
            if (i == 0) {
                cell.setCellStyle(blankStyle);
            }

            if (i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 7) {
                cell.setCellStyle(requiredStyle);
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
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

    @PostMapping("/waybill-excel-sample/action-download")
    @PermissionRole
    public void downloadWaybillExcelSample(HttpServletResponse response) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        CellStyle blankStyle = workbook.createCellStyle();
        CellStyle requiredStyle = workbook.createCellStyle();

        Font blankStyleHeaderFont = workbook.createFont();
        Font requiredStyleHeaderFont = workbook.createFont();

        blankStyleHeaderFont.setColor(IndexedColors.GREY_25_PERCENT.index);
        requiredStyleHeaderFont.setColor(IndexedColors.RED.index);

        blankStyle.setFont(blankStyleHeaderFont);
        requiredStyle.setFont(requiredStyleHeaderFont);

        row = sheet.createRow(rowNum++);

        cell = row.createCell(0);
        cell.setCellValue("수취인명");
        cell = row.createCell(1);
        // cell.setCellValue("!운송코드");
        cell.setCellValue("전화번호1");
        cell = row.createCell(2);
        cell.setCellValue("운송장번호");
        cell = row.createCell(3);
        cell.setCellValue("배송방식");
        cell = row.createCell(4);
        cell.setCellValue("택배사");


        for (int i = 0; i < 5; i++) {
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
