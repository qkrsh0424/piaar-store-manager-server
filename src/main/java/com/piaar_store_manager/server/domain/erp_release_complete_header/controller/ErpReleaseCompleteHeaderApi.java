package com.piaar_store_manager.server.domain.erp_release_complete_header.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_release_complete_header.dto.ErpReleaseCompleteHeaderDto;
import com.piaar_store_manager.server.domain.erp_release_complete_header.service.ErpReleaseCompleteHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/erp-release-complete-headers")
@RequiredLogin
@RequiredArgsConstructor
public class ErpReleaseCompleteHeaderApi {
    private final ErpReleaseCompleteHeaderBusinessService erpReleaseCompleteHeaderBusinessService;

    /**
     * Create one api for erp release complete header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-release-complete-headers</b>
     * 
     * @param headerDto : ErpReleaseCompleteHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpReleaseCompleteHeaderBusinessService#saveOne
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> saveOne(@RequestBody ErpReleaseCompleteHeaderDto headerDto) {
        Message message = new Message();

        erpReleaseCompleteHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search one api for erp release complete header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-release-complete-headers/all</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpReleaseCompleteHeaderBusinessService#searchAll
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(erpReleaseCompleteHeaderBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for release complete header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-release-complete-headers</b>
     * 
     * @param headerDto : ErpReleaseCompleteHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpReleaseCompleteHeaderBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody ErpReleaseCompleteHeaderDto headerDto) {
        Message message = new Message();

        erpReleaseCompleteHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
    * Delete one api for erp release complete header.
    * <p>
    * <b>DELETE : API URL => /api/v1/erp-sales-headers</b>
    * 
    * @param headerId : UUID
    * @see ErpSalesHeaderBusinessService#deleteOne
    */
   @DeleteMapping("/{headerId}")
   @PermissionRole
   public ResponseEntity<?> deleteOne(@PathVariable UUID headerId) {
       Message message = new Message();

       erpReleaseCompleteHeaderBusinessService.deleteOne(headerId);
       message.setStatus(HttpStatus.OK);
       message.setMessage("success");

       return new ResponseEntity<>(message, message.getStatus());
   }
}
