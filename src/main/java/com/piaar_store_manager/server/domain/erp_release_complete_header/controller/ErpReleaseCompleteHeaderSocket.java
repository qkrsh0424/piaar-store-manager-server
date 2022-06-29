package com.piaar_store_manager.server.domain.erp_release_complete_header.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_release_complete_header.dto.ErpReleaseCompleteHeaderDto;
import com.piaar_store_manager.server.domain.erp_release_complete_header.service.ErpReleaseCompleteHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ws/v1/erp-release-complete-headers")
@RequiredLogin
@RequiredArgsConstructor
public class ErpReleaseCompleteHeaderSocket {
    private final ErpReleaseCompleteHeaderBusinessService erpReleaseCompleteHeaderBusinessService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Create one api for erp release complete header.
     * <p>
     * <b>POST : API URL => /ws/v1/erp-release-complete-headers</b>
     *
     * @param headerDto : ErpReleaseCompleteHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpReleaseCompleteHeaderBusinessService#saveOne
     */
    @PostMapping("")
    @PermissionRole
    public void saveOne(@RequestBody ErpReleaseCompleteHeaderDto headerDto) {
        Message message = new Message();

        erpReleaseCompleteHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

//        return new ResponseEntity<>(message, message.getStatus());
        messagingTemplate.convertAndSend("/topic/erp.erp-release-complete-header", message);
    }

    /**
     * Create one api for product.
     * <p>
     * <b>PUT : API URL => /ws/v1/erp-release-complete-headers</b>
     *
     * @param headerDto : ErpReleaseCompleteHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpReleaseCompleteHeaderBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public void updateOne(@RequestBody ErpReleaseCompleteHeaderDto headerDto) {
        Message message = new Message();

        erpReleaseCompleteHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

//        return new ResponseEntity<>(message, message.getStatus());
        messagingTemplate.convertAndSend("/topic/erp.erp-release-complete-header", message);
    }
}
