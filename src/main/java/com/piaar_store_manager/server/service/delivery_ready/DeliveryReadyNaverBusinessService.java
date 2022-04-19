package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyFileDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemLotteExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.service.option_package.OptionPackageService;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseService;
import com.piaar_store_manager.server.service.user.UserService;
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

    // AWS S3
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;
    
    @Value("${file.upload-dir}")
    String fileLocation;
    
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    /**
     * <b>Upload Excel File</b>
     * <p>
     * 엑셀 파일을 업로드하여 화면에 출력한다.
     * 
     * @param file : MultipartFile
     * @return List::DeliveryReadyNaverItemDto::
     * @see CustomExcelUtils#createWorkBook
     * @see DeliveryReadyNaverBusinessService#getDeliveryReadyExcelForm
     */
    public List<DeliveryReadyNaverItemDto> uploadDeliveryReadyExcelFile(MultipartFile file){
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyNaverItemDto> dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일(네이버 배송준비 엑셀)의 데이터들을 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyNaverItemDto::
     */
    private List<DeliveryReadyNaverItemDto> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<DeliveryReadyNaverItemDto> dtos = new ArrayList<>();

        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyNaverItemDto dto = DeliveryReadyNaverItemDto.builder().id(UUID.randomUUID())
                    .prodOrderNumber(row.getCell(0).getStringCellValue())
                    .orderNumber(row.getCell(1).getStringCellValue()).salesChannel(row.getCell(7).getStringCellValue())
                    .buyer(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                    .buyerId(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                    .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                    .paymentDate(row.getCell(14) != null ? DateHandler.getUtcDate(row.getCell(14).getDateCellValue()) : new Date())
                    .prodNumber(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                    .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                    .optionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                    .unit((int) row.getCell(20).getNumericCellValue())
                    .orderConfirmationDate(row.getCell(27) != null ? DateHandler.getUtcDate(row.getCell(27).getDateCellValue()) : new Date())
                    .shipmentDueDate(row.getCell(28) != null ? DateHandler.getUtcDate(row.getCell(28).getDateCellValue()) : new Date())
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
                    .orderDateTime(row.getCell(56) != null ? DateHandler.getUtcDate(row.getCell(56).getDateCellValue()) : new Date())
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
     * <b>S3 Upload Setting Related Method</b>
     * <p>
     * AWS S3 설정 메소드.
     *
     * @param accessKey : String
     * @param secretKey : String
     */
    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    /**
     * <b>S3 Upload & DB Insert Related Method</b>
     * <p>
     * 업로드된 엑셀파일을 S3 및 DB에 저장한다.
     *
     * @param file : MultipartFile
     * @param userId : UUID
     * @return FileUploadResponse
     * @throws IllegalStateException
     */
    @Transactional
    public void storeDeliveryReadyExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String newFileName = "[NAVER_delivery_ready]" + UUID.randomUUID().toString().replaceAll("-", "") + fileName;
        String uploadPath = bucket + "/naver-order";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        try{
            // AWS S3 업로드
            s3Client.putObject(new PutObjectRequest(uploadPath, newFileName, file.getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        // 파일 저장
        DeliveryReadyFileDto fileDto = this.createDeliveryReadyExcelFile(uploadPath, newFileName, (int)file.getSize());
        // 데이터 저장
        this.createDeliveryReadyExcelItem(file, fileDto);
    }
    
    /**
     * <b>Create FileDto Method</b>
     * <p>
     * 파일 
     *
     * @param filePath : String
     * @param fileName : String
     * @param fileSize : Integer
     * @param userId : UUID
     * @see DeliveryReadyNaverService#createFile
     * @see DeliveryReadyFileDto#toDto
     * @return DeliveryReadyFileDto
     */
    public DeliveryReadyFileDto createDeliveryReadyExcelFile(String filePath, String fileName, Integer fileSize) {
        UUID userId = userService.getUserId();
        // File data 생성 및 저장
        DeliveryReadyFileDto fileDto = DeliveryReadyFileDto.builder()
            .id(UUID.randomUUID())
            .filePath(filePath)
            .fileName(fileName)
            .fileSize(fileSize)
            .fileExtension(FilenameUtils.getExtension(fileName))
            .createdAt(DateHandler.getCurrentDate2())
            .createdBy(userId)
            .deleted(false)
            .build();

        DeliveryReadyFileEntity entity = deliveryReadyNaverService.createFile(DeliveryReadyFileEntity.toEntity(fileDto));
        DeliveryReadyFileDto dto = DeliveryReadyFileDto.toDto(entity);
        return dto;
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 정보를 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @throws IllegalArgumentException
     * @see DeliveryReadyNaverBusinessService#getDeliveryReadyNaverExcelItem
     * @see DeliveryReadyNaverItemEntity#toEntity
     * @see DeliveryReadyNaverService#createItemList
     */
    public void createDeliveryReadyExcelItem(MultipartFile file, DeliveryReadyFileDto fileDto) {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyNaverItemDto> dtos = this.getDeliveryReadyNaverExcelItem(sheet, fileDto);
        
        // 상품명 > 옵션정보 > 수취인명 순으로 정렬 
        dtos.sort(Comparator.comparing(DeliveryReadyNaverItemDto::getProdName)
                .thenComparing(DeliveryReadyNaverItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyNaverItemDto::getReceiver));

        List<DeliveryReadyNaverItemEntity> entities = dtos.stream().map(dto -> DeliveryReadyNaverItemEntity.toEntity(dto)).collect(Collectors.toList());
        deliveryReadyNaverService.createItemList(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복을 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @see DeliveryReadyNaverService#findAllProdOrderNubmer
     */
    private List<DeliveryReadyNaverItemDto> getDeliveryReadyNaverExcelItem(Sheet worksheet, DeliveryReadyFileDto fileDto) {
        List<DeliveryReadyNaverItemDto> dtos = new ArrayList<>();

        // 상품주문번호를 모두 가져온다.
        Set<String> storedProdOrderNumber = deliveryReadyNaverService.findAllProdOrderNubmer();

        for(int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyNaverItemDto dto = DeliveryReadyNaverItemDto.builder()
                .id(UUID.randomUUID())
                .prodOrderNumber(row.getCell(0).getStringCellValue())
                .orderNumber(row.getCell(1).getStringCellValue())
                .salesChannel(row.getCell(7).getStringCellValue())
                .buyer(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                .buyerId(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                .paymentDate(row.getCell(14) != null ? DateHandler.getUtcDate(row.getCell(14).getDateCellValue()) : new Date())
                .prodNumber(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                .optionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue().strip() : "")
                .unit((int) row.getCell(20).getNumericCellValue())
                .orderConfirmationDate(row.getCell(27).getDateCellValue() != null ? DateHandler.getUtcDate(row.getCell(27).getDateCellValue()) : new Date())
                .shipmentDueDate(row.getCell(28) != null ? DateHandler.getUtcDate(row.getCell(28).getDateCellValue()) : new Date())
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
                .orderDateTime(row.getCell(56) != null ? DateHandler.getUtcDate(row.getCell(56).getDateCellValue()) : new Date())
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

            // 상품주문번호가 중복되지 않는다면 데이터를 생성한다.
            if(storedProdOrderNumber.add(dto.getProdOrderNumber())){
                dtos.add(dto);
            }
        }
        return dtos;
    }
    
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 미출고 데이터를 조회한다.
     *
     * @return List::DeliveryReadyNaverItemViewResDto::
     * @see DeliveryReadyNaverService#findSelectedUnreleased
     * @see DeliveryReadyNaverBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyNaverItemViewResDto> getDeliveryReadyViewUnreleasedData() {
        List<DeliveryReadyNaverItemViewProj> itemViewProj = deliveryReadyNaverService.findSelectedUnreleased();
        List<DeliveryReadyNaverItemViewResDto> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 선택된 기간의 출 데이터를 조회한다.
     *
     * @param query : Map[startDate, endDate]
     * @return List::DeliveryReadyNaverItemViewResDto::
     * @throws ParseException
     * @see DeliveryReadyNaverService#findSelectedReleased
     * @see DeliveryReadyNaverBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyNaverItemViewResDto> getDeliveryReadyViewReleased(Map<String, Object> query) throws ParseException {
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();
        
        List<DeliveryReadyNaverItemViewProj> itemViewProj = deliveryReadyNaverService.findSelectedReleased(startDate, endDate);
        List<DeliveryReadyNaverItemViewResDto> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * ItemViewProj의 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경.
     *
     * @param itemViewProj : List::DeliveryReadyNaverItemViewProj::
     * @return List::DeliveryReadyNaverItemViewResDto::
     * @see ProductOptionService#searchListByProductListOptionCode
     * @see DeliveryReadyNaverItemViewResDto#toResDtos
     */
    public List<DeliveryReadyNaverItemViewResDto> changeOptionStockUnit(List<DeliveryReadyNaverItemViewProj> itemViewProj) {
        List<String> optionCodes = itemViewProj.stream().map(r -> r.getDeliveryReadyItem().getReleaseOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByOptionCodes(optionCodes);
        List<DeliveryReadyNaverItemViewResDto>  itemViewResDto = new ArrayList<>();

        if(optionGetDtos.isEmpty()) {
            itemViewResDto = itemViewProj.stream().map(proj -> DeliveryReadyNaverItemViewResDto.toResDto(proj)).collect(Collectors.toList());
            return itemViewResDto;
        }

        // 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경
        itemViewResDto = itemViewProj.stream().map(proj -> {
            DeliveryReadyNaverItemViewResDto resDto = DeliveryReadyNaverItemViewResDto.toResDto(proj);

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
     * DeliveryReadyItem 미출고 데이터 중 itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see DeliveryReadyNaverService#deleteOneDeliveryReadyViewData
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyNaverService.deleteOneDeliveryReadyViewData(itemCid);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 선택된 데이터를 모두 삭제한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto::
     * @see DeliveryReadyNaverItemEntity#toEntity
     * @see DeliveryReadyNaverService#deleteOneDeliveryReadyViewData
     */
    public void deleteListDeliveryReadyViewData(List<DeliveryReadyNaverItemDto> dtos) {
        dtos.stream().forEach(dto -> {
            DeliveryReadyNaverItemEntity.toEntity(dto);
            deliveryReadyNaverService.deleteOneDeliveryReadyViewData(dto.getCid());
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchDeliveryReadyItem
     * @see DeliveryReadyNaverService#createItem
     */
    public void updateReleasedDeliveryReadyItem(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchDeliveryReadyItem(dto.getCid());
        entity.setReleased(false).setReleasedAt(null);

        deliveryReadyNaverService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 선택된 데이터들을 미출고 데이터로 변경한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto::
     * @see DeliveryReadyNaverItemEntity#toEntity
     */
    public void updateListToUnreleasedDeliveryReadyItem(List<DeliveryReadyNaverItemDto> dtos) {
        List<DeliveryReadyNaverItemEntity> entities = dtos.stream().map(dto -> {
            dto.setReleased(false).setReleasedAt(null);
            return DeliveryReadyNaverItemEntity.toEntity(dto);
        }).collect(Collectors.toList());

        deliveryReadyNaverService.createItemList(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyNaverService#findAllOptionInfo
     * @see DeliveryReadyItemOptionInfoProj#toResDtos
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchDeliveryReadyItemOptionInfo() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyNaverService.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = optionInfoProjs.stream().map(proj -> DeliveryReadyItemOptionInfoProj.toResDto(proj)).collect(Collectors.toList());
        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem cid에 대응하는 데이터의 옵션관리코드를 수정한다.
     * 옵션관리코드를 변경하면서 출고옵션코드도 함께 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchDeliveryReadyItem
     * @see DeliveryReadyNaverService#createItem
     */
    public void updateDeliveryReadyItemOptionInfo(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchDeliveryReadyItem(dto.getCid());
        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "")
            .setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

        deliveryReadyNaverService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     * 옵션관리코드를 변경하면서 출고옵션코드도 함께 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchDeliveryReadyItem
     * @see DeliveryReadyNaverService#createItem
     * @see DeliveryReadyNaverBusinessService#updateChangedOption
     */
    public void updateDeliveryReadyItemsOptionInfo(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchDeliveryReadyItem(dto.getCid());

        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "")
            .setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

        deliveryReadyNaverService.createItem(entity);
        
        // 같은 상품의 옵션을 모두 변경
        this.updateChangedOption(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem cid에 대응하는 데이터의 출고옵션코드를 수정한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverService#searchDeliveryReadyItem
     * @see DeliveryReadyNaverService#createItem
     */
    public void updateDeliveryReadyItemReleaseOptionInfo(DeliveryReadyNaverItemDto dto) {
        DeliveryReadyNaverItemEntity entity = deliveryReadyNaverService.searchDeliveryReadyItem(dto.getCid());
        entity.setReleaseOptionCode(dto.getReleaseOptionCode() != null ? dto.getReleaseOptionCode() : "");

        deliveryReadyNaverService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param entity : DeliveryReadyNaverItemEntity
     * @see DeliveryReadyNaverService#findByItems
     * @see DeliveryReadyNaverService#createItemList
     */
    public void updateChangedOption(DeliveryReadyNaverItemEntity entity) {
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverService.findByItems(entity);
        entities.stream().forEach(r -> { r.setOptionManagementCode(entity.getOptionManagementCode()).setReleaseOptionCode(entity.getReleaseOptionCode()); });
        deliveryReadyNaverService.createItemList(entities);
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     * @see DeliveryReadyNaverBusinessService#changeDuplicationHansanDtos
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDeliveryReadyItemToHansan(List<DeliveryReadyNaverItemViewDto> viewDtos) {
        List<DeliveryReadyItemHansanExcelFormDto> excelFormDtos = this.changeDuplicationHansanDtos(viewDtos);     // 중복 데이터 처리
        return excelFormDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * (주문번호 + 받는사람 + 상품명(피아르 출고상품명) + 상품상세(피아르 출고옵션명)) 중복데이터 가공
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDuplicationHansanDtos(List<DeliveryReadyNaverItemViewDto> dtos) {
        List<DeliveryReadyItemHansanExcelFormDto> newOrderList = new ArrayList<>();

        // 받는사람 > 주문번호 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing((DeliveryReadyNaverItemViewDto dto) -> dto.getDeliveryReadyItem().getReceiver())
                                .thenComparing((DeliveryReadyNaverItemViewDto dto) -> dto.getDeliveryReadyItem().getOrderNumber())
                                .thenComparing((DeliveryReadyNaverItemViewDto dto) -> dto.getProdManagementName())
                                .thenComparing((DeliveryReadyNaverItemViewDto dto) -> dto.getOptionManagementName()));

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
            int prevOrderIdx = newOrderList.size()-1;   // 추가되는 데이터 리스트의 마지막 index

            DeliveryReadyItemHansanExcelFormDto currentProd = DeliveryReadyItemHansanExcelFormDto.toFormDto(dtos.get(i));

            // 받는사람 + 주소 + 상품명 + 상품상세 : 중복인 경우
            if(!optionSet.add(resultStr)){
                DeliveryReadyItemHansanExcelFormDto prevProd = newOrderList.get(prevOrderIdx);
                
                newOrderList.get(prevOrderIdx).setUnit(prevProd.getUnit() + currentProd.getUnit());     // 중복데이터의 수량을 더한다
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getProdOrderNumber() + "/" + currentProd.getProdOrderNumber());     // 총 상품번호 수정
            }else{
                // 받는사람 + 번호 + 주소 : 중복인 경우
                if(!optionSet.add(receiverStr)){
                    newOrderList.get(prevOrderIdx).setDuplication(true);
                    currentProd.setDuplication(true);
                }
                newOrderList.add(currentProd);
            }
        }
        return newOrderList;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     * @see DeliveryReadyItemLotteExcelFormDto#toFormDto
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeDeliveryReadyItemToLotte(List<DeliveryReadyNaverItemViewDto> viewDtos) {
        List<DeliveryReadyItemLotteExcelFormDto> formDtos = viewDtos.stream().map(dto -> DeliveryReadyItemLotteExcelFormDto.toFormDto(dto)).collect(Collectors.toList());
        List<DeliveryReadyItemLotteExcelFormDto> excelFormDtos = this.changeDuplicationLotteDtos(formDtos);     // 중복 데이터 처리
        return excelFormDtos;
    }
    
    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * (받는사람 + 주소 + 상품명 + 상품상세) 중복데이터 가공
     * (받는사람 + 연락처 + 주소) 중복데이터 가공
     *
     * @param dtos : List::DeliveryReadyItemLotteExcelFormDto::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeDuplicationLotteDtos(List<DeliveryReadyItemLotteExcelFormDto> dtos) {
        List<DeliveryReadyItemLotteExcelFormDto> newOrderList = new ArrayList<>();
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
            int prevOrderIdx = newOrderList.size() - 1;     // 추가되는 데이터 리스트의 마지막 index

            // 받는사람 + 주소 + 상품명 + 상품상세 : 중복인 경우
            if (!optionSet.add(resultStr)) {
                DeliveryReadyItemLotteExcelFormDto prevProd = newOrderList.get(prevOrderIdx);
                DeliveryReadyItemLotteExcelFormDto currentProd = dtos.get(i);
                
                prevProd.setUnit(prevProd.getUnit() + currentProd.getUnit());     // 중복데이터의 수량을 더한다
                newOrderList.get(prevOrderIdx).setAllProdInfo(prevProd.getProdName1() + " [" + prevProd.getOptionInfo1() + "-" + prevProd.getUnit() + "]");
                prevProd.setAllProdOrderNumber(prevProd.getAllProdOrderNumber() + "/" + currentProd.getProdOrderNumber());
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getAllProdOrderNumber());   // 총 상품번호 수정
            } else {
                newOrderList.add(dtos.get(i));
            }
        }

        String prevProdName = "";
         
        for (int i = 0; i < newOrderList.size(); i++) {
            StringBuilder receiverSb = new StringBuilder();
            receiverSb.append(newOrderList.get(i).getReceiver());
            receiverSb.append(newOrderList.get(i).getReceiverContact1());
            receiverSb.append(newOrderList.get(i).getDestination());
            
            String receiverStr = receiverSb.toString();
            int prevOrderIdx = resultList.size() - 1;     // 추가되는 데이터 리스트의 마지막 index

            // 받는사람 + 연락처 + 주소 + 상품상세 : 중복이 아니면서
            // 받는사람 + 연락처 + 주소 : 중복인 경우
            if (!optionSet.add(receiverStr)) {
                DeliveryReadyItemLotteExcelFormDto prevProd = resultList.get(prevOrderIdx);
                DeliveryReadyItemLotteExcelFormDto currentProd = newOrderList.get(i);

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
                resultList.add(newOrderList.get(i));
            }
        }
        return resultList;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 데이터 다운로드 시 출고 정보를 설정한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @see DeliveryReadyNaverService#updateReleasedAtByCid
     */
    public void updateListToReleaseDeliveryReadyItem(List<DeliveryReadyNaverItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        deliveryReadyNaverService.updateReleasedAtByCid(itemCids);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 재고 반영 시 출고완료 값을 변경한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @see DeliveryReadyNaverItemEntity#toEntity
     * @see DeliveryReadyNaverItemRepository#findById
     * @see DeliveryReadyNaverItemRepository#save
     */
    public void updateListReleaseCompleted(List<DeliveryReadyNaverItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(true));
        deliveryReadyNaverService.createItemList(entities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 재고 반영 취소 시 출고완료 값을 변경한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @see DeliveryReadyNaverService#searchDeliveryReadyItemList
     * @see DeliveryReadyNaverService#createItemList
     */
    public void cancelReleaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(false));
        deliveryReadyNaverService.createItemList(entities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @param reflected : boolean
     * @see DeliveryReadyNaverService#searchDeliveryReadyItemList
     * @see DeliveryReadyNaverService#createItemList
     */
    public void updateListReleaseCompleted(List<DeliveryReadyNaverItemViewDto> dtos, boolean reflected) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(reflected));
        deliveryReadyNaverService.createItemList(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 옵션관리 코드와 대응하는 상품옵션의 cid값을 조회한다.
     *
     * @param dto : DeliveryReadyCoupangItemViewDto
     * @return Integer
     * @see ProductOptionService#findOptionCidByCode
     */
    public Integer getOptionCid(DeliveryReadyNaverItemViewDto dto) {
        return productOptionService.findOptionCidByCode(dto.getOptionManagementName());
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 옵션관리 코드와 대응하는 상품옵션 데이터를 조회한다.
     *
     * @param dto : List::DeliveryReadyNaverItemViewDto::
     * @return List::ProductOptionEntity::
     * @see ProductOptionService#findAllByCode
     */
    public List<ProductOptionEntity> getOptionByCode(List<DeliveryReadyNaverItemViewDto> dtos) {
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
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @param userId : UUID
     * @see DeliveryReadyNaveBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyNaveBusinessService#getOptionByCode
     * @see ProductReleaseGetDto#toDto
     * @see productReleaseBusinessService#createPLList
     */
    @Transactional
    public void releaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos) {
        // 재고반영이 선행되지 않은 데이터들만 재고 반영
        List<DeliveryReadyNaverItemViewDto> unreleasedDtos = dtos.stream().filter(dto -> ((!dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null))).collect(Collectors.toList());
        this.updateListReleaseCompleted(unreleasedDtos, true);
        
        // 출고 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(unreleasedDtos);
        // 1. 세트상품 X
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. 세트상품 O
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());
        
        this.reflectStockUnit(unreleasedDtos, originOptionEntities);

        if(parentOptionEntities.size() > 0) {
            this.reflectStockUnitOfPackageOption(unreleasedDtos, parentOptionEntities);
        }
    }

    public void reflectStockUnit(List<DeliveryReadyNaverItemViewDto> unreleasedDtos, List<ProductOptionEntity> originOptionEntities) {
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
        productReleaseService.createPLList(productReleaseEntities);
    }

    public void reflectStockUnitOfPackageOption(List<DeliveryReadyNaverItemViewDto> unreleasedDtos, List<ProductOptionEntity> parentOptionEntities) {
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
        productReleaseService.createPLList(productReleaseEntities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Cancel the stock unit reflection of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 입고(재고 반영 취소) 데이터를 생성하여 재고에 반영한다.
     * 기본 옵션 상품과 세트 구성 상품을 분리하여 재고반영 취소 처리한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @param userId : UUID
     * @see DeliveryReadyNaverBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyNaverBusinessService#getOptionByCode
     * @see ProductReceiveGetDto#toDto
     * @see productReceiveBusinessService#createPRList
     */
    @Transactional
    public void cancelReleaseListStockUnit(List<DeliveryReadyNaverItemViewDto> dtos, UUID userId) {
        // 재고 반영이 선행된 데이터들만 재고 반영
        List<DeliveryReadyNaverItemViewDto> releasedDtos = dtos.stream().filter(dto -> (dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null)).collect(Collectors.toList());
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

    public void cancelReflectedStockUnit(List<DeliveryReadyNaverItemViewDto> unreleasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        // 1. 세트상품이 아닌 애들 재고반영
        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        // 출고 취소데이터 설정 및 생성
        unreleasedDtos.forEach(dto -> {
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
        productReceiveService.createPRList(productReceiveEntities);
    }

    public void cancelReflectedStockUnitOfPackageOption(List<DeliveryReadyNaverItemViewDto> unreleasedDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        unreleasedDtos.forEach(dto -> {
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
        productReceiveService.createPRList(productReceiveEntities);
    }
}
