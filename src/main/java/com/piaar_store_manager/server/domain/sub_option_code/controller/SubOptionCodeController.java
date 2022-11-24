package com.piaar_store_manager.server.domain.sub_option_code.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.sub_option_code.dto.SubOptionCodeDto;
import com.piaar_store_manager.server.domain.sub_option_code.service.SubOptionCodeBusinessService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/sub-option-code")
@RequiredArgsConstructor
@RequiredLogin
public class SubOptionCodeController {
    private final SubOptionCodeBusinessService subOptionCodeBusinessService;

    /**
     * Search list api for sub option code.
     * <p>
     * <b>GET : API URL => /api/v1/sub-option-code/{optionId}</b>
     */
    @GetMapping("/{optionId}")
    public ResponseEntity<?> searchListByProductOptionId(@PathVariable(value = "optionId") UUID optionId) {
        Message message = new Message();

        message.setData(subOptionCodeBusinessService.searchListByProductOptionId(optionId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Delete one api for sub option code.
     * <p>
     * <b>DELETE : API URL => /api/v1/sub-option-code/{subOptionCodeId}</b>
     */
    @DeleteMapping("/{subOptionCodeId}")
    @PermissionRole
    public ResponseEntity<?> destroyOne(@PathVariable(value = "subOptionCodeId") UUID subOptionCodeId) {
        Message message = new Message();

        subOptionCodeBusinessService.destroyOne(subOptionCodeId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Create one api for sub option code.
     * <p>
     * <b>POST : API URL => /api/v1/sub-option-code</b>
     */
    @PostMapping("")
    @PermissionRole
    public ResponseEntity<?> createOne(@RequestBody @Valid SubOptionCodeDto subOptionCode) {
        Message message = new Message();

        subOptionCodeBusinessService.createOne(subOptionCode);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * Update one api for sub option code.
     * <p>
     * <b>PUT : API URL => /api/v1/sub-option-code</b>
     */
    @PutMapping("")
    @PermissionRole
    public ResponseEntity<?> updateOne(@RequestBody @Valid SubOptionCodeDto subOptionCode) {
        Message message = new Message();

        subOptionCodeBusinessService.updateOne(subOptionCode);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
