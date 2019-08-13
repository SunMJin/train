package com.sunrt.train.ticket;

import org.json.JSONObject;

public class OrderInfo {
    private String allEncStr;
    private String passenger_id_type_code;
    private String passenger_id_no;
    private String passenger_name;

    private String globalRepeatSubmitToken;
    private JSONObject orderRequestDTOJson ;
    private String tour_flag;
    private String cancel_flag;
    private String bed_level_order_num;
    private String PassengerTicketStr;
    private String oldPassengerStr;
    private String seatId;
    private JSONObject ticketInfoForPassengerForm;

    public JSONObject getTicketInfoForPassengerForm() {
        return ticketInfoForPassengerForm;
    }

    public void setTicketInfoForPassengerForm(JSONObject ticketInfoForPassengerForm) {
        this.ticketInfoForPassengerForm = ticketInfoForPassengerForm;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getPassengerTicketStr() {
        return PassengerTicketStr;
    }

    public void setPassengerTicketStr(String passengerTicketStr) {
        PassengerTicketStr = passengerTicketStr;
    }

    public String getOldPassengerStr() {
        return oldPassengerStr;
    }

    public void setOldPassengerStr(String oldPassengerStr) {
        this.oldPassengerStr = oldPassengerStr;
    }

    public String getAllEncStr() {
        return allEncStr;
    }

    public void setAllEncStr(String allEncStr) {
        this.allEncStr = allEncStr;
    }

    public String getPassenger_id_type_code() {
        return passenger_id_type_code;
    }

    public void setPassenger_id_type_code(String passenger_id_type_code) {
        this.passenger_id_type_code = passenger_id_type_code;
    }

    public String getPassenger_id_no() {
        return passenger_id_no;
    }

    public void setPassenger_id_no(String passenger_id_no) {
        this.passenger_id_no = passenger_id_no;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public String getGlobalRepeatSubmitToken() {
        return globalRepeatSubmitToken;
    }

    public void setGlobalRepeatSubmitToken(String globalRepeatSubmitToken) {
        this.globalRepeatSubmitToken = globalRepeatSubmitToken;
    }

    public JSONObject getOrderRequestDTOJson() {
        return orderRequestDTOJson;
    }

    public void setOrderRequestDTOJson(JSONObject orderRequestDTOJson) {
        this.orderRequestDTOJson = orderRequestDTOJson;
    }

    public String getTour_flag() {
        return tour_flag;
    }

    public void setTour_flag(String tour_flag) {
        this.tour_flag = tour_flag;
    }

    public String getCancel_flag() {
        return cancel_flag;
    }

    public void setCancel_flag(String cancel_flag) {
        this.cancel_flag = cancel_flag;
    }

    public String getBed_level_order_num() {
        return bed_level_order_num;
    }

    public void setBed_level_order_num(String bed_level_order_num) {
        this.bed_level_order_num = bed_level_order_num;
    }
}
