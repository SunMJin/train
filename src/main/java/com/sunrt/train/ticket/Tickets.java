package com.sunrt.train.ticket;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.bean.Cp;
import com.sunrt.train.bean.Cr;
import com.sunrt.train.constant.QueryTicketConstant;
import com.sunrt.train.bean.Param;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Form;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tickets {


    private static HttpUtils httpUtils= TrainHttp.getInstance();

    public static List<Cr> searchTickets(Param p) {
        if(p==null){
            throw new NullPointerException();
        }
        JSONObject json_result = httpUtils.Get(
                QueryTicketConstant.QUERYURL, Form.form()
                        .add("leftTicketDTO.train_date",p.trainDate)
                        .add("leftTicketDTO.from_station",p.from_sta)
                        .add("leftTicketDTO.to_station",p.to_sta)
                        .add("purpose_codes",p.purpose_codes)
                        .build())
                .getJSONObject("data");
        List<Cr> cN=new ArrayList<>();
        JSONArray cO=json_result.getJSONArray("result");
        JSONObject cQ=json_result.getJSONObject("map");
        for(int cM=0;cM<cO.length();cM++){
            Cr cR=new Cr();
            String cL[]=cO.get(cM).toString().split("\\|");
            cR.secretStr = cL[0];
            cR.buttonTextInfo = cL[1];
            Cp cP = new Cp();
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
}
