package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.naver.repository.DeliveryReadyNaverItemRepository;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.model.delivery_ready.repository.DeliveryReadyFileRepository;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.piaar_store_manager.server.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DeliveryReadyNaverService {

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
    
    // excel file extension.
    private final List<String> EXTENSIONS_EXCEL = Arrays.asList("xlsx", "xls");

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private DeliveryReadyFileRepository deliveryReadyFileRepository;

    @Autowired
    private DeliveryReadyNaverItemRepository deliveryReadyNaverItemRepository;

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
     * @return List::DeliveryReadyNaverItemDto::
     * @throws IOException
     */
    public List<DeliveryReadyNaverItemDto> uploadDeliveryReadyExcelFile(MultipartFile file) {
        Workbook workbook = null;
        
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
        Sheet sheet = workbook.getSheetAt(0);

        List<DeliveryReadyNaverItemDto> dtos = this.getDeliveryReadyExcelForm(sheet);

        return dtos;
    }


    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일의 데이터들을 Dto로 만든다.
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
                    .paymentDate(row.getCell(14) != null ? row.getCell(14).getDateCellValue() : new Date())
                    .prodNumber(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                    .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                    .optionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                    .unit((int) row.getCell(20).getNumericCellValue())
                    .orderConfirmationDate(row.getCell(27) != null ? row.getCell(27).getDateCellValue() : new Date())
                    .shipmentDueDate(row.getCell(28) != null ? row.getCell(28).getDateCellValue() : new Date())
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
                    .orderDateTime(row.getCell(56) != null ? row.getCell(56).getDateCellValue() : new Date()).build();

            dtos.add(dto);
        }

        return dtos;
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
    public FileUploadResponse storeDeliveryReadyExcelFile(MultipartFile file, UUID userId) {
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

        // file DB저장
        // DeliveryReadyFileEntity 생성
        DeliveryReadyFileEntity entity = this.createDeliveryReadyFileDto(s3Client.getUrl(uploadPath, newFileName).toString(), newFileName, (int)file.getSize(), userId);

        // item 중복데이터 제거 후 items DB저장
        this.createDeliveryReadyItemData(file, DeliveryReadyFileDto.toDto(entity));
                                                      
        return new FileUploadResponse(newFileName, s3Client.getUrl(uploadPath, newFileName).toString(), file.getContentType(), file.getSize());
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
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일의 정보를 저장한다.
     *
     * @param filePath : String
     * @param fileName : String
     * @param fileSize : Integer
     * @return DeliveryReadyFileEntity
     * @see DeliveryReadyFileRepository#save
     */
    public DeliveryReadyFileEntity createDeliveryReadyFileDto(String filePath, String fileName, Integer fileSize, UUID userId) {

        DeliveryReadyFileDto fileDto = DeliveryReadyFileDto.builder()
            .id(UUID.randomUUID())
            .filePath(filePath)
            .fileName(fileName)
            .fileSize(fileSize)
            .fileExtension(FilenameUtils.getExtension(fileName))
            .createdAt(dateHandler.getCurrentDate())
            .createdBy(userId)
            .deleted(false)
            .build();

        return deliveryReadyFileRepository.save(DeliveryReadyFileEntity.toEntity(fileDto));
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 정보를 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @throws IllegalArgumentException
     * @see DeliveryReadyNaverItemRepository#saveAll
     */
    public void createDeliveryReadyItemData(MultipartFile file, DeliveryReadyFileDto fileDto) {

        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyNaverItemDto> dtos = this.getDeliveryReadyNaverExcelData(sheet, fileDto);
        List<DeliveryReadyNaverItemEntity> entities = new ArrayList<>();
        
        dtos.sort(Comparator.comparing(DeliveryReadyNaverItemDto::getProdName)
                .thenComparing(DeliveryReadyNaverItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyNaverItemDto::getReceiver));

        for(DeliveryReadyNaverItemDto dto : dtos) {
            entities.add(DeliveryReadyNaverItemEntity.toEntity(dto));
        }

        deliveryReadyNaverItemRepository.saveAll(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복을 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @see DeliveryReadyNaverItemRepository#findAllProdOrderNumber
     */
    private List<DeliveryReadyNaverItemDto> getDeliveryReadyNaverExcelData(Sheet worksheet, DeliveryReadyFileDto fileDto) {
        List<DeliveryReadyNaverItemDto> dtos = new ArrayList<>();

        Set<String> storedProdOrderNumber = deliveryReadyNaverItemRepository.findAllProdOrderNumber();   // 상품 주문번호로 중복데이터를 구분

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
                .paymentDate(row.getCell(14) != null ? row.getCell(14).getDateCellValue() : new Date())
                .prodNumber(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                .optionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                .unit((int) row.getCell(20).getNumericCellValue())
                .orderConfirmationDate(row.getCell(27).getDateCellValue() != null ? row.getCell(27).getDateCellValue() : new Date())
                .shipmentDueDate(row.getCell(28) != null ? row.getCell(28).getDateCellValue() : new Date())
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
                .orderDateTime(row.getCell(56) != null ? row.getCell(56).getDateCellValue() : new Date())
                .released(false)
                .createdAt(fileDto.getCreatedAt())
                .deliveryReadyFileCid(fileDto.getCid())
                .build();

            // 상품주문번호가 중복되지 않는다면
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
     * @see DeliveryReadyNaverItemRepository#findSelectedUnreleased
     * @see DeliveryReadyNaverItemViewProj#toResDto
     */
    public List<DeliveryReadyNaverItemViewResDto> getDeliveryReadyViewUnreleasedData() {
        List<DeliveryReadyNaverItemViewProj> itemViewProj = deliveryReadyNaverItemRepository.findSelectedUnreleased();
        List<DeliveryReadyNaverItemViewResDto> itemViewResDto = DeliveryReadyNaverItemViewProj.toResDtos(itemViewProj);

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
     * @see DeliveryReadyNaverItemRepository#findSelectedReleased
     * @see DeliveryReadyNaverItemViewProj#toResDtos
     */
    public List<DeliveryReadyNaverItemViewResDto> getDeliveryReadyViewReleased(Map<String, Object> query) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;

        try{
            if(query.get("startDate") != null && query.get("endDate") != null) {
                startDate = dateFormat.parse(query.get("startDate").toString());
                endDate = dateFormat.parse(query.get("endDate").toString());
            }
        } catch (ParseException e) {
            throw new ParseException("date format parse error", -1);
        }

        List<DeliveryReadyNaverItemViewProj> itemViewProj = deliveryReadyNaverItemRepository.findSelectedReleased(startDate, endDate);
        List<DeliveryReadyNaverItemViewResDto> itemViewResDto = DeliveryReadyNaverItemViewProj.toResDtos(itemViewProj);

        return itemViewResDto;
    }
 
    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see DeliveryReadyNaverItemRepository#findById
     * @see DeliveryReadyNaverItemRepository#delete
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyNaverItemRepository.findById(itemCid).ifPresent(item -> {
            deliveryReadyNaverItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 선택된 데이터를 삭제한다.
     *
     * @param itemCid : List::Integer::
     */
    public void deleteListDeliveryReadyViewData(List<Integer> itemCids) {
        for(Integer itemCid : itemCids) {
            this.deleteOneDeliveryReadyViewData(itemCid);
        }
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverItemRepository#findById
     * @see DeliveryReadyNaverItemRepository#save
     */
    public void updateReleasedDeliveryReadyItem(DeliveryReadyNaverItemDto dto) {
        deliveryReadyNaverItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            item.setReleased(false).setReleasedAt(null);

            deliveryReadyNaverItemRepository.save(item);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 선택된 데이터들을 미출고 데이터로 변경한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemDto::
     */
    public void updateListToUnreleasedDeliveryReadyItem(List<DeliveryReadyNaverItemDto> dtos) {
        for(DeliveryReadyNaverItemDto dto : dtos) {
            this.updateReleasedDeliveryReadyItem(dto);
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyNaverItemRepository#findAllOptionInfo
     * @see DeliveryReadyItemOptionInfoProj#toResDtos
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchDeliveryReadyItemOptionInfo() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyNaverItemRepository.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = DeliveryReadyItemOptionInfoProj.toResDtos(optionInfoProjs);

        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터의 옵션관리코드를 수정한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverItemRepository#findById
     * @see DeliveryReadyNaverItemRepository#save
     */
    public void updateDeliveryReadyItemOptionInfo(DeliveryReadyNaverItemDto dto) {

        deliveryReadyNaverItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            
            item.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

            deliveryReadyNaverItemRepository.save(item);
            
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param dto : DeliveryReadyNaverItemDto
     * @see DeliveryReadyNaverItemRepository#findById
     * @see DeliveryReadyNaverItemRepository#save
     */
    public void updateDeliveryReadyItemsOptionInfo(DeliveryReadyNaverItemDto dto) {

        deliveryReadyNaverItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            
            item.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

            deliveryReadyNaverItemRepository.save(item);

            // 같은 상품의 옵션을 모두 변경
            this.updateDeliveryReadyItemChangedOption(item);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param item : DeliveryReadyNaverItemEntity
     * @see DeliveryReadyNaverItemRepository#findByItems
     * @see DeliveryReadyNaverItemRepository#save
     */
    public void updateDeliveryReadyItemChangedOption(DeliveryReadyNaverItemEntity item) {
        List<DeliveryReadyNaverItemEntity> entities = deliveryReadyNaverItemRepository.findByItems(item.getProdName(), item.getOptionInfo());

        for(DeliveryReadyNaverItemEntity entity : entities) {
            entity.setOptionManagementCode(item.getOptionManagementCode());

            deliveryReadyNaverItemRepository.save(entity);
        }
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyNaverItemViewDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     * @see DeliveryReadyItemHansanExcelFormDto#toFormDto
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDeliveryReadyItem(List<DeliveryReadyNaverItemViewDto> viewDtos) {
        List<DeliveryReadyItemHansanExcelFormDto> formDtos = new ArrayList<>();

        // DeliveryReadyItemViewDto로 DeliveryReadyItemExcelFromDto를 만든다
        for(DeliveryReadyNaverItemViewDto viewDto : viewDtos) {
            formDtos.add(DeliveryReadyItemHansanExcelFormDto.toFormDto(viewDto));
        }

        // 중복데이터 처리
        List<DeliveryReadyItemHansanExcelFormDto> excelFormDtos = changeDuplicationDtos(formDtos);

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
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getProdOrderNumber() + " / " + currentProd.getProdOrderNumber());     // 총 상품번호 수정
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
     * <b>DB Update Related Method</b>
     * <p>
     * 데이터 다운로드 시 출고 정보를 설정한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @see DeliveryReadyNaverItemRepository#updateReleasedAtByCid
     */
    @Transactional
    public void updateListToReleaseDeliveryReadyItem(List<DeliveryReadyNaverItemViewDto> dtos) {

        List<Integer> cidList = new ArrayList<>();
        
        for(DeliveryReadyNaverItemViewDto dto : dtos){
            cidList.add(dto.getDeliveryReadyItem().getCid());
        }
        deliveryReadyNaverItemRepository.updateReleasedAtByCid(cidList, dateHandler.getCurrentDate());
    }
}
