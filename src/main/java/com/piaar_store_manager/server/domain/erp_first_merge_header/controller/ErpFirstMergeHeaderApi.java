package com.piaar_store_manager.server.domain.erp_first_merge_header.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_first_merge_header.dto.ErpFirstMergeHeaderDto;
import com.piaar_store_manager.server.domain.erp_first_merge_header.service.ErpFirstMergeHeaderBusinessService;
import com.piaar_store_manager.server.model.message.Message;

@RestController
@RequestMapping("/api/v1/erp-first-merge-headers")
@RequiredArgsConstructor
public class ErpFirstMergeHeaderApi {
    private final ErpFirstMergeHeaderBusinessService erpFirstMergeHeaderBusinessService;

    /**
     * Create one api for erp first merge header.
     * <p>
     * <b>POST : API URL => /api/v1/erp-first-merge-headers</b>
     *
     * @param headerDto : ErpFirstMergeHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpFirstMergeHeaderBusinessService#saveOne
     */
    @PostMapping("")
    public ResponseEntity<?> saveOne(@RequestBody ErpFirstMergeHeaderDto headerDto) {
        Message message = new Message();

        erpFirstMergeHeaderBusinessService.saveOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Search list api for erp first merge header.
     * <p>
     * <b>GET : API URL => /api/v1/erp-first-merge-headers</b>
     *
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpFirstMergeHeaderBusinessService#searchAll
     */
    @GetMapping("")
    public ResponseEntity<?> searchAll() {
        Message message = new Message();

        message.setData(erpFirstMergeHeaderBusinessService.searchAll());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp first merge header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-first-merge-headers</b>
     *
     * @param headerDto : ErpFirstMergeHeaderDto
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpFirstMergeHeaderBusinessService#updateOne
     */
    @PutMapping("")
    public ResponseEntity<?> updateOne(@RequestBody ErpFirstMergeHeaderDto headerDto) {
        Message message = new Message();

        erpFirstMergeHeaderBusinessService.updateOne(headerDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for erp first merge header.
     * <p>
     * <b>PUT : API URL => /api/v1/erp-first-merge-headers/{id}</b>
     *
     * @param id : UUID
     * @return ResponseEntity(message, HttpStatus)
     * @see ErpFirstMergeHeaderBusinessService#deleteOne
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable(value = "id") UUID id) {
        Message message = new Message();

        erpFirstMergeHeaderBusinessService.deleteOne(id);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
