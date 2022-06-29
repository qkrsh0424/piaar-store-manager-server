package com.piaar_store_manager.server.utils;

import java.util.regex.Pattern;

public class CustomRegexUtils {
    public static boolean isCheckNumberFormat(Object object) {
        boolean isNumberFormat = true;

        String notAllowChar = "[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z~!@#$^&*()]";
        if(Pattern.compile(notAllowChar).matcher(object.toString()).find()){
            isNumberFormat = false;
        }

        return isNumberFormat;
    }
}
