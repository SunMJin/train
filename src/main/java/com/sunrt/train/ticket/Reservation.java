package com.sunrt.train.ticket;

import com.sunrt.train.bean.Cr;
import com.sunrt.train.bean.OrderInfo;
import com.sunrt.train.bean.TrainParam;
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
    private HttpUtils httpUtils;
    private OrderInfo orderInfo;

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Reservation(HttpUtils httpUtils, OrderInfo orderInfo) {
        this.httpUtils = httpUtils;
        this.orderInfo = orderInfo;
    }

    public String initC(String type) {
        List<NameValuePair> lp= Form.form().add("_json_att", "").build();
        String url=null;
        if(type.equals(Constant.DC)){
            url=Constant.DCURL;
        }else if(type.equals(Constant.WC)){
            url=Constant.WCURL;
        }
        return httpUtils.postHtml(url,lp);
    }

    public JSONObject getPassengerDTOs(String REPEAT_SUBMIT_TOKEN) {
        return httpUtils.postJson(Constant.PASSENGERURL, Form.form()
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", REPEAT_SUBMIT_TOKEN).build());
    }

    public boolean submitOrderRequest(Cr cr, TrainParam p) {
        JSONObject json;
        try {
            json= httpUtils.postJson(Constant.submitOrderRequest,Form.form()
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
        return json.getBoolean("status");
    }

    public JSONObject getQueueCount() {
        JSONObject orderRequestDTOJson=orderInfo.getOrderRequestDTOJson();
        JSONObject ticketInfoForPassengerForm=orderInfo.getTicketInfoForPassengerForm();
        String train_date = DateUtils.getTrainDate(orderRequestDTOJson.getJSONObject("train_date"));
        String train_no = orderRequestDTOJson.getString("train_no");
        String stationTrainCode = orderRequestDTOJson.getString("station_train_code");
        String fromStationTelecode = orderRequestDTOJson.getString("from_station_telecode");
        String toStationTelecode = orderRequestDTOJson.getString("to_station_telecode");
        String leftTicket = ticketInfoForPassengerForm.getString("leftTicketStr");
        String purpose_codes = ticketInfoForPassengerForm.getString("purpose_codes");
        String train_location = ticketInfoForPassengerForm.getString("train_location");
        //String key_check_isChange = ticketInfoForPassengerForm.getString("key_check_isChange");
        List<NameValuePair> list=Form.form()
                .add("train_date", train_date)
                .add("train_no", train_no)
                .add("stationTrainCode", stationTrainCode)
                .add("seatType", orderInfo.getSeatId())
                .add("fromStationTelecode", fromStationTelecode)
                .add("toStationTelecode", toStationTelecode)
                .add("leftTicket", leftTicket)
                .add("purpose_codes", purpose_codes)
                .add("_json_att", "")
                .add("train_location", train_location)
                .add("REPEAT_SUBMIT_TOKEN", orderInfo.getGlobalRepeatSubmitToken())
                .add("", "").build();
        return httpUtils.postJson(Constant.QUEUECOUNTURL,list);
    }

    public JSONObject checkOrderInfo() {
        return  httpUtils.postJson(Constant.CHECKORDERINFOURL, Form.form()
                .add("cancel_flag", orderInfo.getCancel_flag())// 固定值
                .add("bed_level_order_num", orderInfo.getBed_level_order_num())// 固定值
                //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
                .add("passengerTicketStr", orderInfo.getPassengerTicketStr())// 旅客信息字符串
                .add("oldPassengerStr", orderInfo.getOldPassengerStr())// 旅客信息字符串
                .add("tour_flag", orderInfo.getTour_flag())  //ticketInfoForPassengerForm中获取
                .add("randCode", "")// 验证码
                .add("whatsSelect", Constant.WHATSELECT)//是否选择了联系人
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", orderInfo.getGlobalRepeatSubmitToken())
                .build());
    }

    public JSONObject confirmQueue() {
        JSONObject ticketInfoForPassengerForm=orderInfo.getTicketInfoForPassengerForm();
        String leftTicket = ticketInfoForPassengerForm.getString("leftTicketStr");
        String purpose_codes = ticketInfoForPassengerForm.getString("purpose_codes");
        String train_location = ticketInfoForPassengerForm.getString("train_location");
        String key_check_isChange = ticketInfoForPassengerForm.getString("key_check_isChange");
        Form form=Form.form()
                .add("passengerTicketStr", orderInfo.getPassengerTicketStr())
                .add("oldPassengerStr", orderInfo.getOldPassengerStr())
                .add("randCode", "")
                .add("purpose_codes", purpose_codes)
                .add("key_check_isChange", key_check_isChange)
                .add("leftTicketStr", leftTicket)
                .add("train_location", train_location)
                .add("choose_seats", "")
                .add("seatDetailType", "000")
                .add("whatsSelect", "1")
                .add("roomType", "00")
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", orderInfo.getGlobalRepeatSubmitToken());
        String url=null;
        String ctype=orderInfo.getTour_flag();
        if(ctype.equals(Constant.DC)){
            url=Constant.DCQUEUE;
            form.add("dwAll","N");
        }else if(ctype.equals(Constant.WC)){
            url=Constant.WCQUEUE;
        }
        return httpUtils.postJson(url,form.build() );
    }


    public JSONObject queryOrderWaitTime() {
        List<NameValuePair> list=Form.form()
                .add("random",String.valueOf(new Date().getTime()))
                .add("tourFlag", orderInfo.getTour_flag())
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", orderInfo.getGlobalRepeatSubmitToken())
                .build();
        return httpUtils.postJson(Constant.QUERYORDERWAITTIME, list);
    }

    public boolean resultOrderForDcQueue(String orderSequence_no) {
        List<NameValuePair> list=Form.form()
                .add("orderSequence_no",orderSequence_no)
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", orderInfo.getGlobalRepeatSubmitToken())
                .build();
        JSONObject json=httpUtils.postJson(Constant.RESULTORDERURL, list);
        if (json.getBoolean("status")) {
            if (json.getJSONObject("data").getBoolean("submitStatus")) {
                return true;
            }
        }
        return false;
    }





}
