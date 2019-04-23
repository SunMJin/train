package com.sunrt.train.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static boolean isValidDate(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = formatter.parse(str);
            String x=formatter.format(date);
            return str.equals(formatter.format(date));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
