package com.piaar_store_manager.server.domain.erp_download_excel_header.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.erp_download_excel_header.dto.DetailDto;
import com.piaar_store_manager.server.domain.erp_download_excel_header.dto.ErpDownloadExcelHeaderDto;
import com.piaar_store_manager.server.domain.erp_download_excel_header.entity.ErpDownloadExcelHeaderEntity;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpDownloadOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpDownloadItemVo;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomFieldUtils;

@Service
@RequiredArgsConstructor
public class ErpDownloadExcelHeaderBusinessService {
    private final ErpDownloadExcelHeaderService erpDownloadExcelHeaderService;
    private final UserService userService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * erp download excel header를 등록한다.
     *
     * @param headerDto : ErpDownloadExcelHeaderDto
     * @see ErpDownloadExcelHeaderEntity#toEntity
     * @see ErpDownloadExcelHeaderService#saveAndModify
     */
    public void saveOne(ErpDownloadExcelHeaderDto headerDto) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        UUID USER_ID = userService.getUserId();
        ErpDownloadExcelHeaderEntity headerEntity = ErpDownloadExcelHeaderEntity.toEntity(headerDto);
        headerEntity
            .setId(UUID.randomUUID())
            .setCreatedAt(CustomDateUtils.getCurrentDateTime())
            .setCreatedBy(USER_ID)
            .setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpDownloadExcelHeaderService.saveAndModify(headerEntity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 저장된 erp download excel header를 조회한다.
     *
     * @return List::ErpDownloadExcelHeaderDto::
     * @see ErpDownloadExcelHeaderService#searchAll
     * @see ErpDownloadExcelHeaderDto#toDto
     */
    public List<ErpDownloadExcelHeaderDto> searchAll() {
        // access check
        userService.userLoginCheck();

        List<ErpDownloadExcelHeaderEntity> entities = erpDownloadExcelHeaderService.searchAll();
        List<ErpDownloadExcelHeaderDto> dtos = entities.stream().map(r -> ErpDownloadExcelHeaderDto.toDto(r)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 저장된 erp donwload excel header를 변경한다.
     *
     * @param headerDto : ErpDownloadExcelHeaderDto
     * @see ErpDownloadExcelHeaderService#searchOne
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpDownloadExcelHeaderService#saveAndModify
     */
    public void updateOne(ErpDownloadExcelHeaderDto headerDto) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        ErpDownloadExcelHeaderEntity entity = erpDownloadExcelHeaderService.searchOne(headerDto.getId());

        entity.getHeaderDetail().setDetails(headerDto.getHeaderDetail().getDetails());
        entity.setTitle(headerDto.getTitle()).setUpdatedAt(CustomDateUtils.getCurrentDateTime());

        erpDownloadExcelHeaderService.saveAndModify(entity);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * id 값에 대응하는 엑셀 데이터를 삭제한다.
     * 
     * @param id : UUID
     * @ErpDownloadExcelHeaderService#delete
     */
    public void deleteOne(UUID id) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        erpDownloadExcelHeaderService.deleteOne(id);
    }

    /**
     * <b>Data Process Related Method</b>
     * <p>
     * 등록된 다운로드 헤더를 참고하여
     * 전달된 데이터를 병합, 합배송 여부에 따라 데이터를 가공해 엑셀다운로드한다.
     * 
     * @param id : UUID
     * @param headerDto : ErpDownloadExcelHeaderDto
     * @param erpDownloadOrderItemDtos : List::ErpDownloadOrderItemDto::
     * @return List::ErpDownloadItemVo::
     */
    public List<ErpDownloadItemVo> downloadByErpDownloadExcelHeader(ErpDownloadExcelHeaderDto headerDto, List<ErpDownloadOrderItemDto> erpDownloadOrderItemDtos) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();
        
        int HEADER_COLUMN_SIZE = headerDto.getHeaderDetail().getDetails().size();   //  다운로드 헤더에 등록된 항목 개수
        int ERP_DOWNLOAD_ITEM_SIZE = erpDownloadOrderItemDtos.size();   // 다운로드 받아야할 엑셀 데이터 개수
        
        int currentMergeItemIndex = 0;
        List<ErpDownloadItemVo> downloadItemVos = new ArrayList<>();

        for (int i = 0; i < ERP_DOWNLOAD_ITEM_SIZE; i++) {
            List<ErpOrderItemDto> dtos = erpDownloadOrderItemDtos.get(i).getCollections();

            dtos.sort(Comparator.comparing(ErpOrderItemDto::getProdName)
                    .thenComparing(ErpOrderItemDto::getReleaseOptionCode));

            // 다운로드 데이터 개수만큼 반복
            for (int j = 0; j < dtos.size(); j++) {
                ErpOrderItemDto originDto = dtos.get(j);
                List<Object> cellValueList = new ArrayList<>();
                
                // 항목에 설정된 항목만큼 반복
                for (int k = 0; k < HEADER_COLUMN_SIZE; k++) {
                    DetailDto detailDto = headerDto.getHeaderDetail().getDetails().get(k);
                    int VIEW_DETAIL_SIZE = detailDto.getViewDetails().size();
                    
                    // 해당 항목의 view 헤더로 지정된 데이터들을 구분자와 함께 나열한다
                    String appendFieldValue = "";
                    for (int z = 0; z < VIEW_DETAIL_SIZE; z++) {
                        String matchedColumnName = detailDto.getViewDetails().get(z).getMatchedColumnName();

                        Object obj = CustomFieldUtils.getFieldValue(originDto, matchedColumnName) != null ? CustomFieldUtils.getFieldValue(originDto, matchedColumnName) : "";
                        // LocalDateTiem타입의 데이터는 +9시간 설정
                        appendFieldValue += obj.getClass().equals(LocalDateTime.class) ? CustomDateUtils.getLocalDateTimeToDownloadFormat((LocalDateTime)obj) : obj.toString();

                        if (z < VIEW_DETAIL_SIZE - 1) {
                            appendFieldValue += detailDto.getValueSplitter().toString();
                        }
                    }
                    cellValueList.add(appendFieldValue);
                }
                ErpDownloadItemVo downloadItemVo = ErpDownloadItemVo.builder().cellValue(cellValueList).build();
                downloadItemVos.add(downloadItemVo);

                // 중복데이터 처리 (dtos들의 receiver, receiverContact1, destination은 항상 같은 값이기 때문에 중복검사하지 않아도 된다)
                // 첫번째 데이터를 제외한 중복데이터들의 mergeYn을 검사한다
                // mergeYn이 y라면 첫번째데이터에 구분자와 함께 나열
                if(j != 0) {
                    ErpDownloadItemVo prevVo = downloadItemVos.get(currentMergeItemIndex - 1);
                    ErpDownloadItemVo currentVo = downloadItemVos.get(currentMergeItemIndex);

                    for(int k = 0; k < HEADER_COLUMN_SIZE; k++) {
                        if(headerDto.getHeaderDetail().getDetails().get(k).getMergeYn().equals("y")) {
                            String result = prevVo.getCellValue().get(k) + headerDto.getHeaderDetail().getDetails().get(k).getMergeSplitter() + currentVo.getCellValue().get(k);
                            downloadItemVos.get(currentMergeItemIndex-1).getCellValue().set(k, result);
                        }
                    }
                    // 중복데이터 제거
                    downloadItemVos.remove(currentMergeItemIndex);
                } else {
                    currentMergeItemIndex++;
                }
            }
        }
        return downloadItemVos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * secondMergeHeaderId에 대응하는 erp download excel header를 조회한다.
     * 
     * @param secondMergeHeaderId : UUID
     * @return ErpDownloadExcelHeaderDto
     * @see ErpDownloadExcelHeaderService#searchOne
     * @see ErpDownloadExcelHeaderDto#toDto
     */
    public ErpDownloadExcelHeaderDto searchErpDownloadExcelHeader(UUID secondMergeHeaderId) {
        // access check
        userService.userLoginCheck();

        ErpDownloadExcelHeaderEntity downloadHeaderEntity = erpDownloadExcelHeaderService.searchOne(secondMergeHeaderId);
        return ErpDownloadExcelHeaderDto.toDto(downloadHeaderEntity);
    }
}
