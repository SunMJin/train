package com.sunrt.train.ticket;

import com.sunrt.train.TrainHttp;
import com.sunrt.train.data.Cr;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.utils.DateUtils;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

public class Reservation {

    private static HttpUtils httpUtils =TrainHttp.getHttp();

    public static String initC(String type) throws HttpException {
        List<NameValuePair> lp= Form.form().add("_json_att", "").build();
        String url=null;
        if(type.equals(Constant.DC)){
            url=Constant.DCURL;
        }else if(type.equals(Constant.WC)){
            url=Constant.WCURL;
        }
        return httpUtils.PostHtml(url,lp);
    }

    public static JSONObject getPassengerDTOs(String REPEAT_SUBMIT_TOKEN) throws HttpException {
        return httpUtils.Post(Constant.PASSENGERURL, Form.form()
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN).build());
    }

    public static JSONObject submitOrderRequest(Cr cr,Param p) throws HttpException {
        try {
            return httpUtils.Post(Constant.submitOrderRequest,Form.form()
                    .add("secretStr", URLDecoder.decode( cr.secretStr, Constant.ENCODING))//票id
                    .add("train_date", p.trainDate)//出发日期
                    .add("back_train_date", DateUtils.getToday("yyyy-MM-dd"))//返程日期(单程为查票日期)
                    .add("tour_flag", p.tour_flag)//表单单程车票
                    .add("purpose_codes", p.purpose_codes)//成人票
                    .add("query_from_station_name", URLEncoder.encode(p.from_sta_str,Constant.ENCODING))//出发站
                    .add("query_to_station_name", URLEncoder.encode(p.to_sta_str,Constant.ENCODING))//到达站
                    .add("undefined", null)
                    .build());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    public static JSONObject getQueueCount(String train_date,String train_no,String stationTrainCode,
                                           String seatType,String fromStationTelecode,String toStationTelecode
                                            ,String leftTicket,String purpose_codes,String train_location
                                            ,String REPEAT_SUBMIT_TOKEN) throws HttpException {
        List<NameValuePair> list=Form.form()
                .add("train_date", train_date)
                .add("train_no", train_no)
                .add("stationTrainCode", stationTrainCode)
                .add("seatType", seatType)
                .add("fromStationTelecode", fromStationTelecode)
                .add("toStationTelecode", toStationTelecode)
                .add("leftTicket", leftTicket)
                .add("purpose_codes", purpose_codes)
                .add("_json_att", "")
                .add("train_location", train_location)
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN)
                .add("", "").build();
        return httpUtils.Post(Constant.QUEUECOUNTURL,list);
    }

    public static JSONObject checkOrderInfo(String tour_flag,String globalRepeatSubmitToken,String cancel_flag,String bed_level_order_num,String PassengerTicketStr,String oldPassengerStr) throws HttpException {
        return httpUtils.Post(Constant.CHECKORDERINFOURL, Form.form()
                .add("cancel_flag", cancel_flag)// 固定值
                .add("bed_level_order_num", bed_level_order_num)// 固定值
                //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
                .add("passengerTicketStr", PassengerTicketStr)// 旅客信息字符串

                .add("oldPassengerStr", oldPassengerStr)// 旅客信息字符串
                .add("tour_flag", tour_flag)  //ticketInfoForPassengerForm中获取
                .add("randCode", "")// 验证码
                .add("whatsSelect", Constant.WHATSELECT)//是否选择了联系人
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", globalRepeatSubmitToken)
                .build());
    }


    public static JSONObject confirmQueue(String passengerTicketStr,String oldPassengerStr,
                                          String purpose_codes,String key_check_isChange,String leftTicketStr,
                                          String train_location,String choose_seats,String seatDetailType,
                                          String REPEAT_SUBMIT_TOKEN,String ctype) throws HttpException {
        Form form=Form.form()
                .add("passengerTicketStr", passengerTicketStr)
                .add("oldPassengerStr", oldPassengerStr)
                .add("randCode", "")
                .add("purpose_codes", purpose_codes)
                .add("key_check_isChange", key_check_isChange)
                .add("leftTicketStr", leftTicketStr)
                .add("train_location", train_location)
                .add("choose_seats", choose_seats)
                .add("seatDetailType", seatDetailType)
                .add("whatsSelect", "1")
                .add("roomType", "00")
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN);
        String url=null;
        if(ctype.equals(Constant.DC)){
            url=Constant.DCQUEUE;
            form.add("dwAll","N");
        }else if(ctype.equals(Constant.WC)){
            url=Constant.WCQUEUE;
        }
        return httpUtils.Post(url,form.build() );
    }


    public static JSONObject queryOrderWaitTime(String tourFlag,String REPEAT_SUBMIT_TOKEN) throws HttpException {
        List<NameValuePair> list=Form.form()
                .add("random",String.valueOf(new Date().getTime()))
                .add("tourFlag", tourFlag)
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN)
                .build();
        return httpUtils.Post(Constant.QUERYORDERWAITTIME, list);
    }

    public static JSONObject resultOrderForWcQueue(String orderSequence_no,String REPEAT_SUBMIT_TOKEN) throws HttpException {
        List<NameValuePair> list=Form.form()
                .add("orderSequence_no",orderSequence_no)
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN)
                .build();
        return httpUtils.Post(Constant.RESULTORDERURL, list);
    }

    public static String getPassengerTicketStr(String seatId){
        if(seatId==null){
            throw new NullPointerException();
        }
        //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
        return seatId+",0,"+"1,孙梦金,1,"+"320322199403184415,"+"13861732734,"+"N";
    }

    public static String oldPassengerStr(){
        //乘客名,证件类型,证件号,乘客类型
        return "孙梦金,1,320322199403184415,1_";
    }

}
