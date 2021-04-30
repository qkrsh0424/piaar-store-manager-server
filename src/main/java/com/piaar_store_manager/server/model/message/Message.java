package com.piaar_store_manager.server.model.message;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Message {
    private HttpStatus status;
    private String message;
    private Object data;
}
