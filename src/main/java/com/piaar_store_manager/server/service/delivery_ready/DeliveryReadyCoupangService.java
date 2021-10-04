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
import java.util.Locale;
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
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.coupang.repository.DeliveryReadyCoupangItemRepository;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyFileDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
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
public class DeliveryReadyCoupangService {
    
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
    private DeliveryReadyCoupangItemRepository deliveryReadyCoupangItemRepository;

    @Autowired
    private DeliveryReadyNaverService deliveryReadyNaverItemService;

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
     * @throws ParseException
     * @throws IllegalArgumentException
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
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();

        try {
            dtos = this.getDeliveryReadyExcelForm(sheet);
        } catch (ParseException e) {
            throw new ParseException("date format parse error", -1);
        }

        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일의 데이터들을 Dto로 만든다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyCoupangItemDto::
     * @throws ParseException
     */
    private List<DeliveryReadyCoupangItemDto> getDeliveryReadyExcelForm(Sheet worksheet) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            if (row == null) break;

            String prodOrderNumber = null;

            if((row.getCell(2) != null) && (row.getCell(13) != null) && (row.getCell(14) != null)){
                prodOrderNumber = row.getCell(2).getStringCellValue() + " | " + row.getCell(13).getStringCellValue() + " | " + row.getCell(14).getStringCellValue();
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
                    .shipmentDueDate(row.getCell(7) != null ? dateFormat.parse(row.getCell(7).getStringCellValue()) : new Date())
                    .shipmentCostBundleNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                    .receiverContact1(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                    .destination(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                    .buyerContact(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                    .zipCode(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                    .orderDateTime(row.getCell(9) != null ? dateFormat2.parse(row.getCell(9).getStringCellValue()) : new Date()).build();

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
     * @throws ParseException
     * @see DeliveryReadyNaverItemService#createDeliveryReadyFileDto
     */
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

        // file DB저장
        // DeliveryReadyFileEntity 생성
        DeliveryReadyFileEntity entity = deliveryReadyNaverItemService.createDeliveryReadyFileDto(s3Client.getUrl(uploadPath, newFileName).toString(), newFileName, (int)file.getSize(), userId);

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
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 정보를 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @throws IllegalArgumentException
     * @throws ParseException
     * @see DeliveryReadyCoupangItemRepository#saveAll
     */
    public void createDeliveryReadyItemData(MultipartFile file, DeliveryReadyFileDto fileDto) throws ParseException {

        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();
        
        try {
            dtos = this.getDeliveryReadyCoupangExcelData(sheet, fileDto);
        } catch (ParseException e) {
            throw new ParseException("date format parse error", -1);
        }

        List<DeliveryReadyCoupangItemEntity> entities = new ArrayList<>();
        
        dtos.sort(Comparator.comparing(DeliveryReadyCoupangItemDto::getProdName)
                .thenComparing(DeliveryReadyCoupangItemDto::getOptionInfo)
                .thenComparing(DeliveryReadyCoupangItemDto::getReceiver));

        for(DeliveryReadyCoupangItemDto dto : dtos) {
            entities.add(DeliveryReadyCoupangItemEntity.toEntity(dto));
        }

        deliveryReadyCoupangItemRepository.saveAll(entities);
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
    private List<DeliveryReadyCoupangItemDto> getDeliveryReadyCoupangExcelData(Sheet worksheet, DeliveryReadyFileDto fileDto) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        

        Set<String> storedProdOrderNumber = deliveryReadyCoupangItemRepository.findAllProdOrderNumber();   // 상품 주문번호로 중복데이터를 구분


        for(int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            if(row == null) break;

            String prodOrderNumber = null;
            
            if((row.getCell(2) != null) && (row.getCell(13) != null) && (row.getCell(14) != null)){
                prodOrderNumber = row.getCell(2).getStringCellValue() + " | " + row.getCell(13).getStringCellValue() + " | " + row.getCell(14).getStringCellValue();
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
                .shipmentDueDate(row.getCell(7) != null ? dateFormat.parse(row.getCell(7).getStringCellValue()) : new Date())
                .shipmentCostBundleNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                .receiverContact1(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                .destination(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                .buyerContact(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                .zipCode(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                .deliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                .orderDateTime(row.getCell(9) != null ? dateFormat2.parse(row.getCell(9).getStringCellValue()) : new Date())
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
     * @return List::DeliveryReadyCoupangItemViewResDto::
     * @see DeliveryReadyCoupangItemRepository#findSelectedUnreleased
     * @see DeliveryReadyCoupangitemViewProj#toResDtos
     */
    public List<DeliveryReadyCoupangItemViewResDto> getDeliveryReadyViewUnreleasedData() {
        List<DeliveryReadyCoupangItemViewProj> itemViewProj = deliveryReadyCoupangItemRepository.findSelectedUnreleased();
        List<DeliveryReadyCoupangItemViewResDto> itemViewResDto = DeliveryReadyCoupangItemViewProj.toResDtos(itemViewProj);

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
     * @see DeliveryReadyCoupangItemRepository#findSelectedReleased
     * @see DeliveryReadyCoupangItemViewProj#toResDtos
     */
    public List<DeliveryReadyCoupangItemViewResDto> getDeliveryReadyViewReleased(Map<String, Object> query) throws ParseException {
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

        List<DeliveryReadyCoupangItemViewProj> itemViewProj = deliveryReadyCoupangItemRepository.findSelectedReleased(startDate, endDate);
        List<DeliveryReadyCoupangItemViewResDto> itemViewResDto = DeliveryReadyCoupangItemViewProj.toResDtos(itemViewProj);

        return itemViewResDto;
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemId에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see DeliveryReadyCoupangItemRepository#findById
     * @see DeliveryReadyCoupangItemRepository#delete
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyCoupangItemRepository.findById(itemCid).ifPresent(item -> {
            deliveryReadyCoupangItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangItemRepository#findById
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public void updateReleasedDeliveryReadyItem(DeliveryReadyCoupangItemDto dto) {
        deliveryReadyCoupangItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            item.setReleased(false).setReleasedAt(null);

            deliveryReadyCoupangItemRepository.save(item);
        }, null);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyCoupangItemRepository#findAllOptionInfo
     * @see DeliveryReadyItemOptionInfoProj#toResDtos
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchDeliveryReadyItemOptionInfo() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyCoupangItemRepository.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = DeliveryReadyItemOptionInfoProj.toResDtos(optionInfoProjs);

        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터의 옵션관리코드를 수정한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangItemRepository#findById
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public void updateDeliveryReadyItemOptionInfo(DeliveryReadyCoupangItemDto dto) {

        deliveryReadyCoupangItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            
            item.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

            deliveryReadyCoupangItemRepository.save(item);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 중 itemId에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangItemRepository#findById
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public void updateDeliveryReadyItemsOptionInfo(DeliveryReadyCoupangItemDto dto) {

        deliveryReadyCoupangItemRepository.findById(dto.getCid()).ifPresentOrElse(item -> {
            
            item.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");

            deliveryReadyCoupangItemRepository.save(item);

            // 같은 상품의 옵션을 모두 변경
            this.updateDeliveryReadyItemChangedOption(item);

        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 출고 데이터 중 itemId에 대응하는 데이터를 미출고 데이터로 변경한다.
     *
     * @param item : DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangItemRepository#findByItems
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public void updateDeliveryReadyItemChangedOption(DeliveryReadyCoupangItemEntity item) {
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangItemRepository.findByItems(item.getProdName(), item.getOptionInfo());

        for(DeliveryReadyCoupangItemEntity entity : entities) {
            entity.setOptionManagementCode(item.getOptionManagementCode());

            deliveryReadyCoupangItemRepository.save(entity);
        }
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
        List<DeliveryReadyItemHansanExcelFormDto> formDtos = new ArrayList<>();

        // DeliveryReadyCoupangItemViewDto로 DeliveryReadyItemExcelFromDto를 만든다
        for(DeliveryReadyCoupangItemViewDto viewDto : viewDtos) {
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
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangItemRepository#updateReleasedAtByCid
     */
    @Transactional
    public void releasedDeliveryReadyItem(List<DeliveryReadyCoupangItemViewDto> dtos) {

        List<Integer> cidList = new ArrayList<>();
        
        for(DeliveryReadyCoupangItemViewDto dto : dtos){
            cidList.add(dto.getDeliveryReadyItem().getCid());
        }
        deliveryReadyCoupangItemRepository.updateReleasedAtByCid(cidList, dateHandler.getCurrentDate());
    }
}
