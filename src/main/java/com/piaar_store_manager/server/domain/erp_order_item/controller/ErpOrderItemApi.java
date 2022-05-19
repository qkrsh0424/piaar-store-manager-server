package com.piaar_store_manager.server.domain.erp_order_item.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.service.ErpOrderItemBusinessService;
import com.piaar_store_manager.server.domain.excel_form.waybill.WaybillExcelFormDto;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

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
    @GetMapping("")
    public ResponseEntity<?> searchList(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.searchList(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/action-refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, Object> params) {
        List<String> idsStr = (List<String>) params.get("ids");
        List<UUID> ids = idsStr.stream().map(r->UUID.fromString(r)).collect(Collectors.toList());

        Message message = new Message();
        message.setData(erpOrderItemBusinessService.searchBatchByIds(ids, params));
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
    @PatchMapping("/batch/release-option-code")
    public ResponseEntity<?> changeBatchForReleaseOptionCode(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForReleaseOptionCode(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change erp order item to combined delivery item by first merge header
     * <p>
     * <b>POST : API URL => /api/v1/erp-order-items/erp-first-merge-headers/{firstMergeHeaderId}/action-merge</b>
     *
     * @param firstMergeHeaderId : UUID
     * @param itemDtos           : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#getFirstMergeItem
     */
    @PostMapping("/erp-first-merge-headers/{firstMergeHeaderId}/action-merge")
    public ResponseEntity<?> getFirstMergeItem(@PathVariable(value = "firstMergeHeaderId") UUID firstMergeHeaderId, @RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.getFirstMergeItem(firstMergeHeaderId, itemDtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Change erp order item to combined delivery item by second merge header
     * <p>
     * <b>POST : API URL => /api/v1/erp-order-items/erp-second-merge-headers/{secondMergeHeaderId}/action-merge</b>
     *
     * @param secondMergeHeaderId : UUID
     * @param itemDtos            : List::ErpOrderItemDto::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#getSecondMergeItem
     */
    @PostMapping("/erp-second-merge-headers/{secondMergeHeaderId}/action-merge")
    public ResponseEntity<?> getSecondMergeItem(@PathVariable(value = "secondMergeHeaderId") UUID secondMergeHeaderId, @RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.getSecondMergeItem(secondMergeHeaderId, itemDtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping(value = "/batch/waybill")
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
}
