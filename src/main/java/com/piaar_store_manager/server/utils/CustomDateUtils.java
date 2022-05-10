package com.piaar_store_manager.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CustomDateUtils {
    public static LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now();
    }

    public static String getCurrentKRDate2yyyyMMddHHmmss_SSS(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        String result = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_SSS"));
        return result;
    }

    public static Date getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    public static Date getCurrentDate2(){
        Date date = Calendar.getInstance().getTime();
        return date;
    }
}
