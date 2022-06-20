package com.piaar_store_manager.server.utils;

import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
     * 타입이 numeric일 경우 해당 타입(int, double, long)에 맞도록 변환해서 반환.
     * @param cell
     * @return Object : cellValue
     */
    public static Object getCellValueObject(Cell cell) {
        if(cell == null || isBlankCell(cell)) {
            return "";
        }

        switch (cell.getCellType()) {
            case _NONE:
                return "";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDateTime dateTime = cell.getLocalDateTimeCellValue().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(dateTime);
                }

                Double cellValue = cell.getNumericCellValue();
                if(cellValue.equals(Math.floor(cellValue))
                        && !Double.isInfinite(cellValue)
                        && !Double.isNaN(cellValue)
                        && cellValue >= Integer.MIN_VALUE
                        && cellValue <= Integer.MAX_VALUE
                ) {
                    return (int) cell.getNumericCellValue();
                }else if(cellValue >= Integer.MAX_VALUE
                        && cellValue <= Long.MAX_VALUE) {
                    return (long)cell.getNumericCellValue();
                }else {
                    return cellValue;
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

    // public static Object getCellValueObject(Cell cell) {
    //     CellType cellType = cell.getCellType();

    //     switch (cellType) {
    //         case _NONE:
    //             return "";
    //         case NUMERIC:
    //             if (DateUtil.isCellDateFormatted(cell)) {
    //                 LocalDateTime dateTime = cell.getLocalDateTimeCellValue().atZone(ZoneId.systemDefault()).toLocalDateTime();
    //                 return CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(dateTime);
    //             }

    //             int result = (int) cell.getNumericCellValue();
    //             return result;
    //         case STRING:
    //             return cell.getStringCellValue();
    //         case FORMULA:
    //             return cell.getCellFormula();
    //         case BLANK:
    //             return "";
    //         case BOOLEAN:
    //             return cell.getBooleanCellValue();
    //         case ERROR:
    //             return cell.getErrorCellValue();
    //         default:
    //             return "";
    //     }
    // }

    public static Object getCellValueObjectWithDefaultValue(Cell cell, Object defaultValue) {
        if(cell == null || isBlankCell(cell)) {
            return defaultValue;
        }
        
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
                    return cell.getStringCellValue();
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

    public static Integer convertObjectValueToIntegerValue(Object objectValue) {
        try {
            return NumberFormat.getInstance(Locale.getDefault()).parse(objectValue.toString()).intValue();
        } catch (ParseException e) {
            throw new CustomInvalidDataException("수량, 판매금액, 배송비에 올바르지 않은 데이터가 존재합니다. 수정 후 재업로드 해주세요.");
        }
    }

    public static Cell setCellValueFromTypeAndCellData(Cell cell, String cellType, Object cellData) {
        if(isBlankCell(cell)) {
            cell.setCellValue("");
            return cell;
        }

        switch(cellType) {
            case "String":
                cell.setCellValue(cellData.toString());
                break;
            case "Date":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                LocalDateTime date = LocalDateTime.parse(cellData.toString(), formatter);
                cell.setCellValue(CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(date));
                break;
            case "Double":
                cell.setCellValue((double) cellData);
                break;
            case "Long":
                cell.setCellValue((long) cellData);
                break;
            case "Integer":
                cell.setCellValue((int) cellData);
                break;
            default:
                throw new CustomInvalidDataException("올바른 데이터 타입이 아닙니다. 수정 후 재업로드 해주세요.");
        }
        return cell;
    }

    public static boolean isBlankCell(Cell cell) {
        if(cell.getCellType().equals(CellType.BLANK)
            || (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue().isBlank())){
                return true;
        }else {
            return false;
        }
    }
}
