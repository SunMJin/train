package com.sunrt.train.ticket;


import com.sunrt.train.data.Cp;
import com.sunrt.train.data.Cr;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static Stum confirmSeatType(Stum sts[], Cr cr){
        for(Stum st:sts){
            String count=Seats.getSeatCount(st,cr.queryLeftNewDTO);
            if(count!=null){
                return st;
            }
        }
        return null;
    }

    public static String getWZId(Stum sts[],JSONArray seat_type_codes){
        for(int i=0;i<sts.length;i++){
            String id=getSeatId(sts,sts[i],seat_type_codes);
            Matcher match=Pattern.compile("(?<=\"id\":\")"+id).matcher(seat_type_codes.toString());
            if(match.find()){
                return match.group();
            }
        }
        return null;
    }

    public static String getSeatId(Stum []sts,Stum stum, JSONArray seat_type_codes){
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
            case wz:
                return getWZId(sts,seat_type_codes);
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

    public static String getSeatCount(Stum stum, Cp cp){
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

