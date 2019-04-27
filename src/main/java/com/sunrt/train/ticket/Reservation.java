package com.sunrt.train.ticket;

import com.sunrt.train.data.Cr;
import com.sunrt.train.utils.DateUtils;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class Reservation {

    public static String initC(String type){
        List<NameValuePair> lp= Form.form().add("_json_att", "").build();
        if(type.equals(Constant.DC)){
            return HttpUtils.PostStr(Constant.DCURL,lp);
        }else if(type.equals(Constant.WC)){
            return HttpUtils.PostStr(Constant.WCURL, lp);
        }
        return null;
    }

    public static JSONObject getPassengerDTOs(String REPEAT_SUBMIT_TOKEN){
        return HttpUtils.Post(Constant.PASSENGERURL, Form.form()
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN).build());
    }

    public static JSONObject submitOrderRequest(Cr cr,Param p){
        try {
            return HttpUtils.Post(Constant.submitOrderRequest,Form.form()
                    .add("secretStr", URLDecoder.decode( cr.secretStr, Constant.ENCODING))//票id
                    .add("train_date", p.trainDate)//出发日期
                    .add("back_train_date", DateUtils.getToday("yyyy-MM-dd"))//返程日期(单程为查票日期)
                    .add("tour_flag", p.tour_flag)//表单单程车票
                    .add("purpose_codes", p.purpose_codes)//成人票
                    .add("query_from_station_name", URLEncoder.encode(p.from_sta_str,Constant.ENCODING))//出发站
                    .add("query_to_station_name", URLEncoder.encode(p.to_sta_str))//到达站
                    .add("undefined", null)
                    .build());
        } catch (Exception e) {}
        return null;
    }

    public static JSONObject getQueueCount(){
        String queueCountUrl="https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
        return null;
    }

    public static JSONObject checkOrderInfo(String tour_flag,String globalRepeatSubmitToken,String cancel_flag,String bed_level_order_num){
        HttpUtils.Post(Constant.CHECKORDERINFOURL, Form.form()
                .add("cancel_flag", cancel_flag)// 固定值
                .add("bed_level_order_num", bed_level_order_num)// 固定值
                //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
                .add("passengerTicketStr", getPassengerTicketStr())// 旅客信息字符串
                //乘客名,证件类型,证件号,乘客类型
                .add("oldPassengerStr", oldPassengerStr())// 旅客信息字符串
                .add("tour_flag", tour_flag)  //ticketInfoForPassengerForm中获取
                .add("randCode", "")// 验证码
                .add("whatsSelect", Constant.WHATSELECT)//是否选择了联系人
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", globalRepeatSubmitToken)
                .build());
        return null;
    }

    public static String getPassengerTicketStr(){
        String aA = "";
        //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
        /*for (int aB = 0; aB < limit_tickets.length; aB++) {

        }*/
        return null;
    }

    /*getpassengerTickets = function() {

                var aA = "";
                for (var aB = 0; aB < limit_tickets.length; aB++) {
                    var aC = limit_tickets[aB].seat_type + ",0," + limit_tickets[aB].ticket_type + "," + limit_tickets[aB].name + "," + limit_tickets[aB].id_type + "," + limit_tickets[aB].id_no + "," + (limit_tickets[aB].phone_no == null ? "": limit_tickets[aB].phone_no) + "," + (limit_tickets[aB].save_status == "" ? "N": "Y");
                    aA += aC + "_"
                }
                return aA.substring(0, aA.length - 1)
            };*/

    public static String oldPassengerStr(){
        return null;
    }
}
