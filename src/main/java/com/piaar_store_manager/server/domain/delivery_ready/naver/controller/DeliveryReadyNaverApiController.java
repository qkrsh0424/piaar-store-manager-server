package com.piaar_store_manager.server.domain.delivery_ready.naver.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemLotteExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemTailoExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;
import com.piaar_store_manager.server.domain.delivery_ready.naver.dto.DeliveryReadyNaverItemExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.domain.delivery_ready.naver.service.DeliveryReadyNaverBusinessService;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
@RequestMapping("/api/v1/delivery-ready/naver")
@RequiredArgsConstructor
@RequiredLogin
public class DeliveryReadyNaverApiController {
    private final DeliveryReadyNaverBusinessService deliveryReadyNaverBusinessService;

    /**
     * Upload excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/upload</b>
     *
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws CustomExcelFileUploadException
     * @throws NullPointerException
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @see Message
     * @see HttpStatus
     * @see CustomExcelUtils#isExcelFile
     * @see DeliveryReadyNaverBusinessService#uploadDeliveryReadyExcelFile
     */
    @PermissionRole
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        try {
            message.setData(deliveryReadyNaverBusinessService.uploadDeliveryReadyExcelFile(file));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("?????? ?????? ???????????? ???????????? ?????? ?????? ???????????????.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("?????????????????? ?????? ?????? ?????? ????????? ????????? ????????? ?????? ?????? ???????????????.\n????????? ?????? ?????? ?????? ????????? ?????????????????????");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("?????????????????? ?????? ?????? ?????? ????????? ????????????.\n????????? ?????? ?????? ?????? ????????? ?????????????????????");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Store excel data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/store</b>
     *
     * @param file
     * @return ResponseEntity(message, HttpStatus)
     * @throws CustomExcelFileUploadException
     * @see Message
     * @see HttpStatus
     * @see CustomExcelUtils#isExcelFile
     * @see DeliveryReadyNaverBusinessService#storeDeliveryReadyExcelFile
     */
    @PermissionRole
    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        deliveryReadyNaverBusinessService.storeDeliveryReadyExcelFile(file);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search unreleased data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/naver/view/unreleased</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#getDeliveryReadyViewUnreleasedData
     */
    @PermissionRole
    @GetMapping("/view/unreleased")
    public ResponseEntity<?> getDeliveryReadyViewUnreleasedData() {
        Message message = new Message();

        message.setData(deliveryReadyNaverBusinessService.getDeliveryReadyViewUnreleasedData());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search released data for delivery ready.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/naver/view/released</b>
     *
     * @param query : Map[startDate, endDate]
     * @return ResponseEntity(message, HttpStatus)
     * @throws ParseException
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#getDeliveryReadyViewReleased
     */
    @PermissionRole
    @GetMapping("/view/released")
    public ResponseEntity<?> getDeliveryReadyViewReleased(@RequestParam Map<String, Object> query) throws ParseException {
        Message message = new Message();

        message.setData(deliveryReadyNaverBusinessService.getDeliveryReadyViewReleased(query));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) unreleased data for delivery ready.
     * <p>
     * <b>DELETE : API URL => /api/v1/delivery-ready/naver/view/delete/one/{itemCid}</b>
     *
     * @param itemCid : Integer
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#deleteOneDeliveryReadyViewData
     */
    @PermissionRole
    @DeleteMapping("/view/delete/one/{itemCid}")
    public ResponseEntity<?> deleteOneDeliveryReadyViewData(@PathVariable(value = "itemCid") Integer itemCid) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.deleteOneDeliveryReadyViewData(itemCid);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }
        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Destroy( Delete or Remove ) checked unreleased data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/view/delete/batch</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     */
    @PermissionRole
    @PostMapping("/view/delete/batch")
    public ResponseEntity<?> deleteListDeliveryReadyViewData(@RequestBody List<DeliveryReadyNaverItemDto> deliveryReadyNaverItemDtos) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.deleteListDeliveryReadyViewData(deliveryReadyNaverItemDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/update/one</b>
     *
     * @param deliveryReadyNaverItemDto : DeliveryReadyNaverItemDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#updateReleasedDeliveryReadyItem
     */
    @PermissionRole
    @PutMapping("/view/update/one")
    public ResponseEntity<?> updateReleasedDeliveryReadyItem(@RequestBody DeliveryReadyNaverItemDto deliveryReadyNaverItemDto) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.updateReleasedDeliveryReadyItem(deliveryReadyNaverItemDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/update/list/unrelease</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#updateListToUnreleasedDeliveryReadyItem
     */
    @PermissionRole
    @PutMapping("/view/update/list/unrelease")
    public ResponseEntity<?> updateListToUnreleasedDeliveryReadyItem(@RequestBody List<DeliveryReadyNaverItemDto> deliveryReadyNaverItemDtos) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.updateListToUnreleasedDeliveryReadyItem(deliveryReadyNaverItemDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change unreleased data to released data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/update/list/release</b>
     *
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#updateListToReleaseDeliveryReadyItem
     */
    @PermissionRole
    @PutMapping("/view/update/list/release")
    public ResponseEntity<?> updateListToReleaseDeliveryReadyItem(@RequestBody List<DeliveryReadyNaverItemViewDto> viewDtos) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.updateListToReleaseDeliveryReadyItem(viewDtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search option info for product.
     * <p>
     * <b>GET : API URL => /api/v1/delivery-ready/naver/view/seach/list/option-info</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#searchDeliveryReadyItemOptionInfo
     */
    @PermissionRole
    @GetMapping("/view/search/list/option-info")
    public ResponseEntity<?> searchDeliveryReadyItemOptionInfo() {
        Message message = new Message();

        message.setData(deliveryReadyNaverBusinessService.searchDeliveryReadyItemOptionInfo());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/update/option</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#updateDeliveryReadyItemOptionInfo
     */
    @PermissionRole
    @PutMapping("/view/update/option")
    public ResponseEntity<?> updateDeliveryReadyItemOptionInfo(@RequestBody DeliveryReadyNaverItemDto deliveryReadyNaverItemDto) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.updateDeliveryReadyItemOptionInfo(deliveryReadyNaverItemDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/update/options</b>
     *
     * @param deliveryReadyNaverItemDto : DeliveryReadyNaverItemDto
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#updateDeliveryReadyItemsOptionInfo
     */
    @PermissionRole
    @PutMapping("/view/update/options")
    public ResponseEntity<?> updateDeliveryReadyItemsOptionInfo(@RequestBody DeliveryReadyNaverItemDto deliveryReadyNaverItemDto) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.updateDeliveryReadyItemsOptionInfo(deliveryReadyNaverItemDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change released data to unreleased data for delivery ready.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/update/release-option</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#updateDeliveryReadyItemReleaseOptionInfo
     */
    @PermissionRole
    @PutMapping("/view/update/release-option")
    public ResponseEntity<?> updateDeliveryReadyItemReleaseOptionInfo(@RequestBody DeliveryReadyNaverItemDto deliveryReadyNaverItemDto) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.updateDeliveryReadyItemReleaseOptionInfo(deliveryReadyNaverItemDto);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update data for delivery ready data.
     * Reflect the stock unit of product options.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/stock-unit</b>
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     */
    @PermissionRole
    @PutMapping("/view/stock-unit")
    public ResponseEntity<?> releaseListStockUnit(@RequestBody List<DeliveryReadyNaverItemViewDto> dtos) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.releaseListStockUnit(dtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update data for delivery ready.
     * Cancel the stock unit reflection of product options.
     * <p>
     * <b>PUT : API URL => /api/v1/delivery-ready/naver/view/stock-unit/cancel</b>
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     */
    @PermissionRole
    @PutMapping("/view/stock-unit/cancel")
    public ResponseEntity<?> cancelReleaseListStockUnit(@RequestBody List<DeliveryReadyNaverItemViewDto> dtos) {
        Message message = new Message();

        try {
            deliveryReadyNaverBusinessService.cancelReleaseListStockUnit(dtos);
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("not_found");
            message.setMemo("?????? ???????????? ?????? ??? ????????????. ??????????????? ???????????????.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Download data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/view/download/hansan</b>
     *
     * @param response : HttpServletResponse
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @throws IOException
     * @see Message
     * @see HttpStatus
     */
    @PermissionRole
    @PostMapping("/view/download/hansan")
    public void downloadHansanExcelFile(HttpServletResponse response, @RequestBody List<DeliveryReadyNaverItemViewDto> viewDtos) {
        // ??????????????? ??????
        List<DeliveryReadyItemHansanExcelFormDto> dtos = deliveryReadyNaverBusinessService.changeDeliveryReadyItemToHansan(viewDtos);

        // ?????? ??????
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("?????? ?????????");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("????????????");
        cell = row.createCell(1);
        cell.setCellValue("????????????1");
        cell = row.createCell(2);
        cell.setCellValue("????????????");
        cell = row.createCell(3);
        cell.setCellValue("??????");
        cell = row.createCell(4);
        cell.setCellValue("???????????????");
        cell = row.createCell(5);
        cell.setCellValue("?????????1");
        cell = row.createCell(6);
        cell.setCellValue("???????????????(??????)");
        cell = row.createCell(7);
        cell.setCellValue("????????????1(??????)");
        cell = row.createCell(8);
        cell.setCellValue("????????????1");
        cell = row.createCell(9);
        cell.setCellValue("????????????1");
        cell = row.createCell(10);
        cell.setCellValue("???????????????");
        cell = row.createCell(11);
        cell.setCellValue("??????(A??????)");
        cell = row.createCell(12);
        cell.setCellValue("????????????");
        cell = row.createCell(13);
        cell.setCellValue("??????????????????");
        cell = row.createCell(14);
        cell.setCellValue("???????????? ?????????");
        cell = row.createCell(15);
        cell.setCellValue("???????????? ?????????");
        cell = row.createCell(16);
        cell.setCellValue("??????????????????");
        cell = row.createCell(17);
        cell.setCellValue("??? ??????????????????");
        cell = row.createCell(18);
        cell.setCellValue("????????????");


        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.BRICKS);

        for (int i = 0; i < dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            // ???????????? + ?????? + ?????? : ??????????????? ?????? ??? ?????? ??????
            if (dtos.get(i).isDuplication()) {
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
            cell.setCellValue(dtos.get(i).getStoreProdName() != null ? dtos.get(i).getStoreProdName() + " | " + dtos.get(i).getProdManufacturingCode() : "*????????????");       // ????????? ??????????????? + ????????? ??????????????????
            cell = row.createCell(6);
            cell.setCellValue(dtos.get(i).getSender() != null ? dtos.get(i).getSender() : "*????????????");
            cell = row.createCell(7);
            cell.setCellValue(dtos.get(i).getSenderContact1() != null ? dtos.get(i).getSenderContact1() : "*????????????");
            cell = row.createCell(8);
            cell.setCellValue(dtos.get(i).getStoreOptionName() != null ? dtos.get(i).getStoreOptionName() + " | " + dtos.get(i).getOptionManagementCode() : "*????????????");       // ????????? ??????????????? + ????????? ??????????????????
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
            cell.setCellValue(dtos.get(i).getProdName());       // ?????????????????? ?????????
            cell = row.createCell(15);
            cell.setCellValue(dtos.get(i).getOptionInfo());    // ?????????????????? ?????????
            cell = row.createCell(16);
            cell.setCellValue(dtos.get(i).getOptionManagementCode());
            cell = row.createCell(17);
            cell.setCellValue(dtos.get(i).getAllProdOrderNumber());
            cell = row.createCell(18);
            cell.setCellValue("?????????");
        }

        for (int i = 0; i < 19; i++) {
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

        // released, released_at ??????
        deliveryReadyNaverBusinessService.updateListToReleaseDeliveryReadyItem(viewDtos);
    }

    /**
     * Download data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/view/download/tailo</b>
     *
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @throws IOException
     * @see Message
     * @see HttpStatus
     * @see DeliveryReadyNaverBusinessService#releasedDeliveryReadyItem
     */
    @PermissionRole
    @PostMapping("/view/download/tailo")
    public void downloadTailoExcelFile(HttpServletResponse response, @RequestBody List<DeliveryReadyNaverItemViewDto> viewDtos) {
        List<DeliveryReadyItemTailoExcelFormDto> dtos = new ArrayList<>();

        for (DeliveryReadyNaverItemViewDto viewDto : viewDtos) {
            dtos.add(DeliveryReadyItemTailoExcelFormDto.toTailoFormDto(viewDto));
        }

        // ????????? > ???????????? > ??????
        Comparator<DeliveryReadyItemTailoExcelFormDto> comparing = Comparator
                .comparing(DeliveryReadyItemTailoExcelFormDto::getProdMemo2)
                .thenComparing(DeliveryReadyItemTailoExcelFormDto::getReceiver)
                .thenComparing(DeliveryReadyItemTailoExcelFormDto::getDestination1);
        dtos.sort(comparing);


        // ?????? ??????
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("????????? ?????????");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("??????????????????");
        cell = row.createCell(1);
        cell.setCellValue("???????????????");
        cell = row.createCell(2);
        cell.setCellValue("??????");
        cell = row.createCell(3);
        cell.setCellValue("????????????");
        cell = row.createCell(4);
        cell.setCellValue("????????? ??????");
        cell = row.createCell(5);
        cell.setCellValue("????????? ??????");
        cell = row.createCell(6);
        cell.setCellValue("????????????1");
        cell = row.createCell(7);
        cell.setCellValue("????????????2");
        cell = row.createCell(8);
        cell.setCellValue("????????????");
        cell = row.createCell(9);
        cell.setCellValue("??????1");
        cell = row.createCell(10);
        cell.setCellValue("??????2");
        cell = row.createCell(11);
        cell.setCellValue("???????????????");
        cell = row.createCell(12);
        cell.setCellValue("????????????");
        cell = row.createCell(13);
        cell.setCellValue("????????????1");
        cell = row.createCell(14);
        cell.setCellValue("????????????2");
        cell = row.createCell(15);
        cell.setCellValue("????????????3");
        cell = row.createCell(16);
        cell.setCellValue("????????????4");
        cell = row.createCell(17);
        cell.setCellValue("????????????5");
        cell = row.createCell(18);
        cell.setCellValue("????????? ??????1");
        cell = row.createCell(19);
        cell.setCellValue("????????? ??????2");
        cell = row.createCell(20);
        cell.setCellValue("????????? ??????3");
        cell = row.createCell(21);
        cell.setCellValue("?????? ??????");
        cell = row.createCell(22);
        cell.setCellValue("???????????????");

        for (int i = 0; i < dtos.size(); i++) {
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

        for (int i = 0; i < 23; i++) {
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

        // released, released_at ??????
        deliveryReadyNaverBusinessService.updateListToReleaseDeliveryReadyItem(viewDtos);
    }

    /**
     * Download data for delivery ready.
     * <p>
     * <b>POST : API URL => /api/v1/delivery-ready/naver/view/download/lotte</b>
     *
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @throws IOException
     * @see Message
     * @see HttpStatus
     */
    @PermissionRole
    @PostMapping("/view/download/lotte")
    public void downloadLotteExcelFile(HttpServletResponse response, @RequestBody List<DeliveryReadyNaverItemViewDto> viewDtos) {
        // ??????????????? ??????
        List<DeliveryReadyItemLotteExcelFormDto> dtos = deliveryReadyNaverBusinessService.changeDeliveryReadyItemToLotte(viewDtos);

        // ???????????? > ?????? > ?????????
        Comparator<DeliveryReadyItemLotteExcelFormDto> comparing = Comparator
                .comparing(DeliveryReadyItemLotteExcelFormDto::getReceiver)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getDestination)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getProdName1);
        dtos.sort(comparing);


        // ?????? ??????
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("?????? ?????????");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("????????????");
        cell = row.createCell(1);
        cell.setCellValue("????????????");
        cell = row.createCell(2);
        cell.setCellValue("??????");
        cell = row.createCell(3);
        cell.setCellValue("????????????1");
        cell = row.createCell(4);
        cell.setCellValue("????????????2");
        cell = row.createCell(5);
        cell.setCellValue("???????????????");
        cell = row.createCell(6);
        cell.setCellValue("??????????????????");
        cell = row.createCell(7);
        cell.setCellValue("???????????????(??????)");
        cell = row.createCell(8);
        cell.setCellValue("????????????1(??????)");
        cell = row.createCell(9);
        cell.setCellValue("??????(??????)");
        cell = row.createCell(10);
        cell.setCellValue("??????(A??????)");
        cell = row.createCell(11);
        cell.setCellValue("?????????1");
        cell = row.createCell(12);
        cell.setCellValue("????????????1");
        cell = row.createCell(13);
        cell.setCellValue("????????????1");
        cell = row.createCell(14);
        cell.setCellValue("????????????1");
        cell = row.createCell(15);
        cell.setCellValue("????????????2");
        cell = row.createCell(16);
        cell.setCellValue("????????????2");
        cell = row.createCell(17);
        cell.setCellValue("??? ??????????????????");
        cell = row.createCell(18);
        cell.setCellValue("??? ????????????");

        for (int i = 0; i < dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(dtos.get(i).getReceiver());
            cell = row.createCell(1);
            cell.setCellValue(dtos.get(i).getZipCode());
            cell = row.createCell(2);
            cell.setCellValue(dtos.get(i).getDestination());
            cell = row.createCell(3);
            cell.setCellValue(dtos.get(i).getReceiverContact1());
            cell = row.createCell(4);
            cell.setCellValue(dtos.get(i).getReceiverContact2());
            cell = row.createCell(5);
            cell.setCellValue(dtos.get(i).getDeliveryMessage());
            cell = row.createCell(6);
            cell.setCellValue(dtos.get(i).getUnnecessaryCell());
            cell = row.createCell(7);
            cell.setCellValue(dtos.get(i).getSender());
            cell = row.createCell(8);
            cell.setCellValue(dtos.get(i).getSenderContact1());
            cell = row.createCell(9);
            cell.setCellValue(dtos.get(i).getSenderAddress());
            cell = row.createCell(10);
            cell.setCellValue(dtos.get(i).getUnitA());
            cell = row.createCell(11);
            cell.setCellValue(dtos.get(i).getAllProdInfo());
            cell = row.createCell(12);
            cell.setCellValue("");
            cell = row.createCell(13);
            cell.setCellValue("");
            cell = row.createCell(14);
            cell.setCellValue(dtos.get(i).getOrderNumber());
            cell = row.createCell(15);
            cell.setCellValue(dtos.get(i).getProdOrderNumber());
            cell = row.createCell(16);
            cell.setCellValue(dtos.get(i).getPlatformName());
            cell = row.createCell(17);
            cell.setCellValue(dtos.get(i).getAllProdOrderNumber());
        }

        for (int i = 0; i < 18; i++) {
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

        // released, released_at ??????
        deliveryReadyNaverBusinessService.updateListToReleaseDeliveryReadyItem(viewDtos);
    }

    @PermissionRole
    @PostMapping("/view/download/excel")
    public void downloadExcelFile(HttpServletResponse response, @RequestBody List<DeliveryReadyNaverItemViewDto> viewDtos) {
        List<DeliveryReadyNaverItemExcelFormDto> dtos = new ArrayList<>();

        for (DeliveryReadyNaverItemViewDto viewDto : viewDtos) {
            dtos.add(DeliveryReadyNaverItemExcelFormDto.toNaverFormDto(viewDto));
        }

        // ?????? ??????
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("????????? ???????????? ?????????");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("??????????????????");
        cell = row.createCell(1);
        cell.setCellValue("????????????");
        cell = row.createCell(2);
        cell.setCellValue("????????????");
        cell = row.createCell(3);
        cell.setCellValue("?????????ID");
        cell = row.createCell(4);
        cell.setCellValue("????????????");
        cell = row.createCell(5);
        cell.setCellValue("????????? ???????????????");
        cell = row.createCell(6);
        cell.setCellValue("????????? ??????????????????");
        cell = row.createCell(7);
        cell.setCellValue("????????? ??????????????????");
        cell = row.createCell(8);
        cell.setCellValue("????????? ???????????????1");
        cell = row.createCell(9);
        cell.setCellValue("????????? ???????????????2");
        cell = row.createCell(10);
        cell.setCellValue("??????");
        cell = row.createCell(11);
        cell.setCellValue("????????????");
        cell = row.createCell(12);
        cell.setCellValue("?????????");
        cell = row.createCell(13);
        cell.setCellValue("???????????????");
        cell = row.createCell(14);
        cell.setCellValue("????????????");
        cell = row.createCell(15);
        cell.setCellValue("????????? ????????????");
        cell = row.createCell(16);
        cell.setCellValue("????????????");
        cell = row.createCell(17);
        cell.setCellValue("????????? ????????????");
        cell = row.createCell(18);
        cell.setCellValue("?????????");
        cell = row.createCell(19);
        cell.setCellValue("????????????");
        cell = row.createCell(20);
        cell.setCellValue("???????????? ????????????");
        cell = row.createCell(21);
        cell.setCellValue("??????????????????1");
        cell = row.createCell(22);
        cell.setCellValue("??????????????????2");
        cell = row.createCell(23);
        cell.setCellValue("????????????");
        cell = row.createCell(24);
        cell.setCellValue("?????????");
        cell = row.createCell(25);
        cell.setCellValue("??????????????????");
        cell = row.createCell(26);
        cell.setCellValue("???????????????");
        cell = row.createCell(27);
        cell.setCellValue("????????????");
        cell = row.createCell(28);
        cell.setCellValue("????????????");
        cell = row.createCell(29);
        cell.setCellValue("????????????");

        for (int i = 0; i < dtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(dtos.get(i).getProdOrderNumber());
            cell = row.createCell(1);
            cell.setCellValue(dtos.get(i).getOrderNumber());
            cell = row.createCell(2);
            cell.setCellValue(dtos.get(i).getBuyer());
            cell = row.createCell(3);
            cell.setCellValue(dtos.get(i).getBuyerId());
            cell = row.createCell(4);
            cell.setCellValue(dtos.get(i).getReceiver());
            cell = row.createCell(5);
            cell.setCellValue(dtos.get(i).getProdManagementName());
            cell = row.createCell(6);
            cell.setCellValue(dtos.get(i).getProdManufacturingCode());
            cell = row.createCell(7);
            cell.setCellValue(dtos.get(i).getOptionManagementCode());
            cell = row.createCell(8);
            cell.setCellValue(dtos.get(i).getOptionDefaultName());
            cell = row.createCell(9);
            cell.setCellValue(dtos.get(i).getOptionManagementName());
            cell = row.createCell(10);
            cell.setCellValue(dtos.get(i).getUnit());
            cell = row.createCell(11);
            cell.setCellValue(dtos.get(i).getOptionStockUnit());
            cell = row.createCell(12);
            cell.setCellValue(dtos.get(i).getPaymentDate() != null ? dateFormat.format(dtos.get(i).getPaymentDate()) : null);
            cell = row.createCell(13);
            cell.setCellValue(dtos.get(i).getOrderConfirmationDate() != null ? dateFormat.format(dtos.get(i).getOrderConfirmationDate()) : null);
            cell = row.createCell(14);
            cell.setCellValue(dtos.get(i).getShipmentDueDate() != null ? dateFormat.format(dtos.get(i).getShipmentDueDate()) : null);
            cell = row.createCell(15);
            cell.setCellValue(dtos.get(i).getShipmentCostBundleNumber());
            cell = row.createCell(16);
            cell.setCellValue(dtos.get(i).getProdNumber());
            cell = row.createCell(17);
            cell.setCellValue(dtos.get(i).getSellerProdCode());
            cell = row.createCell(18);
            cell.setCellValue(dtos.get(i).getProdName());
            cell = row.createCell(19);
            cell.setCellValue(dtos.get(i).getOptionInfo());
            cell = row.createCell(20);
            cell.setCellValue(dtos.get(i).getOptionNosUniqueCode());
            cell = row.createCell(21);
            cell.setCellValue(dtos.get(i).getReceiverContact1());
            cell = row.createCell(22);
            cell.setCellValue(dtos.get(i).getReceiverContact2());
            cell = row.createCell(23);
            cell.setCellValue(dtos.get(i).getZipCode());
            cell = row.createCell(24);
            cell.setCellValue(dtos.get(i).getDestination());
            cell = row.createCell(25);
            cell.setCellValue(dtos.get(i).getBuyerContact());
            cell = row.createCell(26);
            cell.setCellValue(dtos.get(i).getDeliveryMessage());
            cell = row.createCell(27);
            cell.setCellValue(dtos.get(i).getOrderDateTime() != null ? dateFormat.format(dtos.get(i).getOrderDateTime()) : null);
            cell = row.createCell(28);
            if (dtos.get(i).getReleased()) {
                cell.setCellValue("O");
            } else {
                cell.setCellValue("X");
            }
            cell = row.createCell(29);
            cell.setCellValue(dtos.get(i).getReleasedAt() != null ? dateFormat.format(dtos.get(i).getReleasedAt()) : null);

        }

        for (int i = 0; i < 30; i++) {
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
