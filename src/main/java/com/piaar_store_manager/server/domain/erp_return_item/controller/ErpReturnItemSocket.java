package com.piaar_store_manager.server.domain.erp_return_item.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_return_item.dto.ErpReturnItemDto;
import com.piaar_store_manager.server.domain.erp_return_item.service.ErpReturnItemBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/ws/v1/erp-return-items")
@RequiredLogin
@RequiredArgsConstructor
public class ErpReturnItemSocket {
    private final ErpReturnItemBusinessService erpReturnItemBusinessService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("")
    @PermissionRole
    public void createOne(@RequestBody @Valid ErpReturnItemDto itemDto) {
        Message message = new Message();

        erpReturnItemBusinessService.createOne(itemDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setSocketMemo("[반품 접수] 에 추가된 데이터가 있습니다.");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PutMapping("")
    @PermissionRole
    public void updateOne(@RequestBody @Valid ErpReturnItemDto itemDto) {
        Message message = new Message();

        erpReturnItemBusinessService.updateOne(itemDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PatchMapping("/batch/collect-yn")
    @PermissionRole
    public void changeBatchForCollectYn(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.changeBatchForCollectYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PatchMapping("/batch/collect-complete-yn")
    @PermissionRole
    public void changeBatchForCollectCompleteYn(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.changeBatchForCollectCompleteYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }
    
    @PatchMapping("/batch/return-complete-yn")
    @PermissionRole
    public void changeBatchForReturnCompleteYn(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.changeBatchForReturnCompleteYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PatchMapping("/batch/return-reject-yn")
    @PermissionRole
    public void changeBatchForReturnRejectYn(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.changeBatchForReturnRejectYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PostMapping("/batch-delete")
    @PermissionRole
    public void deleteBatch(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.deleteBatch(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        // erp return item 제거 및 erp order item의 returnYn 수정
        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

    @PatchMapping("/return-reason")
    @PermissionRole
    public void changeForReturnReason(@RequestBody @Valid ErpReturnItemDto itemDto) {
        Message message = new Message();

        erpReturnItemBusinessService.changeForReturnReason(itemDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PatchMapping("/defective/action-reflect")
    @PermissionRole
    public ResponseEntity<?> actionReflectDefective(@RequestBody ErpReturnItemDto itemDto, @RequestParam Map<String, Object> params){
        Message message = new Message();

        erpReturnItemBusinessService.actionReflectDefective(itemDto, params);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/defective/action-cancel")
    @PermissionRole
    public ResponseEntity<?> actionCancelDefective(@RequestBody ErpReturnItemDto itemDto, @RequestParam Map<String, Object> params){
        Message message = new Message();

        erpReturnItemBusinessService.actionCancelDefective(itemDto, params);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/stock/action-reflect")
    @PermissionRole
    public ResponseEntity<?> actionReflectStock(@RequestBody ErpReturnItemDto itemDto, @RequestParam Map<String, Object> params){
        Message message = new Message();

        erpReturnItemBusinessService.actionReflectStock(itemDto, params);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/stock/action-cancel")
    @PermissionRole
    public ResponseEntity<?> actionCancelStock(@RequestBody ErpReturnItemDto itemDto){
        Message message = new Message();

        erpReturnItemBusinessService.actionCancelStock(itemDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
        return new ResponseEntity<>(message, message.getStatus());
    }
}
