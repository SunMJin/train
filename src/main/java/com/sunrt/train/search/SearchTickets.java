package com.sunrt.train.search;

import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class cR{
    String secretStr;
    String buttonTextInfo;
    cP queryLeftNewDTO;
}

class cP{
    String train_no;
    String station_train_code;
    String start_station_telecode;
    String end_station_telecode;
    String from_station_telecode;
    String to_station_telecode;
    String start_time;
    String arrive_time;
    String lishi;
    String canWebBuy;
    String yp_info;
    String start_train_date;
    String train_seat_feature;
    String location_code;
    String from_station_no;
    String to_station_no;
    String is_support_card;
    String controlled_train_flag;
    String gg_num;
    String gr_num;
    String qt_num;
    String rw_num;
    String rz_num;
    String tz_num;
    String wz_num;
    String yb_num;
    String yw_num;
    String yz_num;
    String ze_num;
    String zy_num;
    String swz_num;
    String srrb_num;
    String yp_ex;
    String seat_types;
    String exchange_train_flag;
    String houbu_train_flag;
    String houbu_seat_limit;
    String from_station_name;
    String to_station_name;
}

public class SearchTickets {

    public static List<cR> search(String train_date,String from_station,String to_station,String purpose_codes){
        String resultsStr=HttpUtils.Get("https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date="
                +train_date+"&leftTicketDTO.from_station="+from_station+"&leftTicketDTO.to_station="+to_station+"&purpose_codes="+purpose_codes+"");
        JSONObject json_result=new JSONObject(resultsStr).getJSONObject("data");
        String flag=json_result.getString("flag");
        List<cR> cN=new ArrayList<>();
        if("1".equals(flag)){
            JSONArray cO=json_result.getJSONArray("result");
            JSONObject cQ=json_result.getJSONObject("map");

            for(int cM=0;cM<cO.length();cM++){
                cR cR=new cR();
                String cL[]=cO.get(cM).toString().split("\\|");
                cR.secretStr = cL[0];
                cR.buttonTextInfo = cL[1];
                cP cP = new cP();
                cP.train_no = cL[2];
                cP.station_train_code = cL[3];
                cP.start_station_telecode = cL[4];
                cP.end_station_telecode = cL[5];
                cP.from_station_telecode = cL[6];
                cP.to_station_telecode = cL[7];
                cP.start_time = cL[8];
                cP.arrive_time = cL[9];
                cP.lishi = cL[10];
                cP.canWebBuy = cL[11];
                cP.yp_info = cL[12];
                cP.start_train_date = cL[13];
                cP.train_seat_feature = cL[14];
                cP.location_code = cL[15];
                cP.from_station_no = cL[16];
                cP.to_station_no = cL[17];
                cP.is_support_card = cL[18];
                cP.controlled_train_flag = cL[19];

                cP.gg_num = StringUtils.isNotEmpty(cL[20]) ? cL[20] : "--";
                cP.gr_num = StringUtils.isNotEmpty(cL[21]) ? cL[21] : "--";
                cP.qt_num = StringUtils.isNotEmpty(cL[22]) ? cL[22] : "--";
                cP.rw_num = StringUtils.isNotEmpty(cL[23]) ? cL[23] : "--";
                cP.rz_num = StringUtils.isNotEmpty(cL[24]) ? cL[24] : "--";
                cP.tz_num = StringUtils.isNotEmpty(cL[25]) ? cL[25] : "--";
                cP.wz_num = StringUtils.isNotEmpty(cL[26]) ? cL[26] : "--";
                cP.yb_num = StringUtils.isNotEmpty(cL[27]) ? cL[27] : "--";
                cP.yw_num = StringUtils.isNotEmpty(cL[28]) ? cL[28] : "--";
                cP.yz_num = StringUtils.isNotEmpty(cL[29]) ? cL[29] : "--";
                cP.ze_num = StringUtils.isNotEmpty(cL[30]) ? cL[30] : "--";
                cP.zy_num = StringUtils.isNotEmpty(cL[31]) ? cL[31] : "--";
                cP.swz_num = StringUtils.isNotEmpty(cL[32]) ? cL[32] : "--";
                cP.srrb_num = StringUtils.isNotEmpty(cL[33]) ? cL[33] : "--";
                cP.yp_ex = cL[34];
                cP.seat_types = cL[35];
                cP.exchange_train_flag = cL[36];
                cP.houbu_train_flag = cL[37];
                if (cL.length > 38) {
                    cP.houbu_seat_limit = cL[38];
                }

                cP.from_station_name = cQ.getString(cL[6]);

                cP.to_station_name=cQ.getString(cL[7]);

                cR.queryLeftNewDTO = cP;

                cN.add(cR);
            }
            return cN;
        }
        return null;
    }
    public static void main(String[] args) {
        List<cR> list=search("2019-04-23","BJP","SHH","ADULT");
        for(cR cr:list){
            System.out.println(cr.queryLeftNewDTO.station_train_code+":"+cr.queryLeftNewDTO.yz_num);
        }
    }
}
