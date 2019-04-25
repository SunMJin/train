package com.sunrt.train.ticket;


import com.sunrt.train.data.cP;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Seats {

    private static final String DESC[]=new String[]{"商务座","一等座","二等座","高级软卧","软卧一等卧","动卧","硬卧二等卧","软座","硬座","无座"};
    public Stum stum;
    public String desc;


    public static Seats[] getSeats(){
        Seats sts[]=new Seats[DESC.length];
        int index=0;
        for(Stum stum:Stum.values()){
            Seats st=new Seats();
            st.stum=stum;
            st.desc=DESC[index];
            sts[index]=st;
            index++;
        }
        return sts;
    }


    public static boolean isExistSeat(String numStr){
        if(StringUtils.isNotEmpty(numStr)&&!"无".equals(numStr)&&!"--".equals(numStr)){
            return true;
        }
        return false;
    }



    public static boolean iscontainsSeat(Stum sts[],Stum st){
        for(Stum s:sts){
            if(s==st){
                return true;
            }
        }
        return false;
    }

    public static String getWZId(Stum sts[],JSONArray seat_type_codes){
        if(sts!=null){
            Stum st=null;
            for(int i=0;i<seat_type_codes.length();i++){
                JSONObject jo=seat_type_codes.getJSONObject(i);
                st=getSeatEnum(jo.getString("value"));
                if(st!=null){
                    if(iscontainsSeat(sts,st)){
                        return jo.getString("id");
                    }
                }
            }

        }
        return null;
    }



    public static Stum getSeatEnum(String value){
        Stum st=null;
        switch (value){
            case "商务座":
                st=Stum.swz;
                break;
            case "一等座":
                st=Stum.zy;
                break;
            case "二等座":
                st=Stum.ze;
                break;
            case "高级软卧":
                st=Stum.gr;
                break;
            case "软卧":
                st=Stum.rw;
                break;
            case "动卧":
                st=Stum.srrb;
                break;
            case "硬卧":
                st=Stum.yw;
                break;
            case "软座":
                st=Stum.rz;
                break;
            case "硬座":
                st=Stum.yz;
                break;
        }
        return st;
    }

    public static String getSeatId(Stum stum, JSONArray seat_type_codes){
        String str=null;
        switch (stum){
            case swz:
                str="商务座";
                break;
            case zy:
                str="一等座";
                break;
            case ze:
                str="二等座";
                break;
            case gr:
                str="高级软卧";
                break;
            case rw:
                str="软卧";
                break;
            case srrb:
                str="动卧";
                break;
            case yw:
                str="硬卧";
                break;
            case rz:
                str="软座";
                break;
            case yz:
                str="硬座";
                break;
        }
        if(str!=null){
            String reg="(?<=\"id\":\")\\w*(?=\",\"value\":\""+str+"\")";
            Matcher matcher=Pattern.compile(reg).matcher(seat_type_codes.toString());
            if(matcher.find()){
                return matcher.group();
            }
        }
        return null;
    }

    public static String getSeatCount(Stum stum, cP cp){
        String numStr=null;
        switch(stum){
            case swz:
                if(isExistSeat(cp.swz_num)){
                    numStr=cp.swz_num;
                }
                numStr=cp.tz_num;
                break;
            case zy:
                numStr=cp.zy_num;
                break;
            case ze:
                numStr=cp.ze_num;
                break;
            case gr:
                numStr=cp.gr_num;
                break;
            case rw:
                numStr=cp.rw_num;
                break;
            case srrb:
                numStr=cp.srrb_num;
                break;
            case yw:
                numStr=cp.yw_num;
                break;
            case rz:
                numStr=cp.rz_num;
                break;
            case yz:
                numStr=cp.yz_num;
                break;
            case wz:
                numStr=cp.wz_num;
                break;
        }
        if(isExistSeat(numStr)){
            return numStr;
        }
        return null;
    }
}

