package com.sunrt.train.screening;

import com.sunrt.train.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private static String stations[];
    static{
        String stationsStr=HttpUtils.Get("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9099");
        stations=stationsStr.split("@");
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
