package com.piaar_store_manager.server.exception;

/**
 * invalid user exception
 * http status 401
 */
public class InvalidUserException extends RuntimeException{
    public InvalidUserException(String message) {
        super(message);
    }
    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
