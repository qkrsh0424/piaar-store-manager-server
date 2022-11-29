package com.piaar_store_manager.server.exception;

public class CustomNotAllowedActionException extends RuntimeException {

    public CustomNotAllowedActionException(String message) {
        super(message);
    }

    public CustomNotAllowedActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
