package com.piaar_store_manager.server.exception;

/**
 * http status 400
 */
public class NotMatchedParamsException extends RuntimeException {
    public NotMatchedParamsException() {
        super();
    }

    public NotMatchedParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchedParamsException(String message) {
        super(message);
    }

    public NotMatchedParamsException(Throwable cause) {
        super(cause);
    }
}
