package com.sunrt.train.ticket;

import com.sunrt.train.data.Tickets;
import com.sunrt.train.data.cP;
import com.sunrt.train.data.cR;
import com.sunrt.train.login.Login;
import com.sunrt.train.utils.CharUtils;
import com.sunrt.train.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BuyTicket {
    private static Param p;
    private static List<cR>list;
    public static void setP(Param p) {
        BuyTicket.p = p;
    }

    private final static int DC=0;
    private final static int WC=1;

    private final static String submitOrderRequest="https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
    private final static String DCURL="https://kyfw.12306.cn/otn/confirmPassenger/initDc";
    private final static String WCURL="https://kyfw.12306.cn/otn/confirmPassenger/initWc";

    private final static String DCQUEUE="https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
    private final static String WCQUEUE="https://kyfw.12306.cn/otn/confirmPassenger/confirmGoForQueue";

    private final static String PASSENGERURL="https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";


    public static void start (Param p){
        BuyTicket.p=p;
        list=Tickets.searchTickets(p);
        if(list==null){
            System.out.println("没有搜索到车票！");
            return;
        }
        //筛选
        retrieve();
        //根据优先级决定购买的车次
        selectTicket();
        if(Login.checkUser()){

        }
    }

    private static char getFirstLetter(char c){
        if(CharUtils.isLetter(c)){
           return c;
        }else{
           return 'Q';
        }
    }
    public static void selectTicket(){
        //车次类型>时间
        Collections.sort(list, new Comparator<cR>() {
            @Override
            public int compare(cR c1, cR c2) {
                String tc1=c1.queryLeftNewDTO.station_train_code;
                String tc2=c2.queryLeftNewDTO.station_train_code;
                char le1=getFirstLetter(tc1.charAt(0));
                char le2=getFirstLetter(tc2.charAt(0));
                int a=0;
                int b=0;
                if(p.trainType!=null){
                    a=p.trainType.indexOf(le1);
                    b=p.trainType.indexOf(le2);
                }
                if(a==b&&p.arrTime!=null){
                    int hour=p.arrTime[0];
                    int min=p.arrTime[1];
                    String time1[]=c1.queryLeftNewDTO.arrive_time.split(":");
                    String time2[]=c2.queryLeftNewDTO.arrive_time.split(":");

                    int habs1=Math.abs(Integer.parseInt(time1[0])-hour);
                    int mabs1=Math.abs(Integer.parseInt(time1[1])-min);

                    int habs2=Math.abs(Integer.parseInt(time2[0])-hour);
                    int mabs2=Math.abs(Integer.parseInt(time2[1])-min);
                    if(habs1==habs2){
                        return mabs1-mabs2;
                    }
                    return habs1-habs2;
                }
                return a-b;
            }
        });
    }

    public JSONObject getPassengerDTOs(){
        return HttpUtils.Post(PASSENGERURL, Form.form()
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", "").build());//initc中获取的参数
    }

    public static JSONObject submitOrderRequest(){
        return HttpUtils.Post(submitOrderRequest,Form.form()
                .add("secretStr", "")//cR.secretStr
                .add("train_date", "")//出发日期
                .add("back_train_date", "")//当前查询日期（如果是往返票，则为返程日期，也即出发日期）
                .add("tour_flag", "dc")//表单单程车票
                .add("purpose_codes", "ADULT")//成人票
                .add("query_from_station_name", "")//出发站
                .add("query_to_station_name", "")//到达站
                .add("undefined", "")
                .build());
    }

    public String initC(int type){
        List<NameValuePair> lp=Form.form().add("_json_att", "").build();
        if(type==DC){
            return HttpUtils.PostStr(DCURL,lp);
        }else{
            return HttpUtils.PostStr(WCURL, lp);
        }
    }

    public static List<cR> retrieve(){
        if(list==null){
            System.out.println("没有搜索到车票！");
            return null;
        }
        String trainType=p.trainType;
        Iterator<cR>it=list.iterator();
        while(it.hasNext()){
            cP cp=it.next().queryLeftNewDTO;
            String stacode=cp.station_train_code;
            char fchar=getFirstLetter(stacode.charAt(0));

            //删除无法购买的
            if(!"Y".equals(cp.canWebBuy)){
                it.remove();
                continue;
            }

            //车次筛选
            if(trainType!=null&&trainType.indexOf(fchar)==-1){
                it.remove();
                continue;
            }
            //座位类型筛选
            Stum st[]=p.st;
            if(st!=null){
                boolean flag=false;
                for(int i=0;i<st.length;i++){
                    String count=Seats.getSeatCount(st[i],cp);
                    if(!"0".equals(count)){
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    it.remove();
                    continue;
                }
            }
        }
        return list;
    }
}
