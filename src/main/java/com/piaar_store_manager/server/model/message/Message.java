package com.piaar_store_manager.server.model.message;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class Message {
    private HttpStatus status;
    private String message;
    private String memo;
    private Object data;
    private Object pagenation;

    public Message() {
        this.status = HttpStatus.BAD_REQUEST;
        this.message = null;
        this.memo = null;
        this.data = null;
        this.pagenation = null;
    }

    public int getStatusCode(){
        return this.status.value();
    }
}
