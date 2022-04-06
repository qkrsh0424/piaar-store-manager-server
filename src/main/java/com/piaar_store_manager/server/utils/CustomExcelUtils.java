package com.piaar_store_manager.server.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

public class CustomExcelUtils {
    // Excel file extension.
    private static final List<String> EXTENSIONS_EXCEL = Arrays.asList("xlsx", "xls");

    /**
     * <b>Extension Check</b>
     * <p>
     * 
     * @param file : MultipartFile
     * @throws FileUploadException
     */
    public static boolean isExcelFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());

        return EXTENSIONS_EXCEL.contains(extension);
    }

    /**
     * <b>Create Workbook By MutilpartFile</b>
     * <p>
     * 
     * @param file : MultipartFile
     * @throws IllegalArgumentException
     */
    public static Workbook createWorkBook(MultipartFile file) {
        try{
            return WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}
