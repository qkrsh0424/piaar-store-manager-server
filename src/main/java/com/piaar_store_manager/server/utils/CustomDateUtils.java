package com.piaar_store_manager.server.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                LocalDateTime dateTimeUTC = CustomDateUtils.toUtc(dateTime);
                return dateTimeUTC;
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }
}
