package com.sunrt.train.ticket;

import com.sunrt.train.data.Cr;
import com.sunrt.train.data.TrainConf;
import com.sunrt.train.utils.RegUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private Param param;
    private String html;
    private Cr cr;
    public Context(Param param,Cr cr) {
        this.param=param;
        this.cr=cr;
        if (param.tour_flag.equals(Constant.DC)) {
            html = Reservation.initC(Constant.DC);
        } else if (param.tour_flag.equals(Constant.WC)) {
            html = Reservation.initC(Constant.WC);
        }
        globalRepeatSubmitToken=getGlobalRepeatSubmitToken();
        ticketInfoForPassengerForm = RegUtils.getJSONByReg("(?<=ticketInfoForPassengerForm=).*(?=;)", html);
        //JSONObject limitBuySeatTicketDTOJson = ticketInfoForPassengerForm.getJSONObject("limitBuySeatTicketDTO");
        orderRequestDTOJson = ticketInfoForPassengerForm.getJSONObject("orderRequestDTO");
        tour_flag = orderRequestDTOJson.getString("tour_flag");
        cancel_flag = Constant.CANCELFLOG;
        bed_level_order_num = Constant.BED_LEVEL_ORDER_NUM;
        seatId=getSeatId();
    }

    private String seatId;
    private String tour_flag;
    private String globalRepeatSubmitToken;
    private String cancel_flag;
    private String bed_level_order_num;
    private JSONObject orderRequestDTOJson;
    private JSONObject ticketInfoForPassengerForm;
    private String allEncStr;

    //证件类型
    //ticketInfoForPassengerForm.getJSONArray("cardTypes");
    //票类型
    //JSONArray ticket_type_codes=limitBuySeatTicketDTOJson.getJSONArray("ticket_type_codes");

    public String getGlobalRepeatSubmitToken(){
        return RegUtils.getStrByReg("(?<=globalRepeatSubmitToken = \')[^\']+", html);
    }

    public JSONArray get_seat_type_codes(){
        JSONObject ticketInfoForPassengerForm = RegUtils.getJSONByReg("(?<=ticketInfoForPassengerForm=).*(?=;)", html);
        JSONObject limitBuySeatTicketDTOJson = ticketInfoForPassengerForm.getJSONObject("limitBuySeatTicketDTO");
        //座位类型
        return limitBuySeatTicketDTOJson.getJSONArray("seat_type_codes");
    }

    private JSONArray get_normal_passengers(){
        JSONObject passengerJson = Reservation.getPassengerDTOs(globalRepeatSubmitToken);
        boolean passengerStatus = passengerJson.getBoolean("status");
        if (passengerStatus) {
            return passengerJson.getJSONObject("data").getJSONArray("normal_passengers");
        }
        return null;
    }

    public String getSeatId(){
        //确定购买座位类型
        String st = Seats.confirmSeatType(param.st, cr);
        //获取座位的id
        return Seats.getSeatId(st, get_seat_type_codes());
    }

    public List<OrderInfo> getOrderInfos(){
        List<OrderInfo> list=new ArrayList<>();
        JSONArray normal_passengers=get_normal_passengers();
        //核对身份
        for (int k = 0; k < TrainConf.passenger_id_no.length; k++) {
            String pin_user = TrainConf.passenger_id_no[k];
            String pname_user = TrainConf.passenger_name[k];
            for (int i = 0; i < normal_passengers.length(); i++) {
                JSONObject json = normal_passengers.getJSONObject(i);
                //身份证
                String pin = json.getString("passenger_id_no");
                String pins[] = pin.replace("*", " ").split(" ");
                //姓名
                String pname = json.getString("passenger_name");
                //手机
                String mobile_no = json.getString("mobile_no");
                //乘客加密信息
                allEncStr = json.getString("allEncStr");
                if (json.getString("passenger_id_type_code").equals("1") && pname_user.equals(pname)
                        && pin_user.startsWith(pins[0]) && pin_user.endsWith(pins[1])) {
                    OrderInfo orderInfo=new OrderInfo();
                    orderInfo.setAllEncStr(allEncStr);
                    orderInfo.setPassenger_id_type_code("1");
                    orderInfo.setPassenger_name(pname);
                    orderInfo.setPassenger_id_no(pin);
                    orderInfo.setGlobalRepeatSubmitToken(globalRepeatSubmitToken);
                    orderInfo.setOrderRequestDTOJson(orderRequestDTOJson);
                    orderInfo.setTour_flag(tour_flag);
                    orderInfo.setCancel_flag(cancel_flag);
                    orderInfo.setBed_level_order_num(bed_level_order_num);
                    //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
                    orderInfo.setPassengerTicketStr(seatId+",0,"+"1,"+pname+",1,"+""+pin+","+""+mobile_no+","+"N,"+allEncStr);
                    orderInfo.setOldPassengerStr(pname+",1,"+pin+",1_");
                    orderInfo.setSeatId(seatId);
                    orderInfo.setTicketInfoForPassengerForm(ticketInfoForPassengerForm);
                    list.add(orderInfo);
                }
            }
        }
        return list;
    }
}
