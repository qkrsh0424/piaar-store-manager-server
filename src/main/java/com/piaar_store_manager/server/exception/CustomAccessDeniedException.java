package com.piaar_store_manager.server.exception;
/**
 * user access denied exception
 * http status 403
 */
public class CustomAccessDeniedException extends RuntimeException{
    public CustomAccessDeniedException(String message) {
        super(message);
    }
    public CustomAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
