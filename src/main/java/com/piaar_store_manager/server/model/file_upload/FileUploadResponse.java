package com.piaar_store_manager.server.model.file_upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private String fileName;
    private String fileUploadUri;
    private String fileType;
    private long size;
}
