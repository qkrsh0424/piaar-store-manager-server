package com.piaar_store_manager.server.domain.erp_order_item.service;

import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpReleaseConfirmItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.filter.PerformanceSearchFilter;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErpOrderItemBusinessServiceV2 {
    private final ErpOrderItemService erpOrderItemService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;

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
    // RE-OK
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo> searchList(Map<String, Object> params) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        List<ErpOrderItemVo> itemVos = new ArrayList<>();
        List<ProductOptionEntity> optionEntities = new ArrayList<>();

        if(matchedCode.equals("optionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
            optionEntities = this.setReleaseOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setOptionStockUnitForList(itemVos, optionEntities);
        }else if(matchedCode.equals("releaseOptionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);   // 페이징 처리 x
            optionEntities = this.setReleaseOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setReleaseOptionStockUnitForList(itemVos, optionEntities);
        }

        return itemVos;
    }

    // ERP 대시보드에서 사용. 페이징 처리하지 않는
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo.ManyToOneJoin> searchAll(Map<String, Object> params) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        List<ErpOrderItemVo.ManyToOneJoin> itemVos = new ArrayList<>();
        List<ProductOptionEntity> optionEntities = new ArrayList<>();

        if(matchedCode.equals("optionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
            optionEntities = this.setOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setOptionStockUnitForM2OJList(itemVos, optionEntities);
        }else if(matchedCode.equals("releaseOptionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);   // 페이징 처리 x
            optionEntities = this.setReleaseOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setOptionStockUnitForM2OJList(itemVos, optionEntities);
        }
        // 등록된 모든 엑셀 데이터와 M2OJ 관계에 있는 데이터를 모두 조회한다
        // 옵션재고수량 추가 및 vos 변환
        return itemVos;
    }

    /*
    아이디 리스트 별 ErpOrderItemProjs 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        List<ErpOrderItemVo> itemVos = new ArrayList<>();
        List<ProductOptionEntity> optionEntities = new ArrayList<>();

        if(matchedCode.equals("optionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJ(ids, params);       // 페이징 처리 x
            optionEntities = this.setOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setOptionStockUnitForList(itemVos, optionEntities);
        } else if(matchedCode.equals("releaseOptionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJByReleasedItem(ids, params); // 페이징 처리 x
            optionEntities = this.setReleaseOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setReleaseOptionStockUnitForList(itemVos, optionEntities);
        }
        // 옵션재고수량 추가 및 vos 변환
        return itemVos;
    }

    /*
    조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    @Transactional(readOnly = true)
    public Page<ErpOrderItemVo> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        Page<ErpOrderItemProj> itemPages = null;
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        List<ErpOrderItemVo> itemVos = new ArrayList<>();
        List<ProductOptionEntity> optionEntities = new ArrayList<>();

        if(matchedCode.equals("optionCode")) {
            itemPages = erpOrderItemService.findAllM2OJByPage(params, pageable);
            itemProjs = itemPages.getContent();
            optionEntities = this.setOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
            
            // 재고수량 세팅
            ErpOrderItemVo.setOptionStockUnitForList(itemVos, optionEntities);
        }else if(matchedCode.equals("releaseOptionCode")) {
            itemPages = erpOrderItemService.findReleaseItemM2OJByPage(params, pageable);
            itemProjs = itemPages.getContent();
            optionEntities = this.setReleaseOptionStockUnit(itemProjs);
            itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());

            // 재고수량 세팅
            ErpOrderItemVo.setReleaseOptionStockUnitForList(itemVos, optionEntities);
        }

        return new PageImpl(itemVos, pageable, itemPages.getTotalElements());
    }

    // 판매성과 - 성과 검색에서 조회하는 데이터. 재고수량 세팅 X
    @Transactional(readOnly = true)
    public Page<ErpOrderItemVo> searchSalesPerformanceByPaging(PerformanceSearchFilter filter, Pageable pageable) {
        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findSalesPerformanceByPage(filter, pageable);
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();
        List<ErpOrderItemVo> itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());

        return new PageImpl(itemVos, pageable, itemPages.getTotalElements());
    }

    public List<ErpReleaseConfirmItemDto> searchReleaseConfirmPackageItem(List<ErpReleaseConfirmItemDto> packageReleaseItemDtos) {
        /*
         * PackageReleaseItemDtos의 code값을 추출해 세트옵션의 id를 추출 (option package를 검색하기 위함. option pakcage에는 parentOptionId만을 참조하므로)
         */
        List<String> optionCodes = packageReleaseItemDtos.stream().map(ErpReleaseConfirmItemDto::getCode).collect(Collectors.toList());
        List<ProductOptionGetDto> optionDtos = productOptionService.searchListByOptionCodes(optionCodes);
        List<UUID> optionIds = optionDtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());

        /*
         * 추출된 세트상품 옵션의 id를 이용해 패키지상품을 조회
         */
        List<OptionPackageProjection.RelatedProductAndOption> packageProjs = optionPackageService.searchBatchByParentOptionIds(optionIds);
        List<OptionPackageDto.RelatedOriginOption> packageDtos = packageProjs.stream().map(OptionPackageDto.RelatedOriginOption::toDto).collect(Collectors.toList());
        
        /* 
        * 패키지상품의 parentOptionCode을 세팅
        */
        this.setOptionStockUnit(packageDtos, optionDtos);

        /*
         * 세트상품의 각 구성품으로 생성한 ErpReleaseConfirmItemDto를 반환한다
         */
        List<ErpReleaseConfirmItemDto> releaseItemDtos = this.createPackageReleaseItem(packageReleaseItemDtos, packageDtos);
        return releaseItemDtos;
    }

    /* 
     * 패키지상품의 parentOptionCode을 세팅
    */
    private void setOptionStockUnit(List<OptionPackageDto.RelatedOriginOption> packageDtos, List<ProductOptionGetDto> optionDtos) {
        packageDtos.forEach(packageOption -> {
            optionDtos.forEach(parentOption -> {
                if(packageOption.getParentOptionId().equals(parentOption.getId())) {
                    packageOption.setParentOptionCode(parentOption.getCode());
                }
            });
        });
    }

    /*
     * 세트상품의 각 구성품으로 생성한 ErpReleaseConfirmItemDto를 반환한다
    */
    private List<ErpReleaseConfirmItemDto>  createPackageReleaseItem(List<ErpReleaseConfirmItemDto> packageReleaseItemDtos, List<OptionPackageDto.RelatedOriginOption> packageDtos) {
        List<ErpReleaseConfirmItemDto> releaseItemDtos = new ArrayList<>();

        packageReleaseItemDtos.forEach(releaseItem -> {
            packageDtos.forEach(packageDto -> {
                if(releaseItem.getCode().equals(packageDto.getParentOptionCode())) {
                    ErpReleaseConfirmItemDto dto = ErpReleaseConfirmItemDto.builder()
                        .code(packageDto.getOriginOptionCode())
                        .prodDefaultName(packageDto.getOriginProductDefaultName())
                        .optionDefaultName(packageDto.getOriginOptionDefaultName())
                        .unit(releaseItem.getUnit() * packageDto.getPackageUnit())
                        .optionStockUnit(releaseItem.getOptionStockUnit())
                        .optionPackageYn("n")
                        .parentOptionCode(packageDto.getParentOptionCode())
                        .build();

                    releaseItemDtos.add(dto);
                }
            });
        });

        return releaseItemDtos;
    }

    private List<ProductOptionEntity> setOptionStockUnit(List<ErpOrderItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
        return optionEntities;
    }

    private List<ProductOptionEntity> setReleaseOptionStockUnit(List<ErpOrderItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
        return optionEntities;
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
    // private List<ErpOrderItemVo> setOptionStockUnitAndToVos(List<ErpOrderItemProj> itemProjs) {
    //     /*
    //     ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
    //      */
    //     List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
    //     /*
    //     옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
    //      */
    //     productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
    //     /*
    //     ErpOrderItemProjs -> ErpOrderItemVos 변환
    //      */
    //     List<ErpOrderItemVo> erpOrderItemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
    //     /*
    //     ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
    //      */
    //     ErpOrderItemVo.setOptionStockUnitForList(erpOrderItemVos, optionEntities);
    //     return erpOrderItemVos;
    // }

    // private List<ErpOrderItemVo> setReleaseOptionStockUnitAndToVos(List<ErpOrderItemProj> itemProjs) {
    //     /*
    //     ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
    //      */
    //     List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
    //     /*
    //     옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
    //      */
    //     productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
    //     /*
    //     ErpOrderItemProjs -> ErpOrderItemVos 변환
    //      */
    //     List<ErpOrderItemVo> erpOrderItemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
    //     /*
    //     ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
    //      */
    //     ErpOrderItemVo.setReleaseOptionStockUnitForList(erpOrderItemVos, optionEntities);
    //     return erpOrderItemVos;
    // }

    // private List<ErpOrderItemVo.ManyToOneJoin> setOptionStockUnitAndToM2OJVos(List<ErpOrderItemProj> itemProjs) {
    //     List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);

    //     productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);

    //     List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());
    //     ErpOrderItemVo.setOptionStockUnitForM2OJList(erpOrderItemM2OJVos, optionEntities);

    //     return erpOrderItemM2OJVos;
    // }

    // private List<ErpOrderItemVo.ManyToOneJoin> setReleaseOptionStockUnitAndToM2OJVos(List<ErpOrderItemProj> itemProjs) {
    //     List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);

    //     productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);

    //     List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());
    //     ErpOrderItemVo.setReleaseOptionStockUnitForM2OJList(erpOrderItemM2OJVos, optionEntities);

    //     return erpOrderItemM2OJVos;
    // }

    @Transactional(readOnly = true)
    public Set<String> searchSalesChannel(Map<String, Object> params) {
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        List<ErpOrderItemVo> itemVos = new ArrayList<>();
        Set<String> channel = new HashSet<>();

        itemProjs = erpOrderItemService.findAllM2OJ(params); // 페이징 처리 x
        itemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
        itemVos.forEach(vo -> {
            String channelName = vo.getSalesChannel() == null || vo.getSalesChannel().equals("") ? "미지정" : vo.getSalesChannel();
            channel.add(channelName);
        });
        return channel;
    }
}
