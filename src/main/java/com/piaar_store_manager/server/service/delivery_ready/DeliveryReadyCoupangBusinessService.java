package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyFileDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemLotteExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveBusinessService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseBusinessService;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DeliveryReadyCoupangBusinessService {
    private DeliveryReadyCoupangService deliveryReadyCoupangService;
    private ProductReleaseBusinessService productReleaseBusinessService;
    private ProductReceiveBusinessService productReceiveBusinessService;
    private ProductOptionService productOptionService;

    @Autowired
    public DeliveryReadyCoupangBusinessService(
        DeliveryReadyCoupangService deliveryReadyCoupangService,
        ProductReleaseBusinessService productReleaseBusinessService,
        ProductReceiveBusinessService productReceiveBusinessService,
        ProductOptionService productOptionService
    ) {
        this.deliveryReadyCoupangService = deliveryReadyCoupangService;
        this.productReleaseBusinessService = productReleaseBusinessService;
        this.productReceiveBusinessService = productReceiveBusinessService;
        this.productOptionService = productOptionService;
    }

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

    // Excel file extension.
    private final List<String> EXTENSIONS_EXCEL = Arrays.asList("xlsx", "xls");
    
    /**
     * <b>Extension Check</b>
     * <p>
     * 
     * @param file : MultipartFile
     * @throws FileUploadException
     */
    public void isExcelFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());

        if(EXTENSIONS_EXCEL.contains(extension)){
            return;
        }
        throw new FileUploadException("This is not an excel file.");
    }
    
    /**
     * <b>Upload Excel File</b>
     * <p>
     * 엑셀 파일을 업로드하여 화면에 출력한다.
     * 
     * @param file : MultipartFile
     * @return List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangBusinessService#getDeliveryReadyExcelForm
     */
    public List<DeliveryReadyCoupangItemDto> uploadDeliveryReadyExcelFile(MultipartFile file) throws ParseException {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyCoupangItemDto> dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일(쿠팡 배송준비 엑셀)의 데이터들을 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyCoupangItemDto::
     */
    private List<DeliveryReadyCoupangItemDto> getDeliveryReadyExcelForm(Sheet worksheet) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;

            String prodOrderNumber = null;
            if((row.getCell(2) != null) && (row.getCell(13) != null) && (row.getCell(14) != null)){
                prodOrderNumber = row.getCell(2).getStringCellValue() + "|" + row.getCell(13).getStringCellValue() + "|" + row.getCell(14).getStringCellValue();
            }

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
                    .shipmentDueDate(row.getCell(7) != null ? DateHandler.getUtcDate(dateFormat.parse(row.getCell(7).getStringCellValue())) : new Date())
                    .shipmentCostBundleNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                    .receiverContact1(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                    .destination(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                    .buyerContact(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                    .zipCode(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                    .orderDateTime(row.getCell(9) != null ? DateHandler.getUtcDate(dateFormat2.parse(row.getCell(9).getStringCellValue())) : new Date()).build();

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
     * @throws ParseException
     * @see DeliveryReadyCoupangBusinessService#createDeliveryReadyExcelFile
     * @see DeliveryReadyCoupangBusinessService#createDeliveryReadyExcelItem
     */
    @Transactional
    public FileUploadResponse storeDeliveryReadyExcelFile(MultipartFile file, UUID userId) throws ParseException {
        String fileName = file.getOriginalFilename();
        String newFileName = "[COUPANG_delivery_ready]" + UUID.randomUUID().toString().replaceAll("-", "") + fileName;
        String uploadPath = bucket + "/coupang-order";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        try{
            // AWS S3 업로드
            s3Client.putObject(new PutObjectRequest(uploadPath, newFileName, file.getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        // 파일 저장
        DeliveryReadyFileDto fileDto = this.createDeliveryReadyExcelFile(s3Client.getUrl(uploadPath, newFileName).toString(), newFileName, (int)file.getSize(), userId);
        // 데이터 저장
        this.createDeliveryReadyExcelItem(file, fileDto);
                                                      
        return new FileUploadResponse(newFileName, s3Client.getUrl(uploadPath, newFileName).toString(), file.getContentType(), file.getSize());
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
     * @return DeliveryReadyFileDto
     * @see DeliveryReadyCoupangService#createFile
     * @see DeliveryReadyFileDto#toDto
     */
    public DeliveryReadyFileDto createDeliveryReadyExcelFile(String filePath, String fileName, Integer fileSize, UUID userId) {
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

        DeliveryReadyFileEntity entity = deliveryReadyCoupangService.createFile(DeliveryReadyFileEntity.toEntity(fileDto));
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
     * @throws ParseException
     * @see DeliveryReadyCoupangItemEntity#toEntity
     * @see DeliveryReadyCoupangService#createItemList
     */
    public void createDeliveryReadyExcelItem(MultipartFile file, DeliveryReadyFileDto fileDto) throws ParseException {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyCoupangItemDto> dtos = this.getDeliveryReadyCoupangExcelItem(sheet, fileDto);
        
        dtos.sort(Comparator.comparing(DeliveryReadyCoupangItemDto::getProdName)
                .thenComparing(DeliveryReadyCoupangItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyCoupangItemDto::getReceiver));

        List<DeliveryReadyCoupangItemEntity> entities = dtos.stream().map(dto -> DeliveryReadyCoupangItemEntity.toEntity(dto)).collect(Collectors.toList());
        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복을 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @return List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangItemRepository#findAllProdOrderNumber
     */
    private List<DeliveryReadyCoupangItemDto> getDeliveryReadyCoupangExcelItem(Sheet worksheet, DeliveryReadyFileDto fileDto) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        // 상품주문번호를 모두 가져온다.
        Set<String> storedProdOrderNumber = deliveryReadyCoupangService.findAllProdOrderNumber();

        for(int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;

            String prodOrderNumber = null;
            if((row.getCell(2) != null) && (row.getCell(13) != null) && (row.getCell(14) != null)){
                prodOrderNumber = row.getCell(2).getStringCellValue() + "|" + row.getCell(13).getStringCellValue() + "|" + row.getCell(14).getStringCellValue();
            }

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
                .shipmentDueDate(row.getCell(7) != null ? DateHandler.getUtcDate(dateFormat.parse(row.getCell(7).getStringCellValue())) : new Date())
                .shipmentCostBundleNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                .receiverContact1(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                .destination(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                .buyerContact(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                .zipCode(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                .deliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                .orderDateTime(row.getCell(9) != null ? DateHandler.getUtcDate(dateFormat2.parse(row.getCell(9).getStringCellValue())) : new Date())
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
     * @return List::DeliveryReadyCoupangItemViewResDto::
     * @see DeliveryReadyCoupangService#findSelectedUnreleased
     * @see DeliveryReadyCoupangBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyCoupangItemViewResDto> getDeliveryReadyViewUnreleasedData() {
        List<DeliveryReadyCoupangItemViewProj> itemViewProj = deliveryReadyCoupangService.findSelectedUnreleased();
        List<DeliveryReadyCoupangItemViewResDto> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 선택된 기간의 출 데이터를 조회한다.
     *
     * @param query : Map[startDate, endDate]
     * @return List::DeliveryReadyCoupangItemViewResDto::
     * @throws ParseException
     * @see DeliveryReadyCoupangService#findSelectedReleased
     * @see DeliveryReadyCoupangBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyCoupangItemViewResDto> getDeliveryReadyViewReleased(Map<String, Object> query) throws ParseException {
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date startDate = null;
        // Date endDate = null;

        // if(query.get("startDate") != null && query.get("endDate") != null) {
        //     startDate = dateFormat.parse(query.get("startDate").toString());
        //     endDate = dateFormat.parse(query.get("endDate").toString());
        // }
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = query.get("startDate") != null ? new Date(query.get("startDate").toString())
                : startDateCalendar.getTime();
        Date endDate = query.get("endDate") != null ? new Date(query.get("endDate").toString()) : new Date();

        List<DeliveryReadyCoupangItemViewProj> itemViewProj = deliveryReadyCoupangService.findSelectedReleased(startDate, endDate);
        List<DeliveryReadyCoupangItemViewResDto> itemViewResDto = this.changeOptionStockUnit(itemViewProj);
        return itemViewResDto;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * ItemViewProj의 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경.
     *
     * @param itemViewProj : List::DeliveryReadyCoupangItemViewProj::
     * @return List::DeliveryReadyCoupangItemViewResDto::
     * @see ProductOptionService#searchListByProductListOptionCode
     * @see DeliveryReadyCoupangItemViewResDto#toResDtos
     */
    public List<DeliveryReadyCoupangItemViewResDto> changeOptionStockUnit(List<DeliveryReadyCoupangItemViewProj> itemViewProj) {
        List<String> productOptionCodes = itemViewProj.stream().map(r -> r.getDeliveryReadyItem().getOptionManagementCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductListOptionCode(productOptionCodes);

        // 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경.
        List<DeliveryReadyCoupangItemViewResDto>  itemViewResDto = itemViewProj.stream().map(proj -> {
            DeliveryReadyCoupangItemViewResDto resDto = DeliveryReadyCoupangItemViewResDto.toResDto(proj);

            // 옵션 코드와 동일한 상품의 재고수량을 변경한다
            optionGetDtos.stream().forEach(option -> {
                if(proj.getDeliveryReadyItem().getOptionManagementCode().equals(option.getCode())) {
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
     * @see DeliveryReadyCoupangService#deleteOneDeliveryReadyViewData
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyCoupangService.deleteOneDeliveryReadyViewData(itemCid);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 선택된 데이터를 모두 삭제한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangItemEntity#toEntity
     * @see DeliveryReadyCoupangService#deleteOneDeliveryReadyViewData
     */
    @Transactional
    public void deleteListDeliveryReadyViewData(List<DeliveryReadyCoupangItemDto> dtos) {
        dtos.stream().forEach(dto -> {
            DeliveryReadyCoupangItemEntity.toEntity(dto);
            deliveryReadyCoupangService.deleteOneDeliveryReadyViewData(dto.getCid());
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     * @see DeliveryReadyCoupnagService#createItem
     */
    public void updateReleasedDeliveryReadyItem(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());
        entity.setReleased(false).setReleasedAt(null);

        deliveryReadyCoupangService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 선택된 데이터들을 미출고 데이터로 변경한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangItemEntity#toEntity
     */
    public void updateListToUnreleasedDeliveryReadyItem(List<DeliveryReadyCoupangItemDto> dtos) {
        List<DeliveryReadyCoupangItemEntity> entities = dtos.stream().map(dto -> {
            dto.setReleased(false).setReleasedAt(null);
            return DeliveryReadyCoupangItemEntity.toEntity(dto);
        }).collect(Collectors.toList());

        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyCoupangService#findAllOptionInfo
     * @see DeliveryReadyItemOptionInfoProj#toResDtos
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchDeliveryReadyItemOptionInfo() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyCoupangService.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = optionInfoProjs.stream().map(proj -> DeliveryReadyItemOptionInfoProj.toResDto(proj)).collect(Collectors.toList());
        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터의 옵션관리코드를 수정한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     * @see DeliveryReadyCoupangService#createItem
     */
    public void updateDeliveryReadyItemOptionInfo(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());
    
        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");
        deliveryReadyCoupangService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     * @see DeliveryReadyCoupangService#createItem
     * @see DeliveryReadyCoupangService#updateChangedOption
     */
    public void updateDeliveryReadyItemsOptionInfo(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());

        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");
        deliveryReadyCoupangService.createItem(entity);

        // 같은 상품의 옵션을 모두 변경
        this.updateChangedOption(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param entity : DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangService#findByItems
     * @see DeliveryReadyCoupangService#createItemList
     */
    public void updateChangedOption(DeliveryReadyCoupangItemEntity entity) {
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.findByItems(entity);
        entities.stream().forEach(r -> { r.setOptionManagementCode(entity.getOptionManagementCode()); });
        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyCoupangItemViewDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     * @see DeliveryReadyItemHansanExcelFormDto#toFormDto
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDeliveryReadyItem(List<DeliveryReadyCoupangItemViewDto> viewDtos) {
        List<DeliveryReadyItemHansanExcelFormDto> formDtos = viewDtos.stream().map(dto -> DeliveryReadyItemHansanExcelFormDto.toFormDto(dto)).collect(Collectors.toList());
        List<DeliveryReadyItemHansanExcelFormDto> excelFormDtos = this.changeDuplicationDtos(formDtos);     // 중복 데이터 처리
        return excelFormDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyCoupangItemViewDto::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     * @see DeliveryReadyItemLotteExcelFormDto#toFormDto
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeDeliveryReadyItemToLotte(List<DeliveryReadyCoupangItemViewDto> viewDtos) {
        List<DeliveryReadyItemLotteExcelFormDto> formDtos = viewDtos.stream().map(dto -> DeliveryReadyItemLotteExcelFormDto.toFormDto(dto)).collect(Collectors.toList());
        List<DeliveryReadyItemLotteExcelFormDto> excelFormDtos = this.changeDuplicationLotteDtos(formDtos);     // 중복 데이터 처리
        return excelFormDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * (주문번호 + 받는사람 + 상품명 + 상품상세) 중복데이터 가공
     *
     * @param dtos : List::DeliveryReadyItemHansanExcelFormDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDuplicationDtos(List<DeliveryReadyItemHansanExcelFormDto> dtos) {
        List<DeliveryReadyItemHansanExcelFormDto> newOrderList = new ArrayList<>();

        // 받는사람 > 주문번호 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing(DeliveryReadyItemHansanExcelFormDto::getReceiver)
                                .thenComparing(DeliveryReadyItemHansanExcelFormDto::getOrderNumber)
                                .thenComparing(DeliveryReadyItemHansanExcelFormDto::getProdName)
                                .thenComparing(DeliveryReadyItemHansanExcelFormDto::getOptionInfo));

        Set<String> optionSet = new HashSet<>();        // 받는사람 + 주소 + 상품명 + 상품상세

        for(int i = 0; i < dtos.size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(dtos.get(i).getReceiver());
            sb.append(dtos.get(i).getDestination());
            sb.append(dtos.get(i).getProdName());
            sb.append(dtos.get(i).getOptionInfo());

            StringBuilder receiverSb = new StringBuilder();
            receiverSb.append(dtos.get(i).getReceiver());
            receiverSb.append(dtos.get(i).getReceiverContact1());
            receiverSb.append(dtos.get(i).getDestination());

            String resultStr = sb.toString();
            String receiverStr = receiverSb.toString();
            int prevOrderIdx = newOrderList.size()-1;   // 추가되는 데이터 리스트의 마지막 index
            
            // 받는사람 + 주소 + 상품명 + 상품상세 : 중복인 경우
            if(!optionSet.add(resultStr)){
                DeliveryReadyItemHansanExcelFormDto prevProd = newOrderList.get(prevOrderIdx);
                DeliveryReadyItemHansanExcelFormDto currentProd = dtos.get(i);
                
                newOrderList.get(prevOrderIdx).setUnit(prevProd.getUnit() + currentProd.getUnit());     // 중복데이터의 수량을 더한다
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getProdOrderNumber() + "/" + currentProd.getProdOrderNumber());     // 총 상품번호 수정
            }else{
                // 받는사람 + 번호 + 주소 : 중복인 경우
                if(!optionSet.add(receiverStr)){
                    newOrderList.get(prevOrderIdx).setDuplication(true);
                    dtos.get(i).setDuplication(true);
                }
                newOrderList.add(dtos.get(i));
            }
        }
        return newOrderList;
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
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangService#updateReleasedAtByCid
     */
    public void updateListToReleaseDeliveryReadyItem(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        deliveryReadyCoupangService.updateReleasedAtByCid(itemCids);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 재고 반영 시 출고완료 값을 변경한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangItemEntity#toEntity
     * @see DeliveryReadyCoupangItemRepository#findById
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public void updateListReleaseCompleted(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(true));
        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 재고 반영 취소 시 출고완료 값을 변경한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItemList
     * @see DeliveryReadyCoupangService#createItemList
     */
    public void cancelReleaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(false));
        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @param reflected : boolean
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItemList
     * @see DeliveryReadyCoupangService#createItemList
     */
    public void updateListReleaseCompleted(List<DeliveryReadyCoupangItemViewDto> dtos, boolean reflected) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(reflected));
        deliveryReadyCoupangService.createItemList(entities);
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
    public Integer getOptionCid(DeliveryReadyCoupangItemViewDto dto) {
        return productOptionService.findOptionCidByCode(dto.getOptionManagementName());
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 옵션관리 코드와 대응하는 상품옵션 데이터를 조회한다.
     *
     * @param dto : List::DeliveryReadyCoupangItemViewDto::
     * @return List::ProductOptionEntity::
     * @see ProductOptionService#findAllByCode
     */
    public List<ProductOptionEntity> getOptionByCode(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<String> managementCodes = dtos.stream().map(r -> r.getDeliveryReadyItem().getOptionManagementCode()).collect(Collectors.toList());
        return productOptionService.findAllByCode(managementCodes);
    }
    
    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 출고(재고 반영) 데이터를 생성하여 재고에 반영한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @param userId : UUID
     * @see DeliveryReadyNaveBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyNaveBusinessService#getOptionByCode
     * @see ProductReleaseGetDto#toDto
     * @see productReleaseBusinessService#createPLList
     */
    @Transactional
    public void releaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos, UUID userId) {
        // 재고반영이 선행되지 않은 데이터들만 재고 반영
        List<DeliveryReadyCoupangItemViewDto> unreleasedDtos = dtos.stream().filter(dto -> !dto.getDeliveryReadyItem().getReleaseCompleted()).collect(Collectors.toList());
        this.updateListReleaseCompleted(unreleasedDtos, true);

        // 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(unreleasedDtos);
        List<ProductReleaseGetDto> productReleaseGetDtos = new ArrayList<>();

        // 출고데이터 설정 및 생성
        unreleasedDtos.stream().forEach(dto -> {
            optionEntities.stream().forEach(option -> {
                if(dto.getOptionManagementName() != null && dto.getDeliveryReadyItem().getOptionManagementCode().equals(option.getCode())){
                    ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.toDto(dto, option.getCid());
                    productReleaseGetDtos.add(releaseGetDto);
                }
            });
        });
        productReleaseBusinessService.createPLList(productReleaseGetDtos, userId);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <b>Cancel the stock unit reflection of product options.</b>
     * <p>
     * 배송준비 데이터의 출고완료 항목을 업데이트한다.
     * 입고(재고 반영 취소) 데이터를 생성하여 재고에 반영한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @param userId : UUID
     * @see DeliveryReadyCoupangBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyCoupangBusinessService#getOptionByCode
     * @see ProductReceiveGetDto#toDto
     * @see productReceiveBusinessService#createPRList
     */
    @Transactional
    public void cancelReleaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos, UUID userId) {
        // 재고 반영이 선행된 데이터들만 재고 반영
        List<DeliveryReadyCoupangItemViewDto> releasedDtos = dtos.stream().filter(dto -> dto.getDeliveryReadyItem().getReleaseCompleted()).collect(Collectors.toList());
        this.updateListReleaseCompleted(releasedDtos, false);

        // 옵션데이터 추출
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(releasedDtos);
        List<ProductReceiveGetDto> productReceiveGetDtos = new ArrayList<>();

        // 출고 취소데이터 설정 및 생성
        releasedDtos.stream().forEach(dto -> {
            optionEntities.stream().forEach(option -> {
                if(dto.getOptionManagementName() != null && dto.getDeliveryReadyItem().getOptionManagementCode().equals(option.getCode())){
                    ProductReceiveGetDto receiveGetDto = ProductReceiveGetDto.toDto(dto, option.getCid());
                    productReceiveGetDtos.add(receiveGetDto);
                }
            });
        });
        productReceiveBusinessService.createPRList(productReceiveGetDtos, userId);
    }
}
