package com.sunrt.train.ticket;

public interface Constant {
    String SeatsText[]=new String[]{"商务座","一等座","二等座","高级软卧","软卧一等卧","动卧","硬卧二等卧","软座","硬座","无座"};

    String ENCODING="utf-8";
    String submitOrderRequest="https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
    String DCURL="https://kyfw.12306.cn/otn/confirmPassenger/initDc";
    String WCURL="https://kyfw.12306.cn/otn/confirmPassenger/initWc";

    String DCQUEUE="https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
    String WCQUEUE="https://kyfw.12306.cn/otn/confirmPassenger/confirmGoForQueue";

    String PASSENGERURL="https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";

    String CHECKORDERINFOURL="https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";

    String QUEUECOUNTURL="https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
    String QUERYORDERWAITTIME="https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";
    String RESULTORDERURL="https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForWcQueue";

    String DC="dc";
    String WC="wc";

    String CANCELFLOG="2";
    String BED_LEVEL_ORDER_NUM="000000000000000000000000000000";
    String WHATSELECT="1";


}
