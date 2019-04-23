package com.sunrt.train.ticket;

import com.sunrt.train.data.Tickets;
import com.sunrt.train.data.cR;

import java.util.List;

public class BuyTicket {

    public static void start (Param p){
        retrieve(p);
    }

    public static void retrieve(Param p){
        List<cR> list=Tickets.searchTickets(p);
        for(cR cr:list){
            String stacode=cr.queryLeftNewDTO.station_train_code;
            char tranType[]=p.trainType.toCharArray();
            for(char ty:tranType){
                if(stacode.startsWith(String.valueOf(ty))){
                    System.out.println(stacode);
                }
            }
        }
    }
}
