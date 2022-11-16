package com.piaar_store_manager.server.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class CustomUniqueKeyUtils {
    /**
     * total 18 characters
     * dateKey: 4
     * nanoTimeKey: 10
     * randomKey: 4
     * @return
     */
    public static String generateCode18(){
        String dateKey = getDateKey();
        String nanoTimeKey = getNanoTimeKey();
        String randomKey = getRandomKey(4);

        StringBuilder sb = new StringBuilder();
        sb
                .append(dateKey)
                .append(nanoTimeKey)
                .append(randomKey)
        ;

        return sb.toString();
    }

    /**
     * total 20 characters
     * dateKey: 4
     * nanoTimeKey: 10
     * randomKey: 6
     * @return
     */
    public static String generateCode20(){
        String dateKey = getDateKey();
        String nanoTimeKey = getNanoTimeKey();
        String randomKey = getRandomKey(6);

        StringBuilder sb = new StringBuilder();
        sb
                .append(dateKey)
                .append(nanoTimeKey)
                .append(randomKey)
        ;

        return sb.toString();
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

    private static String getDateKey(){
        Long yymmdd = getYYMMDD();

        return Long.toString(yymmdd, 36);
    }

    private static Long getYYMMDD(){
        return Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
    }

    private static String getNanoTimeKey(){
        Long nano = getNanoTime();
        String nanoPlus = String.valueOf(nano + 100000000000000L);
        String nanoPlusShorts = nanoPlus.substring(0, nanoPlus.length()-3);
        String radixNanoPlusShorts = Long.toString(Long.parseLong(nanoPlusShorts), 16);

        return radixNanoPlusShorts;
    }

    private static Long getNanoTime() {
        Long l = LocalTime.now(ZoneId.of("UTC")).toNanoOfDay();

        return l;
    }

    private static String getRandomKey(int length){
        return getRandomAlphanumeric(length);
    }

    private static String getRandomAlphanumeric(int length){

        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
