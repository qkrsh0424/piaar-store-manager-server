package com.piaar_store_manager.server.domain.delivery_ready.coupang.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import com.piaar_store_manager.server.domain.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;
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
public class DeliveryReadyCoupangBusinessService {
    private final DeliveryReadyCoupangService deliveryReadyCoupangService;
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
     * @return List::DeliveryReadyCoupangItemDto::
     * @see CustomExcelUtils#getWorkbook
     * @see DeliveryReadyCoupangBusinessService#getExcelItemList
     */
    public List<DeliveryReadyCoupangItemDto> uploadDeliveryReadyExcelFile(MultipartFile file) throws ParseException {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyCoupangItemDto> dtos = this.getExcelItemList(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 업로드된 엑셀파일(쿠팡 배송준비 엑셀)의 데이터를 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyCoupangItemDto::
     * @throws ParseException
     */
    private List<DeliveryReadyCoupangItemDto> getExcelItemList(Sheet worksheet) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;

            String prodOrderNumber = null;
            if((row.getCell(2) != null) && (row.getCell(13) != null) && (row.getCell(14) != null)){
                prodOrderNumber = row.getCell(2).getStringCellValue() + "|" + row.getCell(13).getStringCellValue() + "|" + row.getCell(14).getStringCellValue();
            }

            Date date = row.getCell(7) != null ? dateFormat.parse(row.getCell(7).getStringCellValue()) : null;
            LocalDateTime shipmentDueDate = date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

            date = row.getCell(9) != null ? dateFormat2.parse(row.getCell(9).getStringCellValue()) : null;
            LocalDateTime orderDateTime = date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

            DeliveryReadyCoupangItemDto dto = DeliveryReadyCoupangItemDto.builder().id(UUID.randomUUID())
                    .prodOrderNumber(prodOrderNumber != null ? prodOrderNumber : "")
                    .orderNumber(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                    .buyer(row.getCell(24) != null ? row.getCell(24).getStringCellValue() : "")
                    .receiver(row.getCell(26) != null ? row.getCell(26).getStringCellValue() : "")
                    .prodNumber(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                    .prodName(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                    .prodExposureName(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                    .optionInfo(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                    .optionManagementCode(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .coupangOptionId(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                    .unit(Integer.parseInt(row.getCell(22) != null ? row.getCell(22).getStringCellValue() : ""))
                    .shipmentDueDate(shipmentDueDate)
                    .shipmentCostBundleNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                    .receiverContact1(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                    .destination(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                    .buyerContact(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                    .zipCode(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                    .orderDateTime(orderDateTime)
                    .piaarMemo1(row.getCell(40) != null ? row.getCell(40).getStringCellValue() : "")
                    .piaarMemo2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "")
                    .piaarMemo3(row.getCell(42) != null ? row.getCell(42).getStringCellValue() : "")
                    .piaarMemo4(row.getCell(43) != null ? row.getCell(43).getStringCellValue() : "")
                    .piaarMemo5(row.getCell(44) != null ? row.getCell(44).getStringCellValue() : "")
                    .piaarMemo6(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "")
                    .piaarMemo7(row.getCell(46) != null ? row.getCell(46).getStringCellValue() : "")
                    .piaarMemo8(row.getCell(47) != null ? row.getCell(47).getStringCellValue() : "")
                    .piaarMemo9(row.getCell(48) != null ? row.getCell(48).getStringCellValue() : "")
                    .piaarMemo10(row.getCell(49) != null ? row.getCell(49).getStringCellValue() : "")
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * <b>S3 Upload & DB Insert Related Method</b>
     * <p>
     * 업로드된 엑셀파일을 S3 저장하고, 파일과 데이터를 DB에 저장한다.
     *
     * @param file : MultipartFile
     * @throws ParseException
     * @see DeliveryReadyCoupangBusinessService#createFile
     * @see DeliveryReadyCoupangBusinessService#createItem
     */
    @Transactional
    public void storeDeliveryReadyExcelFile(MultipartFile file) throws ParseException {
        String fileName = file.getOriginalFilename();
        String newFileName = "[COUPANG_delivery_ready]" + UUID.randomUUID().toString().replaceAll("-", "") + fileName;
        String uploadPath = awsS3Configuration.getS3().get("bucket") + "/coupang-order";

        // aws s3 저장
        AwsS3ReqDto reqDto = AwsS3ReqDto.builder()
            .uploadPath(uploadPath)
            .fileName(fileName)
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
     * @see DeliveryReadyCoupangService#saveAndGetOfFile
     */
    public DeliveryReadyFileDto createFile(String filePath, String fileName, Integer fileSize) {
        UUID USER_ID = userService.getUserId();

        // File data 생성 및 저장
        DeliveryReadyFileDto fileDto = DeliveryReadyFileDto.builder()
            .id(UUID.randomUUID())
            .filePath(filePath)
            .fileName(fileName)
            .fileSize(fileSize)
            .fileExtension(FilenameUtils.getExtension(fileName))
            .createdAt(CustomDateUtils.getCurrentDateTime())
            .createdBy(USER_ID)
            .deleted(false)
            .build();

        DeliveryReadyFileEntity entity = deliveryReadyCoupangService.saveAndGetOfFile(DeliveryReadyFileEntity.toEntity(fileDto));
        DeliveryReadyFileDto dto = DeliveryReadyFileDto.toDto(entity);
        return dto;
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 업로드된 배송준비 엑셀 파일 데이터들의 정보를 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @throws ParseException
     * @see DeliveryReadyCoupangBusinessService#getExcelItemList
     * @see DeliveryReadyCoupangService#saveAndModifyOfItemList
     */
    public void createItem(MultipartFile file, DeliveryReadyFileDto fileDto) throws ParseException {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyCoupangItemDto> dtos = this.getExcelItemList(sheet, fileDto);
        
        dtos.sort(Comparator.comparing(DeliveryReadyCoupangItemDto::getProdName)
                .thenComparing(DeliveryReadyCoupangItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyCoupangItemDto::getReceiver));

        List<DeliveryReadyCoupangItemEntity> entities = dtos.stream().map(dto -> DeliveryReadyCoupangItemEntity.toEntity(dto)).collect(Collectors.toList());
        deliveryReadyCoupangService.saveAndModifyOfItemList(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복되는 데이터를 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @return List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangService#findAllProdOrderNumber
     */
    private List<DeliveryReadyCoupangItemDto> getExcelItemList(Sheet worksheet, DeliveryReadyFileDto fileDto) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();
        Integer COUPANG_DATA_START_INDEX = 1;
        
        // 상품주문번호를 모두 가져온다.
        Set<String> storedProdOrderNumber = deliveryReadyCoupangService.findAllProdOrderNumber();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        
        for(int i = COUPANG_DATA_START_INDEX; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;

            String prodOrderNumber = null;
            if((row.getCell(2) != null) && (row.getCell(13) != null) && (row.getCell(14) != null)){
                prodOrderNumber = row.getCell(2).getStringCellValue() + "|" + row.getCell(13).getStringCellValue() + "|" + row.getCell(14).getStringCellValue();
            }

            // 상품주문번호가 중복되지 않는다면 데이터를 생성한다.
            if(!storedProdOrderNumber.add(prodOrderNumber)){
                continue;
            }

            Date date = row.getCell(7) != null ? dateFormat.parse(row.getCell(7).getStringCellValue()) : null;
            LocalDateTime shipmentDueDate = date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

            date = row.getCell(9) != null ? dateFormat2.parse(row.getCell(9).getStringCellValue()) : null;
            LocalDateTime orderDateTime = date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;

            DeliveryReadyCoupangItemDto dto = DeliveryReadyCoupangItemDto.builder().id(UUID.randomUUID())
                .prodOrderNumber(prodOrderNumber != null ? prodOrderNumber : "")
                .orderNumber(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                .buyer(row.getCell(24) != null ? row.getCell(24).getStringCellValue() : "")
                .receiver(row.getCell(26) != null ? row.getCell(26).getStringCellValue() : "")
                .prodNumber(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                .prodName(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                .prodExposureName(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                .optionInfo(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                .optionManagementCode(row.getCell(16) != null ? row.getCell(16).getStringCellValue().strip() : "")
                .coupangOptionId(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                .unit(Integer.parseInt(row.getCell(22) != null ? row.getCell(22).getStringCellValue() : ""))
                .shipmentDueDate(shipmentDueDate)
                .shipmentCostBundleNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                .receiverContact1(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                .destination(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                .buyerContact(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                .zipCode(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                .deliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                .orderDateTime(orderDateTime)
                .releaseOptionCode(row.getCell(16) != null ? row.getCell(16).getStringCellValue().strip() : "")
                .piaarMemo1(row.getCell(40) != null ? row.getCell(40).getStringCellValue() : "")
                .piaarMemo2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "")
                .piaarMemo3(row.getCell(42) != null ? row.getCell(42).getStringCellValue() : "")
                .piaarMemo4(row.getCell(43) != null ? row.getCell(43).getStringCellValue() : "")
                .piaarMemo5(row.getCell(44) != null ? row.getCell(44).getStringCellValue() : "")
                .piaarMemo6(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "")
                .piaarMemo7(row.getCell(46) != null ? row.getCell(46).getStringCellValue() : "")
                .piaarMemo8(row.getCell(47) != null ? row.getCell(47).getStringCellValue() : "")
                .piaarMemo9(row.getCell(48) != null ? row.getCell(48).getStringCellValue() : "")
                .piaarMemo10(row.getCell(49) != null ? row.getCell(49).getStringCellValue() : "")
                .released(false)
                .createdAt(CustomDateUtils.getCurrentDateTime())
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
     * @return List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @see DeliveryReadyCoupangService#findUnreleasedItemList
     * @see DeliveryReadyCoupangBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyCoupangItemDto.ViewReqAndRes> getUnreleasedItemList() {
        List<DeliveryReadyCoupangItemViewProj> itemViewProj = deliveryReadyCoupangService.findUnreleasedItemList();
        List<DeliveryReadyCoupangItemDto.ViewReqAndRes> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 선택된 기간의 출고 데이터를 조회한다.
     *
     * @param query : Map[startDate, endDate]
     * @return List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @throws ParseException
     * @see DeliveryReadyCoupangService#findReleasedItemList
     * @see DeliveryReadyCoupangBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyCoupangItemDto.ViewReqAndRes> getReleasedItemList(Map<String, Object> query) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = query.get("startDate") != null ? LocalDateTime.parse(query.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime endDate = query.get("endDate") != null ? LocalDateTime.parse(query.get("endDate").toString(), formatter) : LocalDateTime.now();

        List<DeliveryReadyCoupangItemViewProj> itemViewProj = deliveryReadyCoupangService.findReleasedItemList(startDate, endDate);
        List<DeliveryReadyCoupangItemDto.ViewReqAndRes> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * ItemViewProj의 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경.
     *
     * @param itemViewProj : List::DeliveryReadyCoupangItemViewProj::
     * @return List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @see ProductOptionService#searchListByOptionCodes
     */
    public List<DeliveryReadyCoupangItemDto.ViewReqAndRes> changeOptionStockUnit(List<DeliveryReadyCoupangItemViewProj> itemViewProj) {
        List<String> optionCodes = itemViewProj.stream().map(r -> r.getDeliveryReadyItem().getReleaseOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByOptionCodes(optionCodes);
        List<DeliveryReadyCoupangItemDto.ViewReqAndRes> itemViewResDto = new ArrayList<>();

        if(optionGetDtos.isEmpty()) {
            itemViewResDto = itemViewProj.stream().map(proj -> DeliveryReadyCoupangItemDto.ViewReqAndRes.toDto(proj)).collect(Collectors.toList());
            return itemViewResDto;
        }

        // 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경.
        itemViewResDto = itemViewProj.stream().map(proj -> {
            DeliveryReadyCoupangItemDto.ViewReqAndRes resDto = DeliveryReadyCoupangItemDto.ViewReqAndRes.toDto(proj);
            
            // 출고 옵션코드를 생성하기 전의 데이터들은 getReleaseOptionCode가 null이다.
            if(proj.getDeliveryReadyItem().getReleaseOptionCode() == null) return resDto;

            // 옵션 코드와 동일한 상품의 재고수량을 변경한다
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
     * @see DeliveryReadyCoupangService#deleteOneOfItem
     */
    public void deleteOneOfItem(Integer itemCid) {
        deliveryReadyCoupangService.deleteOneOfItem(itemCid);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 선택된 데이터를 모두 삭제한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangService#deleteListOfItem
     */
    @Transactional
    public void deleteListOfItem(List<DeliveryReadyCoupangItemDto> dtos) {
        List<UUID> idList = dtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        deliveryReadyCoupangService.deleteListOfItem(idList);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto의 itemId에 대응하는 출고 데이터를 미출고 데이터로 변경한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchOneOfItem
     * @see DeliveryReadyCoupangService#saveAndModifyOfItem
     */
    @Transactional
    public void updateItemToUnrelease(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchOneOfItem(dto.getCid());
        entity.setReleased(false).setReleasedAt(null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dtos의 itemId에 대응하는 데이터의 출고 여부를 변경한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto::
     * @param released : boolean
     * @see DeliveryReadyCoupangService#searchListById
     */
    @Transactional
    public void updateListReleased(List<DeliveryReadyCoupangItemDto> dtos, boolean released) {
        List<UUID> idList = dtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.searchListById(idList);

        entities.forEach(entity -> {
            entity.setReleased(released);
            if(released) {
                entity.setReleasedAt(CustomDateUtils.getCurrentDateTime());
            } else {
                entity.setReleasedAt(null);
            }
        });
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyCoupangService#findAllOptionInfo
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchOptionInfoOfItem() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyCoupangService.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = optionInfoProjs.stream().map(proj -> DeliveryReadyItemOptionInfoProj.toResDto(proj)).collect(Collectors.toList());
        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto cid에 대응하는 데이터의 옵션관리코드를 수정한다.
     * 옵션관리코드 변경 시 출고옵션코드도 함께 변경한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchOneOfItem
     */
    @Transactional
    public void updateOptionInfoOfItem(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchOneOfItem(dto.getCid());
        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "")
            .setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto cid에 대응하는 데이터의 출고옵션코드를 수정한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchOneOfItem
     */
    @Transactional
    public void updateReleaseOptionInfoOfItem(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchOneOfItem(dto.getCid());
        entity.setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getReleaseOptionCode() : "");
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * dto cid에 대응하는 데이터, 이와 동일한 옵션관리코드를 가진 상품들을 일괄 수정한다.
     * 옵션관리코드를 변경하면서 출고옵션코드도 함께 변경한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchOneOfItem
     * @see DeliveryReadyCoupangService#findByItems
     */
    @Transactional
    public void updateAllOptionInfoOfItem(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchOneOfItem(dto.getCid());
        String optionManagementCode = dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "";
        entity.setOptionManagementCode(optionManagementCode).setReleaseOptionCode(optionManagementCode);

        // 현재 변경된 상품과 같은 옵션데이터를 가진 모든 item의 옵션관리코드 변경
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.findByItems(entity);
        entities.stream().forEach(r -> { r.setOptionManagementCode(entity.getOptionManagementCode()).setReleaseOptionCode(entity.getReleaseOptionCode()); });
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <p>
     * 재고반영 및 재고반영 취소 시 출고완료(release_completed) 값을 설정한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @param reflected : boolean
     * @see DeliveryReadyCoupangService#searchListById
     */
    @Transactional
    public void updateListReleaseCompleted(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> dtos, boolean reflected) {
        List<UUID> idList = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getId()).collect(Collectors.toList());
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.searchListById(idList);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(reflected));
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * [쿠팡 배송준비 -> 한산 발주서] 다운로드
     * 다운로드 시 중복데이터(주문번호 + 받는사람 + 상품명(피아르 출고상품명) + 상품상세(피아르 출고옵션명)) 처리 및 셀 색상을 지정한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeItemToHansan(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> dtos) {
        List<DeliveryReadyItemHansanExcelFormDto> excelFormDtos = new ArrayList<>();

        // 받는사람 > 주문번호 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing((DeliveryReadyCoupangItemDto.ViewReqAndRes dto) -> dto.getDeliveryReadyItem().getReceiver())
                                .thenComparing((DeliveryReadyCoupangItemDto.ViewReqAndRes dto) -> dto.getDeliveryReadyItem().getOrderNumber())
                                .thenComparing((DeliveryReadyCoupangItemDto.ViewReqAndRes dto) -> dto.getProdManagementName())
                                .thenComparing((DeliveryReadyCoupangItemDto.ViewReqAndRes dto) -> dto.getOptionManagementName()));

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
     * [쿠팡 배송준비 -> 롯데 발주서] 다운로드
     * 다운로드 시 중복데이터(받는사람 + 주소 + 상품명 + 상품상세), (받는사람 + 연락처 + 주소) 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeItemToLotte(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> viewDtos) {
        List<DeliveryReadyItemLotteExcelFormDto> dtos = viewDtos.stream().map(dto -> DeliveryReadyItemLotteExcelFormDto.toFormDto(dto)).collect(Collectors.toList());
        List<DeliveryReadyItemLotteExcelFormDto> excelFormDtos = new ArrayList<>();
        List<DeliveryReadyItemLotteExcelFormDto> resultList = new ArrayList<>();

        // 받는사람 > 주소 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing(DeliveryReadyItemLotteExcelFormDto::getReceiver)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getDestination)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getProdName1)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getOptionInfo1));

        Set<String> optionSet = new HashSet<>(); // 받는사람 + 주소 + 상품명 + 상품상세

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
     * @param dtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @return List::ProductOptionEntity::
     * @see ProductOptionService#searchListByOptionCodes
     */
    public List<ProductOptionEntity> getOptionByCode(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> dtos) {
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
     * @param dtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @see DeliveryReadyCoupangBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyCoupangBusinessService#getOptionByCode
     * @see DeliveryReadyCoupangBusinessService#reflectStockUnit
     * @see DeliveryReadyCoupangBusinessService#reflectStockUnitOfPackageOption
     */
    @Transactional
    public void releaseListStockUnit(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> dtos) {
        // 재고반영이 선행되지 않은 데이터들만 재고 반영
        List<DeliveryReadyCoupangItemDto.ViewReqAndRes> unreleasedDtos = dtos.stream().filter(dto -> ((!dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null))).collect(Collectors.toList());
        this.updateListReleaseCompleted(unreleasedDtos, true);
        
        // 출고 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(unreleasedDtos);

        // 세트상품 재고반영
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());
        this.reflectStockUnit(unreleasedDtos, originOptionEntities);

        if (parentOptionEntities.size() > 0) {
            this.reflectStockUnitOfPackageOption(unreleasedDtos, parentOptionEntities);
        }
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * 기본 옵션 상품의 출고(재고 반영) 데이터를 생성한다.
     * 
     * @param unreleasedDtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @param originOptionEntities : List::ProductOptionEntity::
     * @see ProductReceiveService#saveListAndModify
     */
    @Transactional
    public void reflectStockUnit(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> unreleasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        // 1. 세트상품이 아닌 애들 재고반영
        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
        // 출고데이터 설정 및 생성
        unreleasedDtos.forEach(dto -> {
            originOptionEntities.forEach(option -> {
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
     * @param unreleasedDtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @param parentOptionEntities : List::ProductOptionEntity::
     * @see OptionPackageService#searchListByParentOptionIdList
     * @see ProductReceiveService#saveListAndModify
     */
    @Transactional
    public void reflectStockUnitOfPackageOption(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> unreleasedDtos, List<ProductOptionEntity> parentOptionEntities) {
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
     * @see DeliveryReadyCoupangBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyCoupangBusinessService#getOptionByCode
     * @see DeliveryReadyCoupangBusinessService#cancelReflectedStockUnit
     * @see DeliveryReadyCoupangBusinessService#cancelReflectedStockUnitOfPackageOption
     */
    @Transactional
    public void cancelReleaseListStockUnit(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> dtos) {
        // 재고 반영이 선행된 데이터들만 재고 반영
        List<DeliveryReadyCoupangItemDto.ViewReqAndRes> releasedDtos = dtos.stream().filter(dto -> (dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null)).collect(Collectors.toList());
        this.updateListReleaseCompleted(releasedDtos, false);

        // 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(releasedDtos);
        // 1. 세트상품 X
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. 세트상품 O
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
     * @param releasedDtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @param originOptionEntities : List::ProductOptionEntity::
     * @see ProductReceiveService#saveListAndModify
     */
    @Transactional
    public void cancelReflectedStockUnit(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> releasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        // 1. 세트상품이 아닌 애들 재고반영
        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        // 출고 취소데이터 설정 및 생성
        releasedDtos.stream().forEach(dto -> {
            originOptionEntities.stream().forEach(option -> {
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
     * @param releasedDtos : List::DeliveryReadyCoupangItemDto.ViewReqAndRes::
     * @param parentOptionEntities : List::ProductOptionEntity::
     * @see OptionPackageService#searchListByParentOptionIdList
     * @see ProductReceiveService#saveListAndModify
     */
    @Transactional
    public void cancelReflectedStockUnitOfPackageOption(List<DeliveryReadyCoupangItemDto.ViewReqAndRes> releasedDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        releasedDtos.stream().forEach(dto -> {
            parentOptionEntities.stream().forEach(parentOption -> {
                if (dto.getDeliveryReadyItem().getReleaseOptionCode().equals(parentOption.getCode())) {
                    optionPackageEntities.stream().forEach(option -> {
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
