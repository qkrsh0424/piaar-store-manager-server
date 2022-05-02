package com.piaar_store_manager.server.controller;

import java.text.ParseException;

import javax.validation.ConstraintViolationException;

import com.piaar_store_manager.server.domain.message.Message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

   /**
    * 데이터 베이스 Valid 유효성 검사 예외가 발생했을 때
    * http status 409
    */
   @ExceptionHandler({ ConstraintViolationException.class })
   public ResponseEntity<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
       log.error("ERROR STACKTRACE => {}", e.getStackTrace());

       Message message = new Message();
       message.setStatus(HttpStatus.CONFLICT);
       message.setMessage("data_error");
       message.setMemo("입력된 데이터를 등록할 수 없습니다.\n 수정 후 재등록해주세요.");

       return new ResponseEntity<>(message, message.getStatus());
   }

   @ExceptionHandler({ MethodArgumentNotValidException.class })
   public ResponseEntity<?> argumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
       log.error("ERROR STACKTRACE => {}", e.getStackTrace());

       Message message = new Message();
       message.setStatus(HttpStatus.CONFLICT);
       message.setMessage("data_error");
       message.setMemo("허용할 수 없는 데이터가 존재합니다.");

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

    /**
    * 데이터 베이스 관련 예외가 발생했을 때
    * http status 400
    */
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
