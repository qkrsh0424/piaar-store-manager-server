package com.piaar_store_manager.server.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemTailoExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemViewDto;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.delivery_ready.DeliveryReadyService;
import com.piaar_store_manager.server.service.user.UserService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/delivery-ready")
public class DeliveryReadyApiController {
    
    @Autowired
    private DeliveryReadyService deliveryReadyService;

    @Autowired
    private UserService userService;

    /**
     * Upload excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/upload</b>
     * 
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws IOException
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyService#isExcelFile
     * @see DeliveryReadyService#uploadDeliveryReadyExcelFile
     * @see UserService#isUserLogin
     * @see UserService#userDenyCheck
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        if (!userService.isUserLogin()) {
            message.setStatus(HttpStatus.FORBIDDEN);
            message.setMessage("need_login");
            message.setMemo("need login");
        } else {
            // file extension check.
            deliveryReadyService.isExcelFile(file);

            message.setData(deliveryReadyService.uploadDeliveryReadyExcelFile(file));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

     /**
     * Store excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/store</b>
     * 
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws IOException
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyService#isExcelFile
     * @see DeliveryReadyService#storeDeliveryReadyExcelFile
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // 유저 권한을 체크한다.
        if (userService.isManager()) {
            // file extension check.
            deliveryReadyService.isExcelFile(file);

            message.setData(deliveryReadyService.storeDeliveryReadyExcelFile(file, userService.getUserId()));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search unreleased data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/view/unreleased</b>
     * 
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyService#getDeliveryReadyViewUnreleasedData
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @GetMapping("/view/unreleased")
    public ResponseEntity<?> getDeliveryReadyViewUnreleasedData() {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            message.setData(deliveryReadyService.getDeliveryReadyViewUnreleasedData());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search released data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/view/released</b>
     * 
     * @return ResponseEntity(message, HttpStatus)
     * @param query : Map[startDate, endDate]
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyService#getDeliveryReadyViewReleased
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @GetMapping("/view/release")
    public ResponseEntity<?> getDeliveryReadyViewReleased(@RequestParam Map<String, Object> query) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            message.setData(deliveryReadyService.getDeliveryReadyViewReleased(query));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

     /**
     * Destroy( Delete or Remove ) unreleased data for delivery ready.
     * <p>
     * <b>DELETE : API URL => /api/v1/view/deleteOne/{itemCid}</b>
     *
     * @param itemCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see deliveryReadyService#deleteOneDeliveryReadyViewData
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @DeleteMapping("/view/deleteOne/{itemCid}")
    public ResponseEntity<?> deleteOneDeliveryReadyViewData(@PathVariable(value = "itemCid") Integer itemCid) {
        Message message = new Message();

        if (userService.isManager()) {
            try{
                deliveryReadyService.deleteOneDeliveryReadyViewData(itemCid);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("not_found");
                message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/view/updateOne</b>
     *
     * @param deliveryReadyItemDto : DeliveryReadyItemDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see deliveryReadyService#updateReleasedDeliveryReadyItem
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @PutMapping("/view/updateOne")
    public ResponseEntity<?> updateReleasedDeliveryReadyItem(@RequestBody DeliveryReadyItemDto deliveryReadyItemDto) {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try {
                deliveryReadyService.updateReleasedDeliveryReadyItem(deliveryReadyItemDto);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("not_found");
                message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search option info for product.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/view/seachList/optionInfo</b>
     * 
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyService#searchDeliveryReadyItemOptionInfo
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @GetMapping("/view/searchList/optionInfo")
    public ResponseEntity<?> searchDeliveryReadyItemOptionInfo() {
        Message message = new Message();

        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            message.setData(deliveryReadyService.searchDeliveryReadyItemOptionInfo());
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/view/updateOption</b>
     *
     * @param deliveryReadyItemDto : DeliveryReadyItemDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see deliveryReadyService#updateDeliveryReadyItemOptionInfo
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @PutMapping("/view/updateOption")
    public ResponseEntity<?> updateDeliveryReadyItemOptionInfo(@RequestBody DeliveryReadyItemDto deliveryReadyItemDto) {
        Message message = new Message();
        
        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try {
                deliveryReadyService.updateDeliveryReadyItemOptionInfo(deliveryReadyItemDto);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("not_found");
                message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/view/updateOptions</b>
     *
     * @param deliveryReadyItemDto : DeliveryReadyItemDto
     * @param query : Map[optionCode]
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see deliveryReadyService#updateDeliveryReadyItemsOptionInfo
     * @see UserService#isManager
     * @see UserService#userDenyCheck
     */
    @PutMapping("/view/updateOptions")
    public ResponseEntity<?> updateDeliveryReadyItemsOptionInfo(@RequestBody DeliveryReadyItemDto deliveryReadyItemDto) {
        Message message = new Message();
        
        // 유저의 권한을 체크한다.
        if (userService.isManager()) {
            try {
                deliveryReadyService.updateDeliveryReadyItemsOptionInfo(deliveryReadyItemDto);
                message.setStatus(HttpStatus.OK);
                message.setMessage("success");
            } catch (NullPointerException e) {
                message.setStatus(HttpStatus.NOT_FOUND);
                message.setMessage("not_found");
                message.setMemo("해당 데이터를 찾을 수 없습니다. 관리자에게 문의하세요.");
            }
        } else {
            userService.userDenyCheck(message);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/view/download/hansan</b>
     *
     * @param viewDtos : List::DeliveryReadyItemDto::
     * @see Message
     * @see HttpStatus
     * @see deliveryReadyService#changeDeliveryReadyItem
     * @see deliveryReadyService#releasedDeliveryReadyItem
     */
    @PostMapping("/view/download/hansan")
    public void downloadHansanExcelFile(HttpServletResponse response, @RequestBody List<DeliveryReadyItemViewDto> viewDtos) {

        // 중복데이터 처리
        List<DeliveryReadyItemExcelFormDto> dtos = deliveryReadyService.changeDeliveryReadyItem(viewDtos);
        
        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("한산 발주서");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("받는사람");
        cell = row.createCell(1);
        cell.setCellValue("전화번호1");
        cell = row.createCell(2);
        cell.setCellValue("우편번호");
        cell = row.createCell(3);
        cell.setCellValue("주소");
        cell = row.createCell(4);
        cell.setCellValue("운송장번호");
        cell = row.createCell(5);
        cell.setCellValue("상품명1");
        cell = row.createCell(6);
        cell.setCellValue("보내는사람(지정)");
        cell = row.createCell(7);
        cell.setCellValue("전화번호1(지정)");
        cell = row.createCell(8);
        cell.setCellValue("상품상세1");
        cell = row.createCell(9);
        cell.setCellValue("내품수량1");
        cell = row.createCell(10);
        cell.setCellValue("배송메시지");
        cell = row.createCell(11);
        cell.setCellValue("수량(A타입)");
        cell = row.createCell(12);
        cell.setCellValue("주문번호");
        cell = row.createCell(13);
        cell.setCellValue("상품주문번호");
        cell = row.createCell(14);
        cell.setCellValue("스마트스토어 상품명");
        cell = row.createCell(15);
        cell.setCellValue("스마트스토어 옵션명");
        cell = row.createCell(16);
        cell.setCellValue("옵션관리코드");
        cell = row.createCell(17);
        cell.setCellValue("총 상품주문번호");


        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.BRICKS);

        for (int i=0; i<dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);

            // 받는사람 + 번호 + 주소 : 중복데이터 엑셀 셀 색상 설정
            if(dtos.get(i).isDuplication()){
                cell.setCellStyle(cellStyle);
            }
            cell.setCellValue(dtos.get(i).getReceiver());
            cell = row.createCell(1);
            cell.setCellValue(dtos.get(i).getReceiverContact1());
            cell = row.createCell(2);
            cell.setCellValue(dtos.get(i).getZipCode());
            cell = row.createCell(3);
            cell.setCellValue(dtos.get(i).getDestination());
            cell = row.createCell(4);
            cell.setCellValue(dtos.get(i).getTransportNumber());
            cell = row.createCell(5);
            cell.setCellValue(dtos.get(i).getStoreProdName() != null ? dtos.get(i).getStoreProdName() + " | " + dtos.get(i).getProdManufacturingCode() : "*지정 바람");       // 피아르 상품관리명 + 상품제조번호
            cell = row.createCell(6);
            cell.setCellValue(dtos.get(i).getSender() != null ? dtos.get(i).getSender() : "*지정바람");
            cell = row.createCell(7);
            cell.setCellValue(dtos.get(i).getSenderContact1() != null ? dtos.get(i).getSenderContact1() : "*지정바람");
            cell = row.createCell(8);
            cell.setCellValue(dtos.get(i).getStoreOptionName() != null ? dtos.get(i).getStoreOptionName() + " | " + dtos.get(i).getOptionManagementCode() : "*지정 바람");       // 피아르 옵션관리명 + 피아르 옵션관리코드
            cell = row.createCell(9);
            cell.setCellValue(dtos.get(i).getUnit());
            cell = row.createCell(10);
            cell.setCellValue(dtos.get(i).getDeliveryMessage());
            cell = row.createCell(11);
            cell.setCellValue(dtos.get(i).getUnitA());
            cell = row.createCell(12);
            cell.setCellValue(dtos.get(i).getOrderNumber());
            cell = row.createCell(13);
            cell.setCellValue(dtos.get(i).getProdOrderNumber());
            cell = row.createCell(14);
            cell.setCellValue(dtos.get(i).getProdName());       // 스마트스토어 상품명
            cell = row.createCell(15);
            cell.setCellValue(dtos.get(i).getOptionInfo());    // 스마트스토어 옵션명
            cell = row.createCell(16);
            cell.setCellValue(dtos.get(i).getOptionManagementCode());
            cell = row.createCell(17);
            cell.setCellValue(dtos.get(i).getAllProdOrderNumber());
        }

        for(int i = 0; i < 18; i++){
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // released, released_at 설정
        deliveryReadyService.releasedDeliveryReadyItem(viewDtos);
    }

    /**
     * Download data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/view/download/tailo</b>
     *
     * @param viewDtos : List::DeliveryReadyItemDto::
     * @see Message
     * @see HttpStatus
     * @see deliveryReadyService#changeDeliveryReadyItem
     * @see deliveryReadyService#releasedDeliveryReadyItem
     */
    @PostMapping("/view/download/tailo")
    public void downloadTailoExcelFile(HttpServletResponse response, @RequestBody List<DeliveryReadyItemViewDto> viewDtos) {
        List<DeliveryReadyItemTailoExcelFormDto> dtos = new ArrayList<>();
        
        for(DeliveryReadyItemViewDto viewDto : viewDtos) {
            dtos.add(DeliveryReadyItemTailoExcelFormDto.toTailoFormDto(viewDto));
        }
        
        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("테일로 발주서");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("상품고유코드");
        cell = row.createCell(1);
        cell.setCellValue("판매상품명");
        cell = row.createCell(2);
        cell.setCellValue("수량");
        cell = row.createCell(3);
        cell.setCellValue("배송방식");
        cell = row.createCell(4);
        cell.setCellValue("주문자 이름");
        cell = row.createCell(5);
        cell.setCellValue("받는분 이름");
        cell = row.createCell(6);
        cell.setCellValue("전화번호1");
        cell = row.createCell(7);
        cell.setCellValue("전화번호2");
        cell = row.createCell(8);
        cell.setCellValue("우편번호");
        cell = row.createCell(9);
        cell.setCellValue("주소1");
        cell = row.createCell(10);
        cell.setCellValue("주소2");
        cell = row.createCell(11);
        cell.setCellValue("배송메세지");
        cell = row.createCell(12);
        cell.setCellValue("주문번호");
        cell = row.createCell(13);
        cell.setCellValue("관리번호1");
        cell = row.createCell(14);
        cell.setCellValue("관리번호2");
        cell = row.createCell(15);
        cell.setCellValue("관리번호3");
        cell = row.createCell(16);
        cell.setCellValue("관리번호4");
        cell = row.createCell(17);
        cell.setCellValue("관리번호5");
        cell = row.createCell(18);
        cell.setCellValue("상품별 메모1");
        cell = row.createCell(19);
        cell.setCellValue("상품별 메모2");
        cell = row.createCell(20);
        cell.setCellValue("상품별 메모3");
        cell = row.createCell(21);
        cell.setCellValue("발주 타입");
        cell = row.createCell(22);
        cell.setCellValue("출고희망일");

        for (int i=0; i<dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(dtos.get(i).getProdUniqueCode());
            cell = row.createCell(1);
            cell.setCellValue(dtos.get(i).getSalesProdName());
            cell = row.createCell(2);
            cell.setCellValue(dtos.get(i).getUnit());
            cell = row.createCell(3);
            cell.setCellValue(dtos.get(i).getTransportType());
            cell = row.createCell(4);
            cell.setCellValue(dtos.get(i).getBuyer());
            cell = row.createCell(5);
            cell.setCellValue(dtos.get(i).getReceiver());
            cell = row.createCell(6);
            cell.setCellValue(dtos.get(i).getReceiverContact1());
            cell = row.createCell(7);
            cell.setCellValue(dtos.get(i).getReceiverContact2());
            cell = row.createCell(8);
            cell.setCellValue(dtos.get(i).getZipCode());
            cell = row.createCell(9);
            cell.setCellValue(dtos.get(i).getDestination1());
            cell = row.createCell(10);
            cell.setCellValue(dtos.get(i).getDestination2());
            cell = row.createCell(11);
            cell.setCellValue(dtos.get(i).getDeliveryMessage());
            cell = row.createCell(12);
            cell.setCellValue(dtos.get(i).getOrderNumber());
            cell = row.createCell(13);
            cell.setCellValue(dtos.get(i).getManagementMemo1());
            cell = row.createCell(14);
            cell.setCellValue(dtos.get(i).getManagementMemo2());
            cell = row.createCell(15);
            cell.setCellValue(dtos.get(i).getManagementMemo3());
            cell = row.createCell(16);
            cell.setCellValue(dtos.get(i).getManagementMemo4());
            cell = row.createCell(17);
            cell.setCellValue(dtos.get(i).getManagementMemo5());
            cell = row.createCell(18);
            cell.setCellValue(dtos.get(i).getProdMemo1());
            cell = row.createCell(19);
            cell.setCellValue(dtos.get(i).getProdMemo2());
            cell = row.createCell(20);
            cell.setCellValue(dtos.get(i).getProdMemo3());
            cell = row.createCell(21);
            cell.setCellValue(dtos.get(i).getOrderType());
            cell = row.createCell(22);
            cell.setCellValue(dtos.get(i).getReleaseDesiredDate());
        }

        for(int i = 0; i < 23; i++){
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // released, released_at 설정
        deliveryReadyService.releasedDeliveryReadyItem(viewDtos);
    }
}
