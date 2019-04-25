import com.sunrt.train.ticket.Stum;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test3 {

        public static void sss(){
            /*getpassengerTickets = function() {
                debugger
                var aA = "";
                for (var aB = 0; aB < limit_tickets.length; aB++) {
                    var aC = limit_tickets[aB].seat_type + ",0," + limit_tickets[aB].ticket_type + "," + limit_tickets[aB].name + "," + limit_tickets[aB].id_type + "," + limit_tickets[aB].id_no + "," + (limit_tickets[aB].phone_no == null ? "": limit_tickets[aB].phone_no) + "," + (limit_tickets[aB].save_status == "" ? "N": "Y");
                    aA += aC + "_"
                }
                return aA.substring(0, aA.length - 1)
            };*/

           /* getOldPassengers = function() {
                var aE = "";
                for (var aD = 0; aD < limit_tickets.length; aD++) {
                    var aA = limit_tickets[aD];
                    if (ticketInfoForPassengerForm.tour_flag == ticket_submit_order.tour_flag.fc || ticketInfoForPassengerForm.tour_flag == ticket_submit_order.tour_flag.gc) {
                        var aB = aA.name + "," + aA.id_type + "," + aA.id_no + "," + aA.passenger_type;
                        aE += aB + "_"
                    } else {
                        if (aA.only_id.indexOf("djPassenger_") > -1) {
                            var aC = aA.only_id.split("_")[1];
                            var aB = M[aC].passenger_name + "," + M[aC].passenger_id_type_code + "," + M[aC].passenger_id_no + "," + M[aC].passenger_type;
                            aE += aB + "_"
                        } else {
                            if (aA.only_id.indexOf("normalPassenger_") > -1) {
                                var aC = aA.only_id.split("_")[1];
                                var aB = az[aC].passenger_name + "," + az[aC].passenger_id_type_code + "," + az[aC].passenger_id_no + "," + az[aC].passenger_type;
                                aE += aB + "_"
                            } else {
                                aE += "_ "
                            }
                        }
                    }
                }
                return aE
            };*/
        }


    public static void main(String[] args) throws ParseException {
        String str="{'cardTypes':[{'end_station_name':null,'end_time':null,'id':'1','start_station_name':null,'start_time':null,'value':'\\u4E2D\\u56FD\\u5C45\\u6C11\\u8EAB\\u4EFD\\u8BC1'},{'end_station_name':null,'end_time':null,'id':'C','start_station_name':null,'start_time':null,'value':'\\u6E2F\\u6FB3\\u5C45\\u6C11\\u6765\\u5F80\\u5185\\u5730\\u901A\\u884C\\u8BC1'},{'end_station_name':null,'end_time':null,'id':'G','start_station_name':null,'start_time':null,'value':'\\u53F0\\u6E7E\\u5C45\\u6C11\\u6765\\u5F80\\u5927\\u9646\\u901A\\u884C\\u8BC1'},{'end_station_name':null,'end_time':null,'id':'B','start_station_name':null,'start_time':null,'value':'\\u62A4\\u7167'},{'end_station_name':null,'end_time':null,'id':'H','start_station_name':null,'start_time':null,'value':'\\u5916\\u56FD\\u4EBA\\u6C38\\u4E45\\u5C45\\u7559\\u8EAB\\u4EFD\\u8BC1'}],'isAsync':'1','key_check_isChange':'0D30B00B8925812D2D35F5A729AEE154DDDB8D6973B9888662D91E2D','leftDetails':['\\u4E00\\u7B49\\u5EA7(75.00\\u5143)\\u65E0\\u7968','\\u4E8C\\u7B49\\u5EA7(47.00\\u5143)\\u65E0\\u7968','\\u65E0\\u5EA7(47.00\\u5143)1\\u5F20\\u7968'],'leftTicketStr':'kvb9N7uA1coXxfCeCTpFScPQj9UHGA71pji%2Bh4jawcOxv9YC','limitBuySeatTicketDTO':{'seat_type_codes':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'\\u4E8C\\u7B49\\u5EA7'}],'ticket_seat_codeMap':{'3':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'\\u4E8C\\u7B49\\u5EA7'}],'2':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'\\u4E8C\\u7B49\\u5EA7'}],'1':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'\\u4E8C\\u7B49\\u5EA7'}],'4':[{'end_station_name':null,'end_time':null,'id':'O','start_station_name':null,'start_time':null,'value':'\\u4E8C\\u7B49\\u5EA7'}]},'ticket_type_codes':[{'end_station_name':null,'end_time':null,'id':'1','start_station_name':null,'start_time':null,'value':'\\u6210\\u4EBA\\u7968'},{'end_station_name':null,'end_time':null,'id':'2','start_station_name':null,'start_time':null,'value':'\\u513F\\u7AE5\\u7968'},{'end_station_name':null,'end_time':null,'id':'3','start_station_name':null,'start_time':null,'value':'\\u5B66\\u751F\\u7968'},{'end_station_name':null,'end_time':null,'id':'4','start_station_name':null,'start_time':null,'value':'\\u6B8B\\u519B\\u7968'}]},'maxTicketNum':'5','orderRequestDTO':{'adult_num':0,'apply_order_no':null,'bed_level_order_num':null,'bureau_code':null,'cancel_flag':null,'card_num':null,'channel':null,'child_num':0,'choose_seat':null,'disability_num':0,'end_time':{'date':1,'day':4,'hours':16,'minutes':54,'month':0,'seconds':0,'time':32040000,'timezoneOffset':-480,'year':70},'exchange_train_flag':'0','from_station_name':'\\u4E0A\\u6D77\\u8679\\u6865','from_station_telecode':'AOH','get_ticket_pass':null,'id_mode':'Y','isShowPassCode':null,'leftTicketGenTime':null,'order_date':null,'passengerFlag':null,'realleftTicket':null,'reqIpAddress':null,'reqTimeLeftStr':null,'reserve_flag':'A','seat_detail_type_code':null,'seat_type_code':null,'sequence_no':null,'start_time':{'date':1,'day':4,'hours':16,'minutes':1,'month':0,'seconds':0,'time':28860000,'timezoneOffset':-480,'year':70},'start_time_str':null,'station_train_code':'D3142','student_num':0,'ticket_num':0,'ticket_type_order_num':null,'to_station_name':'\\u65E0\\u9521','to_station_telecode':'WXH','tour_flag':'dc','trainCodeText':null,'train_date':{'date':1,'day':3,'hours':0,'minutes':0,'month':4,'seconds':0,'time':1556640000000,'timezoneOffset':-480,'year':119},'train_date_str':null,'train_location':null,'train_no':'5c000D31430H','train_order':null,'varStr':null},'purpose_codes':'00','queryLeftNewDetailDTO':{'BXRZ_num':'-1','BXRZ_price':'0','BXYW_num':'-1','BXYW_price':'0','EDRZ_num':'-1','EDRZ_price':'0','EDSR_num':'-1','EDSR_price':'0','ERRB_num':'-1','ERRB_price':'0','GG_num':'-1','GG_price':'0','GR_num':'-1','GR_price':'0','HBRW_num':'-1','HBRW_price':'0','HBRZ_num':'-1','HBRZ_price':'0','HBYW_num':'-1','HBYW_price':'0','HBYZ_num':'-1','HBYZ_price':'0','RW_num':'-1','RW_price':'0','RZ_num':'-1','RZ_price':'0','SRRB_num':'-1','SRRB_price':'0','SWZ_num':'-1','SWZ_price':'0','TDRZ_num':'-1','TDRZ_price':'0','TZ_num':'-1','TZ_price':'0','WZ_num':'1','WZ_price':'00470','WZ_seat_type_code':'O','YB_num':'-1','YB_price':'0','YDRZ_num':'-1','YDRZ_price':'0','YDSR_num':'-1','YDSR_price':'0','YRRB_num':'-1','YRRB_price':'0','YW_num':'-1','YW_price':'0','YYRW_num':'-1','YYRW_price':'0','YZ_num':'-1','YZ_price':'0','ZE_num':'0','ZE_price':'00470','ZY_num':'0','ZY_price':'00750','arrive_time':'1654','control_train_day':'','controlled_train_flag':null,'controlled_train_message':null,'day_difference':null,'end_station_name':null,'end_station_telecode':null,'from_station_name':'\\u4E0A\\u6D77\\u8679\\u6865','from_station_telecode':'AOH','is_support_card':null,'lishi':'00:53','seat_feature':'','start_station_name':null,'start_station_telecode':null,'start_time':'1601','start_train_date':'','station_train_code':'D3142','to_station_name':'\\u65E0\\u9521','to_station_telecode':'WXH','train_class_name':null,'train_no':'5c000D31430H','train_seat_feature':'','yp_ex':''},'queryLeftTicketRequestDTO':{'arrive_time':'16:54','bigger20':'Y','exchange_train_flag':'0','from_station':'AOH','from_station_name':'\\u4E0A\\u6D77\\u8679\\u6865','from_station_no':'22','lishi':'00:53','login_id':null,'login_mode':null,'login_site':null,'purpose_codes':'00','query_type':null,'seatTypeAndNum':null,'seat_types':'OMO','start_time':'16:01','start_time_begin':null,'start_time_end':null,'station_train_code':'D3142','ticket_type':null,'to_station':'WXH','to_station_name':'\\u65E0\\u9521','to_station_no':'25','train_date':'20190501','train_flag':null,'train_headers':null,'train_no':'5c000D31430H','useMasterPool':true,'useWB10LimitTime':true,'usingGemfireCache':false,'ypInfoDetail':'kvb9N7uA1coXxfCeCTpFScPQj9UHGA71pji%2Bh4jawcOxv9YC'},'tour_flag':'dc','train_location':'G2'};";
        JSONObject json=new JSONObject(str);
        System.out.println(json);
        System.out.println(json.getJSONObject("limitBuySeatTicketDTO").getJSONArray("seat_type_codes"));
        System.out.println(json.getJSONObject("limitBuySeatTicketDTO").getJSONArray("ticket_type_codes"));

        System.out.println(json.getJSONArray("cardTypes"));

        System.out.println(json.getJSONObject("orderRequestDTO").get("cancel_flag"));
        System.out.println(json.getJSONObject("orderRequestDTO").get("bed_level_order_num"));
        System.out.println(json.getJSONObject("orderRequestDTO").getString("tour_flag"));
        System.out.println();
    }
}
