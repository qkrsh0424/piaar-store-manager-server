package com.piaar_store_manager.server.domain.option_package.controller;

import java.util.UUID;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageBusinessService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/option-package")
@RequiredArgsConstructor
@RequiredLogin
public class OptionPackageApiController {
    private final OptionPackageBusinessService optionPackageBusinessService;

    /**
     * Search one api for option package.
     * <p>
     * <b>GET : API URL => /api/v1/option-package/parent-option/{parentOptionId}</b>
     *
     * @param parentOptionId : UUID
     * @see OptionPackageBusinessService#searchListByParentOptionId
     */
    @GetMapping("/parent-option/{parentOptionId}")
    public ResponseEntity<?> searchListByParentOptionId(@PathVariable(value = "parentOptionId") UUID parentOptionId) {
        Message message = new Message();

        message.setData(optionPackageBusinessService.searchListByParentOptionId(parentOptionId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
