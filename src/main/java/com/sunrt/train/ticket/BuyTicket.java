package com.sunrt.train.ticket;

import com.sunrt.train.data.Tickets;
import com.sunrt.train.data.cP;
import com.sunrt.train.data.cR;
import com.sunrt.train.login.Login;
import com.sunrt.train.utils.CharUtils;
import com.sunrt.train.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyTicket {
    private static Param p;
    private static List<cR>list;
    private static cR cr;
    public static void setP(Param p) {
        BuyTicket.p = p;
    }

    private final static String submitOrderRequest="https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
    private final static String DCURL="https://kyfw.12306.cn/otn/confirmPassenger/initDc";
    private final static String WCURL="https://kyfw.12306.cn/otn/confirmPassenger/initWc";

    private final static String DCQUEUE="https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
    private final static String WCQUEUE="https://kyfw.12306.cn/otn/confirmPassenger/confirmGoForQueue";

    private final static String PASSENGERURL="https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";

    private static String globalRepeatSubmitToken;
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

        //是否登录
        if(Login.checkUser()){
            JSONObject sorJson=submitOrderRequest();
            boolean sorStatus=sorJson.getBoolean("status");
            String sorData=sorJson.getString("data");
            if(sorStatus&&"N".equals(sorData)){
                String html=null;
                if(p.tour_flag.equals(TicketType.DC)){
                    html=initC(TicketType.DC);
                }else if(p.tour_flag.equals(TicketType.WC)){
                    html=initC(TicketType.WC);
                }

                if(StringUtils.isNotEmpty(html)){
                    String htmlArr[]=html.split("\n");
                    String globalRepeatSubmitTokenReg="(?<=globalRepeatSubmitToken = \')[^\']+";
                    Matcher globalRepeatSubmitTokenRegMatch=Pattern.compile(globalRepeatSubmitTokenReg).matcher(html);
                    globalRepeatSubmitToken=globalRepeatSubmitTokenRegMatch.find()?globalRepeatSubmitTokenRegMatch.group():null;
                    String ticketInfoForPassengerFormReg="(?<=ticketInfoForPassengerForm=).*(?=;)";
                    String ticketInfoForPassengerForm;
                    Matcher ticketInfoForPassengerFormMatch=Pattern.compile(globalRepeatSubmitTokenReg).matcher(html);
                    ticketInfoForPassengerForm=ticketInfoForPassengerFormMatch.find()?ticketInfoForPassengerFormMatch.group():null;

                }
                if(globalRepeatSubmitToken!=null){
                    JSONObject passengerJson=getPassengerDTOs(globalRepeatSubmitToken);


                }

            }else{
                System.out.println("存在未处理订单！");
            }
            System.out.println(sorJson);
        }else{
            System.out.println("未登录");
        }
    }

    public static JSONObject getQueueCount(){
        String queueCountUrl="https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
        return null;
    }

    public static JSONObject checkOrderInfo(){
        String checkOrderInfoUrl="https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
        HttpUtils.Post(checkOrderInfoUrl, Form.form()
                .add("cancel_flag", "2")// 固定值
                .add("bed_level_order_num", "000000000000000000000000000000")// 固定值
                //座位编号,0,票类型,乘客名,证件类型,证件号,手机号码,保存常用联系人(Y或N)
                .add("passengerTicketStr", "")// 旅客信息字符串
                //乘客名,证件类型,证件号,乘客类型
                .add("oldPassengerStr", "")// 旅客信息字符串
                .add("tour_flag", "dc")  //
                .add("randCode", "")// 前台输入验证码
                .add("whatsSelect", "1")//固定值
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", globalRepeatSubmitToken)
                .build());

        return null;
    }
    public static JSONObject submitOrderRequest(){
        return HttpUtils.Post(submitOrderRequest,Form.form()
                .add("secretStr", cr.secretStr)//票id
                .add("train_date", p.trainDate)//出发日期
                .add("back_train_date", p.trainDate)//返程日期(单程和出发日期一样)
                .add("tour_flag", p.tour_flag)//表单单程车票
                .add("purpose_codes", p.purpose_codes)//成人票
                .add("query_from_station_name", p.from_sta_str)//出发站
                .add("query_to_station_name", p.to_sta_str)//到达站
                .add("undefined", "")
                .build());
    }

    public static void PRList(){
        System.out.println();
        for(cR cr:list){
            System.out.println(cr.queryLeftNewDTO.station_train_code);
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
        Proority prooritys[]=Proority.getProority();
        if(list==null||list.size()==0){
            cr=null;
            return;
        }
        Collections.sort(list, new Comparator<cR>() {
            @Override
            public int compare(cR c1, cR c2) {
                int x=0;
                if(prooritys!=null){
                    for(Proority proority:prooritys){
                        if(x==0){
                            switch (proority.proorityId){
                                case traintype:
                                    if(p.trainType!=null){
                                        String tc1=c1.queryLeftNewDTO.station_train_code;
                                        String tc2=c2.queryLeftNewDTO.station_train_code;
                                        char le1=getFirstLetter(tc1.charAt(0));
                                        char le2=getFirstLetter(tc2.charAt(0));
                                        int a=p.trainType.indexOf(le1);
                                        int b=p.trainType.indexOf(le2);
                                        x=a-b;
                                    }
                                    break;
                                case seattype:
                                    Stum psts[]=p.st;
                                    if(psts!=null){
                                        for(int k=0;k<psts.length;k++){
                                            int sc1=0;
                                            int sc2=0;
                                            if(Seats.getSeatCount(psts[k],c1.queryLeftNewDTO)!=null){
                                                sc1=1;
                                            }
                                            if(Seats.getSeatCount(psts[k],c2.queryLeftNewDTO)!=null){
                                                sc2=1;
                                            }
                                            if(sc1!=sc2){
                                                x=sc2-sc1;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                case starttime:
                                    if(p.starttime!=null){
                                        int hour=p.starttime[0];
                                        int min=p.starttime[1];
                                        String time1[]=c1.queryLeftNewDTO.start_time.split(":");
                                        String time2[]=c2.queryLeftNewDTO.start_time.split(":");
                                        int habs1=Math.abs(Integer.parseInt(time1[0])-hour);
                                        int mabs1=Math.abs(Integer.parseInt(time1[1])-min);
                                        int habs2=Math.abs(Integer.parseInt(time2[0])-hour);
                                        int mabs2=Math.abs(Integer.parseInt(time2[1])-min);
                                        x=habs1==habs2?mabs1-mabs2:habs1-habs2;
                                    }
                                    break;
                                case arrivetime:
                                    if(p.arrTime!=null){
                                        int hour=p.arrTime[0];
                                        int min=p.arrTime[1];
                                        String time1[]=c1.queryLeftNewDTO.arrive_time.split(":");
                                        String time2[]=c2.queryLeftNewDTO.arrive_time.split(":");
                                        int habs1=Math.abs(Integer.parseInt(time1[0])-hour);
                                        int mabs1=Math.abs(Integer.parseInt(time1[1])-min);
                                        int habs2=Math.abs(Integer.parseInt(time2[0])-hour);
                                        int mabs2=Math.abs(Integer.parseInt(time2[1])-min);
                                        x=habs1==habs2?mabs1-mabs2:habs1-habs2;
                                    }
                                    break;
                                case lishi:
                                    String lishi1[]=c1.queryLeftNewDTO.lishi.split(":");
                                    String lishi2[]=c2.queryLeftNewDTO.lishi.split(":");
                                    int hour1=Integer.parseInt(lishi1[0]);
                                    int hour2=Integer.parseInt(lishi2[0]);
                                    int min1=Integer.parseInt(lishi1[1]);
                                    int min2=Integer.parseInt(lishi2[1]);
                                    x=hour1==hour2?min1-min2:hour1-hour2;
                                    break;
                            }
                        }else{
                            break;
                        }
                    }
                }
                return x;
            }
        });
        cr=list.get(0);
    }

    public static JSONObject getPassengerDTOs(String REPEAT_SUBMIT_TOKEN){
        return HttpUtils.Post(PASSENGERURL, Form.form()
                .add("_json_att", "")
                .add("REPEAT_SUBMIT_TOKEN", globalRepeatSubmitToken).build());
    }

    public static String initC(String type){
        List<NameValuePair> lp=Form.form().add("_json_att", "").build();
        if(type.equals(TicketType.DC)){
            return HttpUtils.PostStr(DCURL,lp);
        }else if(type.equals(TicketType.WC)){
            return HttpUtils.PostStr(WCURL, lp);
        }
        return null;
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
                    if(count!=null){
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
