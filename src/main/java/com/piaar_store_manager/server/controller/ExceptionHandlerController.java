package com.piaar_store_manager.server.controller;

import java.text.ParseException;

import com.piaar_store_manager.server.exception.DeliveryReadyFileUploadException;
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.message.Message;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler({ FileUploadException.class })
    public ResponseEntity<?> FileUploadExceptionHandler(FileUploadException e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setMessage("file_upload_error");
        message.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        message.setMemo(e.getMessage());

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({ NullPointerException.class })
    public ResponseEntity<?> NullPointerExceptionHandler(NullPointerException e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("null_pointer_error");
        message.setMemo("존재하지 않는 데이터입니다.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({ DataAccessException.class })
    public ResponseEntity<?> DataAccessExceptionHandler(DataAccessException e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("db_error");
        message.setMemo("데이터베이스 오류. 관리자에게 문의하세요.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> ExceptionErrorHandler(Exception e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        message.setMessage("error");
        message.setMemo("undefined error.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({ DeliveryReadyFileUploadException.class })
    public ResponseEntity<?> DeliveryReadyExceptionHandler(DeliveryReadyFileUploadException e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("data_error");
        message.setMemo(e.getMessage());

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<?> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("file_io_error");
        message.setMemo("파일을 입출력 오류가 발생했습니다. 관리자에게 문의해주세요.");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler({ ParseException.class })
    public ResponseEntity<?> ParseExceptionHandler(ParseException e) {
        log.error("ERROR STACKTRACE => {}", e.getStackTrace());

        Message message = new Message();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("parse_error");
        message.setMemo("데이터를 변환할 수 없습니다. 관리자에게 문의해주세요.");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
