package com.piaar_store_manager.server.domain.erp_second_merge_header.controller;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_second_merge_header.dto.ErpSecondMergeHeaderDto;
import com.piaar_store_manager.server.domain.erp_second_merge_header.service.ErpSecondMergeHeaderBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

@RestController
@RequestMapping("/api/v1/erp-second-merge-headers")
@RequiredArgsConstructor
@RequiredLogin
public class ErpSecondMergeHeaderApi {
    private ErpSecondMergeHeaderBusinessService erpSecondMergeHeaderBusinessService;

    /**
     * Create one api for erp second merge header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-second-merge-headers</b>
     *
     * @param headerDto : ErpSecondMergeHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSecondMergeHeaderBusinessService#saveOne
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> saveOne(@RequestBody ErpSecondMergeHeaderDto headerDto) {
        Message message = new Message();

        erpSecondMergeHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for erp second merge header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-second-merge-headers</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSecondMergeHeaderBusinessService#searchAll
     */
    @GetMapping("")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(erpSecondMergeHeaderBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp second merge header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-second-merge-headers</b>
     *
     * @param headerDto : ErpSecondMergeHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSecondMergeHeaderBusinessService#updateOne
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody ErpSecondMergeHeaderDto headerDto) {
        Message message = new Message();

        erpSecondMergeHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp second merge header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-second-merge-headers/{id}</b>
     *
     * @param id : UUID
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpSecondMergeHeaderBusinessService#deleteOne
     */
    @DeleteMapping("/{id}")
    @PermissionRole
    public ResponseEntity<?> deleteOne(@PathVariable(value = "id") UUID id) {
        Message message = new Message();

        erpSecondMergeHeaderBusinessService.deleteOne(id);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
