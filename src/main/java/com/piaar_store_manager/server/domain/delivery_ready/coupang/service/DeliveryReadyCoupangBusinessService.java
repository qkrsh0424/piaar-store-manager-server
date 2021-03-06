package com.piaar_store_manager.server.domain.delivery_ready.coupang.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemLotteExcelFormDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemOptionInfoResDto;
import com.piaar_store_manager.server.domain.delivery_ready.common.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewResDto;
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
import com.piaar_store_manager.server.utils.DateHandler;

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
     * ?????? ????????? ??????????????? ????????? ????????????.
     * 
     * @param file : MultipartFile
     * @return List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangBusinessService#getDeliveryReadyExcelForm
     */
    public List<DeliveryReadyCoupangItemDto> uploadDeliveryReadyExcelFile(MultipartFile file) throws ParseException {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<DeliveryReadyCoupangItemDto> dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ????????? ????????????(?????? ???????????? ??????)??? ??????????????? Dto??? ????????????.
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
                    .orderDateTime(row.getCell(9) != null ? DateHandler.getUtcDate(dateFormat2.parse(row.getCell(9).getStringCellValue())) : new Date())
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
     * <b>S3 Upload Setting Related Method</b>
     * <p>
     * AWS S3 ?????? ?????????
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
     * ???????????? ??????????????? S3 ??? DB??? ????????????.
     *
     * @param file : MultipartFile
     * @throws ParseException
     * @see DeliveryReadyCoupangBusinessService#createDeliveryReadyExcelFile
     * @see DeliveryReadyCoupangBusinessService#createDeliveryReadyExcelItem
     */
    @Transactional
    public void storeDeliveryReadyExcelFile(MultipartFile file) throws ParseException {
        String fileName = file.getOriginalFilename();
        String newFileName = "[COUPANG_delivery_ready]" + UUID.randomUUID().toString().replaceAll("-", "") + fileName;
        String uploadPath = bucket + "/coupang-order";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        try{
            // AWS S3 ?????????
            s3Client.putObject(new PutObjectRequest(uploadPath, newFileName, file.getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        // ?????? ??????
        DeliveryReadyFileDto fileDto = this.createDeliveryReadyExcelFile(s3Client.getUrl(uploadPath, newFileName).toString(), newFileName, (int)file.getSize());
        // ????????? ??????
        this.createDeliveryReadyExcelItem(file, fileDto);
    }

    /**
     * <b>Create FileDto Method</b>
     * <p>
     * ?????? 
     *
     * @param filePath : String
     * @param fileName : String
     * @param fileSize : Integer
     * @return DeliveryReadyFileDto
     * @see DeliveryReadyCoupangService#createFile
     * @see DeliveryReadyFileDto#toDto
     */
    public DeliveryReadyFileDto createDeliveryReadyExcelFile(String filePath, String fileName, Integer fileSize) {
        UUID USER_ID = userService.getUserId();

        // File data ?????? ??? ??????
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

        DeliveryReadyFileEntity entity = deliveryReadyCoupangService.createFile(DeliveryReadyFileEntity.toEntity(fileDto));
        DeliveryReadyFileDto dto = DeliveryReadyFileDto.toDto(entity);
        return dto;
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * ???????????? ?????? ?????? ??????????????? ????????? ????????????.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @throws IllegalArgumentException
     * @throws ParseException
     * @see DeliveryReadyCoupangItemEntity#toEntity
     * @see DeliveryReadyCoupangService#createItemList
     */
    public void createDeliveryReadyExcelItem(MultipartFile file, DeliveryReadyFileDto fileDto) throws ParseException {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
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
     * ???????????? ?????? ?????? ??????????????? ????????? ???????????? DB??? ????????????.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     * @return List::DeliveryReadyCoupangItemDto::
     */
    private List<DeliveryReadyCoupangItemDto> getDeliveryReadyCoupangExcelItem(Sheet worksheet, DeliveryReadyFileDto fileDto) throws ParseException {
        List<DeliveryReadyCoupangItemDto> dtos = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        // ????????????????????? ?????? ????????????.
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
                .createdAt(DateHandler.getCurrentDate())
                .releaseCompleted(false)
                .deliveryReadyFileCid(fileDto.getCid())
                .build();

            // ????????????????????? ???????????? ???????????? ???????????? ????????????.
            if(storedProdOrderNumber.add(dto.getProdOrderNumber())){
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem ??? ????????? ???????????? ????????????.
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
     * DeliveryReadyItem ??? ????????? ????????? ??? ???????????? ????????????.
     *
     * @param query : Map[startDate, endDate]
     * @return List::DeliveryReadyCoupangItemViewResDto::
     * @throws ParseException
     * @see DeliveryReadyCoupangService#findSelectedReleased
     * @see DeliveryReadyCoupangBusinessService#changeOptionStockUnit
     */
    public List<DeliveryReadyCoupangItemViewResDto> getDeliveryReadyViewReleased(Map<String, Object> query) throws ParseException {
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
     * ItemViewProj??? ?????? ??????????????? StockSumUnit(??? ?????? ?????? - ??? ?????? ??????)?????? ??????.
     *
     * @param itemViewProj : List::DeliveryReadyCoupangItemViewProj::
     * @return List::DeliveryReadyCoupangItemViewResDto::
     */
    public List<DeliveryReadyCoupangItemViewResDto> changeOptionStockUnit(List<DeliveryReadyCoupangItemViewProj> itemViewProj) {
        List<String> optionCodes = itemViewProj.stream().map(r -> r.getDeliveryReadyItem().getReleaseOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByOptionCodes(optionCodes);
        List<DeliveryReadyCoupangItemViewResDto> itemViewResDto = new ArrayList<>();

        if(optionGetDtos.isEmpty()) {
            itemViewResDto = itemViewProj.stream().map(proj -> DeliveryReadyCoupangItemViewResDto.toResDto(proj)).collect(Collectors.toList());
            return itemViewResDto;
        }

        // ?????? ??????????????? StockSumUnit(??? ?????? ?????? - ??? ?????? ??????)?????? ??????.
        itemViewResDto = itemViewProj.stream().map(proj -> {
            DeliveryReadyCoupangItemViewResDto resDto = DeliveryReadyCoupangItemViewResDto.toResDto(proj);
            
            // ?????? ??????????????? ???????????? ?????? ??????????????? getReleaseOptionCode??? null??????.
            if(proj.getDeliveryReadyItem().getReleaseOptionCode() == null) return resDto;

            // ?????? ????????? ????????? ????????? ??????????????? ????????????
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
     * DeliveryReadyItem ????????? ????????? ??? itemCid??? ???????????? ???????????? ????????????.
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
     * DeliveryReadyItem ????????? ????????? ??? ????????? ???????????? ?????? ????????????.
     *
     * @param dtos : List::DeliveryReadyCoupangItemDto::
     * @see DeliveryReadyCoupangItemEntity#toEntity
     * @see DeliveryReadyCoupangService#deleteOneDeliveryReadyViewData
     */
    @Transactional
    public void deleteListDeliveryReadyViewData(List<DeliveryReadyCoupangItemDto> dtos) {
        List<UUID> idList = dtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        deliveryReadyCoupangService.deleteListDeliveryReadyViewData(idList);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem??? ?????? ????????? ??? itemId??? ???????????? ???????????? ????????? ???????????? ????????????.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     */
    public void updateReleasedDeliveryReadyItem(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());
        entity.setReleased(false).setReleasedAt(null);

        deliveryReadyCoupangService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem??? ?????? ????????? ??? ????????? ??????????????? ????????? ???????????? ????????????.
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
     * ????????? ?????? ????????? ?????????????????? ????????????.
     *
     * @return List::DeliveryReadyItemOptionInfoResDto::
     * @see DeliveryReadyCoupangService#findAllOptionInfo
     */
    public List<DeliveryReadyItemOptionInfoResDto> searchDeliveryReadyItemOptionInfo() {
        List<DeliveryReadyItemOptionInfoProj> optionInfoProjs = deliveryReadyCoupangService.findAllOptionInfo();
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = optionInfoProjs.stream().map(proj -> DeliveryReadyItemOptionInfoProj.toResDto(proj)).collect(Collectors.toList());
        return optionInfoDto;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem??? ????????? ??? itemId??? ???????????? ???????????? ????????????????????? ????????????.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     * @see DeliveryReadyCoupangService#createItem
     */
    public void updateDeliveryReadyItemOptionInfo(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());
    
        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "")
            .setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");
        deliveryReadyCoupangService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem??? ????????? ??? itemId??? ???????????? ???????????? ????????? ???????????? ????????????????????? ?????? ????????????.
     * ????????????????????? ??????????????? ????????????????????? ?????? ????????????.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     * @see DeliveryReadyCoupangService#createItem
     */
    public void updateDeliveryReadyItemsOptionInfo(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());

        entity.setOptionManagementCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "")
            .setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getOptionManagementCode() : "");
        deliveryReadyCoupangService.createItem(entity);

        // ?????? ????????? ????????? ?????? ??????
        this.updateChangedOption(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem??? ????????? ??? itemId??? ???????????? ???????????? ????????????????????? ????????????.
     * ????????????????????? ??????????????? ????????????????????? ?????? ????????????.
     *
     * @param dto : DeliveryReadyCoupangItemDto
     * @see DeliveryReadyCoupangService#searchDeliveryReadyItem
     * @see DeliveryReadyCoupangService#createItem
     */
    public void updateDeliveryReadyItemReleaseOptionInfo(DeliveryReadyCoupangItemDto dto) {
        DeliveryReadyCoupangItemEntity entity = deliveryReadyCoupangService.searchDeliveryReadyItem(dto.getCid());
        entity.setReleaseOptionCode(dto.getOptionManagementCode() != null ? dto.getReleaseOptionCode() : "");

        deliveryReadyCoupangService.createItem(entity);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem??? ?????? ????????? ??? itemId??? ???????????? ???????????? ????????? ???????????? ????????????.
     *
     * @param entity : DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangService#findByItems
     * @see DeliveryReadyCoupangService#createItemList
     */
    public void updateChangedOption(DeliveryReadyCoupangItemEntity entity) {
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.findByItems(entity);
        entities.stream().forEach(r -> { r.setOptionManagementCode(entity.getOptionManagementCode()).setReleaseOptionCode(entity.getReleaseOptionCode()); });
        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * DeliveryReadyItem ???????????? ??? ??????????????? ?????? ??? ??? ????????? ????????????.
     *
     * @param viewDtos : List::DeliveryReadyCoupangItemViewDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     * @see DeliveryReadyCoupangBusinessService#changeDuplicationHansanDtos
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDeliveryReadyItemToHansan(List<DeliveryReadyCoupangItemViewDto> viewDtos) {
        List<DeliveryReadyItemHansanExcelFormDto> excelFormDtos = this.changeDuplicationHansanDtos(viewDtos);     // ?????? ????????? ??????
        return excelFormDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * (???????????? + ???????????? + ?????????(????????? ???????????????) + ????????????(????????? ???????????????)) ??????????????? ??????
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @return List::DeliveryReadyItemHansanExcelFormDto::
     */
    public List<DeliveryReadyItemHansanExcelFormDto> changeDuplicationHansanDtos(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<DeliveryReadyItemHansanExcelFormDto> newOrderList = new ArrayList<>();

        // ???????????? > ???????????? > ????????? > ???????????? ??????
        dtos.sort(Comparator.comparing((DeliveryReadyCoupangItemViewDto dto) -> dto.getDeliveryReadyItem().getReceiver())
                                .thenComparing((DeliveryReadyCoupangItemViewDto dto) -> dto.getDeliveryReadyItem().getOrderNumber())
                                .thenComparing((DeliveryReadyCoupangItemViewDto dto) -> dto.getProdManagementName())
                                .thenComparing((DeliveryReadyCoupangItemViewDto dto) -> dto.getOptionManagementName()));

        Set<String> optionSet = new HashSet<>();        // ???????????? + ?????? + ????????? + ????????????

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
            int prevOrderIdx = newOrderList.size()-1;   // ???????????? ????????? ???????????? ????????? index

            DeliveryReadyItemHansanExcelFormDto currentProd = DeliveryReadyItemHansanExcelFormDto.toFormDto(dtos.get(i));
            
            // ???????????? + ?????? + ????????? + ???????????? : ????????? ??????
            if(!optionSet.add(resultStr)){
                DeliveryReadyItemHansanExcelFormDto prevProd = newOrderList.get(prevOrderIdx);
                
                newOrderList.get(prevOrderIdx).setUnit(prevProd.getUnit() + currentProd.getUnit());     // ?????????????????? ????????? ?????????
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getProdOrderNumber() + "/" + currentProd.getProdOrderNumber());     // ??? ???????????? ??????
            }else{
                // ???????????? + ?????? + ?????? : ????????? ??????
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
     * DeliveryReadyItem ???????????? ??? ??????????????? ?????? ??? ??? ????????? ????????????.
     *
     * @param viewDtos : List::DeliveryReadyCoupangItemViewDto::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     * @see DeliveryReadyItemLotteExcelFormDto#toFormDto
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeDeliveryReadyItemToLotte(List<DeliveryReadyCoupangItemViewDto> viewDtos) {
        List<DeliveryReadyItemLotteExcelFormDto> formDtos = viewDtos.stream().map(dto -> DeliveryReadyItemLotteExcelFormDto.toFormDto(dto)).collect(Collectors.toList());
        List<DeliveryReadyItemLotteExcelFormDto> excelFormDtos = this.changeDuplicationLotteDtos(formDtos);     // ?????? ????????? ??????
        return excelFormDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * (???????????? + ?????? + ????????? + ????????????) ??????????????? ??????
     * (???????????? + ????????? + ??????) ??????????????? ??????
     *
     * @param dtos : List::DeliveryReadyItemLotteExcelFormDto::
     * @return List::DeliveryReadyItemLotteExcelFormDto::
     */
    public List<DeliveryReadyItemLotteExcelFormDto> changeDuplicationLotteDtos(List<DeliveryReadyItemLotteExcelFormDto> dtos) {
        List<DeliveryReadyItemLotteExcelFormDto> newOrderList = new ArrayList<>();
        List<DeliveryReadyItemLotteExcelFormDto> resultList = new ArrayList<>();

        // ???????????? > ?????? > ????????? > ???????????? ??????
        dtos.sort(Comparator.comparing(DeliveryReadyItemLotteExcelFormDto::getReceiver)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getDestination)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getProdName1)
                .thenComparing(DeliveryReadyItemLotteExcelFormDto::getOptionInfo1));

        Set<String> optionSet = new HashSet<>(); // ???????????? + ?????? + ????????? + ????????????

        for (int i = 0; i < dtos.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(dtos.get(i).getReceiver());
            sb.append(dtos.get(i).getDestination());
            sb.append(dtos.get(i).getProdName1());
            sb.append(dtos.get(i).getOptionInfo1());

            String resultStr = sb.toString();
            int prevOrderIdx = newOrderList.size() - 1;     // ???????????? ????????? ???????????? ????????? index

            // ???????????? + ?????? + ????????? + ???????????? : ????????? ??????
            if (!optionSet.add(resultStr)) {
                DeliveryReadyItemLotteExcelFormDto prevProd = newOrderList.get(prevOrderIdx);
                DeliveryReadyItemLotteExcelFormDto currentProd = dtos.get(i);
                
                prevProd.setUnit(prevProd.getUnit() + currentProd.getUnit());     // ?????????????????? ????????? ?????????
                newOrderList.get(prevOrderIdx).setAllProdInfo(prevProd.getProdName1() + " [" + prevProd.getOptionInfo1() + "-" + prevProd.getUnit() + "]");
                prevProd.setAllProdOrderNumber(prevProd.getAllProdOrderNumber() + "/" + currentProd.getProdOrderNumber());
                newOrderList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getAllProdOrderNumber());   // ??? ???????????? ??????
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
            int prevOrderIdx = resultList.size() - 1;     // ???????????? ????????? ???????????? ????????? index

            // ???????????? + ????????? + ?????? + ???????????? : ????????? ????????????
            // ???????????? + ????????? + ?????? : ????????? ??????
            if (!optionSet.add(receiverStr)) {
                DeliveryReadyItemLotteExcelFormDto prevProd = resultList.get(prevOrderIdx);
                DeliveryReadyItemLotteExcelFormDto currentProd = newOrderList.get(i);

                // ???????????? ????????? ??????, ???????????? ????????? ??? ?????? ????????? ???????????? ????????? ??????.
                if(prevProd.getProdName1().equals(currentProd.getProdName1()) || prevProdName.equals(currentProd.getProdName1())){
                    prevProdName = "";
                    resultList.get(prevOrderIdx).setAllProdInfo(prevProd.getAllProdInfo() + " | " + "[" + currentProd.getOptionInfo1() + "-" + currentProd.getUnit() + "]");
                }else{      // ???????????? ???????????? ?????? ??????
                    prevProdName = currentProd.getProdName1();
                    resultList.get(prevOrderIdx).setAllProdInfo(prevProd.getAllProdInfo() + " | " + currentProd.getAllProdInfo());
                }
                resultList.get(prevOrderIdx).setAllProdOrderNumber(prevProd.getAllProdOrderNumber() + "/" + currentProd.getAllProdOrderNumber());    // ??? ???????????? ??????
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
     * ????????? ???????????? ??? ?????? ????????? ????????????.
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
     * ?????? ?????? ??? ???????????? ?????? ????????????.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangItemEntity#toEntity
     */
    public void updateListReleaseCompleted(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<Integer> itemCids = dtos.stream().map(dto -> dto.getDeliveryReadyItem().getCid()).collect(Collectors.toList());
        List<DeliveryReadyCoupangItemEntity> entities = deliveryReadyCoupangService.searchDeliveryReadyItemList(itemCids);
        entities.stream().forEach(entity -> entity.setReleaseCompleted(true));
        deliveryReadyCoupangService.createItemList(entities);
    }

    /**
     * <b>Update data for delivery ready.</b>
     * <p>
     * ???????????? ???????????? ???????????? ????????? ??????????????????.
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

    // /**
    //  * <b>DB Select Related Method</b>
    //  * <p>
    //  * ???????????? ????????? ???????????? ??????????????? cid?????? ????????????.
    //  *
    //  * @param dto : DeliveryReadyCoupangItemViewDto
    //  * @return Integer
    //  * @see ProductOptionService#findOptionCidByCode
    //  */
    // public Integer getOptionCid(DeliveryReadyCoupangItemViewDto dto) {
    //     return productOptionService.findOptionCidByCode(dto.getOptionManagementName());
    // }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ???????????? ????????? ???????????? ???????????? ???????????? ????????????.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @return List::ProductOptionEntity::
     * @see ProductOptionService#searchListByOptionCodes
     */
    public List<ProductOptionEntity> getOptionByCode(List<DeliveryReadyCoupangItemViewDto> dtos) {
        List<String> managementCodes = dtos.stream().map(r -> r.getDeliveryReadyItem().getReleaseOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByOptionCodes(managementCodes);
        return optionGetDtos.stream().map(dto -> ProductOptionEntity.toEntity(dto)).collect(Collectors.toList());
    }
    
    /**
     * <b>Update data for delivery ready.</b>
     * <b>Reflect the stock unit of product options.</b>
     * <p>
     * ???????????? ???????????? ???????????? ????????? ??????????????????.
     * ??????(?????? ??????) ???????????? ???????????? ????????? ????????????.
     * ?????? ?????? ????????? ?????? ?????? ????????? ???????????? ???????????? ????????????.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyCoupangBusinessService#getOptionByCode
     * @see DeliveryReadyCoupangBusinessService#reflectStockUnitOfPackageOption
     */
    @Transactional
    public void releaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos) {
        // ??????????????? ???????????? ?????? ??????????????? ?????? ??????
        List<DeliveryReadyCoupangItemViewDto> unreleasedDtos = dtos.stream().filter(dto -> ((!dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null))).collect(Collectors.toList());
        this.updateListReleaseCompleted(unreleasedDtos, true);
        
        // ?????? ??????????????? ??????
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(unreleasedDtos);
        // 1. ???????????? X
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. ???????????? O
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        this.reflectStockUnit(unreleasedDtos, originOptionEntities);

        if (parentOptionEntities.size() > 0) {
            this.reflectStockUnitOfPackageOption(unreleasedDtos, parentOptionEntities);
        }
    }

    public void reflectStockUnit(List<DeliveryReadyCoupangItemViewDto> unreleasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        // 1. ??????????????? ?????? ?????? ????????????
        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
        // ??????????????? ?????? ??? ??????
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

    public void reflectStockUnitOfPackageOption(List<DeliveryReadyCoupangItemViewDto> unreleasedDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. ?????? ????????? ???????????? ?????? ????????? ????????? ??????
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. ????????? ??????????????? ?????? - ?????? ??????????????? ??? ???????????? ??????
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
     * ???????????? ???????????? ???????????? ????????? ??????????????????.
     * ??????(?????? ?????? ??????) ???????????? ???????????? ????????? ????????????.
     * ?????? ?????? ????????? ?????? ?????? ????????? ???????????? ???????????? ?????? ????????????.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangBusinessService#updateListReleaseCompleted
     * @see DeliveryReadyCoupangBusinessService#getOptionByCode
     * @see DeliveryReadyCoupangBusinessService#cancelReflectedStockUnitOfPackageOption
     */
    @Transactional
    public void cancelReleaseListStockUnit(List<DeliveryReadyCoupangItemViewDto> dtos) {
        // ?????? ????????? ????????? ??????????????? ?????? ??????
        List<DeliveryReadyCoupangItemViewDto> releasedDtos = dtos.stream().filter(dto -> (dto.getDeliveryReadyItem().getReleaseCompleted()) && (dto.getOptionManagementName() != null)).collect(Collectors.toList());
        this.updateListReleaseCompleted(releasedDtos, false);

        // ??????????????? ??????
        List<ProductOptionEntity> optionEntities = this.getOptionByCode(releasedDtos);
        // 1. ???????????? X
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        // 2. ???????????? O
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        this.cancelReflectedStockUnit(releasedDtos, originOptionEntities);

        if(parentOptionEntities.size() > 0) {
            this.cancelReflectedStockUnitOfPackageOption(releasedDtos, parentOptionEntities);
        }
    }

    public void cancelReflectedStockUnit(List<DeliveryReadyCoupangItemViewDto> unreleasedDtos, List<ProductOptionEntity> originOptionEntities) {
        UUID USER_ID = userService.getUserId();
        
        // 1. ??????????????? ?????? ?????? ????????????
        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        // ?????? ??????????????? ?????? ??? ??????
        unreleasedDtos.stream().forEach(dto -> {
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

    public void cancelReflectedStockUnitOfPackageOption(List<DeliveryReadyCoupangItemViewDto> unreleasedDtos, List<ProductOptionEntity> parentOptionEntities) {
        UUID USER_ID = userService.getUserId();

        // 1. ?????? ????????? ???????????? ?????? ????????? ????????? ??????
        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
        // 2. ????????? ??????????????? ?????? - ?????? ??????????????? ??? ???????????? ??????
        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

        List<ProductReceiveEntity> productReceiveEntities = new ArrayList<>();
        unreleasedDtos.stream().forEach(dto -> {
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
