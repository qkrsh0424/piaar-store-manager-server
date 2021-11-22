package com.piaar_store_manager.server.handler;

import java.util.Calendar;
import java.util.Date;

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
}
