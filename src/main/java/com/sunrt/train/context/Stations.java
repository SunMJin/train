package com.sunrt.train.context;

import com.sunrt.train.PublicHttp;
import com.sunrt.train.constant.QueryTicketConstant;
import com.sunrt.train.utils.FileUtils;
import com.sunrt.train.utils.HttpUtils;
import com.sunrt.train.utils.PropertiesUtil;
import com.sunrt.train.utils.RegUtils;

public class Stations {
    private static HttpUtils httpUtils = PublicHttp.getInstance();

    private static String stations;
    private static final String sta_filename ="stations.txt";
    private static final String ver_filename ="ver.properties";
    private static String sta_ver;
    private static final String sta_ver_key="stations";

    static{
        System.out.println("正在更新站点...");
        if(isNewVer()){
            stations=updateStations();
        }else{
            String text=FileUtils.getText(sta_filename);
            stations=RegUtils.getStrByReg("(?<=var station_names ='@).*(?=';)", text);
        }
        System.out.println("站点更新完毕");
    }

    public static String getCode(String name){
        return RegUtils.getStrByReg("(?<=\\|"+name+"\\|)"+"[A-Z]+", stations);
    }

    private static String updateStations() {
        String stationsStr = httpUtils.GetHtml(QueryTicketConstant.STATIONSURL+sta_ver);
        FileUtils.writeText(sta_filename,stationsStr);
        PropertiesUtil.writeValueByFile(ver_filename, sta_ver_key, sta_ver);
        return stationsStr;
    }

    private static boolean isNewVer(){
        String html = httpUtils.GetHtml(QueryTicketConstant.INIT);
        sta_ver=RegUtils.getStrByReg("(?<=station_version=)[0-9]+([.][0-9]+)*", html);
        double ver_new=Double.valueOf(sta_ver);
        double ver_old=Double.valueOf(PropertiesUtil.getValueByFile(ver_filename,sta_ver_key));
        if(ver_new>ver_old){
            return true;
        }
        return false;
    }

}
