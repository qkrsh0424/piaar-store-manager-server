package com.piaar_store_manager.server.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class CustomDateUtils {
    private static Integer NINE_HOUR = 9;

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static String getCurrentKRDate2yyyyMMddHHmmss_SSS() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        String result = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_SSS"));
        return result;
    }

    public static Date getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    public static Date getCurrentDate2() {
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    // yyyy-MM-dd HH:mm:ss
    public static String getLocalDateTimeToyyyyMMddHHmmss(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // createdAt, salesAt, releaseAt 시간은 +9시간 세팅
    public static String getLocalDateTimeToDownloadFormat(LocalDateTime localDateTime) {
        localDateTime = localDateTime.plusHours(NINE_HOUR);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime toZone(final LocalDateTime time, final ZoneId fromZone, final ZoneId toZone) {
        final ZonedDateTime zonedtime = time.atZone(fromZone);
        final ZonedDateTime converted = zonedtime.withZoneSameInstant(toZone);
        return converted.toLocalDateTime();
    }

    public static LocalDateTime toZone(final LocalDateTime time, final ZoneId toZone) {
        return CustomDateUtils.toZone(time, ZoneId.systemDefault(), toZone);
    }

    public static LocalDateTime toUtc(final LocalDateTime time, final ZoneId fromZone) {
        return CustomDateUtils.toZone(time, fromZone, ZoneOffset.UTC);
    }

    public static LocalDateTime toUtc(final LocalDateTime time) {
        return CustomDateUtils.toUtc(time, ZoneId.of("Asia/Seoul"));
    }

    // TODO :: 판매성과 날짜변환 - 배포 후 확인
    public static LocalDateTime toLocalDateStartTime(final LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static boolean isValidDate(String inDate, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /*
     * startDate와 endDate의 일(date) 차이를 반환
     */
    public static long getDateDiff(LocalDate startDate, LocalDate endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(startDate.toString());
        Date date2 = format.parse(endDate.toString());

        // 24*60*60*1000 : 하루
        return (date2.getTime() - date1.getTime()) / (24*60*60*1000);
    }

    /**
     * <p>@JsonDeserialize(using = CustomDateUtils.JsonLocalDateTimeBasicDeserializer.class)</p>
     * <p>private LocalDateTime example</p>
     *
     * <p>필드에 매핑되는 값이 yyyy-MM-dd HH:mm:ss 일 경우 LocalDateTime으로 반환되고 정상적인 포맷 형태가 아닐경우 null 값을 반환한다.</p>
     */
    public static class JsonLocalDateTimeBasicDeserializer extends JsonDeserializer {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String dateStr = p.getText();

            /*
            요청 데이터가 UTC의 경우
            Pattern : "yyyy-MM-dd'T'HH:mm:ss'Z'"
             */
            if (CustomDateUtils.isValidDate(dateStr, "yyyy-MM-dd'T'HH:mm:ss'Z'")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    return LocalDateTime.parse(dateStr, formatter);
                }catch (DateTimeParseException e){
                    return null;
                }
            }

            /*
            요청 데이터가 String일 경우 UTC로 변환 후 받아온다. (요청 데이터는 Asia/seoul 시간으로 간주한다.)
            Pattern : "yyyy-MM-dd HH:mm:ss"
             */
            if (CustomDateUtils.isValidDate(dateStr, "yyyy-MM-dd HH:mm:ss")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                    LocalDateTime dateTimeUTC = CustomDateUtils.toUtc(dateTime);
                    return dateTimeUTC;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }

            /*
             요청 데이터가 String일 경우 UTC로 변환 후 seconds값을 0으로 세팅한다. (요청 데이터는 Asia/seoul 시간으로 간주한다.)
             Pattern : "yyyy-MM-dd HH:mm"
             */
            if (CustomDateUtils.isValidDate(dateStr, "yyyy-MM-dd HH:mm")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter).truncatedTo(ChronoUnit.SECONDS);
                    LocalDateTime dateTimeUTC = CustomDateUtils.toUtc(dateTime);
                    return dateTimeUTC;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }

            return null;
        }
    }
}
