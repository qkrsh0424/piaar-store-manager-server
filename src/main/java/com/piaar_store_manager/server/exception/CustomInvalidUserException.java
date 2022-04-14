package com.piaar_store_manager.server.exception;

/**
 * invalid user exception
 * http status 401
 */
public class CustomInvalidUserException extends RuntimeException{
    public CustomInvalidUserException(String message) {
        super(message);
    }
    public CustomInvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
