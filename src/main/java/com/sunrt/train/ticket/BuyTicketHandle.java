package com.sunrt.train.ticket;

import com.sunrt.train.data.Cr;
import com.sunrt.train.data.Tickets;
import com.sunrt.train.exception.HttpException;
import com.sunrt.train.login.Login;
import com.sunrt.train.utils.DateUtils;
import com.sunrt.train.utils.RegUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BuyTicketHandle {

    public static void start (Param param) throws HttpException {
        Param p=param;
        List<Cr>list=Tickets.searchTickets(p);
        if(list==null||list.size()==0){
            System.out.println("没有搜索到车票或查询失败！");
            return;
        }
        //筛选
        list=SelectTickets.retrieve(list,p);
        if(list.size()==0){
            System.out.println("没有符合条件的车次！");
            return;
        }
        //根据优先级决定购买的车次
        Cr cr=SelectTickets.orderTicket(list,p);
        //是否登录
        if(!Login.checkUser()){
            System.out.println("未登录");
            return;
        }
        //是否存在未完成订单
        JSONObject sorJson=Reservation.submitOrderRequest(cr,p);
        if(!sorJson.getBoolean("status")){
            System.out.println(sorJson.getJSONArray("messages"));
            return;
        }
        //获取相关配置
        String html=null;
        if(p.tour_flag.equals(Constant.DC)){
            html=Reservation.initC(Constant.DC);
        }else if(p.tour_flag.equals(Constant.WC)){
            html=Reservation.initC(Constant.WC);
        }

        //相关变量
        String globalRepeatSubmitToken= RegUtils.getStrByReg("(?<=globalRepeatSubmitToken = \')[^\']+",html);


        JSONObject ticketInfoForPassengerForm=RegUtils.getJSONByReg("(?<=ticketInfoForPassengerForm=).*(?=;)",html);
        JSONObject limitBuySeatTicketDTOJson=ticketInfoForPassengerForm.getJSONObject("limitBuySeatTicketDTO");
        //证件类型
        ticketInfoForPassengerForm.getJSONArray("cardTypes");
        //座位类型
        JSONArray seat_type_codes=limitBuySeatTicketDTOJson.getJSONArray("seat_type_codes");
        //票类型
        JSONArray ticket_type_codes=limitBuySeatTicketDTOJson.getJSONArray("ticket_type_codes");
        //联系人列表
        JSONArray normal_passengers=null;
        JSONObject passengerJson=Reservation.getPassengerDTOs(globalRepeatSubmitToken);
        boolean passengerStatus=passengerJson.getBoolean("status");
        if(passengerStatus){
            normal_passengers=passengerJson.getJSONObject("data").getJSONArray("normal_passengers");
        }

        //确定购买座位类型
        Stum st=Seats.confirmSeatType(p.st,cr);
        String seatId=null;
        if(st!=null){
            //获取座位的id
            seatId=Seats.getSeatId(p.st,st,seat_type_codes);
        }

        JSONObject orderRequestDTOJson=ticketInfoForPassengerForm.getJSONObject("orderRequestDTO");
        String tour_flag=orderRequestDTOJson.getString("tour_flag");

        String cancel_flag=Constant.CANCELFLOG;
        String bed_level_order_num=Constant.BED_LEVEL_ORDER_NUM;

        String PassengerTicketStr=Reservation.getPassengerTicketStr(seatId);
        String oldPassengerStr=Reservation.oldPassengerStr();
        JSONObject checkOrderInfoJson=Reservation.checkOrderInfo(tour_flag, globalRepeatSubmitToken, cancel_flag, bed_level_order_num,PassengerTicketStr,oldPassengerStr);
        if(checkOrderInfoJson.getBoolean("status")){
            JSONObject checkOrderInfoJsonData=checkOrderInfoJson.getJSONObject("data");
            if(checkOrderInfoJsonData.getBoolean("submitStatus")){
                //参数
                String train_date=DateUtils.getTrainDate(orderRequestDTOJson.getJSONObject("train_date"));
                String train_no=orderRequestDTOJson.getString("train_no");
                String stationTrainCode=orderRequestDTOJson.getString("station_train_code");
                String fromStationTelecode=orderRequestDTOJson.getString("from_station_telecode");
                String toStationTelecode=orderRequestDTOJson.getString("to_station_telecode");
                String leftTicket=ticketInfoForPassengerForm.getString("leftTicketStr");
                String purpose_codes=ticketInfoForPassengerForm.getString("purpose_codes");
                String train_location=ticketInfoForPassengerForm.getString("train_location");

                String key_check_isChange=ticketInfoForPassengerForm.getString("key_check_isChange");

                JSONObject queueCountJson=Reservation.getQueueCount(train_date,train_no,stationTrainCode,seatId,fromStationTelecode,toStationTelecode,leftTicket,purpose_codes,train_location,globalRepeatSubmitToken);

                if(queueCountJson.getBoolean("status")){
                    JSONObject queueCountData=queueCountJson.getJSONObject("data");
                    System.out.println(queueCountData.getString("ticket").split("0")[0]);
                    int count=Integer.parseInt(queueCountData.getString("ticket").split(",")[0]);
                    if(count>0){
                        JSONObject confirmQueueJson=Reservation.confirmQueue(PassengerTicketStr,oldPassengerStr,
                                purpose_codes,key_check_isChange,leftTicket,train_location,"","000",globalRepeatSubmitToken,p.tour_flag);
                        if(confirmQueueJson.getBoolean("status")&&confirmQueueJson.getJSONObject("data").getBoolean("submitStatus")){
                            String orderId=null;
                            while(true){
                                JSONObject queryOrderWaitTimeJson=Reservation.queryOrderWaitTime(tour_flag,globalRepeatSubmitToken);
                                if(queryOrderWaitTimeJson.getBoolean("status")){
                                    orderId=queryOrderWaitTimeJson.getJSONObject("data").getString("orderId");
                                    if(StringUtils.isNotEmpty(orderId)){
                                        System.out.println("已下单！");
                                        break;
                                    }
                                }
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException();
                                }
                            }
                            JSONObject resultOrderForWcQueueJson=Reservation.resultOrderForWcQueue(orderId, globalRepeatSubmitToken);
                            if(resultOrderForWcQueueJson.getBoolean("status")){
                                if(resultOrderForWcQueueJson.getJSONObject("data").getBoolean("submitStatus")){
                                    System.out.println("下单成功！");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
