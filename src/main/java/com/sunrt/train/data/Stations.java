package com.sunrt.train.data;

import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Stations {
    private static String stations[];
    public static boolean init(){
        String stationsStr= null;
        try {
            stationsStr = HttpUtils.GetStr(Constant.STATIONSURL);
        } catch (IOException e) {
        } catch (URISyntaxException e) {
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
