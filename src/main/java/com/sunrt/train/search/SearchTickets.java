package com.sunrt.train.search;

import com.sunrt.train.utils.HttpUtils;
import org.json.JSONObject;

public class SearchTickets {

    public static void search(){
        String resultsStr=HttpUtils.Get("https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2019-04-23&leftTicketDTO.from_station=WXH&leftTicketDTO.to_station=XCH&purpose_codes=ADULT");
        JSONObject json=new JSONObject(resultsStr);
        json.getString("")
    }
    public static void main(String[] args) {
        search();
    }
}
