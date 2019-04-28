package com.sunrt.train.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {
    public static String getStrByReg(String reg,String html){
        if(reg==null||html==null){
            throw new NullPointerException();
        }
        Matcher match= Pattern.compile(reg).matcher(html);
        return match.find()?match.group():null;
    }

    public static JSONObject getJSONByReg(String reg, String html){
        String str=getStrByReg(reg,html);
        try{
            if(StringUtils.isNotEmpty(str)){
                return new JSONObject(str);
            }
            return new JSONObject();
        }catch (JSONException e){
            throw new RuntimeException();
        }
    }
}
