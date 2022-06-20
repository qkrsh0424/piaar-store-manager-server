package com.piaar_store_manager.server.utils;

import java.util.regex.Pattern;

public class CustomRegexUtils {
    public static boolean isCheckNumberFormat(Object object) {
        boolean isNumberFormat = true;
        
        isNumberFormat = !Pattern.compile("[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z!@#$^&*()]").matcher(object.toString()).find();

        return isNumberFormat;
    }
}
