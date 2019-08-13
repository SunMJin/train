package com.sunrt.train.ticket;
import com.sunrt.train.data.Cp;
import com.sunrt.train.data.Cr;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seats {

    public static boolean isExistSeat(String numStr){
        if(StringUtils.isNotEmpty(numStr)&&!"无".equals(numStr)&&!"--".equals(numStr)){
            return true;
        }
        return false;
    }

    public static String confirmSeatType(String sts[], Cr cr){
        for(String st:sts){
            String count=Seats.getSeatCount(st,cr.queryLeftNewDTO);
            if(count!=null){
                return st;
            }
        }
        return null;
    }

    public static String getSeatId(String st,JSONArray seat_type_codes){
        if(st==null){
            new NullPointerException();
        }
        String reg="(?<=\"id\":\")\\w*(?=\",\"value\":\""+st+"\")";
        Matcher matcher=Pattern.compile(reg).matcher(seat_type_codes.toString());
        if(matcher.find()){
            return matcher.group();
        }
        return null;
    }

    public static String getSeatCount(String seatType, Cp cp){
        String numStr=null;
        switch(seatType){
            case "商务座":
                if(isExistSeat(cp.swz_num)){
                    return cp.swz_num;
                }
                numStr=cp.tz_num;
                break;
            case "一等座":
                numStr=cp.zy_num;
                break;
            case "二等座":
                numStr=cp.ze_num;
                break;
            case "高级软卧":
                numStr=cp.gr_num;
                break;
            case "软卧":
                numStr=cp.rw_num;
                break;
            case "动卧":
                numStr=cp.srrb_num;
                break;
            case "硬卧":
                numStr=cp.yw_num;
                break;
            case "软座":
                numStr=cp.rz_num;
                break;
            case "硬座":
                numStr=cp.yz_num;
                break;
            case "无座":
                numStr=cp.wz_num;
                break;
        }
        if(isExistSeat(numStr)){
            return numStr;
        }
        return null;
    }
}

