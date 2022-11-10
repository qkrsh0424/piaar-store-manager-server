package com.piaar_store_manager.server.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class CustomUniqueKeyUtils {
//    total : 18 characters
    public static String generateKey() {
        StringBuilder sb = new StringBuilder();

        String prefix = "p";
        String randomStr = CustomUniqueKeyUtils.getRandomAlphanumeric(1);
        String yymmddStr = Long.toString(CustomUniqueKeyUtils.getYYMMDD(), 36);
        String nanoTimeStr = Long.toString(CustomUniqueKeyUtils.getNanoTime(), 16);

        sb.append(prefix.substring(0,1));
        sb.append(randomStr.substring(0,1));
        sb.append(yymmddStr.substring(0,4));
        sb.append(nanoTimeStr.substring(0,12));

        return sb.toString();
    }

//    total : 18 characters
    public static String generateKey(String prefix) {
        StringBuilder sb = new StringBuilder();

        String randomStr = CustomUniqueKeyUtils.getRandomAlphanumeric(1);
        String yymmddStr = Long.toString(CustomUniqueKeyUtils.getYYMMDD(), 36);
        String nanoTimeStr = Long.toString(CustomUniqueKeyUtils.getNanoTime(), 16);

        sb.append(prefix.substring(0,1));
        sb.append(randomStr.substring(0,1));
        sb.append(yymmddStr.substring(0,4));
        sb.append(nanoTimeStr.substring(0,12));

        return sb.toString();
    }

    public static String generateCategoryCode() {
        String key = generateKey("c");

        return key;
    }

    public static String generateProductCode(){
        String key = generateKey("p");

        return key;
    }

    public static String generateOptionCode(){
        String key = generateKey("o");

        return key;
    }

    // total : 4 random characters => upper case, lower case, Number 1~9
    public static String generateFreightCode() {
        int[] NUMBER_BOUND = {49, 57};     // asci 49~57 => number 1~9
        int[] UPPER_CASE_BOUND = {65, 90};      // asci 65~90 => upper case A~Z
        int[] LOWER_CASE_BOUND = {97, 122};     // asci 97~122 => lower case a~z

        int targetLength = 4;
        Random random = new Random();
        String generatedCode = random.ints(NUMBER_BOUND[0], LOWER_CASE_BOUND[1]+1)
                            .filter(i -> (i <= NUMBER_BOUND[1] || i >= UPPER_CASE_BOUND[0]) && (i <= UPPER_CASE_BOUND[1] || i >= LOWER_CASE_BOUND[0]))
                            .limit(targetLength)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();

        return generatedCode;
    }

    // public static Long getCurrentMillis() {
    //     Long l = System.currentTimeMillis();
    //     return l;
    // }

    // public static Long getNanoTime() {
    //     Long l = System.nanoTime();
    //     return l;
    // }

    // public static String getRandomNumeric(int length) {
    //     return RandomStringUtils.randomNumeric(length);
    // }
        
    // // total : 'CA' + 10 random number
    // public static String generateCategoryCode() {
    //     int[] NUMBER_BOUND = {49, 57};     // asci 49~57 => number 1~9

    //     int targetLength = 10;
    //     Random random = new Random();
    //     String randomNumber = random.ints(NUMBER_BOUND[0], NUMBER_BOUND[1])
    //                         .limit(targetLength)
    //                         .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
    //                         .toString();

    //     String generatedCode = "CA" + randomNumber;
        
    //     return generatedCode;
    // }

    private static Long getYYMMDD(){
        return Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
    }
    
    private static Long getCurrentMillis() {
        Long l = System.currentTimeMillis();
        return l;
    }

    private static Long getNanoTime() {
        Long l = LocalTime.now().toNanoOfDay();

        return l;
    }

    private static String getRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    private static String getRandomAlphanumeric(int length){
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
