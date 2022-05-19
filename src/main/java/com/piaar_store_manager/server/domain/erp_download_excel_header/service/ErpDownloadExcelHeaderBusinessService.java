package com.piaar_store_manager.server.domain.erp_download_excel_header.service;

import com.piaar_store_manager.server.domain.erp_download_excel_header.dto.ViewDetailDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
     * <b>DB Select Related Method</b>
     * <p>
     * 병합 여부와 splitter로 구분해 나타낼 컬럼들을 확인해 데이터를 나열한다
     * 동일 수령인정보라면 구분자(|&&|)로 표시해 병합한다
     * 고정값 여부를 체크해서 데이터를 고정값으로 채워넣는다
     *
     * @param id : UUID
     * @param erpDownloadOrderItemDtos : List::ErpDownloadOrderItemDto::
     * @return List::ErpOrderItemVo::
     * @see ErpDownloadExcelHeaderBusinessService#searchErpDownloadExcelHeader
     * @see ErpOrderItemVo#toVo
     * @see CustomFieldUtils#getFieldValue
     * @see CustomFieldUtils#setFieldValue
     */
    // public List<ErpOrderItemVo> downloadByErpDownloadExcelHeader(UUID id, List<ErpDownloadOrderItemDto> erpDownloadOrderItemDtos) {
    //     // access check
    //     userService.userLoginCheck();
    //     userService.userManagerRoleCheck();

    //     // 선택된 병합 헤더데이터 조회
    //     ErpDownloadExcelHeaderDto headerDto = this.searchErpDownloadExcelHeader(id);

    //     // 병합 여부가 y인 데이터들의 컬럼명, 구분자 추출
    //     Map<String, String> splitterMap = headerDto.getHeaderDetail().getDetails().stream()
    //             .filter(r -> r.getMergeYn().equals("y")).collect(Collectors.toList())
    //             .stream().collect(Collectors.toMap(
    //                     r -> r.getMatchedColumnName(),
    //                     r -> r.getSplitter()));

    //     // fixedValue가 존재하는 컬럼의 컬럼명과 fixedValue값 추출
    //     Map<String, String> fixedValueMap = headerDto.getHeaderDetail().getDetails().stream()
    //             .filter(r -> !r.getFixedValue().isBlank()).collect(Collectors.toList())
    //             .stream().collect(Collectors.toMap(
    //                     r -> r.getMatchedColumnName(),
    //                     r -> r.getFixedValue()));

    //     List<ErpOrderItemVo> mergeItemVos = new ArrayList<>();
    //     for (int k = 0; k < erpDownloadOrderItemDtos.size(); k++) {
    //         // downloadDto의 k번쨰 collection
    //         List<ErpOrderItemDto> dtos = erpDownloadOrderItemDtos.get(k).getCollections();
    //         List<ErpOrderItemVo> itemVos = dtos.stream().map(r -> ErpOrderItemVo.toVo(r)).collect(Collectors.toList());

    //         itemVos.sort(Comparator.comparing(ErpOrderItemVo::getReceiver)
    //                 .thenComparing(ErpOrderItemVo::getReceiverContact1)
    //                 .thenComparing(ErpOrderItemVo::getDestination)
    //                 .thenComparing(ErpOrderItemVo::getProdName)
    //                 .thenComparing(ErpOrderItemVo::getOptionName));

    //         // 구분자로 데이터들 병합
    //         for (int i = 0; i < itemVos.size() && i < dtos.size(); i++) {
    //             ErpOrderItemVo currentVo = itemVos.get(i);
    //             ErpOrderItemDto originDto = dtos.get(i);

    //             // 1. splitter로 나타낼 데이터 컬럼을 추출
    //             splitterMap.entrySet().stream().forEach(mergeMap -> {
    //                 // detailDto에 splitter로 구분지을 컬럼을 가진 viewDetails 존재. 이를 추출
    //                 DetailDto matchedDetail = headerDto.getHeaderDetail().getDetails().stream()
    //                         .filter(r -> r.getMatchedColumnName().equals(mergeMap.getKey()))
    //                         .collect(Collectors.toList()).get(0);

    //                 String appendFieldValue = "";

    //                 for (int j = 0; j < matchedDetail.getViewDetails().size(); j++) {
    //                     appendFieldValue += CustomFieldUtils.getFieldValue(originDto, matchedDetail.getViewDetails().get(j).getMatchedColumnName()).toString();
    //                     if (j < matchedDetail.getViewDetails().size() - 1) {
    //                         appendFieldValue += mergeMap.getValue().toString();
    //                     }
    //                 }
    //                 CustomFieldUtils.setFieldValue(currentVo, mergeMap.getKey(), appendFieldValue);
    //             });

    //             CustomFieldUtils.getFieldByIndex(currentVo, i);
    //         }

    //         // 합배송 처리
    //         Set<String> deliverySet = new HashSet<>();
    //         for (int i = 0; i < itemVos.size(); i++) {
    //             StringBuilder sb = new StringBuilder();
    //             sb.append(itemVos.get(i).getReceiver());
    //             sb.append(itemVos.get(i).getReceiverContact1());
    //             sb.append(itemVos.get(i).getDestination());

    //             String resultStr = sb.toString();

    //             mergeItemVos.add(itemVos.get(i));
    //             int currentMergeItemIndex = mergeItemVos.size() - 1;

    //             // 중복데이터(상품 + 옵션)
    //             if (!deliverySet.add(resultStr)) {
    //                 ErpOrderItemVo currentVo = mergeItemVos.get(currentMergeItemIndex);
    //                 ErpOrderItemVo prevVo = mergeItemVos.get(currentMergeItemIndex - 1);

    //                 // 구분자로 이미 데이터를 병합해놓았으므로 그 데이터를 가져온다.
    //                 splitterMap.entrySet().stream().forEach(mergeMap -> {
    //                     String prevFieldValue = CustomFieldUtils.getFieldValue(prevVo, mergeMap.getKey()) == null ? "" : CustomFieldUtils.getFieldValue(prevVo, mergeMap.getKey());
    //                     String currentFieldValue = CustomFieldUtils.getFieldValue(currentVo, mergeMap.getKey()) == null ? "" : CustomFieldUtils.getFieldValue(currentVo, mergeMap.getKey());
    //                     CustomFieldUtils.setFieldValue(prevVo, mergeMap.getKey(), prevFieldValue + "|&&|" + currentFieldValue);
    //                 });

    //                 // 중복데이터 제거
    //                 mergeItemVos.remove(currentMergeItemIndex);
    //             }

    //             // 3. fixedValue가 지정된 column들은 fixedValue값으로 데이터를 덮어씌운다
    //             fixedValueMap.entrySet().stream().forEach(map -> {
    //                 CustomFieldUtils.setFieldValue(mergeItemVos.get(mergeItemVos.size() - 1), map.getKey(), map.getValue());
    //             });
    //         }
    //     }
    //     return mergeItemVos;
    // }

    /**
     * TEST 1
     * downloadByErpDownloadExcelHeader
     * <p>
     * 1. headerDto에 작성된 데이터들만 erpDownloadOrderItemDtos에서 추출해 ErpDownloadItemVo의 cellValue로 지정한다
     */
    public List<ErpDownloadItemVo> downloadByErpDownloadExcelHeader(UUID id, ErpDownloadExcelHeaderDto headerDto, List<ErpDownloadOrderItemDto> erpDownloadOrderItemDtos) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        int currentMergeItemIndex = 0;
        List<ErpDownloadItemVo> downloadItemVos = new ArrayList<>();
        for (int i = 0; i < erpDownloadOrderItemDtos.size(); i++) {
            List<ErpOrderItemDto> dtos = erpDownloadOrderItemDtos.get(i).getCollections();

            // optionName -> releaseOptionCode로 변경
            dtos.sort(Comparator.comparing(ErpOrderItemDto::getReceiver)
                    .thenComparing(ErpOrderItemDto::getReceiverContact1)
                    .thenComparing(ErpOrderItemDto::getDestination)
                    .thenComparing(ErpOrderItemDto::getProdName)
                    // .thenComparing(ErpOrderItemDto::getOptionName));
                    .thenComparing(ErpOrderItemDto::getReleaseOptionCode));

            // 다운로드 데이터 개수만큼 반복
            for (int j = 0; j < dtos.size(); j++) {
                ErpOrderItemDto originDto = dtos.get(j);

                List<Object> cellValueList = new ArrayList<>();
                // 헤더에 설정된 다운로드 view 헤더만큼 반복
                for (int k = 0; k < headerDto.getHeaderDetail().getDetails().size(); k++) {
                    DetailDto detailDto = headerDto.getHeaderDetail().getDetails().get(k);

                    String appendFieldValue = "";
                    // 다운로드 view 헤더에서 viewDetails의 개수만큼 반복해 필드값을 update한다
                    for (int z = 0; z < detailDto.getViewDetails().size(); z++) {
                        String matchedColumnName = detailDto.getViewDetails().get(z).getMatchedColumnName();

                        Object obj = CustomFieldUtils.getFieldValue(originDto, matchedColumnName) != null ? CustomFieldUtils.getFieldValue(originDto, matchedColumnName) : "";
                        if (obj.getClass().equals(LocalDateTime.class)) {
                            appendFieldValue += CustomDateUtils.getLocalDateTimeToDownloadFormat((LocalDateTime) obj);
                        } else {
                            appendFieldValue += obj.toString();
                        }

                        if (z < detailDto.getViewDetails().size() - 1) {
                            appendFieldValue += detailDto.getValueSplitter().toString();
                        }
                    }
                    cellValueList.add(appendFieldValue);
                }
                ErpDownloadItemVo downloadItemVo = ErpDownloadItemVo.builder().cellValue(cellValueList).build();
                downloadItemVos.add(downloadItemVo);
            }

            // 합배송 처리
            Set<String> deliverySet = new HashSet<>();
            for (int j = 0; j < dtos.size(); j++) {
                StringBuilder sb = new StringBuilder();
                sb.append(dtos.get(j).getReceiver());
                sb.append(dtos.get(j).getReceiverContact1());
                sb.append(dtos.get(j).getDestination());

                String resultStr = sb.toString();

                // 중복데이터 처리
                if (!deliverySet.add(resultStr)) {
                    ErpDownloadItemVo prevVo = downloadItemVos.get(currentMergeItemIndex - 1);
                    ErpDownloadItemVo currentVo = downloadItemVos.get(currentMergeItemIndex);

                    for (int k = 0; k < headerDto.getHeaderDetail().getDetails().size(); k++) {
                        if (headerDto.getHeaderDetail().getDetails().get(k).getMergeYn().equals("y")) {
                            String result = prevVo.getCellValue().get(k) + headerDto.getHeaderDetail().getDetails().get(k).getMergeSplitter() + currentVo.getCellValue().get(k);
                            downloadItemVos.get(currentMergeItemIndex - 1).getCellValue().set(k, result);
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

    /*
    TEST 2
     */
    public List<List<String>> downloadByErpDownloadExcelHeader2(UUID id, ErpDownloadExcelHeaderDto headerDto, List<ErpDownloadOrderItemDto> erpDownloadOrderItemDtos) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        List<List<String>> matrix = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        int DETAILS_SIZE = headerDto.getHeaderDetail().getDetails().size();

        for (int i = 0; i < DETAILS_SIZE; i++) {
            DetailDto detail = headerDto.getHeaderDetail().getDetails().get(i);
            String column = detail.getCustomCellName();
            columns.add(column);
        }

        matrix.add(columns);

        for (int i = 0; i < erpDownloadOrderItemDtos.size(); i++) {
            List<ErpOrderItemDto> dtos = erpDownloadOrderItemDtos.get(i).getCollections();
            int ERP_ORDER_ITEM_DTOS_SIZE = dtos.size();

            dtos.sort(Comparator.comparing(ErpOrderItemDto::getReceiver)
                    .thenComparing(ErpOrderItemDto::getReceiverContact1)
                    .thenComparing(ErpOrderItemDto::getDestination)
                    .thenComparing(ErpOrderItemDto::getProdName)
                    .thenComparing(ErpOrderItemDto::getReleaseOptionCode));


            List<String> columsValue = new ArrayList<>();

            for (int j = 0; j < DETAILS_SIZE; j++) {
                DetailDto detail = headerDto.getHeaderDetail().getDetails().get(j);
                List<ViewDetailDto> viewDetails = detail.getViewDetails();
                String appendValue = "";

                if (detail.getFieldType().equals("운송코드")) {
                    appendValue = erpDownloadOrderItemDtos.get(i).getCombinedFreightCode();
                    columsValue.add(appendValue);
                    continue;
                }

                if (detail.getFieldType().equals("고정값")) {
                    if (detail.getMergeYn().equals("n")) {
                        appendValue = detail.getFixedValue();
                        break;
                    }

                    if (detail.getMergeYn().equals("y")) {
                        for (int k = 0; k < ERP_ORDER_ITEM_DTOS_SIZE; k++) {
                            appendValue += detail.getFixedValue();

                            if (k < ERP_ORDER_ITEM_DTOS_SIZE - 1) {
                                appendValue += detail.getMergeSplitter();
                            }
                        }
                    }
                }

                if (detail.getFieldType().equals("일반")) {
                    if (detail.getMergeYn().equals("n")) {
                        ErpOrderItemDto originDto = dtos.get(0);

                        for (int z = 0; z < viewDetails.size(); z++) {
                            String matchedColumnName = viewDetails.get(z).getMatchedColumnName();

                            Object obj = CustomFieldUtils.getFieldValue(originDto, matchedColumnName) != null ? CustomFieldUtils.getFieldValue(originDto, matchedColumnName) : "";
                            if (obj.getClass().equals(LocalDateTime.class)) {
                                appendValue += CustomDateUtils.getLocalDateTimeToDownloadFormat((LocalDateTime) obj);
                            } else {
                                appendValue += obj.toString();
                            }

                            if (z < viewDetails.size() - 1) {
                                appendValue += detail.getValueSplitter();
                            }
                        }
                    }

                    if (detail.getMergeYn().equals("y")) {
                        for (int k = 0; k < ERP_ORDER_ITEM_DTOS_SIZE; k++) {
                            ErpOrderItemDto originDto = dtos.get(k);

                            for (int z = 0; z < viewDetails.size(); z++) {
                                String matchedColumnName = viewDetails.get(z).getMatchedColumnName();

                                Object obj = CustomFieldUtils.getFieldValue(originDto, matchedColumnName) != null ? CustomFieldUtils.getFieldValue(originDto, matchedColumnName) : "";
                                if (obj.getClass().equals(LocalDateTime.class)) {
                                    appendValue += CustomDateUtils.getLocalDateTimeToDownloadFormat((LocalDateTime) obj);
                                } else {
                                    appendValue += obj.toString();
                                }

                                if (z < viewDetails.size() - 1) {
                                    appendValue += detail.getValueSplitter();
                                }
                            }

                            if (k < ERP_ORDER_ITEM_DTOS_SIZE - 1) {
                                appendValue += detail.getMergeSplitter();
                            }
                        }
                    }
                }

                columsValue.add(appendValue);
            }
            matrix.add(columsValue);
        }
        return matrix;
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
