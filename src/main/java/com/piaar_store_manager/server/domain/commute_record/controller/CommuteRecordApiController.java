package com.piaar_store_manager.server.domain.commute_record.controller;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.commute_record.service.CommuteRecordBusinessService;
import com.piaar_store_manager.server.domain.message.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/commute-record")
@RequiredArgsConstructor
@RequiredLogin
public class CommuteRecordApiController {
    private final CommuteRecordBusinessService commuteRecordBusinessService;

    @GetMapping("/today/strict")
    public ResponseEntity<?> searchTodayRecordStrict() {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(commuteRecordBusinessService.searchTodayRecordStrict());
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/work/start")
    public ResponseEntity<?> setWorkStart(@RequestBody Map<String, Object> params){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        commuteRecordBusinessService.setWorkStart(params);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/work/end")
    public ResponseEntity<?> setWorkEnd(@RequestBody Map<String, Object> params){
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        commuteRecordBusinessService.setWorkEnd(params);
        return new ResponseEntity<>(message, message.getStatus());
    }
}
