package com.sunrt.train.ticket;

public class Param {
    public Stum st[];
    public String trainDate;
    public String from_sta;
    public String to_sta;
    public String purpose_codes;

    public String trainType;
    public int[] starttime;
    public int[] arrTime;
    public String from_sta_str;
    public String to_sta_str;
    public String tour_flag;

    public Param(Stum[] st, String trainDate, String from_sta, String to_sta, String purpose_codes, String trainType, int[] starttime, int[] arrTime, String from_sta_str, String to_sta_str, String tour_flag) {
        this.st = st;
        this.trainDate = trainDate;
        this.from_sta = from_sta;
        this.to_sta = to_sta;
        this.purpose_codes = purpose_codes;
        this.trainType = trainType;
        this.starttime = starttime;
        this.arrTime = arrTime;
        this.from_sta_str = from_sta_str;
        this.to_sta_str = to_sta_str;
        this.tour_flag = tour_flag;
    }
}
