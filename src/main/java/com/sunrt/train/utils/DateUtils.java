package com.sunrt.train.utils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static boolean isValid(String str,SimpleDateFormat sdf) {
        try{
            Date date = sdf.parse(str);
            return str.equals(sdf.format(date));
        }catch(ParseException e){
            return false;
        }
    }
    public static String getToday(SimpleDateFormat sdf){
        return sdf.format(new Date());
    }
    public static String getToday(String formatter){
        return getToday(new SimpleDateFormat(formatter));
    }

    public static String getTrainDate(JSONObject train_date){
        Date date=new Date(train_date.getLong("time"));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT+0800 (中国标准时间)'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }
}
