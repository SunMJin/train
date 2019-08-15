package com.sunrt.train.ticket;

import com.sunrt.train.bean.Cp;
import com.sunrt.train.bean.Cr;
import com.sunrt.train.bean.OrderInfo;
import com.sunrt.train.bean.TrainParam;
import com.sunrt.train.conf.TrainConf;
import com.sunrt.train.constant.QueryTicketConstant;
import com.sunrt.train.context.Context;
import com.sunrt.train.login.LoginService;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Form;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private HttpUtils httpUtils;
    private LoginService loginService;

    private TicketService() {}
    public TicketService(HttpUtils httpUtils, LoginService loginService) {
        this.httpUtils = httpUtils;
        this.loginService = loginService;
        this.reservation=new Reservation(httpUtils,null);
    }
    private Reservation reservation;
    public void start(TrainParam param) {
        List<Cr> list = searchTickets(param);
        if (list == null || list.size() == 0) {
            System.out.println("没有搜索到车票或查询失败！");
            return;
        }
        //筛选
        list = SelectTickets.retrieve(list, param);
        if (list.size() == 0) {
            System.out.println("没有符合条件的车次！");
            return;
        }
        //根据优先级决定购买的车次
        Cr cr = SelectTickets.orderTicket(list, param);
        //是否登录
        if (!loginService.checkUser()) {
            System.out.println("未登录");
            return;
        }
        //是否存在未完成订单
        if (!reservation.submitOrderRequest(cr, param)) {
            System.out.println("存在未完成订单！");
            return;
        }
        Context context=new Context(param,cr,reservation);
        List<OrderInfo> plist=context.getOrderInfos();
        for(OrderInfo orderInfo:plist){
            buy(orderInfo);
        }
    }

    public List<Cr> searchTickets(TrainParam p) {
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

    public void buy(OrderInfo orderInfo) {
        reservation.setOrderInfo(orderInfo);
        JSONObject checkOrderInfoJson = reservation.checkOrderInfo();
        if (checkOrderInfoJson.getBoolean("status")) {
            JSONObject checkOrderInfoJsonData = checkOrderInfoJson.getJSONObject("data");
            if (checkOrderInfoJsonData.getBoolean("submitStatus")) {
                //查询余票
                JSONObject queueCountJson = reservation.getQueueCount();
                if (queueCountJson.getBoolean("status")) {
                    JSONObject queueCountData = queueCountJson.getJSONObject("data");
                    String tiket=queueCountData.getString("ticket");
                    int count = Integer.parseInt(tiket.split(",")[0]);
                    System.out.println(TrainConf.seatType+"剩余："+count+"张");
                    if (count > 0) {
                        //进入下单队列
                        JSONObject confirmQueueJson = reservation.confirmQueue();
                        System.out.println("已进入下单队列");
                        if (confirmQueueJson.getBoolean("status") && confirmQueueJson.getJSONObject("data").getBoolean("submitStatus")) {
                            String orderId;
                            while (true) {
                                //轮询订单状态
                                JSONObject queryOrderWaitTimeJson = reservation.queryOrderWaitTime();
                                if (queryOrderWaitTimeJson.getBoolean("status")) {
                                    orderId = queryOrderWaitTimeJson.getJSONObject("data").getString("orderId");
                                    if (StringUtils.isNotEmpty(orderId)) {
                                        System.out.println("已下单！");
                                        break;
                                    }
                                }
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //查询最终结果
                            if(reservation.resultOrderForDcQueue(orderId)){
                                System.out.println("订单已完成！请及时支付！");
                            }

                        }
                    }
                }
            } else {
                System.out.println(checkOrderInfoJsonData);
            }
        }
    }

}
