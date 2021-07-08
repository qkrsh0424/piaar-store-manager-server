package com.piaar_store_manager.server.controller.api;

import java.io.IOException;

import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.shipment.packing_list.PackingListCoupangService;
import com.piaar_store_manager.server.service.shipment.packing_list.PackingListNaverService;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/shipment")
public class ShipmentApiController {
    @Autowired
    PackingListNaverService packingListNaverService;

    @Autowired
    PackingListCoupangService packingListCoupangService;

    /**
     * Read excel for shipment packing list of naver smartstore and combine to create new data.
     * <p>
     * <b>POST : API URL => /api/v1/shipment/packing-list/naver/excel/read</b>
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws IOException
     * @see Message
     * @see HttpStatus
     */
    @PostMapping("/packing-list/naver/excel/read")
    public ResponseEntity<?> readPackingListNaverExcel(@RequestParam("file") MultipartFile file) throws IOException{
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

        message.setMessage("success");
        message.setStatus(HttpStatus.OK);
        message.setData(packingListNaverService.getPackingListData(worksheet));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Read excel for shipment packing list of coupang and combine to create new data.
     * <p>
     * <b>POST : API URL => /api/v1/shipment/packing-list/coupang/excel/read</b>
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws IOException
     * @see Message
     * @see HttpStatus
     */
    @PostMapping("/packing-list/coupang/excel/read")
    public ResponseEntity<?> readPackingListCoupangExcel(@RequestParam("file") MultipartFile file) throws IOException{
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

        message.setMessage("success");
        message.setStatus(HttpStatus.OK);
        message.setData(packingListCoupangService.getPackingListData(worksheet));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
