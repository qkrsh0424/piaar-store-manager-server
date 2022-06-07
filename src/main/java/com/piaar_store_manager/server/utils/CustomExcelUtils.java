package com.piaar_store_manager.server.utils;

import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CustomExcelUtils {
    private static final List<String> EXTENSIONS_EXCEL = Arrays.asList("xlsx", "xls");
    public static final int NUMERIC_TO_DOUBLE = 0;
    public static final int NUMERIC_TO_INT = 1;

    public static boolean isExcelFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase());

        if (EXTENSIONS_EXCEL.contains(extension)) {
            return true;
        }
        return false;
    }

    public static Workbook getWorkbook(MultipartFile file) {
        Workbook workbook = null;

        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new CustomExcelFileUploadException("올바른 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        }

        return workbook;
    }

    public static Sheet getSheet(Workbook workbook, Integer SHEET_INDEX) {
        Sheet worksheet = workbook.getSheetAt(SHEET_INDEX);
        return worksheet;
    }

    public static Integer getCellCount(Sheet worksheet, Integer headerIndex) {
        Row HEADER_ROW = worksheet.getRow(headerIndex);
        return HEADER_ROW.getPhysicalNumberOfCells();
    }

    public static boolean isCheckedHeaderCell(Row headerRow, List<String> headerNames) {
        Integer size = headerRow.getPhysicalNumberOfCells();

        for (int i = 0; i < size; i++) {
            Cell cell = headerRow.getCell(i);
            String headerName = cell != null ? cell.getStringCellValue() : null;
            // 지정된 양식이 아니라면
            if (!headerNames.get(i).equals(headerName)) {
                return false;
            }
        }

        return true;
    }

    /**
     * CellValue를 Object 타입으로 리턴한다.
     *
     * @param cell
     * @param numericType 타입이 numeric일 경우 int로 변환해서 내보낼지 double로 변환해서 내보낼지 결정한다.
     * @return Object : cellValue
     */
    public static Object getCellValueObject(Cell cell, int numericType) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case _NONE:
                return "";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDateTime dateTime = cell.getLocalDateTimeCellValue().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(dateTime);
                }
                if (numericType == NUMERIC_TO_DOUBLE) {
                    return cell.getNumericCellValue();
                }
                if (numericType == NUMERIC_TO_INT) {
                    int result = (int) cell.getNumericCellValue();
                    return result;
                }
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return cell.getErrorCellValue();
            default:
                return "";
        }
    }

    public static Object getCellValueObject(Cell cell) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case _NONE:
                return "";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDateTime dateTime = cell.getLocalDateTimeCellValue().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(dateTime);
                }

                int result = (int) cell.getNumericCellValue();
                return result;
            case STRING:
                return cell.getStringCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return cell.getErrorCellValue();
            default:
                return "";
        }
    }

    public static Object getCellValueObjectWithDefaultValue(Cell cell, Object defaultValue) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDateTime dateTime = cell.getLocalDateTimeCellValue().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(dateTime);
                }
                int result = (int) cell.getNumericCellValue();
                return result;
            case STRING:
                return cell.getStringCellValue().strip();   // 앞뒤 공백제거. (유니코드의 공백들을 모두 제거 ex) space, tab 등)
            case FORMULA:
                return cell.getCellFormula();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return cell.getErrorCellValue();
            case BLANK:
            case _NONE:
            default:
                return defaultValue;
        }
    }
}
