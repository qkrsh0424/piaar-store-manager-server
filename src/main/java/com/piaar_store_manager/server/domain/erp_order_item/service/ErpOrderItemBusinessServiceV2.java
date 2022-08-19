package com.piaar_store_manager.server.domain.erp_order_item.service;

import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_order_item.service.strategy.search.SearchContext;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
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
    private final SearchContext erpOrderItemSearchContext;

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
    // deprecated
    // @Transactional(readOnly = true)
    // public List<ErpOrderItemVo> searchList(Map<String, Object> params) {
    //     String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
    //     List<ErpOrderItemProj> itemProjs = new ArrayList<>();

    //     if(matchedCode.equals("optionCode")) {
    //         itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
    //     }else if(matchedCode.equals("releaseOptionCode")) {
    //         itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);   // 페이징 처리 x
    //     }

    //     // 옵션재고수량 추가 및 vos 변환
    //     return this.setOptionStockUnitAndToVos(itemProjs);
    // }

    // @Transactional(readOnly = true)
    // public <T> List<T> searchList(Map<String, Object> params) {
    //     String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
    //     erpOrderItemSearchContext.setSearchStrategy(objectType);

    //     return erpOrderItemSearchContext.searchList(params);
    // }

    // 대시보드 에서 사용. 페이징 처리하지 않는
    // Deprecated
    // @Transactional(readOnly = true)
    // public List<ErpOrderItemVo.ManyToOneJoin> searchAll(Map<String, Object> params) {
    //     String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
    //     List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        
    //     if(matchedCode.equals("optionCode")) {
    //         itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
    //     }else if(matchedCode.equals("releaseOptionCode")) {
    //         itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);   // 페이징 처리 x
    //     }
    //     // 등록된 모든 엑셀 데이터와 M2OJ 관계에 있는 데이터를 모두 조회한다
    //     // 옵션재고수량 추가 및 vos 변환
    //     return this.setOptionStockUnitAndToM2OJVos(itemProjs);
    // }

    @Transactional(readOnly = true)
    public <T> List<T> searchList(Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        erpOrderItemSearchContext.setSearchStrategy(objectType);

        return erpOrderItemSearchContext.searchList(params);
    }

    /*
    아이디 리스트 별 ErpOrderItemProjs 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    // deprecated
    // @Transactional(readOnly = true)
    // public List<ErpOrderItemVo> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
    //     String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
    //     // 등록된 모든 엑셀 데이터를 조회한다
    //     List<ErpOrderItemProj> itemProjs = new ArrayList<>();

    //     if(matchedCode.equals("optionCode")) {
    //         itemProjs = erpOrderItemService.findAllM2OJ(ids, params);       // 페이징 처리 x
    //     } else if(matchedCode.equals("releaseOptionCode")) {
    //         itemProjs = erpOrderItemService.findAllM2OJByReleasedItem(ids, params); // 페이징 처리 x
    //     }
    //     // 옵션재고수량 추가 및 vos 변환
    //     return this.setOptionStockUnitAndToVos(itemProjs);
    // }
    
    @Transactional(readOnly = true)
    public <T> List<T> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        erpOrderItemSearchContext.setSearchStrategy(objectType);

        return erpOrderItemSearchContext.searchBatchByIds(ids, params);
    }


    /*
    조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    // deprecated
    // @Transactional(readOnly = true)
    // public Page<ErpOrderItemVo> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
    //     String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";        // 기본값은 옵션관리코드로 지정
    //     Page<ErpOrderItemProj> itemPages = null;

    //     if(matchedCode.equals("optionCode")) {
    //         itemPages = erpOrderItemService.findAllM2OJByPage(params, pageable);
    //     }else if(matchedCode.equals("releaseOptionCode")) {
    //         itemPages = erpOrderItemService.findReleaseItemM2OJByPage(params, pageable);
    //     }

    //     /*
    //     조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
    //      */
    //     List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o

    //     // 옵션재고수량 추가 및 vos 변환
    //     List<ErpOrderItemVo> erpOrderItemVos = this.setOptionStockUnitAndToVos(itemProjs);

    //     return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
    // }

    @Transactional(readOnly = true)
    public <T> Page<T> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        String objectType = params.get("objectType") != null ? params.get("objectType").toString() : "basic";
        erpOrderItemSearchContext.setSearchStrategy(objectType);

        return erpOrderItemSearchContext.searchListByPage(params, pageable);
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

    // deprecated
    // private List<ErpOrderItemVo.ManyToOneJoin> setOptionStockUnitAndToM2OJVos(List<ErpOrderItemProj> itemProjs) {
    //     List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);

    //     productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);

    //     List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());
    //     ErpOrderItemVo.setOptionStockUnitForM2OJList(erpOrderItemM2OJVos, optionEntities);

    //     return erpOrderItemM2OJVos;
    // }
}
