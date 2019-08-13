package com.sunrt.train.ticket;

import com.sunrt.train.data.Cr;
import com.sunrt.train.data.Tickets;
import com.sunrt.train.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;


public class TicketService {

    private static TicketService ticketService;
    private TicketService() {}
    public static TicketService getInstance() {
        if (ticketService == null) {
            ticketService = new TicketService();
        }
        return ticketService;
    }
    private LoginService login = LoginService.getInstance();
    private Context context;
    private Param param;


    public void start(Param p) {
        this.param = p;
        List<Cr> list = Tickets.searchTickets(param);
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
        if (!login.checkUser()) {
            System.out.println("未登录");
            return;
        }
        //是否存在未完成订单
        if (!Reservation.submitOrderRequest(cr, param)) {
            System.out.println("存在未完成订单！");
            return;
        }
        context=new Context(param,cr);
        List<OrderInfo> plist=context.getOrderInfos();
        for(OrderInfo orderInfo:plist){
            buy(orderInfo);
        }
    }

    public void buy(OrderInfo orderInfo) {
        Reservation reservation=new Reservation(orderInfo);
        JSONObject checkOrderInfoJson = reservation.checkOrderInfo();
        if (checkOrderInfoJson.getBoolean("status")) {
            JSONObject checkOrderInfoJsonData = checkOrderInfoJson.getJSONObject("data");
            if (checkOrderInfoJsonData.getBoolean("submitStatus")) {
                JSONObject queueCountJson = reservation.getQueueCount();
                if (queueCountJson.getBoolean("status")) {
                    JSONObject queueCountData = queueCountJson.getJSONObject("data");
                    System.out.println(queueCountData.getString("ticket").split("0")[0]);
                    int count = Integer.parseInt(queueCountData.getString("ticket").split(",")[0]);
                    if (count > 0) {
                        JSONObject confirmQueueJson = reservation.confirmQueue();
                        if (confirmQueueJson.getBoolean("status") && confirmQueueJson.getJSONObject("data").getBoolean("submitStatus")) {
                            String orderId;
                            while (true) {
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
                            JSONObject resultOrderForWcQueueJson = reservation.resultOrderForWcQueue(orderId);
                            if (resultOrderForWcQueueJson.getBoolean("status")) {
                                if (resultOrderForWcQueueJson.getJSONObject("data").getBoolean("submitStatus")) {
                                    System.out.println("下单状态成功！");
                                }
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
