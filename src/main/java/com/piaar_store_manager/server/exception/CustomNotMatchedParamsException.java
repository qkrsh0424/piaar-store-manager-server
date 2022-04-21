package com.piaar_store_manager.server.exception;

/**
 * http status 400
 */
public class CustomNotMatchedParamsException extends RuntimeException {
    public CustomNotMatchedParamsException() {
        super();
    }

    public CustomNotMatchedParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomNotMatchedParamsException(String message) {
        super(message);
    }

    public CustomNotMatchedParamsException(Throwable cause) {
        super(cause);
    }
}
