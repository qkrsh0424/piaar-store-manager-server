package com.piaar_store_manager.server.domain.erp_order_item.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.service.ErpOrderItemBusinessService;
import com.piaar_store_manager.server.domain.excel_form.waybill.WaybillExcelFormDto;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.utils.CustomExcelUtils;
import com.piaar_store_manager.server.utils.StaticErpItemDataUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/erp-order-items")
@RequiredArgsConstructor
@RequiredLogin
public class ErpOrderItemApi {
    private final ErpOrderItemBusinessService erpOrderItemBusinessService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Upload excel data for order excel.
     * 주문 파일 업로드 엑셀 대량등록시 사용하는 API
     * <p>
     * <b>POST : API URL => /api/v1/erp-order-items/excel/upload</b>
     *
     * @param file : MultipartFile
     * @return ResponseEntity(message, HttpStatus)
     * @see CustomExcelUtils#isExcelFile
     * @see ErpOrderItemBusinessService#uploadErpOrderExcel
     */
    @PostMapping("/excel/upload")
    @PermissionRole
    public ResponseEntity<?> uploadErpOrderExcel(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        message.setData(erpOrderItemBusinessService.uploadErpOrderExcel(file));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/excel/upload/{headerId}")
    @PermissionRole
    public ResponseEntity<?> uploadErpOrderExcel(@PathVariable(value = "headerId") UUID headerId, @RequestParam("file") MultipartFile file) {
        Message message = new Message();

        // file extension check.
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("This is not an excel file.");
        }

        message.setData(erpOrderItemBusinessService.uploadErpOrderExcelByOtherForm(headerId, file));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Store excel data for order excel.
     * <p>
     * <b>POST : API URL => /api/v1/erp-order-items/batch</b>
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#createBatch
     */
    @PostMapping("/batch")
    @PermissionRole
    public ResponseEntity<?> createBatch(@RequestBody @Valid List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.createBatch(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search erp order item.
     * <p>
     * <b>GET : API URL => /api/v1/erp-order-items</b>
     *
     * @param params : Map::String, Object::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#searchList
     */
    // @GetMapping("")
    // public ResponseEntity<?> searchList(@RequestParam Map<String, Object> params) {
    //     Message message = new Message();

    //     message.setData(erpOrderItemBusinessService.searchList(params));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    // @GetMapping("")
    // public ResponseEntity<?> searchAll(@RequestParam Map<String, Object> params, @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 300) Pageable pageable) {
    //     Message message = new Message();

    //     message.setData(erpOrderItemBusinessService.searchAllByPaging(params, pageable));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    @GetMapping("")
    public ResponseEntity<?> searchAll(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.searchAll(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/target:order/action-refresh")
    public ResponseEntity<?> orderRefresh(@RequestBody Map<String, Object> params) {
        List<String> idsStr = (List<String>) params.get("ids");
        List<UUID> ids = idsStr.stream().map(r->UUID.fromString(r)).collect(Collectors.toList());

        Message message = new Message();
        message.setData(erpOrderItemBusinessService.searchBatchByIds(ids, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/target:sales/action-refresh")
    public ResponseEntity<?> salesRefresh(@RequestBody Map<String, Object> params) {
        List<String> idsStr = (List<String>) params.get("ids");
        List<UUID> ids = idsStr.stream().map(r->UUID.fromString(r)).collect(Collectors.toList());

        Message message = new Message();
        message.setData(erpOrderItemBusinessService.searchBatchByIds(ids, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/target:release/action-refresh")
    public ResponseEntity<?> releaseRefresh(@RequestBody Map<String, Object> params) {
        List<String> idsStr = (List<String>) params.get("ids");
        List<UUID> ids = idsStr.stream().map(r->UUID.fromString(r)).collect(Collectors.toList());

        Message message = new Message();
        message.setData(erpOrderItemBusinessService.searchBatchByReleasedItemIds(ids, params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search erp order item.
     * Mapping by option code.
     * <p>
     * <b>GET : API URL => /api/v1/erp-order-items/search</b>
     *
     * @param params   : Map::String, Object::
     * @param pageable : Pageable
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#searchBatchByPaging
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchBatchByPaging(@RequestParam Map<String, Object> params, @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 300) Pageable pageable) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.searchBatchByPaging(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search erp order item.
     * Mapping by release option code.
     * <p>
     * <b>GET : API URL => /api/v1/erp-order-items/search/release</b>
     *
     * @param params   : Map::String, Object::
     * @param pageable : Pageable
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#searchBatchByPaging
     */
    @GetMapping("/search/release")
    public ResponseEntity<?> searchReleaseItemBatchByPaging(@RequestParam Map<String, Object> params, @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 300) Pageable pageable) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.searchReleaseItemBatchByPaging(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change salesYn of erp order item.
     * <p>
     * <b>PATCH : API URL => /api/v1/erp-order-items/batch/sales-yn</b>
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#changeBatchForSalesYn
     */
    @PatchMapping("/batch/sales-yn")
    @PermissionRole
    public ResponseEntity<?> changeBatchForSalesYn(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForSalesYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change releaseYn of erp order item.
     * <p>
     * <b>PATCH : API URL => /api/v1/erp-order-items/batch/release-yn</b>
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#changeBatchForReleaseYn
     */
    @PatchMapping("/batch/release-yn")
    @PermissionRole
    public ResponseEntity<?> changeBatchForReleaseYn(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForReleaseYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete erp order item.
     * <p>
     * <b>POST : API URL => /api/v1/erp-order-items/batch-delete</b>
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#deleteBatch
     */
    @PostMapping("/batch-delete")
    @PermissionRole
    public ResponseEntity<?> deleteBatch(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.deleteBatch(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update erp order item.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-order-items</b>
     *
     * @param itemDtos : ErpOrderItemDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody @Valid ErpOrderItemDto itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.updateOne(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change option code and release option code of erp order item.
     * <p>
     * <b>PATCH : API URL => /api/v1/erp-order-items/batch/option-code/all</b>
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#changeBatchForAllOptionCode
     */
    @PatchMapping("/batch/option-code/all")
    @PermissionRole
    public ResponseEntity<?> changeBatchForAllOptionCode(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForAllOptionCode(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change release option code of erp order item.
     * <p>
     * <b>PATCH : API URL => /api/v1/erp-order-items/batch/release-option-code</b>
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#changeBatchForReleaseOptionCode
     */
    @PatchMapping("/batch/release-option-code/all")
    @PermissionRole
    public ResponseEntity<?> changeBatchForReleaseOptionCode(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForReleaseOptionCode(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping(value = "/batch/waybill")
    @PermissionRole
    public ResponseEntity<?> changeBatchForWaybill(
            @RequestPart(value = "file") MultipartFile file, @RequestPart(value = "orderItems") List<ErpOrderItemDto> data
    ) {
        Message message = new Message();

        List<WaybillExcelFormDto> waybillExcelFormDtos = erpOrderItemBusinessService.readWaybillExcelFile(file);
        int updatedCount = erpOrderItemBusinessService.changeBatchForWaybill(data, waybillExcelFormDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setMemo("선택된 데이터 : " + data.size() + " 건\n" + "운송장 입력된 데이터 총 : " + updatedCount + " 건");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/download-release-items/action-download")
    @PermissionRole
    public void downloadForDownloadReleaseItems(HttpServletResponse response, @RequestBody List<ErpOrderItemDto> itemDtos) {

//         엑셀 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        List<String> releaseItemHeader = StaticErpItemDataUtils.getReleaseItemListHeader();
        
        // 출고 리스트 다운로드 헤더
        for(int i = 0; i < releaseItemHeader.size(); i++) {
            String cellName = releaseItemHeader.get(i);
            cell = row.createCell(i);
            cell.setCellValue(cellName);
        }

        // 출 리스트 다운로드 데이터
        for(int i = 0; i < itemDtos.size(); i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(itemDtos.get(i).getProdDefaultName() != "" ? itemDtos.get(i).getProdDefaultName() : "*지정필요");
            cell = row.createCell(1);
            cell.setCellValue(itemDtos.get(i).getOptionDefaultName() != "" ? itemDtos.get(i).getOptionDefaultName() : "*지정필요");
            cell = row.createCell(2);
            cell.setCellValue(itemDtos.get(i).getUnit());
        }

        for (int i = 0; i < itemDtos.size(); i++) {
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
}
