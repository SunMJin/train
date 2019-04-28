package com.sunrt.train.data;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private static HttpUtils httpUtils = TrainHttp.getHttp();

    private static String stations[];
    public static boolean init() {
        String stationsStr= null;
        try {
            stationsStr = httpUtils.GetHtml(Constant.STATIONSURL);
        } catch (HttpException e) {
            return false;
        }
        if(stationsStr!=null){
            stations=stationsStr.split("@");
            if(stations!=null&&stations.length>0){
                return true;
            }
        }
        return false;
    }

    public static List<String> selectStations(String staName){
        if(staName==null){
            throw new NullPointerException();
        }
        List<String> list=new ArrayList<>();
        for(String sta:stations){
            if(sta.indexOf(staName)>-1){
                list.add(sta);
            }
        }
        return list;
    }
}
