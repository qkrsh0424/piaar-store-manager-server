package com.piaar_store_manager.server.controller;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.message.Message;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler({FileUploadException.class})
    public ResponseEntity<?> FileUploadExceptionHandler(FileUploadException e){
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setMessage(e.getMessage());
        message.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        message.setMemo("파일 형식이 올바르지 않습니다.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> NullPointerExceptionHandler(NullPointerException e){
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("data_error");
        message.setMemo("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> IllegalStateExceptionHandler(IllegalStateException e){
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("excel_type_error");
        message.setMemo("배송 준비 엑셀 파일이 아닙니다. 배송 준비 엑셀 파일을 업로드해주세요.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<?> DataAccessExceptionHandler(DataAccessException e){
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("db_error");
        message.setMemo("데이터베이스 오류. 관리자에게 문의하세요.");

        return new ResponseEntity<>(message, message.getStatus());
    }
    
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> ExceptionErrorHandler(Exception e){
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        message.setMessage("error");
        message.setMemo("undefined error.");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
