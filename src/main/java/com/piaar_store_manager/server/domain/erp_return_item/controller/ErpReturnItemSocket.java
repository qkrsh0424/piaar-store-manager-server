package com.piaar_store_manager.server.domain.erp_return_item.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_return_item.dto.ErpReturnItemDto;
import com.piaar_store_manager.server.domain.erp_return_item.service.ErpReturnItemBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ws/v1/erp-return-items")
@RequiredLogin
@RequiredArgsConstructor
public class ErpReturnItemSocket {
    private final ErpReturnItemBusinessService erpReturnItemBusinessService;
    private final SimpMessagingTemplate messagingTemplate;

    @PutMapping("")
    @PermissionRole
    public void updateOne(@RequestBody ErpReturnItemDto itemDto) {
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

    @PostMapping("/batch-delete")
    @PermissionRole
    public void deleteBatch(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.deleteBatch(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }

    @PatchMapping("/batch/return-reason")
    @PermissionRole
    public void changeBatchForReturnReason(@RequestBody List<ErpReturnItemDto> itemDtos) {
        Message message = new Message();

        erpReturnItemBusinessService.changeBatchForReturnReason(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-item", message);
    }
}
