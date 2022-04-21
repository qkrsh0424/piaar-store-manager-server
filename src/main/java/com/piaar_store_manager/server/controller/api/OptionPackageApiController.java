package com.piaar_store_manager.server.controller.api;

import java.util.UUID;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.service.option_package.OptionPackageBusinessService;
import com.piaar_store_manager.server.service.user.UserService;

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
     * Search one api for optionPackage.
     * <p>
     * <b>GET : API URL => /api/v1/option-package/parent-option/{parentOptionId}</b>
     *
     * @param parentOptionId : UUID
     * @return ResponseEntity(message, HttpStatus)
     * @see Message
     * @see HttpStatus
     */
    @GetMapping("/parent-option/{parentOptionId}")
    public ResponseEntity<?> searchListByParentOptionId(@PathVariable(value = "parentOptionId") UUID parentOptionId) {
        Message message = new Message();

        try {
            message.setData(optionPackageBusinessService.searchListByParentOptionId(parentOptionId));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (NullPointerException e) {
            message.setStatus(HttpStatus.NOT_FOUND);
            message.setMessage("Not found optionId=" + parentOptionId + " value.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
