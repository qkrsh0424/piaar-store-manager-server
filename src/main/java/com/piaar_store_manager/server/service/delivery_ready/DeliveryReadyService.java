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
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.repository.DeliveryReadyFileRepository;
import com.piaar_store_manager.server.model.delivery_ready.repository.DeliveryReadyItemRepository;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeliveryReadyService {

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
    private DeliveryReadyItemRepository deliveryReadyItemRepository;

    /**
     * <b>Extension Check</b>
     * <p>
     * 
     * @param file : MultipartFile
     * @throws IOException
     */
    public void isExcelFile(MultipartFile file) throws IOException{
        String extension = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());

        if(EXTENSIONS_EXCEL.contains(extension)){
            return;
        }
        throw new IOException("엑셀파일만 업로드 해주세요.");
    }

    // entity -> dto :: FileEntity
    private DeliveryReadyFileDto getFileDtoByEntity(DeliveryReadyFileEntity entity) {
        DeliveryReadyFileDto fileDto = new DeliveryReadyFileDto();

        fileDto.setCid(entity.getCid()).setId(entity.getId()).setFilePath(entity.getFilePath()).setFileName(entity.getFileName())
        .setFileSize(entity.getFileSize()).setFileExtension(entity.getFileExtension())
        .setCreatedAt(entity.getCreatedAt()).setCreatedBy(entity.getCreatedBy()).setDeleted(entity.getDeleted());
        
        return fileDto;
    }

    // entity -> dto :: ItemEntity
    private DeliveryReadyItemDto getItemDtoByEntity(DeliveryReadyItemEntity entity) {
        DeliveryReadyItemDto itemDto = new DeliveryReadyItemDto();

        itemDto.setCid(entity.getCid()).setId(entity.getId()).setProdOrderNumber(entity.getProdOrderNumber())
            .setOrderNumber(entity.getOrderNumber()).setSalesChannel(entity.getSalesChannel())
            .setBuyer(entity.getBuyer()).setBuyerId(entity.getBuyerId()).setReceiver(entity.getReceiver())
            .setPaymentDate(entity.getPaymentDate()).setProdNumber(entity.getProdNumber()).setProdName(entity.getProdName())
            .setOptionInfo(entity.getOptionInfo()).setOptionManagementCode(entity.getOptionManagementCode())
            .setUnit(entity.getUnit()).setOrderConfirmationDate(entity.getOrderConfirmationDate())
            .setShipmentDueDate(entity.getShipmentDueDate()).setShipmentCostBundleNumber(entity.getShipmentCostBundleNumber())
            .setSellerProdCode(entity.getSellerProdCode()).setSellerInnerCode1(entity.getSellerInnerCode1())
            .setSellerInnerCode2(entity.getSellerInnerCode2()).setReceiverContact1(entity.getReceiverContact1())
            .setReceiverContact2(entity.getReceiverContact2()).setDestination(entity.getDestination())
            .setBuyerContact(entity.getBuyerContact()).setZipCode(entity.getZipCode()).setDeliveryMessage(entity.getDeliveryMessage())
            .setReleaseArea(entity.getReleaseArea()).setOrderDateTime(entity.getOrderDateTime())
            .setReleased(entity.getReleased()).setReleasedAt(entity.getReleasedAt()).setCreatedAt(entity.getCreatedAt())
            .setDeliveryReadyFileCid(entity.getDeliveryReadyFileCid());

        return itemDto;
    }

    private DeliveryReadyFileEntity convFileEntityByDto(DeliveryReadyFileDto dto) {
        DeliveryReadyFileEntity entity = new DeliveryReadyFileEntity();

        entity.setId(dto.getId()).setFilePath(dto.getFilePath()).setFileName(dto.getFileName())
            .setFileSize(dto.getFileSize()).setFileExtension(dto.getFileExtension()).setCreatedAt(dto.getCreatedAt())
            .setCreatedBy(dto.getCreatedBy()).setDeleted(dto.getDeleted());

        return entity;
    }

    // dto -> entity :: ItemDto
    private DeliveryReadyItemEntity convItemEntityByDto(DeliveryReadyItemDto dto) {
        DeliveryReadyItemEntity entity = new DeliveryReadyItemEntity();

        entity.setId(dto.getId()).setProdOrderNumber(dto.getProdOrderNumber())
            .setOrderNumber(dto.getOrderNumber()).setSalesChannel(dto.getSalesChannel())
            .setBuyer(dto.getBuyer()).setBuyerId(dto.getBuyerId()).setReceiver(dto.getReceiver())
            .setPaymentDate(dto.getPaymentDate()).setProdNumber(dto.getProdNumber()).setProdName(dto.getProdName())
            .setOptionInfo(dto.getOptionInfo()).setOptionManagementCode(dto.getOptionManagementCode())
            .setUnit(dto.getUnit()).setOrderConfirmationDate(dto.getOrderConfirmationDate())
            .setShipmentDueDate(dto.getShipmentDueDate()).setShipmentCostBundleNumber(dto.getShipmentCostBundleNumber())
            .setSellerProdCode(entity.getSellerProdCode()).setSellerInnerCode1(entity.getSellerInnerCode1())
            .setSellerInnerCode2(dto.getSellerInnerCode2()).setReceiverContact1(dto.getReceiverContact1())
            .setReceiverContact2(dto.getReceiverContact2()).setDestination(dto.getDestination())
            .setBuyerContact(dto.getBuyerContact()).setZipCode(dto.getZipCode()).setDeliveryMessage(dto.getDeliveryMessage())
            .setReleaseArea(dto.getReleaseArea()).setOrderDateTime(dto.getOrderDateTime())
            .setReleased(dto.getReleased()).setReleasedAt(dto.getReleasedAt()).setCreatedAt(dto.getCreatedAt())
            .setDeliveryReadyFileCid(dto.getDeliveryReadyFileCid());

        return entity;
    }
    
    /**
     * <b>Upload Excel File</b>
     * <p>
     * 엑셀 파일을 업로드하여 화면에 출력한다.
     * 
     * @param file : MultipartFile
     * @return List::DeliveryReadyItemDto::
     * @throws IOException
     */
    public List<DeliveryReadyItemDto> uploadDeliveryReadyExcelFile(MultipartFile file) throws IOException {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyItemDto> dtos = this.getDeliveryReadyExcelForm(sheet);

        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일의 데이터들을 Dto로 만든다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyItemDto::
     */
    private List<DeliveryReadyItemDto> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<DeliveryReadyItemDto> dtos = new ArrayList<>();

        for(int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyItemDto dto = new DeliveryReadyItemDto();

            dto.setId(UUID.randomUUID())
                .setProdOrderNumber(row.getCell(0).getStringCellValue())
                .setOrderNumber(row.getCell(1).getStringCellValue())
                .setSalesChannel(row.getCell(7).getStringCellValue())
                .setBuyer(row.getCell(8).getStringCellValue())
                .setBuyerId(row.getCell(9).getStringCellValue())
                .setReceiver(row.getCell(10).getStringCellValue())
                .setPaymentDate(row.getCell(14).getDateCellValue())
                .setProdNumber(row.getCell(15).getStringCellValue())
                .setProdName(row.getCell(16).getStringCellValue())
                .setOptionInfo(row.getCell(18).getStringCellValue())
                .setOptionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                .setUnit((int) row.getCell(20).getNumericCellValue())
                .setOrderConfirmationDate(row.getCell(27).getDateCellValue())
                .setShipmentDueDate(row.getCell(28).getDateCellValue())
                .setShipmentCostBundleNumber(row.getCell(32).getStringCellValue())
                .setSellerProdCode(row.getCell(37) != null ? row.getCell(37).getStringCellValue() : "")
                .setSellerInnerCode1(row.getCell(38) != null ? row.getCell(38).getStringCellValue() : "")
                .setSellerInnerCode2(row.getCell(39) != null ? row.getCell(39).getStringCellValue() : "")
                .setReceiverContact1(row.getCell(40).getStringCellValue())
                .setReceiverContact2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "")
                .setDestination(row.getCell(42).getStringCellValue())
                .setBuyerContact(row.getCell(43).getStringCellValue())
                .setZipCode(row.getCell(44).getStringCellValue())
                .setDeliveryMessage(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "")
                .setReleaseArea(row.getCell(46).getStringCellValue())
                .setOrderDateTime(row.getCell(56).getDateCellValue());

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
     * @return FileUploadResponse
     * @see ProductRepository#findById
     */
    public FileUploadResponse storeDeliveryReadyExcelFile(MultipartFile file, UUID userId) throws IOException {
        String fileName = file.getOriginalFilename();
        String uploadPath = bucket + "/naver-order";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        // AWS S3 업로드
        s3Client.putObject(new PutObjectRequest(uploadPath, fileName, file.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // file DB저장
        // DeliveryReadyFileEntity 생성
        DeliveryReadyFileEntity entity = this.createDeliveryReadyFileDto(s3Client.getUrl(uploadPath, fileName).toString(), fileName, (int)file.getSize(), userId);

        // DeliveryReadyFileDto fileDto = this.get(dto)

        // item 중복데이터 제거 후 items DB저장
        this.createDeliveryReadyItemData(file, this.getFileDtoByEntity(entity));
                                                      
        return new FileUploadResponse(fileName, s3Client.getUrl(uploadPath, fileName).toString(), file.getContentType(), file.getSize());
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
        DeliveryReadyFileDto fileDto = new DeliveryReadyFileDto();

        fileDto.setId(UUID.randomUUID()).setFilePath(filePath).setFileName(fileName)
                .setFileSize(fileSize).setFileExtension(FilenameUtils.getExtension(fileName))
                .setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId).setDeleted(false);

        return deliveryReadyFileRepository.save(convFileEntityByDto(fileDto));
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 정보를 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @return DeliveryReadyFileEntity
     * @throws IllegalArgumentException
     */
    public void createDeliveryReadyItemData(MultipartFile file, DeliveryReadyFileDto fileDto) throws IllegalArgumentException {

        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        this.getDeliveryReadyExcelData(sheet, fileDto);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복을 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @see DeliveryReadyItemRepository#findAllProdOrderNumber
     */
    private void getDeliveryReadyExcelData(Sheet worksheet, DeliveryReadyFileDto fileDto) {
        // List<DeliveryReadyItemDto> dtos = new ArrayList<>();
        List<DeliveryReadyItemEntity> entities = new ArrayList<>();

        Set<String> storedProdOrderNumber = deliveryReadyItemRepository.findAllProdOrderNumber();   // 상품 주문번호로 중복데이터를 구분

        for(int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyItemDto dto = new DeliveryReadyItemDto();

            dto.setId(UUID.randomUUID())
                .setProdOrderNumber(row.getCell(0).getStringCellValue())
                .setOrderNumber(row.getCell(1).getStringCellValue())
                .setSalesChannel(row.getCell(7).getStringCellValue())
                .setBuyer(row.getCell(8).getStringCellValue())
                .setBuyerId(row.getCell(9).getStringCellValue())
                .setReceiver(row.getCell(10).getStringCellValue())
                .setPaymentDate(row.getCell(14).getDateCellValue())
                .setProdNumber(row.getCell(15).getStringCellValue())
                .setProdName(row.getCell(16).getStringCellValue())
                .setOptionInfo(row.getCell(18).getStringCellValue())
                .setOptionManagementCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                .setUnit((int) row.getCell(20).getNumericCellValue())
                .setOrderConfirmationDate(row.getCell(27).getDateCellValue())
                .setShipmentDueDate(row.getCell(28).getDateCellValue())
                .setShipmentCostBundleNumber(row.getCell(32).getStringCellValue())
                .setSellerProdCode(row.getCell(37) != null ? row.getCell(37).getStringCellValue() : "")
                .setSellerInnerCode1(row.getCell(38) != null ? row.getCell(38).getStringCellValue() : "")
                .setSellerInnerCode2(row.getCell(39) != null ? row.getCell(39).getStringCellValue() : "")
                .setReceiverContact1(row.getCell(40).getStringCellValue())
                .setReceiverContact2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "")
                .setDestination(row.getCell(42).getStringCellValue())
                .setBuyerContact(row.getCell(43).getStringCellValue())
                .setZipCode(row.getCell(44).getStringCellValue())
                .setDeliveryMessage(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "")
                .setReleaseArea(row.getCell(46).getStringCellValue())
                .setOrderDateTime(row.getCell(56).getDateCellValue())
                .setReleased(false)
                .setCreatedAt(fileDto.getCreatedAt())
                .setDeliveryReadyFileCid(fileDto.getCid());

            // 상품주문번호가 중복되지 않는다면
            if(storedProdOrderNumber.add(dto.getProdOrderNumber())){
                DeliveryReadyItemEntity entity = this.convItemEntityByDto(dto);
                entities.add(entity);
            }
        }
        deliveryReadyItemRepository.saveAll(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 미출고 데이터를 조회한다.
     *
     * @return List::DeliveryReadyItemViewResDto::
     * @see deliveryReadyItemRepository#findAllUnreleased
     */
    public List<DeliveryReadyItemViewResDto> getDeliveryReadyViewUnreleasedData() {
        List<DeliveryReadyItemViewResDto> itemViewResDto = new ArrayList<>();
        List<DeliveryReadyItemViewProj> itemViewProj = deliveryReadyItemRepository.findAllUnreleased();
        
        // TODO :: 메소드 합치기
        for(DeliveryReadyItemViewProj proj : itemViewProj) {
            DeliveryReadyItemViewResDto dto = new DeliveryReadyItemViewResDto();

            dto.setDeliveryReadyItem(this.getItemDtoByEntity(proj.getDeliveryReadyItem()))
                .setOptionDefaultName(proj.getOptionDefaultName())
                .setOptionManagementName(proj.getOptionManagementName())
                .setOptionStockUnit(proj.getOptionStockUnit())
                .setProdManagementName(proj.getProdManagementName());

            itemViewResDto.add(dto);
        }
        return itemViewResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 선택된 기간의 출 데이터를 조회한다.
     *
     * @param date1 : String
     * @param date2 : String
     * @return List::DeliveryReadyItemViewResDto::
     * @see deliveryReadyItemRepository#findSelectedReleased
     */
    public List<DeliveryReadyItemViewResDto> getDeliveryReadyViewReleased(String date1, String date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;

        try{
            startDate = dateFormat.parse(date1);
            endDate = dateFormat.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<DeliveryReadyItemViewProj> releasedItems = deliveryReadyItemRepository.findSelectedReleased(startDate, endDate);
        List<DeliveryReadyItemViewResDto> itemViewResDto = new ArrayList<>();

        // TODO :: 메소드 합치기
        for(DeliveryReadyItemViewProj proj : releasedItems) {
            DeliveryReadyItemViewResDto dto = new DeliveryReadyItemViewResDto();

            dto.setDeliveryReadyItem(this.getItemDtoByEntity(proj.getDeliveryReadyItem()))
                .setOptionDefaultName(proj.getOptionDefaultName())
                .setOptionManagementName(proj.getOptionManagementName())
                .setOptionStockUnit(proj.getOptionStockUnit())
                .setProdManagementName(proj.getProdManagementName());

            itemViewResDto.add(dto);
        }
        return itemViewResDto;
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemId에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#delete
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyItemRepository.findById(itemCid).ifPresent(item -> {
            deliveryReadyItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param itemCid : Integer
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#delete
     */
    public void updateReleasedDeliveryReadyItem(Integer itemCid) {
        deliveryReadyItemRepository.findById(itemCid).ifPresentOrElse(item -> {
            item.setReleased(false).setReleasedAt(null);

            deliveryReadyItemRepository.save(item);
        }, null);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see deliveryReadyItemRepository#findAllOptionInfo
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchDeliveryReadyItemOptionInfo() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyItemRepository.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = new ArrayList<>();

        for(DeliveryReadyItemOptionInfoProj proj : optionInfoProjs) {
            DeliveryReadyItemOptionInfoResDto dto = new DeliveryReadyItemOptionInfoResDto();

            dto.setOptionCode(proj.getOptionCode())
                .setOptionDefaultName(proj.getOptionDefaultName())
                .setOptionManagementName(proj.getOptionManagementName())
                .setProdDefaultName(proj.getProdDefaultName());

            optionInfoDto.add(dto);
        }
        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터의 옵션관리코드를 수정한다.
     *
     * @param itemCid : Integer, optionCode : String
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#save
     */
    public void updateDeliveryReadyItemOptionInfo(Integer itemCid, String optionCode) {
        deliveryReadyItemRepository.findById(itemCid).ifPresentOrElse(item -> {
            if(optionCode.equals("null")) {
                item.setOptionManagementCode("");
            }
            else{
                item.setOptionManagementCode(optionCode);
            }
            deliveryReadyItemRepository.save(item);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param itemCid : Integer
     * @param optionCode : String
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#save
     */
    public void updateDeliveryReadyItemsOptionInfo(Integer itemCid, String optionCode) {
        deliveryReadyItemRepository.findById(itemCid).ifPresentOrElse(item -> {
            if(optionCode.equals("null")) {
                item.setOptionManagementCode("");
            }
            else{
                item.setOptionManagementCode(optionCode);
            }
            deliveryReadyItemRepository.save(item);

            // 같은 상품의 옵션을 모두 변경
            this.updateDeliveryReadyItemChangedOption(item, optionCode);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param item : DeliveryReadyItemEntity
     * @param optionCode : String
     * @see deliveryReadyItemRepository#findByItems
     * @see deliveryReadyItemRepository#delete
     */
    public void updateDeliveryReadyItemChangedOption(DeliveryReadyItemEntity item, String optionCode) {
        List<DeliveryReadyItemEntity> entities = deliveryReadyItemRepository.findByItems(item.getProdName(), item.getOptionInfo());

        for(DeliveryReadyItemEntity entity : entities) {
            if(optionCode.equals("null")) {
                entity.setOptionManagementCode("");
            }
            else{
                entity.setOptionManagementCode(optionCode);
            }
            deliveryReadyItemRepository.save(entity);
        }
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param dtos : List::DeliveryReadyItemViewDto::
     * @return List::DeliveryReadyItemExcelFormDto::
     */
    public List<DeliveryReadyItemExcelFormDto> changeDeliveryReadyItem(List<DeliveryReadyItemViewDto> viewDtos) {
        List<DeliveryReadyItemDto> dtos = new ArrayList<>();

        // DeliveryReadyItemViewDto에서 DeliveryReadyItemEntity만 뽑아낸다
        for(DeliveryReadyItemViewDto viewDto : viewDtos) {
            dtos.add(this.getItemDtoByEntity(viewDto.getDeliveryReadyItem()));
        }

        // DeliveryReadyItemDto로 DeliveryReadyItemExcelFromDto를 만든다
        List<DeliveryReadyItemExcelFormDto> formDtos = getFromDtoByDto(dtos);

        return changeDuplicationDtos(formDtos);
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyItemEntity => DeliveryReadyItemExcelFormDto
     *
     * @param entities : List::DeliveryReadyItemEntity::
     * @return List::DeliveryReadyItemExcelFormDto::
     */
    public List<DeliveryReadyItemExcelFormDto> getFromDtoByDto(List<DeliveryReadyItemDto> dtos) {
        List<DeliveryReadyItemExcelFormDto> formDtos = new ArrayList<>();

        for(DeliveryReadyItemDto dto : dtos){
            DeliveryReadyItemExcelFormDto formDto = new DeliveryReadyItemExcelFormDto();

            formDto.setProdOrderNumber(dto.getProdOrderNumber()).setOrderNumber(dto.getOrderNumber())
                    .setReceiver(dto.getReceiver()).setReceiverContact1(dto.getReceiverContact1())
                    .setZipCode(dto.getZipCode()).setDestination(dto.getDestination())
                    .setTransportNumber("").setProdName(dto.getProdName())
                    .setSender("스토어명").setSenderContact1("070-0000-0000").setOptionInfo(dto.getOptionInfo())
                    .setOptionManagementCode(dto.getOptionManagementCode()).setUnit(dto.getUnit())
                    .setDeliveryMessage(dto.getDeliveryMessage()).setUnitA("").setAllProdOrderNumber(dto.getProdNumber()).setDuplication(false);

            formDtos.add(formDto);
        }

        return formDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * (주문번호 + 받는사람 + 상품명 + 상품상세) 중복데이터 가공
     *
     * @param dtos : List::DeliveryReadyItemExcelFormDto::
     * @return List::DeliveryReadyItemExcelFormDto::
     */
    public List<DeliveryReadyItemExcelFormDto> changeDuplicationDtos(List<DeliveryReadyItemExcelFormDto> dtos) {
        List<DeliveryReadyItemExcelFormDto> newOrderList = new ArrayList<>();

        // 주문번호 > 받는사람 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing(DeliveryReadyItemExcelFormDto::getOrderNumber)
                                .thenComparing(DeliveryReadyItemExcelFormDto::getReceiver)
                                .thenComparing(DeliveryReadyItemExcelFormDto::getProdName)
                                .thenComparing(DeliveryReadyItemExcelFormDto::getOptionInfo));

        Set<String> optionSet = new HashSet();        // 받는사람 + 주소 + 상품명 + 상품상세

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

            // 받는사람 + 번호 + 주소 : 중복인 경우
            if(!optionSet.add(receiverStr)){
                newOrderList.get(prevOrderIdx).setDuplication(true);
                dtos.get(i).setDuplication(true);
            }

            // 받는사람 + 주소 + 상품명 + 상품상세 : 중복인 경우
            if(!optionSet.add(resultStr)){
                DeliveryReadyItemExcelFormDto prevProd = newOrderList.get(prevOrderIdx);
                DeliveryReadyItemExcelFormDto currentProd = dtos.get(i);

                newOrderList.get(prevOrderIdx).setUnit(prevProd.getUnit() + currentProd.getUnit());     // 중복데이터의 수량을 더한다
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getProdOrderNumber() + " / " + currentProd.getProdOrderNumber());     // 총 상품번호 수정
            }else{
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
     * @param dtos : List::DeliveryReadyItemViewDto::
     * @see deliveryReadyItemRepository#updateReleasedAtByCid
     */
    @Transactional
    public void releasedDeliveryReadyItem(List<DeliveryReadyItemViewDto> dtos) {

        List<Integer> cidList = new ArrayList<>();
        
        for(DeliveryReadyItemViewDto dto : dtos){
            cidList.add(dto.getDeliveryReadyItem().getCid());
        }
        deliveryReadyItemRepository.updateReleasedAtByCid(cidList, dateHandler.getCurrentDate());
    }
}