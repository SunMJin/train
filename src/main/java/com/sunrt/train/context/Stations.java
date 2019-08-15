package com.sunrt.train.context;

import com.sunrt.train.constant.QueryTicketConstant;
import com.sunrt.train.utils.HttpUtils;
import com.sunrt.train.utils.PublicHttp;
import com.sunrt.train.utils.RegUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class Stations {

    private static HttpUtils httpUtils = PublicHttp.getInstance();
    private static final String sta_ver_key="stations_version";
    private static final String sta_key="stations_info";

    public static String getLastStations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForValue().get(sta_key);
    }

    public static boolean isLastVer(String client_ver_str,StringRedisTemplate stringRedisTemplate){
        double client_ver=StringUtils.isNotEmpty(client_ver_str)?Double.valueOf(client_ver_str):0;
        String html = httpUtils.GetHtml(QueryTicketConstant.INIT);
        double x=Double.valueOf(RegUtils.getStrByReg("(?<=station_version=)[0-9]+([.][0-9]+)*", html));
        String local_ver_str=stringRedisTemplate.opsForValue().get(sta_ver_key);
        double last_ver= StringUtils.isEmpty(local_ver_str)?0:Double.valueOf(local_ver_str);
        if(x!=last_ver){
            html = httpUtils.GetHtml(QueryTicketConstant.STATIONSURL+x);
            String stastr=RegUtils.getStrByReg("(?<=var station_names ='@).*(?=';)", html);
            stringRedisTemplate.opsForValue().set(sta_key, stastr);
        }
        stringRedisTemplate.opsForValue().set(sta_ver_key,String.valueOf(x));
        return client_ver!=x;
    }
}