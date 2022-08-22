package com.piaar_store_manager.server.domain.erp_order_item.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_order_item.service.ErpOrderItemBusinessServiceV2;
import com.piaar_store_manager.server.domain.message.Message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v2/erp-order-items")
@RequiredArgsConstructor
@RequiredLogin
public class ErpOrderItemApiV2 {
    private final ErpOrderItemBusinessServiceV2 erpOrderItemBusinessService;

    /**
     * Search erp order item.
     * <p>
     * <b>GET : API URL => /api/v2/erp-order-items/batch</b>
     *
     * @param params : Map::String, Object::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#searchList
     */
    @GetMapping("/batch")
    public ResponseEntity<?> searchBatch(@RequestParam Map<String, Object> params) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.searchBatch(params));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search erp order item.
     * <p>
     * <b>POST : API URL => /api/v2/erp-order-items/action-refresh</b>
     * 
     * @param params : Map::String, Object::
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#searchBatchByIds
     */
    @PostMapping("/action-refresh")
    public ResponseEntity<?> orderRefresh(@RequestBody Map<String, Object> params) {
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
     * <p>
     * <b>GET : API URL => /api/v1/erp-order-items/batch/page</b>
     *
     * @param params   : Map::String, Object::
     * @param pageable : Pageable
     * @see ErpOrderItemBusinessService#searchBatchByPaging
     */
    @GetMapping("/batch/page")
    public ResponseEntity<?> searchBatchByPaging(@RequestParam Map<String, Object> params, @PageableDefault(sort = "cid", direction = Sort.Direction.DESC, size = 50) Pageable pageable) {
        Message message = new Message();

        message.setData(erpOrderItemBusinessService.searchBatchByPaging(params, pageable));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search erp order item.
     * <p>
     * <b>GET : API URL => /api/v2/erp-order-items/batch/search</b>
     * 
     * @param params   : Map::String, Object::
     * @param pageable : Pageable
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpOrderItemBusinessService#searchBatchByPaging
     */
    // deprecated
    // @GetMapping("/batch/search")
    // public ResponseEntity<?> searchBatch(@RequestParam Map<String, Object> params) {
    //     Message message = new Message();

    //     message.setData(erpOrderItemBusinessService.searchList(params));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }
}
