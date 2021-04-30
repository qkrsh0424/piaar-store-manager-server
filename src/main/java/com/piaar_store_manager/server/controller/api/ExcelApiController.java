package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.order_confirm.dto.OrderConfirmGetDto;
import com.piaar_store_manager.server.model.waybill.WaybillAssembledDto;
import com.piaar_store_manager.server.model.waybill.WaybillGetDto;
import com.piaar_store_manager.server.service.order_confirm.OrderConfirmService;
import com.piaar_store_manager.server.service.waybill.WaybillService;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
@RequestMapping("/api/excel")
public class ExcelApiController {
    @Autowired
    OrderConfirmService orderConfirmService;

    @Autowired
    WaybillService waybillService;

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

    // /api/excel/waybill/read
    @PostMapping("/waybill/read")
    public ResponseEntity<?> readWaybill(@RequestParam("file") MultipartFile file) throws IOException{
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
        // List<OrderConfirmGetDto> orderManageDtos = orderConfirmService.getReadExcel(worksheet);
        List<WaybillGetDto> dtos = waybillService.getReadExcel(worksheet);
        message.setMessage("success");
        message.setStatus(HttpStatus.OK);
        message.setData(dtos);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // /api/excel/waybill/logen/write
    @PostMapping("/waybill/logen/write")
    public void writeLogenWaybill(HttpServletResponse response, @RequestBody WaybillGetDto getDto){
        System.out.println(getDto);
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
        cell.setCellValue("배송메세지");

        // Body
        for (int i=0; i<getDto.getList().size(); i++) {
            WaybillAssembledDto assembledDto = getDto.getList().get(i);
            String receiverStr = assembledDto.getReceiver() + " ";
            for(int j = 0 ; j < assembledDto.getOptionInfos().size(); j++){
                receiverStr += assembledDto.getOptionInfos().get(j).getOptionInfo() + "-" + assembledDto.getOptionInfos().get(j).getUnit();
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
            cell.setCellValue(assembledDto.getDeliveryMessage());
            
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
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
}
