package com.piaar_store_manager.server.domain.excel_form.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.waybill.dto.WaybillAssembledDto;
import com.piaar_store_manager.server.domain.waybill.dto.WaybillGetDto;
import com.piaar_store_manager.server.domain.waybill.dto.WaybillOptionInfo;
import com.piaar_store_manager.server.domain.waybill.service.WaybillService;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@RequiredLogin
public class ExcelApiController {
    private final WaybillService waybillService;

    // /api/excel/waybill/read
    @PostMapping("/waybill/read")
    public ResponseEntity<?> readWaybill(@RequestParam("file") MultipartFile file) throws IOException {
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
        // List<OrderConfirmGetDto> orderManageDtos =
        // orderConfirmService.getReadExcel(worksheet);
        List<WaybillGetDto> dtos = waybillService.getReadExcel(worksheet);
        message.setMessage("success");
        message.setStatus(HttpStatus.OK);
        message.setData(dtos);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // /api/excel/waybill/logen/write
    @PostMapping("/waybill/logen/write")
    public void writeLogenWaybill(HttpServletResponse response, @RequestBody WaybillGetDto getDto) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 15000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(7, 3000);
        sheet.setColumnWidth(8, 20000);
        sheet.setColumnWidth(9, 1000);
        sheet.setColumnWidth(10, 10000);

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("수하인명");
        cell = row.createCell(1);
        cell.setCellValue("");
        cell = row.createCell(2);
        cell.setCellValue("수하인주소");
        cell = row.createCell(3);
        cell.setCellValue("수하인전화번호");
        cell = row.createCell(4);
        cell.setCellValue("수하인핸드폰번호");
        cell = row.createCell(5);
        cell.setCellValue("박스수량");
        cell = row.createCell(6);
        cell.setCellValue("택배운임");
        cell = row.createCell(7);
        cell.setCellValue("운임구분");
        cell = row.createCell(8);
        cell.setCellValue("품목명");
        cell = row.createCell(9);
        cell.setCellValue("");
        cell = row.createCell(10);
        cell.setCellValue("배송메세지");

        // Body
        for (int i = 0; i < getDto.getList().size(); i++) {
            WaybillAssembledDto assembledDto = getDto.getList().get(i);
            String receiverStr = assembledDto.getReceiver() + " ";
            for (int j = 0; j < assembledDto.getOptionInfos().size(); j++) {
                receiverStr += assembledDto.getOptionInfos().get(j).getOptionInfo() + "-"
                        + assembledDto.getOptionInfos().get(j).getUnit() + ", ";
            }
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(receiverStr);
            cell = row.createCell(1);
            cell.setCellValue("");
            cell = row.createCell(2);
            cell.setCellValue(assembledDto.getDestination());
            cell = row.createCell(3);
            cell.setCellValue(assembledDto.getBuyerContact());
            cell = row.createCell(4);
            cell.setCellValue(assembledDto.getBuyerContact());
            cell = row.createCell(5);
            cell.setCellValue(1);
            cell = row.createCell(6);
            cell.setCellValue(5000);
            cell = row.createCell(7);
            cell.setCellValue("선불");
            cell = row.createCell(8);
            cell.setCellValue(assembledDto.getProdName());
            cell = row.createCell(9);
            cell.setCellValue("");
            cell = row.createCell(10);
            cell.setCellValue(assembledDto.getDeliveryMessage());
            
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        // response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        try {
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // /api/excel/waybill/logen/write
    @PostMapping("/waybill/logen-all/write")
    public void writeLogenAllWaybill(HttpServletResponse response, @RequestBody List<WaybillGetDto> getDtos) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("수하인명");
        cell = row.createCell(1);
        cell.setCellValue("");
        cell = row.createCell(2);
        cell.setCellValue("수하인주소");
        cell = row.createCell(3);
        cell.setCellValue("수하인전화번호");
        cell = row.createCell(4);
        cell.setCellValue("수하인핸드폰번호");
        cell = row.createCell(5);
        cell.setCellValue("박스수량");
        cell = row.createCell(6);
        cell.setCellValue("택배운임");
        cell = row.createCell(7);
        cell.setCellValue("운임구분");
        cell = row.createCell(8);
        cell.setCellValue("품목명");
        cell = row.createCell(9);
        cell.setCellValue("");
        cell = row.createCell(10);
        cell.setCellValue("배송메세지");
        cell = row.createCell(11);
        cell.setCellValue("마지막컬럼은지워주세요");

        List<WaybillAssembledDto> assembledDtos = new ArrayList<>();

        for (WaybillGetDto getDto : getDtos) {
            assembledDtos.addAll(getDto.getList());
        }

        // Body
        for (WaybillAssembledDto dto : assembledDtos) {
            row = sheet.createRow(rowNum++);

            cell = row.createCell(0);
            cell.setCellValue(dto.getReceiver());

            cell = row.createCell(1);
            cell.setCellValue("");

            cell = row.createCell(2);
            cell.setCellValue(dto.getDestination());

            cell = row.createCell(3);
            cell.setCellValue(dto.getReceiverContact1());

            cell = row.createCell(4);
            cell.setCellValue(dto.getReceiverContact2());

            cell = row.createCell(5);
            cell.setCellValue(1);

            cell = row.createCell(6);
            cell.setCellValue(5000);

            cell = row.createCell(7);
            cell.setCellValue("선불");

            cell = row.createCell(8);
            cell.setCellValue(((dto.getProdCode()==null || dto.getProdCode().equals(""))?"품목코드지정바람":dto.getProdCode()) +"\n"+getOptionInfosString(dto.getOptionInfos()));

            cell = row.createCell(9);
            cell.setCellValue("");

            cell = row.createCell(10);
            cell.setCellValue(dto.getDeliveryMessage());

            cell = row.createCell(11);
            cell.setCellValue(dto.getProdName());

            row.setHeight((short) -1);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
        sheet.autoSizeColumn(9);
        sheet.autoSizeColumn(10);
        sheet.autoSizeColumn(11);

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        // response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        try {
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // /api/excel/waybill/send-today/write
    @PostMapping("/waybill/send-today/write")
    public void writeSendTodayWaybill(HttpServletResponse response, @RequestBody List<WaybillGetDto> getDtos) {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        CreationHelper creationHelper = wb.getCreationHelper();
        CellStyle headerRowStyle1 = wb.createCellStyle();
        headerRowStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerRowStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateRowStyle1 = wb.createCellStyle();
        dateRowStyle1.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy/mm/dd dddd"));
        dateRowStyle1.setAlignment(HorizontalAlignment.CENTER);
        dateRowStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("수취인명");

        cell = row.createCell(1);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("상품코드");

        cell = row.createCell(2);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("옵션 및 수량");

        cell = row.createCell(3);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("상품명");

        cell = row.createCell(4);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("수취인 연락처1");

        cell = row.createCell(5);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("수취인 연락처2");

        cell = row.createCell(6);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("우편번호");

        cell = row.createCell(7);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("배송지 주소");

        cell = row.createCell(8);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("구매일자");

        cell = row.createCell(9);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("구매자");

        cell = row.createCell(10);
        cell.setCellStyle(headerRowStyle1);
        cell.setCellValue("구매자연락처");

        List<WaybillAssembledDto> assembledDtos = new ArrayList<>();

        for (WaybillGetDto getDto : getDtos) {
            assembledDtos.addAll(getDto.getList());
        }

        
        // Body
        for (WaybillAssembledDto dto : assembledDtos) {
            row = sheet.createRow(rowNum++);

            cell = row.createCell(0);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getReceiver());

            cell = row.createCell(1);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getProdCode() != null ? dto.getProdCode() : "");

            cell = row.createCell(2);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(getOptionInfosString(dto.getOptionInfos()));

            cell = row.createCell(3);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getProdName());

            cell = row.createCell(4);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getReceiverContact1());

            cell = row.createCell(5);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getReceiverContact2());

            cell = row.createCell(6);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getZipcode());

            cell = row.createCell(7);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getDestination());

            cell = row.createCell(8);
            cell.setCellStyle(dateRowStyle1);
            cell.setCellValue(dto.getPaidTime());

            cell = row.createCell(9);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getBuyer());

            cell = row.createCell(10);
            cell.setCellStyle(headerRowStyle1);
            cell.setCellValue(dto.getBuyerContact());

            row.setHeight((short) -1);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
        sheet.autoSizeColumn(9);
        sheet.autoSizeColumn(10);

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        // response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        try {
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

     /**
     * Check password for order excel.
     * 엑셀 업로드 시 엑셀에 암호화가 걸려있는지 검사하는 API
     * <p>
     * <b>POST : API URL => /api/excel/check-password</b>
     * 
     * @param file : MultipartFile
     * @see CustomExcelUtils#isExcelFile
     */
    // @PermissionRole
    @PostMapping("/check-password")
    public ResponseEntity<?> checkPasswordForUploadedErpOrderExcel(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        CustomExcelUtils.checkPasswordForUploadedErpOrderExcel(file);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    private String getOptionInfosString(List<WaybillOptionInfo> optionInfos) {
        String optionInfosString = "";

        for (WaybillOptionInfo dto : optionInfos) {
            if (dto.getOptionInfo() == null || dto.getOptionInfo().equals("")) {
                optionInfosString += "단일" + "-" + dto.getUnit() + "&&";
                continue;
            }
            if (dto.getOptionInfo().split("/").length >= 1) {
                String[] options = dto.getOptionInfo().split("/");
                String multipleOptions = "";
                for (String option : options) {
                    if (option.split(":")[1] != null) {
                        multipleOptions += option.split(":")[1].replace(" ", "") + "/";
                        
                    }
                }
                
                
                optionInfosString += multipleOptions.substring(0, multipleOptions.length()-1) + "-" + dto.getUnit() + "&&";

                continue;
            }
            if (dto.getOptionInfo().split(":")[1] != null) {
                optionInfosString += dto.getOptionInfo().split(":")[1].replace(" ", "") + "-" + dto.getUnit() + "&&";
                continue;
            }
            optionInfosString += dto.getOptionInfo() + "-" + dto.getUnit() + "&&";
        }
        
        return optionInfosString.substring(0, optionInfosString.length() -2);
    }
}