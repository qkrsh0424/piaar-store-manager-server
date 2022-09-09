package com.piaar_store_manager.server.exception;

public class CustomExcelFileRequiredPwdException extends RuntimeException {

    public CustomExcelFileRequiredPwdException() {
    }

    public CustomExcelFileRequiredPwdException(String message) {
        super(message);
    }

    public CustomExcelFileRequiredPwdException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
