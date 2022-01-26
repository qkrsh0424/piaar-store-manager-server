package com.piaar_store_manager.server.handler;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

@Component
public class DateHandler {
    public Date getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    public static Date getCurrentDate2(){
        Date date = Calendar.getInstance().getTime();
        return date;
    }

    /**
     * <b>Convert Date To UTC Date.</b>
     * <p>
     * 엑셀 데이터의 Date 데이터를 UTC 시간으로 변환하기 위한 메소드.
     * 
     * @param date : Date
     * @return Date
     */
    public static Date getUtcDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        c.add(Calendar.MILLISECOND, -(c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        return c.getTime();
    }

    /**
     * <b>Convert UTC Date To KST Date.</b>
     * <p>
     * UTC Date 데이터를 KST 시간으로 변환하기 위한 메소드.
     * 
     * @param date : Date
     * @return Date
     */
    public static Date getKstDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, 9);
        return c.getTime();
    }
}
