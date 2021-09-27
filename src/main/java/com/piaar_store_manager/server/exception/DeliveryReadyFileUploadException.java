package com.piaar_store_manager.server.exception;

public class DeliveryReadyFileUploadException extends RuntimeException {
    public DeliveryReadyFileUploadException(String message) {
        super(message);
    }

    public DeliveryReadyFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
