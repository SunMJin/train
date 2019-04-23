package com.sunrt.train.ticket;


import com.sunrt.train.data.cP;
import org.apache.commons.lang3.StringUtils;

enum Stum {
    swz,zy,ze,gr,rw,srrb,yw,rz,yz,wz;
}

public class Seats {
    private static final String DESC[]=new String[]{"商务座","一等座","二等座","高级软卧","软卧一等卧","动卧","硬卧二等座","软座","硬座","无座"};
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

    public static String getSeatCount(Stum stum, cP cp){
        String numStr=null;
        switch(stum){
            case swz:
                numStr=cp.swz_num;
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
        if(StringUtils.isNotEmpty(numStr)&&!"无".equals(numStr)&&!"--".equals(numStr)){
            return numStr;
        }
        return "0";
    }
}

