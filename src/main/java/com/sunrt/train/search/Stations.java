package com.sunrt.train.search;

import com.sunrt.train.utils.HttpUtils;

public class Stations {

    public static void getStations(){
        String stationsStr=HttpUtils.Get("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9099");
        String stationsArr[]=stationsStr.split("@");
    }

    public static void main(String[] args) {
        Stations.getStations();
    }
}
