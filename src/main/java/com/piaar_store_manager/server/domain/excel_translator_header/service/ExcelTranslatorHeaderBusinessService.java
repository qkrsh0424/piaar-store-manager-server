package com.piaar_store_manager.server.domain.excel_translator_header.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.excel_translator_header.dto.ExcelTranslatorDownloadHeaderDetailDto;
import com.piaar_store_manager.server.domain.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.piaar_store_manager.server.domain.excel_translator_header.dto.ExcelTranslatorUploadHeaderDetailDto;
import com.piaar_store_manager.server.domain.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.piaar_store_manager.server.domain.excel_translator_item.dto.ExcelDataDetailDto;
import com.piaar_store_manager.server.domain.excel_translator_item.dto.UploadExcelDataGetDto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelTranslatorHeaderBusinessService {
    private final ExcelTranslatorHeaderService excelTranslatorHeaderService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 엑셀 변환 양식의 타이틀 및 데이터 시작행을 저장한다.
     * 
     * @see ExcelTranslatorHeaderService#saveAndModify
     */
    public void createTitle(ExcelTranslatorHeaderGetDto dto) {
        excelTranslatorHeaderService.saveAndModify(ExcelTranslatorHeaderEntity.toEntity(dto));
    }

    public List<ExcelTranslatorHeaderGetDto> searchList() {
        List<ExcelTranslatorHeaderEntity> entities = excelTranslatorHeaderService.searchList();
        List<ExcelTranslatorHeaderGetDto> dtos = entities.stream().map(r -> ExcelTranslatorHeaderGetDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    public void changeOne(ExcelTranslatorHeaderGetDto dto) {
        ExcelTranslatorHeaderEntity entity = excelTranslatorHeaderService.searchOne(dto.getId());
        entity.setId(dto.getId()).setUploadHeaderTitle(dto.getUploadHeaderTitle())
                .setDownloadHeaderTitle(dto.getDownloadHeaderTitle()).setRowStartNumber(dto.getRowStartNumber());

        excelTranslatorHeaderService.saveAndModify(entity);
    }

    public void deleteOne(UUID excelTranslatorId) {
        excelTranslatorHeaderService.destroyOne(excelTranslatorId);
    }

    /**
     * <b>Data Processing Related Method</b>
     * 업로드된 엑셀 파일을 읽어 List::UploadExcelDataGetDto::형태로 추출한다.
     * 
     * @param file : MultipartFile
     * @param dto : ExcelTranslatorHeaderGetDto
     * @return List::UploadExcelDataGetDto::
     * @see ExcelTranslatorHeaderBusinessService#getUploadedExcelForm
     */
    public List<UploadExcelDataGetDto> uploadExcelFile(MultipartFile file, ExcelTranslatorHeaderGetDto dto) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<UploadExcelDataGetDto> excelDto = this.getUploadedExcelForm(sheet, dto);
        return excelDto;
    }

    /**
     * <b>Data Processing Related Method</b>
     * 업로드된 엑셀 파일을 데이터 시작 행부터 읽어 List::UploadExcelDataGetDto::로 반환한다.
     * 
     * @param worksheet : Sheet
     * @param dto : ExcelTranslatorHeaderGetDto
     * @return List::UploadExcelDataGetDto::
     */
    private List<UploadExcelDataGetDto> getUploadedExcelForm(Sheet worksheet, ExcelTranslatorHeaderGetDto dto) {
        List<UploadExcelDataGetDto> dtos = new ArrayList<>();
        List<ExcelTranslatorUploadHeaderDetailDto.DetailDto> uploadDetailDtos = dto.getUploadHeaderDetail().getDetails();

        Row headerRow = worksheet.getRow(dto.getRowStartNumber()-1);
        // 저장된 양식이 존재하는데 지정양식과 다른 엑셀이 업로드된 경우
        if(uploadDetailDtos.size() != 0 && uploadDetailDtos.size() != headerRow.getLastCellNum()) {
            throw new IllegalArgumentException();
        }

        // 저장된 데이터 시작행부터 엑셀을 읽는다.
        for(int i = dto.getRowStartNumber()-1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            List<ExcelDataDetailDto.UploadedDetailDto> uploadedDetailDtos = new ArrayList<>();

            for(int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                Object cellObj = new Object();

                if(cell == null || cell.getCellType().equals(CellType.BLANK)) {
                    cellObj = "";
                } else if (cell.getCellType().equals(CellType.STRING)) {
                    cellObj = cell.getStringCellValue();
                } else if (cell.getCellType().equals(CellType.NUMERIC)) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellObj = cell.getDateCellValue();
                    } else {
                        cellObj = cell.getNumericCellValue();
                    }
                }
                ExcelDataDetailDto.UploadedDetailDto detailDto = ExcelDataDetailDto.UploadedDetailDto.builder()
                        .id(UUID.randomUUID()).colData(cellObj).cellType(cellObj.getClass().getSimpleName()).build();
                uploadedDetailDtos.add(detailDto);
            }
            ExcelDataDetailDto uploadedData = ExcelDataDetailDto.builder().details(uploadedDetailDtos).build();
            UploadExcelDataGetDto dataDto = UploadExcelDataGetDto.builder().id(UUID.randomUUID()).uploadedData(uploadedData).build();
            dtos.add(dataDto);
        }
        return dtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * 업로드된 엑셀 파일을 첫번째 행(헤더 데이터)만 읽어 UploadExcelDataGetDto로 반환한다. (다운로드 헤더 양식 업로드 시 사용)
     * 
     * @param file : MultipartFile
     * @return UploadExcelDataGetDto
     */
    public UploadExcelDataGetDto uploadDownloadHeaderExcelFile(MultipartFile file) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);

        List<ExcelDataDetailDto.UploadedDetailDto> detailDtos = new ArrayList<>();
        for(int j = 0; j < headerRow.getLastCellNum(); j++) {
            Cell cell = headerRow.getCell(j);
            Object cellObj = new Object();

            if(cell == null || cell.getCellType().equals(CellType.BLANK)) {
                cellObj = "";
            } else {
                cellObj = cell.getStringCellValue();
            }
            ExcelDataDetailDto.UploadedDetailDto detailDto = ExcelDataDetailDto.UploadedDetailDto.builder()
                    .id(UUID.randomUUID()).colData(cellObj).cellType(cellObj.getClass().getSimpleName()).build();
            detailDtos.add(detailDto);
        }
        ExcelDataDetailDto uploadedData = ExcelDataDetailDto.builder().details(detailDtos).build();
        UploadExcelDataGetDto dataDto = UploadExcelDataGetDto.builder().id(UUID.randomUUID()).uploadedData(uploadedData).build();
        return dataDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * 엑셀 변환기 헤더 데이터의 업로드 헤더 양식을 업데이트한다.
     * 
     * @param dto : ExcelTranslatorHeaderGetDto
     * @see ExcelTranslatorHeaderService#searchOne
     * @see ExcelTranslatorHeaderService#saveAndModify
     */
    public void updateUploadHeaderDetailOfExcelTranslator(ExcelTranslatorHeaderGetDto dto) {
        ExcelTranslatorHeaderEntity entity = excelTranslatorHeaderService.searchOne(dto.getId());
        entity.setUploadHeaderDetail(dto.getUploadHeaderDetail());

        // 업로드 헤더가 변경된다면 다운로드 헤더를 초기화
        ExcelTranslatorDownloadHeaderDetailDto downloadDetail = new ExcelTranslatorDownloadHeaderDetailDto();
        List<ExcelTranslatorDownloadHeaderDetailDto.DetailDto> details = new ArrayList<>();
        downloadDetail.setDetails(details);
        entity.setDownloadHeaderDetail(downloadDetail);

        excelTranslatorHeaderService.saveAndModify(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * 엑셀 변환기 헤더 데이터의 다운로드 헤더 양식을 업데이트한다.
     * 
     * @param dto : ExcelTranslatorHeaderGetDto
     * @see ExcelTranslatorHeaderService#searchOne
     * @see ExcelTranslatorHeaderService#saveAndModify
     */
    public void updateDownloadHeaderDetailOfExcelTranslator(ExcelTranslatorHeaderGetDto dto) {
        ExcelTranslatorHeaderEntity entity = excelTranslatorHeaderService.searchOne(dto.getId());
        entity.setDownloadHeaderDetail(dto.getDownloadHeaderDetail());
        excelTranslatorHeaderService.saveAndModify(entity);
    }
}

