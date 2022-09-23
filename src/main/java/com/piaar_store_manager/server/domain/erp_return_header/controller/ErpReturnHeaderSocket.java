package com.piaar_store_manager.server.domain.erp_return_header.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_return_header.dto.ErpReturnHeaderDto;
import com.piaar_store_manager.server.domain.erp_return_header.service.ErpReturnHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ws/v1/erp-return-headers")
@RequiredArgsConstructor
@RequiredLogin
public class ErpReturnHeaderSocket {
    private final ErpReturnHeaderBusinessService erpReturnHeaderBusinessService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("")
    @PermissionRole
    public void createOne(@RequestBody ErpReturnHeaderDto headerDto) {
        Message message = new Message();

        erpReturnHeaderBusinessService.createOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-header",message);
    }

    @PutMapping("")
    @PermissionRole
    public void updateOne(@RequestBody ErpReturnHeaderDto headerDto) {
        Message message = new Message();

        erpReturnHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-header",message);
    }

    @DeleteMapping("/{headerId}")
    @PermissionRole
    public void deleteOne(@PathVariable UUID headerId) {
        Message message = new Message();

        erpReturnHeaderBusinessService.deleteOne(headerId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-return-header",message);

    }
}
