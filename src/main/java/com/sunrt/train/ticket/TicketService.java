package com.sunrt.train.ticket;

import com.sunrt.train.bean.Cr;
import com.sunrt.train.bean.OrderInfo;
import com.sunrt.train.bean.Param;
import com.sunrt.train.context.Context;
import com.sunrt.train.login.LoginService;

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

    public void start(Param param) {
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
        Context context=new Context(param,cr);
        List<OrderInfo> plist=context.getOrderInfos();
        for(OrderInfo orderInfo:plist){
            Tickets.buy(orderInfo);
        }
    }
}
