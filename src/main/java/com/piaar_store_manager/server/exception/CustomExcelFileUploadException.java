package com.piaar_store_manager.server.exception;

public class CustomExcelFileUploadException extends RuntimeException {
    public CustomExcelFileUploadException(String message) {
        super(message);
    }

    public CustomExcelFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
