package com.piaar_store_manager.server.exception;

public class CustomFileDownloadException extends RuntimeException {
    public CustomFileDownloadException(String message) {
        super(message);
    }
    
    public CustomFileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}

