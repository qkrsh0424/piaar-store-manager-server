package com.piaar_store_manager.server.domain.erp_order_item.service;

import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.excel_form.waybill.WaybillExcelFormDto;
import com.piaar_store_manager.server.domain.excel_form.waybill.WaybillExcelFormManager;
import com.piaar_store_manager.server.domain.excel_translator_header.dto.ExcelTranslatorDownloadHeaderDetailDto;
import com.piaar_store_manager.server.domain.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.piaar_store_manager.server.domain.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.piaar_store_manager.server.domain.excel_translator_header.service.ExcelTranslatorHeaderService;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.domain.sub_option_code.dto.SubOptionCodeDto;
import com.piaar_store_manager.server.domain.sub_option_code.proj.SubOptionCodeProjection;
import com.piaar_store_manager.server.domain.sub_option_code.service.SubOptionCodeService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomExcelFileUploadException;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomExcelUtils;
import com.piaar_store_manager.server.utils.CustomFieldUtils;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;
import lombok.RequiredArgsConstructor;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErpOrderItemBusinessService {
    private final ErpOrderItemService erpOrderItemService;
    private final ProductOptionService productOptionService;
    private final ProductReleaseService productReleaseService;
    private final OptionPackageService optionPackageService;
    private final SubOptionCodeService subOptionCodeService;
    private final UserService userService;
    private final ExcelTranslatorHeaderService excelTranslatorHeaderervice;

    /**
     * <b>Upload Excel File</b>
     * <p>
     * 엑셀 파일을 업로드한다.
     * 
     * @param file : MultipartFile
     * @param params : Map::String, Object::
     * @return List::ErpOrderItemVo.ExcelVo::
     */
    public List<ErpOrderItemVo.ExcelVo> uploadErpOrderExcel(MultipartFile file, Map<String, Object> params) {
        UUID headerId = params.get("headerId") != null ? UUID.fromString(params.get("headerId").toString()) : null;
        
        List<ErpOrderItemVo.ExcelVo> vos = new ArrayList<>();
        
        // 변환여부에 따라 엑셀 데이터 추출. headerId가 존재한다면 변환된 엑셀.
        if(headerId != null) {
            vos = this.uploadOtherForm(file, params);
        }else {
            vos = this.uploadPiaarForm(file, params);
        }
        return vos;
    }

    /*
     * 피아르 엑셀파일 업로드
     */
    public List<ErpOrderItemVo.ExcelVo> uploadPiaarForm(MultipartFile file, Map<String, Object> params) {
        Workbook workbook = null;

        String excelPassword = params.get("excelPassword") != null ? params.get("excelPassword").toString() : null;

        try {
            if (excelPassword != null) {
                workbook = WorkbookFactory.create(file.getInputStream(), excelPassword);
            } else {
                workbook = WorkbookFactory.create(file.getInputStream());
            }
        } catch (IOException e) {
            throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        } catch (EncryptedDocumentException e) {
            throw new CustomExcelFileUploadException("비밀번호가 올바르지 않습니다. \n엑셀 파일을 재업로드 해주세요.");
        }

        Sheet sheet = workbook.getSheetAt(0);

        List<ErpOrderItemVo.ExcelVo> vos = new ArrayList<>();
        try {
            vos = ErpOrderItemVo.excelSheetToVos(sheet);
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("피아르 엑셀 양식과 데이터 타입이 다른 값이 존재합니다.\n올바른 엑셀 파일을 업로드해주세요.");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        }

        // subOptionCode의 superOptionCode값을 추출해 ProductOptionCode항목에 대입.
        // this.updateOptionCodeBySubOptionCode(vos);
        return vos;
    }

    /*
     * 타 양식 업로드 > 피아르 양식으로 변환
     */
    public List<ErpOrderItemVo.ExcelVo> uploadOtherForm(MultipartFile file, Map<String, Object> params) {
        Workbook workbook;

        UUID headerId = params.get("headerId") != null ? UUID.fromString(params.get("headerId").toString()) : null;
        String excelPassword = params.get("excelPassword") != null ? params.get("excelPassword").toString() : null;

        try {
            if (excelPassword != null) {
                workbook = WorkbookFactory.create(file.getInputStream(), excelPassword);
            } else {
                workbook = WorkbookFactory.create(file.getInputStream());
            }
        } catch (IOException e) {
            throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        } catch (EncryptedDocumentException e) {
            if(e.getMessage().equals("Password incorrect")) {
                throw new CustomExcelFileUploadException("비밀번호가 올바르지 않습니다. \n엑셀 파일을 재업로드 해주세요.");
            }
            throw new CustomExcelFileUploadException("엑셀 암호 오류. \n올바른 엑셀 파일을 업로드해주세요.");
        }

        Sheet sheet = workbook.getSheetAt(0);

        List<ErpOrderItemVo.ExcelVo> vos = new ArrayList<>();
        try {
            // 엑셀변환기에 등록된 타 엑셀 양식을 피아르 양식으로 변환해 업로드하는 경우
            // headerId에 대응하는 excelTranslator 데이터 조회
            ExcelTranslatorHeaderEntity translatorEntity = excelTranslatorHeaderervice.searchOne(headerId);
            ExcelTranslatorHeaderGetDto translatorGetDto = ExcelTranslatorHeaderGetDto.toDto(translatorEntity);

            // header size 비교로 업로드 양식이 올바른지 검사
            Row headerRow = sheet.getRow(translatorGetDto.getRowStartNumber() - 1);
            if (translatorGetDto.getUploadHeaderDetail().getDetails().size() != headerRow.getLastCellNum()) {
                throw new CustomExcelFileUploadException("업로드 엑셀 양식과 동일한 양식의 파일을 업로드해주세요.");
            }

            vos = this.excelSheetToPiaarVos(translatorGetDto, sheet);
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("피아르 엑셀 양식과 데이터 타입이 다른 값이 존재합니다.\n올바른 엑셀 파일을 업로드해주세요.");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        }

        // subOptionCode의 superOptionCode값을 추출해 ProductOptionCode항목에 대입.
        // this.updateOptionCodeBySubOptionCode(vos);
        return vos;
    }

    public List<ErpOrderItemVo.ExcelVo> excelSheetToPiaarVos(ExcelTranslatorHeaderGetDto translatorGetDto, Sheet sheet) {
        // 다운로드 엑셀 디테일에 따라 값 설정.
        List<ErpOrderItemVo.ExcelVo> vos = new ArrayList<>();
        List<Integer> PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX = Arrays.asList(1, 2, 3, 4, 5, 7);
        Row row = null;
        int rowNum = translatorGetDto.getRowStartNumber();

        for(int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(rowNum++);

            if(row == null) {
                break;
            }

            List<ExcelTranslatorDownloadHeaderDetailDto.DetailDto> details = translatorGetDto.getDownloadHeaderDetail().getDetails();
            for(int j = 0; j < details.size(); j++) {
                if(PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX.contains(j)) {
                    // targetCellNumber가 -1이면서 고정값이 공백인 경우. targetCellNumber가 가리키는 cell값이 null이거나 빈값인 경우 예외처리.
                    if(details.get(j).getTargetCellNumber().equals(-1)) {
                        if(details.get(j).getFixedValue().isBlank()) {
                            throw new CustomInvalidDataException("필수값 항목이 비어있습니다. 수정 후 재업로드 해주세요.");
                        }
                    } else if(row.getCell(details.get(j).getTargetCellNumber()) == null || CustomExcelUtils.isBlankCell(row.getCell(details.get(j).getTargetCellNumber()))) {
                        throw new CustomInvalidDataException("필수값 항목이 비어있습니다. 수정 후 재업로드 해주세요.");
                    }
                }
            }

            // 수량, 가격, 배송비의 타입을 체크해 Number format 으로 변환한다
            Object unitObj = getTranslatorTargetCellValue(row, details.get(3)).equals("") ? 0 : getTranslatorTargetCellValue(row, details.get(3));
            Object priceObj = getTranslatorTargetCellValue(row, details.get(19)).equals("") ? 0 : getTranslatorTargetCellValue(row, details.get(19));
            Object deliveryChargeObj = getTranslatorTargetCellValue(row, details.get(20)).equals("") ? 0 : getTranslatorTargetCellValue(row, details.get(20));

            if(!unitObj.getClass().equals(Integer.class)) {
                unitObj = CustomExcelUtils.convertObjectValueToIntegerValue(unitObj);
            }
            if(!priceObj.getClass().equals(Integer.class)){
                priceObj = CustomExcelUtils.convertObjectValueToIntegerValue(priceObj);
            }
            if(!deliveryChargeObj.getClass().equals(Integer.class)) {
                deliveryChargeObj = CustomExcelUtils.convertObjectValueToIntegerValue(deliveryChargeObj);
            }

            ErpOrderItemVo.ExcelVo excelVo = ErpOrderItemVo.ExcelVo.builder()
                .id(UUID.randomUUID())
                .uniqueCode(null)
                .prodName(getTranslatorTargetCellValue(row, details.get(1)))
                .optionName(getTranslatorTargetCellValue(row, details.get(2)))
                .unit(unitObj)
                .receiver(getTranslatorTargetCellValue(row, details.get(4)))
                .receiverContact1(getTranslatorTargetCellValue(row, details.get(5)))
                .receiverContact2(getTranslatorTargetCellValue(row, details.get(6)))
                .destination(getTranslatorTargetCellValue(row, details.get(7)))
                .destinationDetail(getTranslatorTargetCellValue(row, details.get(8)))
                .salesChannel(getTranslatorTargetCellValue(row, details.get(9)))
                .orderNumber1(getTranslatorTargetCellValue(row, details.get(10)))
                .orderNumber2(getTranslatorTargetCellValue(row, details.get(11)))
                .channelProdCode(getTranslatorTargetCellValue(row, details.get(12)))
                .channelOptionCode(getTranslatorTargetCellValue(row, details.get(13)))
                .zipCode(getTranslatorTargetCellValue(row, details.get(14)))
                .courier(getTranslatorTargetCellValue(row, details.get(15)))
                .transportType(getTranslatorTargetCellValue(row, details.get(16)))
                .deliveryMessage(getTranslatorTargetCellValue(row, details.get(17)))
                .waybillNumber(getTranslatorTargetCellValue(row, details.get(18)))
                .price(priceObj)
                .deliveryCharge(deliveryChargeObj)
                .barcode(getTranslatorTargetCellValue(row, details.get(21)))
                .prodCode(getTranslatorTargetCellValue(row, details.get(22)))
                .optionCode(getTranslatorTargetCellValue(row, details.get(23)))
                .releaseOptionCode(getTranslatorTargetCellValue(row, details.get(24)))
                .channelOrderDate(getTranslatorTargetCellValue(row, details.get(25)))
                .managementMemo1(getTranslatorTargetCellValue(row, details.get(26)))
                .managementMemo2(getTranslatorTargetCellValue(row, details.get(27)))
                .managementMemo3(getTranslatorTargetCellValue(row, details.get(28)))
                .managementMemo4(getTranslatorTargetCellValue(row, details.get(29)))
                .managementMemo5(getTranslatorTargetCellValue(row, details.get(30)))
                .managementMemo6(getTranslatorTargetCellValue(row, details.get(31)))
                .managementMemo7(getTranslatorTargetCellValue(row, details.get(32)))
                .managementMemo8(getTranslatorTargetCellValue(row, details.get(33)))
                .managementMemo9(getTranslatorTargetCellValue(row, details.get(34)))
                .managementMemo10(getTranslatorTargetCellValue(row, details.get(35)))
                .freightCode(null)
                .build();

            vos.add(excelVo);
        }
        return vos;
    }

    /*
    detail을 참고해서 고정값 여부에 따라 값을 세팅한다
     */
    private Object getTranslatorTargetCellValue(Row row, ExcelTranslatorDownloadHeaderDetailDto.DetailDto detail) {
        if(detail.getTargetCellNumber().equals(-1)) {
            return detail.getFixedValue();
        }else {
            return CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(detail.getTargetCellNumber()), "");
        }
    }

    /**
    판매채널 옵션코드 항목에 입력된 값을 대체옵션관리코드를 확인해 업데이트한다.
     */
    // public void updateOptionCodeBySubOptionCode(List<ErpOrderItemVo.ExcelVo> vos) {
    //     // 판매채널 옵션코드 항목을 sub_option_code테이블에서 찾는다
    //     // 있으면 super_option_code를 optionCode에 대입
    //     List<SubOptionCodeEntity> subOptionEntities = subOptionCodeService.findAll();
    //     List<SubOptionCodeDto> subOptionCodeDtos = subOptionEntities.stream().map(entity -> SubOptionCodeDto.toDto(entity)).collect(Collectors.toList());
                
    //     // TODO :: projection을 사용하자
    //     List<UUID> optionIds = subOptionCodeDtos.stream().map(r -> r.getProductOptionId()).collect(Collectors.toList());
    //     List<ProductOptionEntity> optionEntities = productOptionService.searchListByIds(optionIds);

    //     List<SubOptionCodeDto.MatchedOptionCode> matchedSubOptionDtos = new ArrayList<>();
    //     subOptionCodeDtos.forEach(subOptionCode -> {
    //         optionEntities.forEach(optionEntity -> {
    //             if(subOptionCode.getProductOptionId().equals(optionEntity.getId())) {
    //                 SubOptionCodeDto.MatchedOptionCode matchedSubOption = SubOptionCodeDto.MatchedOptionCode.builder()
    //                     .id(subOptionCode.getId())
    //                     .subOptionCode(subOptionCode.getSubOptionCode())
    //                     .memo(subOptionCode.getMemo())
    //                     .productOptionId(subOptionCode.getProductOptionId())
    //                     .productOptionCode(optionEntity.getCode())
    //                     .createdAt(subOptionCode.getCreatedAt())
    //                     .createdBy(subOptionCode.getCreatedBy())
    //                     .updatedAt(subOptionCode.getUpdatedAt())
    //                     .build();

    //                 matchedSubOptionDtos.add(matchedSubOption);
    //             }
    //         });
    //     });
        

    //     // vos를 돌면서 channelOptionCode를 확인해 옵션코드에 대응되는 값을 '피아르옵션코드'항목에 세팅.
    //    vos.forEach(vo -> {
    //         if(vo.getOptionCode().equals("")) {
    //             matchedSubOptionDtos.forEach(subOptionCode -> {
    //                 Object channelOptionCode = vo.getChannelOptionCode();
    //                 if(channelOptionCode != null
    //                      && !channelOptionCode.equals("") 
    //                      && channelOptionCode.equals(subOptionCode.getSubOptionCode())) {
    //                     vo.setOptionCode(subOptionCode.getProductOptionCode());
    //                 }
    //             });
    //         }
    //     });
    // }

    // 221115 : 기존, 엑셀 업로드 시점에 실행 -> 변경후, 엑셀 주문데이터 저장 시점에 실행
    // '판매채널 옵션코드' 항목을 확인해 대체옵션코드와 대응되는 데이터가 존재하면 피아르 옵션코드를 변경시킨다.
    public void updateOptionCodeBySubOptionCode(List<ErpOrderItemDto> itemDtos) {
        // 판매채널 옵션코드 항목을 sub_option_code테이블에서 찾는다
        // 있으면 super_option_code를 optionCode에 대입
        List<SubOptionCodeProjection.RelatedProductOption> subOptionProjs = subOptionCodeService.qSearchAll();
        List<SubOptionCodeDto.RelatedProductOption> subOptionDtos = subOptionProjs.stream().map(proj -> SubOptionCodeDto.RelatedProductOption.toDto(proj)).collect(Collectors.toList());

        // dtos를 돌면서 channelOptionCode를 확인해 옵션코드에 대응되는 값을 '피아르옵션코드'항목에 세팅.
        itemDtos.forEach(dto -> {
            if(dto.getOptionCode().equals("")) { 
                subOptionDtos.forEach(dtos -> {
                    Object channelOptionCode = dto.getChannelOptionCode();
                    if(channelOptionCode != null
                        && !channelOptionCode.equals("")
                        && channelOptionCode.equals(dtos.getSubOptionCode().getSubOptionCode())) {
                            dto.setOptionCode(dtos.getProductOption().getCode());
                        }
                });
            }
        });
    }

    @Transactional
    public void createBatch(List<ErpOrderItemDto> orderItemDtos) {
        UUID USER_ID = userService.getUserId();
        List<ErpOrderItemDto> newOrderItemDtos = this.itemDuplicationCheck(orderItemDtos);
        this.updateOptionCodeBySubOptionCode(newOrderItemDtos);

        // String 타입의 데이터들의 앞뒤 공백을 제거한다
        this.convertToStripedDto(newOrderItemDtos);
        
        List<ErpOrderItemEntity> orderItemEntities = newOrderItemDtos.stream()
                .map(r -> {
                    r.setId(UUID.randomUUID())
                            .setUniqueCode(CustomUniqueKeyUtils.generateCode18())
                            .setFreightCode(CustomUniqueKeyUtils.generateFreightCode())
                            .setSalesYn("n")
                            .setReleaseOptionCode(r.getOptionCode())
                            .setReleaseYn("n")
                            .setStockReflectYn("n")
                            .setReturnYn("n")
                            // .setExchangeYn("n")
                            .setCreatedAt(CustomDateUtils.getCurrentDateTime())
                            .setCreatedBy(USER_ID);

                    return ErpOrderItemEntity.toEntity(r);
                }).collect(Collectors.toList());

        erpOrderItemService.bulkInsert(orderItemEntities);
    }

    // String 타입의 데이터들의 앞뒤 공백을 제거한다
    public void convertToStripedDto(List<ErpOrderItemDto> dtos) {
        dtos.forEach(orderItemDto -> {
            List<String> fieldsName = CustomFieldUtils.getAllFieldNames(orderItemDto);
            for(String fieldName : fieldsName) {
                Object obj = CustomFieldUtils.getFieldValue(orderItemDto, fieldName);
                if(obj != null && obj.getClass().equals(String.class)) {
                    CustomFieldUtils.setFieldValue(orderItemDto, fieldName, obj.toString().strip());
                }
            }
        });
    }

    /*
    로직 분리 부분 한번 더 고려 해보기
     */
    public List<ErpOrderItemDto> itemDuplicationCheck(List<ErpOrderItemDto> dtos) {
        List<ErpOrderItemDto> newItems = dtos.stream().filter(r -> r.getOrderNumber1().isEmpty()).collect(Collectors.toList());
        List<ErpOrderItemDto> duplicationCheckItems = dtos.stream().filter(r -> !r.getOrderNumber1().isEmpty()).collect(Collectors.toList());

        this.convertToStripedDto(duplicationCheckItems);

        List<String> orderNumber1 = new ArrayList<>();
        List<String> receiver = new ArrayList<>();
        List<String> prodName = new ArrayList<>();
        List<String> optionName = new ArrayList<>();
        List<Integer> unit = new ArrayList<>();
        duplicationCheckItems.forEach(r -> {
            orderNumber1.add(r.getOrderNumber1());
            receiver.add(r.getReceiver());
            prodName.add(r.getProdName());
            optionName.add(r.getOptionName());
            unit.add(r.getUnit());
        });

        List<ErpOrderItemEntity> duplicationEntities = erpOrderItemService.findDuplicationItems(orderNumber1, receiver, prodName, optionName, unit);

        if (duplicationEntities.size() == 0) {
            return dtos;
        } else {
            for (ErpOrderItemDto duplicationCheckItem : duplicationCheckItems) {
                boolean duplication = false;
                // 주문번호 + 수령인 + 상품명 + 옵션명 + 수량 이 동일하다면 저장 제외
                for (ErpOrderItemEntity duplicationEntity : duplicationEntities) {
                    if (duplicationEntity.getOrderNumber1().equals(duplicationCheckItem.getOrderNumber1())
                            && duplicationEntity.getReceiver().equals(duplicationCheckItem.getReceiver())
                            && duplicationEntity.getProdName().equals(duplicationCheckItem.getProdName())
                            && duplicationEntity.getOptionName().equals(duplicationCheckItem.getOptionName())
                            && duplicationEntity.getUnit().equals(duplicationCheckItem.getUnit())) {
                        duplication = true;
                        break;
                    }
                }
                if (!duplication) {
                    newItems.add(duplicationCheckItem);
                }
            }
        }

        // System.out.println(newItems);
        return newItems;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 아이디 리스트 별 ErpOrderItemProjs 데이터를 가져온다.
     * 옵션 재고 수량 추가 및 vos 변환
     * <p>
     *
     * @param params : Map::String, Object::
     * @return List::ErpOrderItemVo::
     */
//    RE-OK
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo> searchList(Map<String, Object> params) {
        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
        // 옵션재고수량 추가 및 vos 변환
        return this.setOptionStockUnitAndToVos(itemProjs);
    }

    // TEST 대시보드 -> searchAllByPaging 사용. => 판매성과에서 사용
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo.ManyToOneJoin> searchAll(Map<String, Object> params) {
        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
        // 옵션재고수량 추가 및 vos 변환
        return this.setOptionStockUnitAndToM2OJVos(itemProjs);
    }

    /*
    아이디 리스트 별 ErpOrderItemProjs 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJ(ids, params);       // 페이징 처리 x
        // 옵션재고수량 추가 및 vos 변환
        return this.setOptionStockUnitAndToVos(itemProjs);
    }

    // 출고 상태 관리에서 action-refresh
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo> searchBatchByReleasedItemIds(List<UUID> ids, Map<String, Object> params) {
        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJByReleasedItem(ids, params);       // 페이징 처리 x
        // 옵션재고수량 추가 및 vos 변환
        return this.setOptionStockUnitAndToVos(itemProjs);
    }

    /*
    조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    @Transactional(readOnly = true)
    public Page<ErpOrderItemVo> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        /*
        조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
         */
        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findAllM2OJByPage(params, pageable);
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o

        // 옵션재고수량 추가 및 vos 변환
        List<ErpOrderItemVo> erpOrderItemVos = this.setOptionStockUnitAndToVos(itemProjs);

        return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
    }

    // TEST 대시보드
    @Transactional(readOnly = true)
    public Page<ErpOrderItemVo.ManyToOneJoin> searchAllByPaging(Map<String, Object> params, Pageable pageable) {
        /*
        조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
         */
        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findAllM2OJByPage(params, pageable);
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o

        // 옵션재고수량 추가 및 vos 변환
        List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = this.setOptionStockUnitAndToM2OJVos(itemProjs);

        return new PageImpl(erpOrderItemM2OJVos, pageable, itemPages.getTotalElements());
    }

    /*
    조건별 페이지별 출고 상태인 ErpOrderItemProj Page 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    @Transactional(readOnly = true)
    public Page<ErpOrderItemVo> searchReleaseItemBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findReleaseItemM2OJByPage(params, pageable);
        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o
        // 옵션재고수량 추가 및 vos 변환
        List<ErpOrderItemVo> erpOrderItemVos = this.setOptionStockUnitAndToVos(itemProjs);

        return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
    }

    /**
     * ErpOrderItemProjs => ErpOrderItemVos
     * proj -> vos 변환 및 재고 수량 셋
     *
     * @param itemProjs
     * @return
     */
    /*
    ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
    옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
    ErpOrderItemProjs -> ErpOrderItemVos 변환
    ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
     */
//    RE-OK
    private List<ErpOrderItemVo> setOptionStockUnitAndToVos(List<ErpOrderItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
        /*
        ErpOrderItemProjs -> ErpOrderItemVos 변환
         */
        List<ErpOrderItemVo> erpOrderItemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
        /*
        ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
         */
        ErpOrderItemVo.setOptionStockUnitForList(erpOrderItemVos, optionEntities);
        return erpOrderItemVos;
    }

    private List<ErpOrderItemVo.ManyToOneJoin> setOptionStockUnitAndToM2OJVos(List<ErpOrderItemProj> itemProjs) {
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);

        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);

        List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());
        ErpOrderItemVo.setOptionStockUnitForM2OJList(erpOrderItemM2OJVos, optionEntities);

        return erpOrderItemM2OJVos;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 엑셀 데이터의 salesYn(판매 여부)을 업데이트한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     */
//    PASS
    @Transactional
    public void changeBatchForSalesYn(List<ErpOrderItemDto> itemDtos) {
        List<UUID> idList = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {

                if (dto.getSalesYn().equals("n")) {
                    entity.setSalesYn("n");
                    entity.setSalesAt(null);
                    entity.setReleaseYn("n");
                    entity.setReleaseAt(null);
                    return;
                }

                entity.setSalesYn("y");
                entity.setSalesAt(CustomDateUtils.getCurrentDateTime());
            }
        }));

        erpOrderItemService.saveListAndModify(entities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 엑셀 데이터의 releaseYn(출고 여부)을 업데이트한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemService#findAllByIdList
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpOrderItemService#saveListAndModify
     */
//    PASS
    @Transactional
    public void changeBatchForReleaseYn(List<ErpOrderItemDto> itemDtos) {
        List<UUID> idList = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {

                if (dto.getReleaseYn().equals("n")) {
                    entity.setReleaseYn("n");
                    entity.setReleaseAt(null);
                    return;
                }

                entity.setReleaseYn("y");
                entity.setReleaseAt(CustomDateUtils.getCurrentDateTime());
            }
        }));

        erpOrderItemService.saveListAndModify(entities);
    }

    @Transactional
    public void changeForReturnYn(ErpOrderItemDto itemDto) {
        ErpOrderItemEntity entity = erpOrderItemService.searchOne(itemDto.getId());
        entity.setReturnYn(itemDto.getReturnYn());
    }

    @Transactional
    public void changeBatchForReturnYn(List<ErpOrderItemDto> itemDtos) {
        List<UUID> orderIds = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.searchBatchByIds(orderIds);

        itemDtos.forEach(dto -> {
            ErpOrderItemEntity itemEntity = entities.stream().filter(entity -> dto.getId().equals(entity.getId())).findFirst().get();
            itemEntity.setReturnYn(dto.getReturnYn());
        });
    }

    /**
     * <b>Data Delete Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 삭제한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemEntity#toEntity
     * @see ErpOrderItemService#delete
     */
//    PASS
    @Transactional
    public void deleteBatch(List<ErpOrderItemDto> itemDtos) {
        List<UUID> itemIds = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        erpOrderItemService.deleteBatch(itemIds);
    }

    /**
     * <b>Data Update Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 수정한다.
     *
     * @param dto : ErpOrderItemDto
     * @see ErpOrderItemService#searchOne
     * @see ErpOrderItemService#saveAndModify
     */
//    PASS
    @Transactional
    public void updateOne(ErpOrderItemDto dto) {
        ErpOrderItemEntity entity = erpOrderItemService.searchOne(dto.getId());

        /*
        Dirty Checking 업데이트
         */
        entity.setProdName(dto.getProdName()).setOptionName(dto.getOptionName())
                .setUnit(dto.getUnit()).setReceiver(dto.getReceiver()).setReceiverContact1(dto.getReceiverContact1())
                .setReceiverContact2(dto.getReceiverContact2())
                .setDestination(dto.getDestination())
                .setDestinationDetail(dto.getDestinationDetail())
                .setSalesChannel(dto.getSalesChannel())
                .setOrderNumber1(dto.getOrderNumber1())
                .setOrderNumber2(dto.getOrderNumber2())
                .setChannelProdCode(dto.getChannelProdCode())
                .setChannelOptionCode(dto.getChannelOptionCode())
                .setZipCode(dto.getZipCode())
                .setCourier(dto.getCourier())
                .setTransportType(dto.getTransportType())
                .setDeliveryMessage(dto.getDeliveryMessage())
                .setWaybillNumber(dto.getWaybillNumber())
                .setPrice(dto.getPrice())
                .setDeliveryCharge(dto.getDeliveryCharge())
                .setBarcode(dto.getBarcode())
                .setProdCode(dto.getProdCode())
                .setOptionCode(dto.getOptionCode())
                .setReleaseOptionCode(dto.getReleaseOptionCode())
                .setChannelOrderDate(dto.getChannelOrderDate())
                .setManagementMemo1(dto.getManagementMemo1())
                .setManagementMemo2(dto.getManagementMemo2())
                .setManagementMemo3(dto.getManagementMemo3())
                .setManagementMemo4(dto.getManagementMemo4())
                .setManagementMemo5(dto.getManagementMemo5())
                .setManagementMemo6(dto.getManagementMemo6())
                .setManagementMemo7(dto.getManagementMemo7())
                .setManagementMemo8(dto.getManagementMemo8())
                .setManagementMemo9(dto.getManagementMemo9())
                .setManagementMemo10(dto.getManagementMemo10());
    }

    /**
     * <b>Data Update Related Method</b>
     * <p>
     * 변경 주문 옵션코드를 참고해 주문 옵션코드와 출고 옵션코드를 변경한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemService#findAllByIdList
     * @see ErpOrderItemService#saveListAndModify
     */
    @Transactional
    public void changeBatchForAllOptionCode(List<ErpOrderItemDto> itemDtos) {
        List<ErpOrderItemEntity> entities = erpOrderItemService.getEntities(itemDtos);

//        Dirty Checking update
        entities.forEach(entity -> {
            // 재고반영 데이터 제한
            if (entity.getStockReflectYn().equals("y")) {
                throw new CustomInvalidDataException("재고반영된 데이터는 옵션코드를 변경할 수 없습니다.");
            }

            itemDtos.forEach(dto -> {
                if (entity.getId().equals(dto.getId())) {
                    entity.setOptionCode(dto.getOptionCode())
                            .setReleaseOptionCode(dto.getOptionCode());
                }
            });
        });
    }

    /**
     * <b>Data Update Related Method</b>
     * <p>
     * 출고 옵션코드를 변경한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemService#findAllByIdList
     * @see ErpOrderItemService#saveListAndModify
     */
//    PASS
    @Transactional
    public void changeBatchForReleaseOptionCode(List<ErpOrderItemDto> itemDtos) {
        List<UUID> idList = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);
        
        /*
        Dirty Checking update
         */
        entities.forEach(entity -> {
            // 재고반영 데이터 제한
            if (entity.getStockReflectYn().equals("y")) {
                throw new CustomInvalidDataException("재고반영된 데이터는 출고옵션코드를 변경할 수 없습니다.");
            }

            itemDtos.forEach(dto -> {
                if (entity.getId().equals(dto.getId())) {
                    entity.setReleaseOptionCode(dto.getReleaseOptionCode());
                }
            });
        });
    }

    /**
     * 운송장 업로드 엑셀 파일을 읽어들이고 해당 엑셀 파일을 DTO로 변환해서 리턴해준다.
     * <p>Last Modifier : Austin Park 22.4.22</p>
     *
     * @see ErpOrderItemBusinessService#changeBatchForWaybill
     * @since 1.1
     */
    @Transactional(readOnly = true)
    public List<WaybillExcelFormDto> readWaybillExcelFile(MultipartFile file) {
        if (!CustomExcelUtils.isExcelFile(file)) {
            throw new CustomExcelFileUploadException("올바른 파일은 업로드 해주세요.\n[.xls, .xlsx] 확장자 파일만 허용됩니다.");
        }

        List<String> HEADER_NAMES = WaybillExcelFormManager.HEADER_NAMES;
        List<String> FIELD_NAMES = WaybillExcelFormManager.getAllFieldNames();
        List<Integer> REQUIRED_CELL_NUMBERS = WaybillExcelFormManager.REQUIRED_CELL_NUMBERS;

        int SHEET_INDEX = 0;
        Integer HEADER_ROW_INDEX = WaybillExcelFormManager.HEADER_ROW_INDEX;
        Integer DATA_START_ROW_INDEX = WaybillExcelFormManager.DATA_START_ROW_INDEX;
        Integer ALLOWED_CELL_SIZE = WaybillExcelFormManager.ALLOWED_CELL_SIZE;

        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet worksheet = workbook.getSheetAt(SHEET_INDEX);
        Row headerRow = worksheet.getRow(HEADER_ROW_INDEX);

        /*
        최종 데이터 담는 리스트 생성
         */
        List<WaybillExcelFormDto> waybillExcelFormDtos = new ArrayList<>();

        System.out.println(CustomExcelUtils.getCellCount(worksheet, HEADER_ROW_INDEX));
        System.out.println(ALLOWED_CELL_SIZE);
        /*
        엑셀 형식 검사 => cell size, header cell name check
         */
        if (
                !CustomExcelUtils.getCellCount(worksheet, HEADER_ROW_INDEX).equals(ALLOWED_CELL_SIZE) ||
                        !CustomExcelUtils.isCheckedHeaderCell(headerRow, HEADER_NAMES)
        ) {
            throw new CustomExcelFileUploadException("올바른 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드 해주세요.");
        }

        /*
        엑셀 데이터 부분 컨트롤 Row Loop
         */
        for (int i = DATA_START_ROW_INDEX; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            WaybillExcelFormDto waybillExcelFormDto = new WaybillExcelFormDto();

            /*
            Cell Loop
             */
            for (int j = 0; j < ALLOWED_CELL_SIZE; j++) {
                Cell cell = row.getCell(j);
                CellType cellType = cell.getCellType();
                Object cellValue;
                /*
                필수 데이터 셀이 하나라도 비어있게 되면 dto를 Null로 처리하고 break
                 */
                if (REQUIRED_CELL_NUMBERS.contains(j) && cellType.equals(CellType.BLANK)) {
                    waybillExcelFormDto = null;
                    break;
                }
                /*
                cellValue 가져오기
                 */
               cellValue = CustomExcelUtils.getCellValueObject(cell);
                /*
                cellValue dto에 매핑시키기
                 */
                CustomFieldUtils.setFieldValueWithSuper(waybillExcelFormDto, FIELD_NAMES.get(j), cellValue.toString());
            }
            /*
            dto가 널이 아니라면 리스트에 담는다.
             */
            if (waybillExcelFormDto != null) {
                waybillExcelFormDtos.add(waybillExcelFormDto);
            }
        }

        return waybillExcelFormDtos;
    }

    /**
     * <p>선택된 데이터와 운송장 일괄 등록을 위해 엑셀에서 dto 로 변환된 데이터를 비교 대조해서 선택된 데이터의 운송장 관련 데이터들을 업데이트 한다.</p>
     * <p>일반적으로 ErpOrderItemBusinessService#readWaybillExcelFile의 작업이 선행 되어야한다. </p>
     * <p>Last Modifier : Austin Park 22.4.22</p>
     *
     * @return 운송장 관련 데이터가 정상적으로 적용된 데이터의 수
     * @see ErpOrderItemBusinessService#readWaybillExcelFile
     * @since 1.1
     */
    @Transactional
    public int changeBatchForWaybill(List<ErpOrderItemDto> erpOrderItemDtos, List<WaybillExcelFormDto> waybillExcelFormDtos) {
        /*
        운송코드 별 운임번호 분해작업
         */
        List<WaybillExcelFormDto> dismantledWaybillExcelFormDtos = new ArrayList<>();
        waybillExcelFormDtos.forEach(r -> {
            List<String> freightCodes = List.of(r.getFreightCode().split(","));

            freightCodes.forEach(freightCode -> {
                WaybillExcelFormDto dto = new WaybillExcelFormDto();
                dto.setReceiver(r.getReceiver());
                dto.setFreightCode(freightCode);
                dto.setReceiverContact1(r.getReceiverContact1());
                dto.setWaybillNumber(r.getWaybillNumber());
                dto.setTransportType(r.getTransportType());
                dto.setCourier(r.getCourier());

                dismantledWaybillExcelFormDtos.add(dto);
            });

        });

        /*
        선택된 주문건의 엔터티들을 불러온다.
         */
        List<UUID> ids = erpOrderItemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> erpOrderItemEntities = erpOrderItemService.findAllByIdList(ids);
        AtomicInteger updatedCount = new AtomicInteger();

        /*
         * 분해작업이 완료된 운임코드+수취인명과 선택된 주문건들의 운임코드+수취인명을 비교해서 일치하는 데이터에 운송장 관련 데이터들을 업데이트한다.
         * 업데이트 완료된건 카운팅
         */
        erpOrderItemEntities.forEach(erpOrderItemEntity -> {
            String matchingData = erpOrderItemEntity.getReceiver() + erpOrderItemEntity.getFreightCode();
            dismantledWaybillExcelFormDtos.forEach(waybillExcelFormDto -> {
                String matchedData = waybillExcelFormDto.getReceiver() + waybillExcelFormDto.getFreightCode();

                if (matchingData.equals(matchedData)) {
                    erpOrderItemEntity.setWaybillNumber(waybillExcelFormDto.getWaybillNumber());
                    erpOrderItemEntity.setTransportType(waybillExcelFormDto.getTransportType());
                    erpOrderItemEntity.setCourier(waybillExcelFormDto.getCourier());
                    updatedCount.getAndIncrement();
                }
            });
        });

        return updatedCount.get();
    }

    /**
     * 선택된 데이터들의 재고를 반영한다.
     * <p>
     * TODO : 현재 세트상품과 세트상품이 아닌 데이터들을 구분해서 insert 쿼리를 날리는데 이렇게되면 insert쿼리가 두번이 나누어져서 던져지게됨. 세트상품을 먼저 분해해서 일반 옵션 엔터티로 만들어서 한번에 던지는게 낫지않을까?
     */
    @Transactional
    public Integer actionReflectStock(List<ErpOrderItemDto> itemDtos, Map<String, Object> params) {
        Set<String> optionCodeSet = new HashSet<>();
        List<ErpOrderItemEntity> erpOrderItemEntities = erpOrderItemService.getEntities(itemDtos);
        List<ProductOptionEntity> productOptionEntities;
        AtomicInteger count = new AtomicInteger();

        for (ErpOrderItemEntity r : erpOrderItemEntities) {
            if (!r.getReleaseOptionCode().isEmpty()) {
                optionCodeSet.add(r.getReleaseOptionCode());
            }
        }

        List<ProductOptionGetDto> productOptionGetDtos = productOptionService.searchListByOptionCodes(new ArrayList<>(optionCodeSet));
        productOptionEntities = productOptionGetDtos.stream().map(ProductOptionEntity::toEntity).collect(Collectors.toList());

        // 1. 세트상품 X
        List<ProductOptionEntity> originOptionEntities = productOptionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. 세트상품 O
        List<ProductOptionEntity> parentOptionEntities = productOptionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        String memo = params.get("memo") == null ? "" : params.get("memo").toString();
        count.addAndGet(this.reflectStockUnit(erpOrderItemEntities, originOptionEntities, memo));
        count.addAndGet(this.reflectStockUnitOfPackageOption(erpOrderItemEntities, parentOptionEntities, memo));
        return count.get();
    }

    public int reflectStockUnit(List<ErpOrderItemEntity> erpOrderItemEntities, List<ProductOptionEntity> originOptionEntities, String memo) {
        UUID USER_ID = userService.getUserId();
        List<ProductReleaseEntity> releaseEntities = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        for (ErpOrderItemEntity orderItemEntity : erpOrderItemEntities) {
            originOptionEntities.forEach(optionEntity -> {
                if (optionEntity.getCode().equals(orderItemEntity.getReleaseOptionCode()) && orderItemEntity.getStockReflectYn().equals("n")) {
                    count.getAndIncrement();
                    ProductReleaseEntity releaseEntity = new ProductReleaseEntity();

                    releaseEntity.setId(UUID.randomUUID());
                    releaseEntity.setErpOrderItemId(orderItemEntity.getId());
                    releaseEntity.setReleaseUnit(orderItemEntity.getUnit());
                    releaseEntity.setMemo(memo);
                    releaseEntity.setCreatedAt(CustomDateUtils.getCurrentDateTime());
                    releaseEntity.setCreatedBy(USER_ID);
                    releaseEntity.setProductOptionCid(optionEntity.getCid());
                    releaseEntity.setProductOptionId(optionEntity.getId());

                    orderItemEntity.setStockReflectYn("y");
                    releaseEntities.add(releaseEntity);
                }
            });
        }

        productReleaseService.bulkInsert(releaseEntities);
        return count.get();
    }

    public int reflectStockUnitOfPackageOption(List<ErpOrderItemEntity> erpOrderItemEntities, List<ProductOptionEntity> parentOptionEntities, String memo) {
        UUID USER_ID = userService.getUserId();
        List<ProductReleaseEntity> releaseEntities = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIds = parentOptionEntities.stream().map(ProductOptionEntity::getId).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageProjection.RelatedProductAndOption> optionPackageEntities = optionPackageService.searchBatchByParentOptionIds(parentOptionIds);

        for (ErpOrderItemEntity orderItemEntity : erpOrderItemEntities) {
            parentOptionEntities.forEach(parentOption -> {
                if (parentOption.getCode().equals(orderItemEntity.getReleaseOptionCode()) && orderItemEntity.getStockReflectYn().equals("n")) {
                    optionPackageEntities.forEach(option -> {
                        if (option.getOptionPackage().getParentOptionId().equals(parentOption.getId())) {
                            count.getAndIncrement();

                            ProductReleaseEntity releaseEntity = new ProductReleaseEntity();
                            releaseEntity.setId(UUID.randomUUID());
                            releaseEntity.setErpOrderItemId(orderItemEntity.getId());
                            releaseEntity.setReleaseUnit(orderItemEntity.getUnit() * option.getOptionPackage().getPackageUnit());
                            releaseEntity.setMemo(memo);
                            releaseEntity.setCreatedAt(CustomDateUtils.getCurrentDateTime());
                            releaseEntity.setCreatedBy(USER_ID);
                            releaseEntity.setProductOptionCid(option.getProductOption().getCid());
                            releaseEntity.setProductOptionId(option.getOptionPackage().getOriginOptionId());

                            orderItemEntity.setStockReflectYn("y");
                            releaseEntities.add(releaseEntity);
                        }
                    });
                }
            });
        }

        productReleaseService.bulkInsert(releaseEntities);
        return count.get();
    }

    /**
     * 선택된 데이터들중 이미 재고가 반영된 데이터들을 대상으로 재고 반영을 취소시킨다.
     *
     * @param itemDtos
     * @return
     */
    @Transactional
    public Integer actionCancelStock(List<ErpOrderItemDto> itemDtos) {
        /*
        재고 반영이 완료된 데이터들을 가져온다.
         */
        itemDtos = itemDtos.stream().filter(r -> r.getStockReflectYn().equals("y")).collect(Collectors.toList());
        List<ErpOrderItemEntity> erpOrderItemEntities = erpOrderItemService.getEntities(itemDtos);
        List<UUID> erpOrderItemIds = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();

        /*
        재고에 이미 반영된 데이터들의 재고반영 여부를 업데이트하고, 재고에 이미 반영된 데이터의 아이디 값들을 가져온다.
         */
        for (ErpOrderItemEntity orderItemEntity : erpOrderItemEntities) {
            count.getAndIncrement();
            erpOrderItemIds.add(orderItemEntity.getId());
            orderItemEntity.setStockReflectYn("n");
        }

        /*
        이미 재고가 반영되었던 데이터들의 아이디값을 이용해서 재고 반영 여부에서 해당 데이터들을 삭제한다.
         */
        productReleaseService.deleteByErpOrderItemIds(erpOrderItemIds);
        return count.get();
    }
}
