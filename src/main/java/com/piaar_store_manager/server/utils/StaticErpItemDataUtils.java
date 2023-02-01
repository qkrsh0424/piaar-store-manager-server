package com.piaar_store_manager.server.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticErpItemDataUtils {
    public static String[] UPLOAD_HEADER_EXCEL_SAMPLE = {
            "피아르 고유번호 (비워두세요.)",
            "상품명 (필수)",
            "옵션정보 (필수)",
            "수량 (필수)",
            "수취인명 (필수)",
            "전화번호1 (필수)",
            "전화번호2",
            "주소 (필수)",
            "주소 상세",
            "판매채널",
            "판매채널 주문번호1",
            "판매채널 주문번호2",
            "판매채널 상품코드",
            "판매채널 옵션코드",
            "우편번호",
            "택배사",
            "배송방식",
            "배송메세지",
            "운송장번호",
            "판매금액",
            "배송비",
            "바코드",
            "피아르 상품코드",
            "피아르 옵션코드",
            "출고 옵션코드",
            "주문일시",
            "관리메모1",
            "관리메모2",
            "관리메모3",
            "관리메모4",
            "관리메모5",
            "관리메모6",
            "관리메모7",
            "관리메모8",
            "관리메모9",
            "관리메모10"
    };

    public static List<Object> getUploadHeaderExcelSample() {
        List<Object> result = new ArrayList<>();

        Arrays.stream(UPLOAD_HEADER_EXCEL_SAMPLE).forEach(r -> {
            result.add(r);
        });

        return result;
    }

    // 출고 리스트 다운로드 양식
    public static String[] RELEASE_ITEM_LIST_HEADER = {
        "$피아르 상품명",
        "$피아르 옵션명",
        "수량"
    };

    public static List<String> getReleaseItemListHeader() {
        List<String> result = new ArrayList<>();

        Arrays.stream(RELEASE_ITEM_LIST_HEADER).forEach(r -> {
            result.add(r);
        });

        return result;
    }
}
