package com.sunrt.train.ticket;

import com.sunrt.train.data.Cr;
import com.sunrt.train.data.Tickets;
import com.sunrt.train.login.Login;
import com.sunrt.train.utils.RegUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BuyTicketHandle {

    public static void start (Param param){
        try{
            Param p=param;
            List<Cr>list=Tickets.searchTickets(p);
            if(list==null||list.size()==0){
                System.out.println("没有搜索到车票或查询失败！");
                return;
            }
            //筛选
            list=SelectTickets.retrieve(list,p);
            //根据优先级决定购买的车次
            Cr cr=SelectTickets.orderTicket(list,p);
            //是否登录
            if(!Login.checkUser()){
                System.out.println("未登录");
                return;
            }
            //是否存在未完成订单
            JSONObject sorJson=Reservation.submitOrderRequest(cr,p);
            String sorData=sorJson.getString("data");
            if(!"N".equals(sorData)){
                System.out.println("请先处理未完成订单！");
                return;
            }
            //获取相关配置
            String html=null;
            if(p.tour_flag.equals(Constant.DC)){
                html=Reservation.initC(Constant.DC);
            }else if(p.tour_flag.equals(Constant.WC)){
                html=Reservation.initC(Constant.WC);
            }
            String globalRepeatSubmitToken= RegUtils.getStrByReg("(?<=globalRepeatSubmitToken = \')[^\']+",html);
            JSONObject ticketInfoForPassengerForm=RegUtils.getJSONByReg("(?<=ticketInfoForPassengerForm=).*(?=;)",html);
            JSONObject limitBuySeatTicketDTOJson=ticketInfoForPassengerForm.getJSONObject("limitBuySeatTicketDTO");
            JSONArray seat_type_codes=limitBuySeatTicketDTOJson.getJSONArray("seat_type_codes");
            JSONArray ticket_type_codes=limitBuySeatTicketDTOJson.getJSONArray("ticket_type_codes");

            //确定购买座位类型
            Stum st=Seats.confirmSeatType(p.st,cr);
            if(st!=null){
                //获取座位的id
                String seatId=Seats.getSeatId(p.st,st,seat_type_codes);
            }

            ticketInfoForPassengerForm.getJSONArray("cardTypes");
            JSONObject orderRequestDTOJson=ticketInfoForPassengerForm.getJSONObject("orderRequestDTO");
            String cancel_flag=null;
            if(orderRequestDTOJson.get("cancel_flag")==null){
                cancel_flag=Constant.CANCELFLOG;
            }
            String bed_level_order_num=null;
            if(orderRequestDTOJson.get("bed_level_order_num")==null){
                bed_level_order_num=Constant.BED_LEVEL_ORDER_NUM;
            }
            String tour_flag=null;
            tour_flag=orderRequestDTOJson.getString("tour_flag");
            JSONObject passengerJson=Reservation.getPassengerDTOs(globalRepeatSubmitToken);
            boolean passengerStatus=passengerJson.getBoolean("status");
            if(passengerStatus){
                JSONArray normal_passengers=passengerJson.getJSONObject("data").getJSONArray("normal_passengers");
            }
        }catch (Exception e){
        }
    }
}
