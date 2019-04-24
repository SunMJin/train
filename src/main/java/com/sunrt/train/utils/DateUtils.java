package com.sunrt.train.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static boolean isValid(String str,SimpleDateFormat sdf) {
        try{
            Date date = sdf.parse(str);
            return str.equals(sdf.format(date));
        }catch(Exception e){
            return false;
        }
    }
    public static String getToday(SimpleDateFormat sdf){
        return sdf.format(new Date());
    }
}
