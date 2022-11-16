package com.piaar_store_manager.server.domain.option_package.controller;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageBusinessService;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageBusinessServiceV2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/option-packages")
@RequiredArgsConstructor
@RequiredLogin
public class OptionPackageApiControllerV2 {
    private final OptionPackageBusinessServiceV2 optionPackageBusinessService;

    /**
     * Search one api for option package.
     * <p>
     * <b>GET : API URL => /api/v1/option-package/parent-option/{parentOptionId}</b>
     *
     * @param parentOptionId : UUID
     * @see OptionPackageBusinessService#searchBatchByParentOptionId
     */
    @GetMapping("/parent-option/{parentOptionId}")
    public ResponseEntity<?> searchBatchByParentOptionId(@PathVariable(value = "parentOptionId") UUID parentOptionId) {
        Message message = new Message();

        message.setData(optionPackageBusinessService.searchBatchByParentOptionId(parentOptionId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/{parentOptionId}")
    public ResponseEntity<?> deleteAndCreateBatch(@PathVariable("parentOptionId") UUID parentOptionId, @RequestBody List<OptionPackageDto> dtos) {
        Message message = new Message();

        optionPackageBusinessService.deleteAndCreateBatch(parentOptionId, dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
