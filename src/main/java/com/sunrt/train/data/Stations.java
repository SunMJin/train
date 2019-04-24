package com.sunrt.train.data;

import com.sunrt.train.utils.HttpUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private static String stations[];

    public static boolean init(){
        String stationsStr=HttpUtils.GetStr("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9099");
        stations=stationsStr.split("@");
        if(stations!=null){
            return true;
        }
        return false;
    }

    public static List<String> selectStations(String staName){
        List<String> list=new ArrayList<>();
        if(stations!=null){
            for(String sta:stations){
                if(sta.indexOf(staName)>-1){
                    list.add(sta);
                }
            }
        }
        return list;
    }
}
