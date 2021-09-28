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

import com.piaar_store_manager.server.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
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
     * @return List::DeliveryReadyItemDto::
     * @throws IOException
     */
    public List<DeliveryReadyItemDto> uploadDeliveryReadyExcelFile(MultipartFile file) {
        Workbook workbook = null;
        
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
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

        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyItemDto dto = DeliveryReadyItemDto.builder().id(UUID.randomUUID())
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
     * @return FileUploadResponse
     * @see ProductRepository#findById
     */
    public FileUploadResponse storeDeliveryReadyExcelFile(MultipartFile file, UUID userId) {
        String fileName = file.getOriginalFilename();
        String uploadPath = bucket + "/naver-order";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        try{
            // AWS S3 업로드
            s3Client.putObject(new PutObjectRequest(uploadPath, fileName, file.getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        // file DB저장
        // DeliveryReadyFileEntity 생성
        DeliveryReadyFileEntity entity = this.createDeliveryReadyFileDto(s3Client.getUrl(uploadPath, fileName).toString(), fileName, (int)file.getSize(), userId);

        // item 중복데이터 제거 후 items DB저장
        this.createDeliveryReadyItemData(file, DeliveryReadyFileDto.toDto(entity));
                                                      
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
     * @return DeliveryReadyFileEntity
     * @throws IllegalArgumentException
     */
    public void createDeliveryReadyItemData(MultipartFile file, DeliveryReadyFileDto fileDto) {

        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyItemDto> dtos = this.getDeliveryReadyExcelData(sheet, fileDto);
        List<DeliveryReadyItemEntity> entities = new ArrayList<>();
        
        dtos.sort(Comparator.comparing(DeliveryReadyItemDto::getProdName)
                .thenComparing(DeliveryReadyItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyItemDto::getReceiver));

        for(DeliveryReadyItemDto dto : dtos) {
            entities.add(DeliveryReadyItemEntity.toEntity(dto));
        }

        deliveryReadyItemRepository.saveAll(entities);
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
    private List<DeliveryReadyItemDto> getDeliveryReadyExcelData(Sheet worksheet, DeliveryReadyFileDto fileDto) {
        List<DeliveryReadyItemDto> dtos = new ArrayList<>();

        Set<String> storedProdOrderNumber = deliveryReadyItemRepository.findAllProdOrderNumber();   // 상품 주문번호로 중복데이터를 구분

        for(int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyItemDto dto = DeliveryReadyItemDto.builder()
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
     * @return List::DeliveryReadyItemViewResDto::
     * @see deliveryReadyItemRepository#findAllUnreleased
     */
    public List<DeliveryReadyItemViewResDto> getDeliveryReadyViewUnreleasedData() {
        List<DeliveryReadyItemViewProj> itemViewProj = deliveryReadyItemRepository.findSelectedUnreleased();
        List<DeliveryReadyItemViewResDto> itemViewResDto = DeliveryReadyItemViewProj.toResDtos(itemViewProj);

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
     * @throws ParseException
     * @see deliveryReadyItemRepository#findSelectedReleased
     */
    public List<DeliveryReadyItemViewResDto> getDeliveryReadyViewReleased(Map<String, Object> query) throws ParseException {
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

        List<DeliveryReadyItemViewProj> itemViewProj = deliveryReadyItemRepository.findSelectedReleased(startDate, endDate);
        List<DeliveryReadyItemViewResDto> itemViewResDto = DeliveryReadyItemViewProj.toResDtos(itemViewProj);

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
     * @param dto : DeliveryReadyItemDto
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#delete
     */
    public void updateReleasedDeliveryReadyItem(DeliveryReadyItemDto dto) {
        deliveryReadyItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
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
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = DeliveryReadyItemOptionInfoProj.toResDtos(optionInfoProjs);

        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터의 옵션관리코드를 수정한다.
     *
     * @param dto : DeliveryReadyItemDto
     * @param query : Map[optionCode]
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#save
     */
    public void updateDeliveryReadyItemOptionInfo(DeliveryReadyItemDto dto) {

        deliveryReadyItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            
            item.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

            deliveryReadyItemRepository.save(item);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param dto : DeliveryReadyItemDto
     * @see deliveryReadyItemRepository#findById
     * @see deliveryReadyItemRepository#save
     */
    public void updateDeliveryReadyItemsOptionInfo(DeliveryReadyItemDto dto) {

        deliveryReadyItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            
            item.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

            deliveryReadyItemRepository.save(item);

            // 같은 상품의 옵션을 모두 변경
            this.updateDeliveryReadyItemChangedOption(item);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param item : DeliveryReadyItemEntity
     * @see deliveryReadyItemRepository#findByItems
     * @see deliveryReadyItemRepository#delete
     */
    public void updateDeliveryReadyItemChangedOption(DeliveryReadyItemEntity item) {
        List<DeliveryReadyItemEntity> entities = deliveryReadyItemRepository.findByItems(item.getProdName(), item.getOptionInfo());

        for(DeliveryReadyItemEntity entity : entities) {
            entity.setOptionManagementCode(item.getOptionManagementCode());

            deliveryReadyItemRepository.save(entity);
        }
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem 다운로드 시 중복데이터 처리 및 셀 색상을 지정한다.
     *
     * @param viewDtos : List::DeliveryReadyItemViewDto::
     * @return List::DeliveryReadyItemExcelFormDto::
     */
    public List<DeliveryReadyItemExcelFormDto> changeDeliveryReadyItem(List<DeliveryReadyItemViewDto> viewDtos) {
        List<DeliveryReadyItemExcelFormDto> formDtos = new ArrayList<>();

        // DeliveryReadyItemViewDto로 DeliveryReadyItemExcelFromDto를 만든다
        for(DeliveryReadyItemViewDto viewDto : viewDtos) {
            formDtos.add(DeliveryReadyItemExcelFormDto.toFormDto(viewDto));
        }

        // 중복데이터 처리
        List<DeliveryReadyItemExcelFormDto> excelFormDtos = changeDuplicationDtos(formDtos);

        return excelFormDtos;
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

        // 받는사람 > 주문번호 > 상품명 > 상품상세 정렬
        dtos.sort(Comparator.comparing(DeliveryReadyItemExcelFormDto::getReceiver)
                                .thenComparing(DeliveryReadyItemExcelFormDto::getOrderNumber)
                                .thenComparing(DeliveryReadyItemExcelFormDto::getProdName)
                                .thenComparing(DeliveryReadyItemExcelFormDto::getOptionInfo));

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
