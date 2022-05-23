package com.piaar_store_manager.server.domain.delivery_ready.naver.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.config.aws.AwsS3Configuration;
import com.piaar_store_manager.server.domain.aws.s3.dto.AwsS3ReqDto;
import com.piaar_store_manager.server.domain.aws.s3.service.AwsS3Service;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemLotteExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.domain.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;
import com.piaar_store_manager.server.domain.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.domain.delivery_ready_file.dto.DeliveryReadyFileDto;
import com.piaar_store_manager.server.domain.delivery_ready_file.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.service.ProductReceiveService;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.service.ProductReleaseService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryReadyNaverBusinessService {
    private final DeliveryReadyNaverService deliveryReadyNaverService;
    private final ProductReleaseService productReleaseService;
    private final ProductReceiveService productReceiveService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;
    private final AwsS3Configuration awsS3Configuration;
    private final AwsS3Service awsS3Service;
    
    @Value("${file.upload-dir}")
    String fileLocation;

    /**
     * <b>Upload Excel File</b>
     * <p>
     * 엑셀 파일을 업로드하여 화면에 출력한다.
     * 
     * @param file : MultipartFile
     * @return List::DeliveryReadyNaverItemDto::
     * @see CustomExcelUtils#getWorkbook
     * @see DeliveryReadyNaverBusinessService#getExcelItemList
     */
    public List<DeliveryReadyNaverItemDto> uploadDeliveryReadyExcelFile(MultipartFile file){
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyNaverItemDto> dtos = this.getExcelItemList(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 업로드된 엑셀파일(네이버 배송준비 엑셀)의 데이터를 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyNaverItemDto::
     */
    private List<DeliveryReadyNaverItemDto> getExcelItemList(Sheet worksheet) {
        List<DeliveryReadyNaverItemDto> dtos = new ArrayList<>();
        Integer NAVER_DATA_START_INDEX = 2;

        for (int i = NAVER_DATA_START_INDEX; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyNaverItemDto dto = DeliveryReadyNaverItemDto.builder().id(UUID.randomUUID())
                    .prodOrderNumber(row.getCell(0).getStringCellValue())
                    .orderNumber(row.getCell(1).getStringCellValue()).salesChannel(row.getCell(7).getStringCellValue())
                    .buyer(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                    .buyerId(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                    .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                    .paymentDate(row.getCell(14) != null ? row.getCell(14).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                    .prodNumber(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                    .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                    .optionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                    .unit((int) row.getCell(20).getNumericCellValue())
                    .orderConfirmationDate(row.getCell(27) != null ? row.getCell(27).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                    .shipmentDueDate(row.getCell(28) != null ? row.getCell(28).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                    .shipmentCostBundleNumber(row.getCell(32) != null ? row.getCell(32).getStringCellValue() : "")
                    .sellerProdCode(row.getCell(37) != null ? row.getCell(37).getStringCellValue() : "")
                    .sellerInnerCode1(row.getCell(38) != null ? row.getCell(38).getStringCellValue() : "")
                    .sellerInnerCode2(row.getCell(39) != null ? row.getCell(39).getStringCellValue() : "")
                    .receiverContact1(row.getCell(40) != null ? row.getCell(40).getStringCellValue() : "")
                    .receiverContact2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "")
                    .destination(row.getCell(42) != null ? row.getCell(42).getStringCellValue() : "")
                    .buyerContact(row.getCell(43) != null ? row.getCell(43).getStringCellValue() : "")
                    .zipCode(row.getCell(44) != null ? row.getCell(44).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "")
                    .releaseArea(row.getCell(46) != null ? row.getCell(46).getStringCellValue() : "")
                    .orderDateTime(row.getCell(56) != null ? row.getCell(56).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                    .piaarMemo1(row.getCell(67) != null ? row.getCell(67).getStringCellValue() : "")
                    .piaarMemo2(row.getCell(68) != null ? row.getCell(68).getStringCellValue() : "")
                    .piaarMemo3(row.getCell(69) != null ? row.getCell(69).getStringCellValue() : "")
                    .piaarMemo4(row.getCell(70) != null ? row.getCell(70).getStringCellValue() : "")
                    .piaarMemo5(row.getCell(71) != null ? row.getCell(71).getStringCellValue() : "")
                    .piaarMemo6(row.getCell(72) != null ? row.getCell(72).getStringCellValue() : "")
                    .piaarMemo7(row.getCell(73) != null ? row.getCell(73).getStringCellValue() : "")
                    .piaarMemo8(row.getCell(74) != null ? row.getCell(74).getStringCellValue() : "")
                    .piaarMemo9(row.getCell(75) != null ? row.getCell(75).getStringCellValue() : "")
                    .piaarMemo10(row.getCell(76) != null ? row.getCell(76).getStringCellValue() : "")
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * <b>S3 Upload Related Method</b>
     * <p>
     * 업로드된 엑셀파일을 S3 저장하고, 파일과 데이터를 DB에 저장한다.
     *
     * @param file : MultipartFile
     * @see DeliveryReadyNaverBusinessService#createFile
     * @see DeliveryReadyNaverBusinessService#createItem
     */
    @Transactional
    public void storeDeliveryReadyExcelFile(MultipartFile file) {
        String newFileName = "[NAVER_delivery_ready]" + UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename();
        String uploadPath = awsS3Configuration.getS3().get("bucket") + "/naver-order";

        // aws s3 저장
        AwsS3ReqDto reqDto = AwsS3ReqDto.builder()
            .uploadPath(uploadPath)
            .fileName(newFileName)
            .file(file)
            .build();
        awsS3Service.putObject(reqDto);

        // 파일 저장
        DeliveryReadyFileDto fileDto = this.createFile(uploadPath, newFileName, (int)file.getSize());
        // 데이터 저장
        this.createItem(file, fileDto);
    }
    
    /**
     * <b>Create Related Method</b>
     * <p>
     * 업로드된 배송준비 엑셀 파일의 정보를 DeliveryReadyFileDto로 생성하고 저장한다. 
     *
     * @param filePath : String
     * @param fileName : String
     * @param fileSize : Integer
     * @return DeliveryReadyFileDto
     * @see DeliveryReadyNaverService#saveAndGetOfFile
     */
    public DeliveryReadyFileDto createFile(String filePath, String fileName, Integer fileSize) {
        UUID userId = userService.getUserId();
        // File data 생성 및 저장
        DeliveryReadyFileDto fileDto = DeliveryReadyFileDto.builder()
            .id(UUID.randomUUID())
            .filePath(filePath)
            .fileName(fileName)
            .fileSize(fileSize)
            .fileExtension(FilenameUtils.getExtension(fileName))
            .createdAt(CustomDateUtils.getCurrentDateTime())
            .createdBy(userId)
            .deleted(false)
            .build();

        DeliveryReadyFileEntity entity = deliveryReadyNaverService.saveAndGetOfFile(DeliveryReadyFileEntity.toEntity(fileDto));
        DeliveryReadyFileDto dto = DeliveryReadyFileDto.toDto(entity);
        return dto;
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 업로드된 배송준비 엑셀 파일의 데이터를 모두 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @see DeliveryReadyNaverBusinessService#getExcelItemList
     * @see DeliveryReadyNaverService#saveAndModifyOfItemList
     */
    public void createItem(MultipartFile file, DeliveryReadyFileDto fileDto) {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyNaverItemDto> dtos = this.getExcelItemList(sheet, fileDto);
        
        // 상품명 > 옵션정보 > 수취인명 순으로 정렬 
        dtos.sort(Comparator.comparing(DeliveryReadyNaverItemDto::getProdName)
                .thenComparing(DeliveryReadyNaverItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyNaverItemDto::getReceiver));

        List<DeliveryReadyNaverItemEntity> entities = dtos.stream().map(dto -> DeliveryReadyNaverItemEntity.toEntity(dto)).collect(Collectors.toList());
        deliveryReadyNaverService.saveAndModifyOfItemList(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복되는 데이터를 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @see DeliveryReadyNaverService#findAllProdOrderNumber
     */
    private List<DeliveryReadyNaverItemDto> getExcelItemList(Sheet worksheet, DeliveryReadyFileDto fileDto) {
        List<DeliveryReadyNaverItemDto> dtos = new ArrayList<>();
        Integer NAVER_DATA_START_INDEX = 2;

        // 상품주문번호를 모두 가져온다.
        Set<String> storedProdOrderNumber = deliveryReadyNaverService.findAllProdOrderNumber();

        for(int i = NAVER_DATA_START_INDEX; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            // 상품주문번호가 중복된다면 다음 데이터로 넘어간다
            String prodOrderNumber = row.getCell(0).getStringCellValue();
            if(!storedProdOrderNumber.add(prodOrderNumber)){
                continue;
            }

            DeliveryReadyNaverItemDto dto = DeliveryReadyNaverItemDto.builder()
                .id(UUID.randomUUID())
                .prodOrderNumber(row.getCell(0).getStringCellValue())
                .orderNumber(row.getCell(1).getStringCellValue())
                .salesChannel(row.getCell(7).getStringCellValue())
                .buyer(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                .buyerId(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                .paymentDate(row.getCell(14) != null ? row.getCell(14).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                .prodNumber(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                .optionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue().strip() : "")
                .unit((int) row.getCell(20).getNumericCellValue())
                .orderConfirmationDate(row.getCell(27) != null ? row.getCell(27).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                .shipmentDueDate(row.getCell(28) != null ? row.getCell(28).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                .shipmentCostBundleNumber(row.getCell(32) != null ? row.getCell(32).getStringCellValue() : "")
                .sellerProdCode(row.getCell(37) != null ? row.getCell(37).getStringCellValue() : "")
                .sellerInnerCode1(row.getCell(38) != null ? row.getCell(38).getStringCellValue() : "")
                .sellerInnerCode2(row.getCell(39) != null ? row.getCell(39).getStringCellValue() : "")
                .receiverContact1(row.getCell(40) != null ? row.getCell(40).getStringCellValue() : "")
                .receiverContact2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "")
                .destination(row.getCell(42) != null ? row.getCell(42).getStringCellValue() : "")
                .buyerContact(row.getCell(43) != null ? row.getCell(43).getStringCellValue() : "")
                .zipCode(row.getCell(44) != null ? row.getCell(44).getStringCellValue() : "")
                .deliveryMessage(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "")
                .releaseArea(row.getCell(46) != null ? row.getCell(46).getStringCellValue() : "")
                .orderDateTime(row.getCell(56) != null ? row.getCell(56).getLocalDateTimeCellValue() : CustomDateUtils.getCurrentDateTime())
                .releaseOptionCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue().strip() : "")
                .piaarMemo1(row.getCell(67) != null ? row.getCell(67).getStringCellValue() : "")
                .piaarMemo2(row.getCell(68) != null ? row.getCell(68).getStringCellValue() : "")
                .piaarMemo3(row.getCell(69) != null ? row.getCell(69).getStringCellValue() : "")
                .piaarMemo4(row.getCell(70) != null ? row.getCell(70).getStringCellValue() : "")
                .piaarMemo5(row.getCell(71) != null ? row.getCell(71).getStringCellValue() : "")
                .piaarMemo6(row.getCell(72) != null ? row.getCell(72).getStringCellValue() : "")
                .piaarMemo7(row.getCell(73) != null ? row.getCell(73).getStringCellValue() : "")
                .piaarMemo8(row.getCell(74) != null ? row.getCell(74).getStringCellValue() : "")
                .piaarMemo9(row.getCell(75) != null ? row.getCell(75).getStringCellValue() : "")
                .piaarMemo10(row.getCell(76) != null ? row.getCell(76).getStringCellValue() : "")
                .released(false)
                .createdAt(fileDto.getCreatedAt())
                .releaseCompleted(false)
                .deliveryReadyFileCid(fileDto.getCid())
                .build();

            dtos.add(dto);
        }
        return dtos;
    }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 미출고 데이터를 조회한다.
     * 조회된 데이터의 출고옵션코드를 확인해 옵션재고수량을 설정한다.
     *
     * @return List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @see DeliveryReadyNaverService#findUnreleasedItemList
     * @see DeliveryReadyNaverBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyNaverItemDto.ViewReqAndRes> getUnreleasedItemList() {
        List<DeliveryReadyNaverItemViewProj> itemViewProj = deliveryReadyNaverService.findUnreleasedItemList();
        List<DeliveryReadyNaverItemDto.ViewReqAndRes> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 선택된 기간의 출고 데이터를 조회한다.
     *
     * @param query : Map[startDate, endDate]
     * @return List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @see DeliveryReadyNaverService#findReleasedItemList
     * @see DeliveryReadyNaverBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyNaverItemDto.ViewReqAndRes> getReleasedItemList(Map<String, Object> query) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);  /* 지정된 startDate 값이 있다면 해당 데이터로 조회, 없다면 1970년을 기준으로 조회 */
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();   /* 지정된 endDate 값이 있다면 해당 데이터로 조회, 없다면 현재시간을 기준으로 조회 */
        
        List<DeliveryReadyNaverItemViewProj> itemViewProj = deliveryReadyNaverService.findReleasedItemList(startDate, endDate);
        List<DeliveryReadyNaverItemDto.ViewReqAndRes> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * ItemViewProj의 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경.
     *
     * @param itemViewProj : List::DeliveryReadyNaverItemViewProj::
     * @return List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @see ProductOptionService#searchListByOptionCodes
     */
    public List<DeliveryReadyNaverItemDto.ViewReqAndRes> changeOptionStockUnit(List<DeliveryReadyNaverItemViewProj> itemViewProj) {
        List<String> optionCodes = itemViewProj.stream().map(r -> r.getDeliveryReadyItem().getReleaseOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByOptionCodes(optionCodes);
        List<DeliveryReadyNaverItemDto.ViewReqAndRes>  itemViewResDto = new ArrayList<>();

        // 등록된 옵션관리코드가 없다면 옵션 재고수량을 설정하지 않고 바로 리턴
        if(optionGetDtos.isEmpty()) {
            itemViewResDto = itemViewProj.stream().map(proj -> DeliveryReadyNaverItemDto.ViewReqAndRes.toDto(proj)).collect(Collectors.toList());
            return itemViewResDto;
        }

        // 옵션 재고수량을 optionGetDtos의 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 update
        itemViewResDto = itemViewProj.stream().map(proj -> {
            DeliveryReadyNaverItemDto.ViewReqAndRes resDto = DeliveryReadyNaverItemDto.ViewReqAndRes.toDto(proj);

            // 출고 옵션코드를 생성하기 전의 데이터들은 getReleaseOptionCode가 null이다.
            if(proj.getDeliveryReadyItem().getReleaseOptionCode() == null) return resDto;

            // 출고 옵션 코드와 동일한 옵션의 재고수량을 변경한다
            optionGetDtos.stream().forEach(option -> {
                if(proj.getDeliveryReadyItem().getReleaseOptionCode().equals(option.getCode())) {
                    resDto.setOptionStockUnit(option.getStockSumUnit());
                }
            });
            return resDto;
        }).collect(Collectors.toList());
        return itemViewResDto;
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see DeliveryReadyNaverService#deleteOneOfItem
     */
    public void deleteOneOfItem(Integer itemCid) {
        deliveryReadyNaverService.deleteOneOfItem(itemCid);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 선택된 데이터를 모두 삭제한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto::
     * @see DeliveryReadyNaverService#deleteListOfItem
     */
    @Transactional
    public void deleteListOfItem(List<DeliveryReadyNaverItemDto> dtos) {
        List<UUID> idList = dtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        deliveryReadyNaverService.deleteListOfItem(idList);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto의 itemId에 대응하는 출고 데이터를 미출고 데이터로 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchOneOfItem
     * @see DeliveryReadyNaverService#saveAndModifyOfItem
     */
    public void updateItemToUnrelease(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchOneOfItem(dto.getCid());
        entity.setReleased(false).setReleasedAt(null);

        deliveryReadyNaverService.saveAndModifyOfItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dtos의 itemId에 대응하는 모든 출고 데이터를 미출고 데이터로 변경한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto::
     * @see DeliveryReadyNaverService#saveAndModifyOfItemList
     */
    public void updateListReleased(List<DeliveryReadyNaverItemDto> dtos, boolean released) {
        List<DeliveryReadyNaverItemEntity> entities = dtos.stream().map(dto -> {
            dto.setReleased(released);
            if(released) {
                dto.setReleasedAt(CustomDateUtils.getCurrentDateTime());
            }else {
                dto.setReleasedAt(null);
            }
            return DeliveryReadyNaverItemEntity.toEntity(dto);
        }).collect(Collectors.toList());

        deliveryReadyNaverService.saveAndModifyOfItemList(entities);
    }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyNaverService#findAllOptionInfo
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchOptionInfoOfItem() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyNaverService.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = optionInfoProjs.stream().map(proj -> DeliveryReadyItemOptionInfoProj.toResDto(proj)).collect(Collectors.toList());
        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto cid에 대응하는 데이터의 옵션관리코드를 수정한다.
     * 옵션관리코드 변경 시 출고옵션코드도 함께 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchOneOfItem
     * @see DeliveryReadyNaverService#saveAndModifyOfItem
     */
    public void updateOptionInfoOfItem(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchOneOfItem(dto.getCid());
        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "")
            .setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

        deliveryReadyNaverService.saveAndModifyOfItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto cid에 대응하는 데이터, 이와 동일한 옵션관리코드를 가진 상품들을 일괄 수정한다.
     * 옵션관리코드를 변경하면서 출고옵션코드도 함께 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchOneOfItem
     * @see DeliveryReadyNaverService#saveAndModifyOfItem
     * @see DeliveryReadyNaverService#findByItems
     * @see DeliveryReadyNaverService#saveAndModifyOfItemList
     */
    public void updateAllOptionInfoOfItem(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchOneOfItem(dto.getCid());
        String optionManagementCode = dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "";
        entity.setOptionManagementCode(optionManagementCode).setReleaseOptionCode(optionManagementCode);
        deliveryReadyNaverService.saveAndModifyOfItem(entity);
        
        // 현재 변경된 상품과 같은 옵션데이터를 가진 모든 item의 옵션관리코드 변경
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverService.findByItems(entity);
        entities.stream().forEach(r -> { r.setOptionManagementCode(entity.getOptionManagementCode()).setReleaseOptionCode(entity.getReleaseOptionCode()); });
        deliveryReadyNaverService.saveAndModifyOfItemList(entities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto cid에 대응하는 데이터의 출고옵션코드를 수정한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchOneOfItem
     * @see DeliveryReadyNaverService#saveAndModifyOfItem
     */
    public void updateReleaseOptionInfoOfItem(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchOneOfItem(dto.getCid());
        entity.setReleaseOptionCode(dto.getReleaseOptionCode() != null ? dto.getReleaseOptionCode() : "");

        deliveryReadyNaverService.saveAndModifyOfItem(entity);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <p>
     * 재고반영 및 재고반영 취소 시 출고완료(release_completed) 값을 설정한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @param reflected : boolean
     * @see DeliveryReadyNaverService#searchDeliveryReadyItemList
     * @see DeliveryReadyNaverService#saveAndModifyOfItemList
     */
    public void updateListReleaseCompleted(List<DeliveryReadyNaverItemDto.ViewReqAndRes> dtos, boolean reflected) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(reflected));
        deliveryReadyNaverService.saveAndModifyOfItemList(entities);
    }
    
    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * [네이버 배송준비 -> 한산 발주서] 다운로드
     * 다운로드 시 중복데이터(주문번호 + 받는사람 + 상품명(피아르 출고상품명) + 상품상세(피아르 출고옵션명)) 처리 및 셀 색상을 지정한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeItemToHansan(List<DeliveryReadyNaverItemDto.ViewReqAndRes> dtos) {
        List<DeliveryReadyItemHansanExcelFormDto> excelFormDtos = new ArrayList<>();

        // 받는사람 > 주문번호 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing((DeliveryReadyNaverItemDto.ViewReqAndRes dto) -> dto.getDeliveryReadyItem().getReceiver())
                                .thenComparing((DeliveryReadyNaverItemDto.ViewReqAndRes dto) -> dto.getDeliveryReadyItem().getOrderNumber())
                                .thenComparing((DeliveryReadyNaverItemDto.ViewReqAndRes dto) -> dto.getProdManagementName())
                                .thenComparing((DeliveryReadyNaverItemDto.ViewReqAndRes dto) -> dto.getOptionManagementName()));

        Set<String> optionSet = new HashSet<>();        // 받는사람 + 주소 + 상품명 + 상품상세

        for(int i = 0; i < dtos.size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(dtos.get(i).getDeliveryReadyItem().getReceiver());
            sb.append(dtos.get(i).getDeliveryReadyItem().getDestination());
            sb.append(dtos.get(i).getProdManagementName());
            sb.append(dtos.get(i).getOptionManagementName());

            StringBuilder receiverSb = new StringBuilder();
            receiverSb.append(dtos.get(i).getDeliveryReadyItem().getReceiver());
            receiverSb.append(dtos.get(i).getDeliveryReadyItem().getReceiverContact1());
            receiverSb.append(dtos.get(i).getDeliveryReadyItem().getDestination());

            String resultStr = sb.toString();
            String receiverStr = receiverSb.toString();
            int prevOrderIdx = excelFormDtos.size()-1;   // 추가되는 데이터 리스트의 마지막 index

            DeliveryReadyItemHansanExcelFormDto currentProd = DeliveryReadyItemHansanExcelFormDto.toFormDto(dtos.get(i));

            // 받는사람 + 주소 + 상품명 + 상품상세 : 중복인 경우
            if(!optionSet.add(resultStr)){
                DeliveryReadyItemHansanExcelFormDto prevProd = excelFormDtos.get(prevOrderIdx);
                
                excelFormDtos.get(prevOrderIdx).setUnit(prevProd.getUnit() + currentProd.getUnit());     // 중복데이터의 수량을 더한다
                excelFormDtos.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getProdOrderNumber() + "/" + currentProd.getProdOrderNumber());     // 총 상품번호 수정
            }else{
                // 받는사람 + 번호 + 주소 : 중복인 경우
                if(!optionSet.add(receiverStr)){
                    excelFormDtos.get(prevOrderIdx).setDuplication(true);
                    currentProd.setDuplication(true);
                }
                excelFormDtos.add(currentProd);
            }
        }

        return excelFormDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * [네이버 배송준비 -> 롯데 발주서] 다운로드
     * 다운로드 시 중복데이터(받는사람 + 주소 + 상품명 + 상품상세), (받는사람 + 연락처 + 주소) 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeItemToLotte(List<DeliveryReadyNaverItemDto.ViewReqAndRes> viewDtos) {
        List<DeliveryReadyItemLotteExcelFormDto> dtos = viewDtos.stream().map(dto -> DeliveryReadyItemLotteExcelFormDto.toFormDto(dto)).collect(Collectors.toList());
        List<DeliveryReadyItemLotteExcelFormDto> excelFormDtos = new ArrayList<>();
        List<DeliveryReadyItemLotteExcelFormDto> resultList = new ArrayList<>();

        // 받는사람 > 주소 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing(DeliveryReadyItemLotteExcelFormDto::getReceiver)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getDestination)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getProdName1)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getOptionInfo1));

        Set<String> optionSet = new HashSet<>();    // 받는사람 + 주소 + 상품명 + 상품상세 Set

        for (int i = 0; i < dtos.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(dtos.get(i).getReceiver());
            sb.append(dtos.get(i).getDestination());
            sb.append(dtos.get(i).getProdName1());
            sb.append(dtos.get(i).getOptionInfo1());

            String resultStr = sb.toString();
            int prevOrderIdx = excelFormDtos.size() - 1;     // 추가되는 데이터 리스트의 마지막 index

            // 받는사람 + 주소 + 상품명 + 상품상세 : 중복인 경우
            if (!optionSet.add(resultStr)) {
                DeliveryReadyItemLotteExcelFormDto prevProd = excelFormDtos.get(prevOrderIdx);
                DeliveryReadyItemLotteExcelFormDto currentProd = dtos.get(i);
                
                prevProd.setUnit(prevProd.getUnit() + currentProd.getUnit());     // 중복데이터의 수량을 더한다
                excelFormDtos.get(prevOrderIdx).setAllProdInfo(prevProd.getProdName1() + " [" + prevProd.getOptionInfo1() + "-" + prevProd.getUnit() + "]");
                prevProd.setAllProdOrderNumber(prevProd.getAllProdOrderNumber() + "/" + currentProd.getProdOrderNumber());
                excelFormDtos.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getAllProdOrderNumber());   // 총 상품번호 수정
            } else {
                excelFormDtos.add(dtos.get(i));
            }
        }

        String prevProdName = "";
         
        for (int i = 0; i < excelFormDtos.size(); i++) {
            StringBuilder receiverSb = new StringBuilder();
            receiverSb.append(excelFormDtos.get(i).getReceiver());
            receiverSb.append(excelFormDtos.get(i).getReceiverContact1());
            receiverSb.append(excelFormDtos.get(i).getDestination());
            
            String receiverStr = receiverSb.toString();
            int prevOrderIdx = resultList.size() - 1;     // 추가되는 데이터 리스트의 마지막 index

            // 받는사람 + 연락처 + 주소 + 상품상세 : 중복이 아니면서
            // 받는사람 + 연락처 + 주소 : 중복인 경우
            if (!optionSet.add(receiverStr)) {
                DeliveryReadyItemLotteExcelFormDto prevProd = resultList.get(prevOrderIdx);
                DeliveryReadyItemLotteExcelFormDto currentProd = excelFormDtos.get(i);

                // 상품명이 동일한 경우, 중복처리 되었을 때 바로 이전의 상품명과 동일한 경우.
                if(prevProd.getProdName1().equals(currentProd.getProdName1()) || prevProdName.equals(currentProd.getProdName1())){
                    prevProdName = "";
                    resultList.get(prevOrderIdx).setAllProdInfo(prevProd.getAllProdInfo() + " | " + "[" + currentProd.getOptionInfo1() + "-" + currentProd.getUnit() + "]");
                }else{      // 상품명이 동일하지 않은 경우
                    prevProdName = currentProd.getProdName1();
                    resultList.get(prevOrderIdx).setAllProdInfo(prevProd.getAllProdInfo() + " | " + currentProd.getAllProdInfo());
                }
                resultList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getAllProdOrderNumber() + "/" + currentProd.getAllProdOrderNumber());    // 총 상품번호 수정
            } else {
                prevProdName = "";
                resultList.add(excelFormDtos.get(i));
            }
        }
        return resultList;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 옵션관리 코드와 대응하는 상품옵션 데이터를 조회한다.
     *
     * @param dto : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @return List::ProductOptionEntity::
     * @see ProductOptionService#findAllByCode
     */
    public List<ProductOptionEntity> getOptionByCode(List<DeliveryReadyNaverItemDto.ViewReqAndRes> dtos) {
        List<String> managementCodes = dtos.stream().map(r -> r.getDeliveryReadyItem().getReleaseOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByOptionCodes(managementCodes);
        return optionGetDtos.stream().map(dto -> ProductOptionEntity.toEntity(dto)).collect(Collectors.toList());
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 출고(재고 반영) 데이터를 생성하여 재고에 반영한다.
     * 기본 옵션 상품과 세트 구성 상품을 분리하여 재고반영 처리한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @see DeliveryReadyNaveBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyNaveBusinessService#getOptionByCode
     * @see DeliveryReadyNaveBusinessService#reflectStockUnit
     * @see DeliveryReadyNaveBusinessService#reflectStockUnitOfPackageOption
     */
    @Transactional
    public void releaseListStockUnit(List<DeliveryReadyNaverItemDto.ViewReqAndRes> dtos) {
        // 재고반영이 선행되지 않은 데이터들만 재고 반영
        List<DeliveryReadyNaverItemDto.ViewReqAndRes> unreleasedDtos = dtos.stream().filter(dto -> ((!dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null))).collect(Collectors.toList());
        this.updateListReleaseCompleted(unreleasedDtos, true);
        
        // 출고 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(unreleasedDtos);
        
        // 세트상품 재고반영
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());
        this.reflectStockUnit(unreleasedDtos, originOptionEntities);

        if(parentOptionEntities.size() > 0) {
            this.reflectStockUnitOfPackageOption(unreleasedDtos, parentOptionEntities);
        }
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * 기본 옵션 상품의 출고(재고 반영) 데이터를 생성한다.
     * 
     * @param unreleasedDtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @param originOptionEntities : List::ProductOptionEntity::
     * @see ProductReceiveService#saveListAndModify
     */
    public void reflectStockUnit(List<DeliveryReadyNaverItemDto.ViewReqAndRes> unreleasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        // 1. 세트상품이 아닌 애들 재고반영
        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
        // 출고데이터 설정 및 생성
        unreleasedDtos.stream().forEach(dto -> {
            originOptionEntities.stream().forEach(option -> {
                if (dto.getDeliveryReadyItem().getReleaseOptionCode().equals(option.getCode())) {
                    ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                            .id(UUID.randomUUID())
                            .releaseUnit(dto.getDeliveryReadyItem().getUnit())
                            .memo(dto.getReleaseMemo())
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .productOptionCid(option.getCid())
                            .productOptionId(option.getId())
                            .build();

                    productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
                }
            });
        });
        productReleaseService.saveListAndModify(productReleaseEntities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * 세트 옵션 상품의 출고(재고 반영) 데이터를 생성한다.
     * 
     * @param unreleasedDtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @param parentOptionEntities : List::ProductOptionEntity::
     * @see OptionPackageService#searchListByParentOptionIdList
     * @see ProductReceiveService#saveListAndModify
     */
    public void reflectStockUnitOfPackageOption(List<DeliveryReadyNaverItemDto.ViewReqAndRes> unreleasedDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
        unreleasedDtos.forEach(dto -> {
            parentOptionEntities.forEach(parentOption -> {
                if (dto.getDeliveryReadyItem().getReleaseOptionCode().equals(parentOption.getCode())) {
                    optionPackageEntities.forEach(option -> {
                        if(option.getParentOptionId().equals(parentOption.getId())) {
                            ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                                .id(UUID.randomUUID())
                                .releaseUnit(option.getPackageUnit() * dto.getDeliveryReadyItem().getUnit())
                                .memo(dto.getReleaseMemo())
                                .createdAt(CustomDateUtils.getCurrentDateTime())
                                .createdBy(USER_ID)
                                .productOptionCid(option.getOriginOptionCid())
                                .productOptionId(option.getOriginOptionId())
                                .build();
                            
                            productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
                        }
                    });
                }
            });
        });
        productReleaseService.saveListAndModify(productReleaseEntities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Cancel the stock unit reflection of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 입고(재고 반영 취소) 데이터를 생성하여 재고에 반영한다.
     * 기본 옵션 상품과 세트 구성 상품을 분리하여 재고반영 취소 처리한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @see DeliveryReadyNaverBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyNaverBusinessService#getOptionByCode
     * @see DeliveryReadyNaverBusinessService#cancelReflectedStockUnit
     * @see DeliveryReadyNaverBusinessService#cancelReflectedStockUnitOfPackageOption
     */
    @Transactional
    public void cancelReleaseListStockUnit(List<DeliveryReadyNaverItemDto.ViewReqAndRes> dtos) {
        // 재고 반영이 선행된 데이터들만 재고 반영
        List<DeliveryReadyNaverItemDto.ViewReqAndRes> releasedDtos = dtos.stream().filter(dto -> (dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null)).collect(Collectors.toList());
        this.updateListReleaseCompleted(releasedDtos, false);

        // 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(releasedDtos);
        
        // 세트상품 재고반영 취소
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        this.cancelReflectedStockUnit(releasedDtos, originOptionEntities);

        if(parentOptionEntities.size() > 0) {
            this.cancelReflectedStockUnitOfPackageOption(releasedDtos, parentOptionEntities);
        }
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Cancel the stock unit reflection of product options.</b>
     * <p>
     * 기본 옵션 상품의 입고(재고 반영 취소) 데이터를 생성한다.
     * 
     * @param releasedDtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @param originOptionEntities : List::ProductOptionEntity::
     * @see ProductReceiveService#saveListAndModify
     */
    public void cancelReflectedStockUnit(List<DeliveryReadyNaverItemDto.ViewReqAndRes> releasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        // 출고 취소데이터 설정 및 생성
        releasedDtos.forEach(dto -> {
            originOptionEntities.forEach(option -> {
                if (dto.getDeliveryReadyItem().getReleaseOptionCode().equals(option.getCode())) {
                    ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                            .id(UUID.randomUUID())
                            .receiveUnit(dto.getDeliveryReadyItem().getUnit())
                            .memo(dto.getReceiveMemo())
                            .createdAt(CustomDateUtils.getCurrentDateTime())
                            .createdBy(USER_ID)
                            .productOptionCid(option.getCid())
                            .build();

                    productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
                }
            });
        });
        productReceiveService.saveListAndModify(productReceiveEntities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Cancel the stock unit reflection of product options.</b>
     * <p>
     * 세트 옵션 상품의 입고(재고 반영 취소) 데이터를 생성한다.
     * 
     * @param releasedDtos : List::DeliveryReadyNaverItemDto.ViewReqAndRes::
     * @param parentOptionEntities : List::ProductOptionEntity::
     * @see OptionPackageService#searchListByParentOptionIdList
     * @see ProductReceiveService#saveListAndModify
     */
    public void cancelReflectedStockUnitOfPackageOption(List<DeliveryReadyNaverItemDto.ViewReqAndRes> releasedDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        releasedDtos.forEach(dto -> {
            parentOptionEntities.forEach(parentOption -> {
                if (dto.getDeliveryReadyItem().getReleaseOptionCode().equals(parentOption.getCode())) {
                    optionPackageEntities.forEach(option -> {
                        if(option.getParentOptionId().equals(parentOption.getId())) {
                            ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.builder()
                                .id(UUID.randomUUID())
                                .receiveUnit(option.getPackageUnit() * dto.getDeliveryReadyItem().getUnit())
                                .memo(dto.getReceiveMemo())
                                .createdAt(CustomDateUtils.getCurrentDateTime())
                                .createdBy(USER_ID)
                                .productOptionCid(option.getOriginOptionCid())
                                .build();
                            
                            productReceiveEntities.add(ProductReceiveEntity.toEntity(receiveGetDto));
                        }
                    });
                }
            });
        });
        productReceiveService.saveListAndModify(productReceiveEntities);
    }
}
