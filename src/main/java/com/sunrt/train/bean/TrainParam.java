package com.sunrt.train.bean;

public class TrainParam {
    public String st[];
    public String trainDate;
    public String from_sta;
    public String to_sta;
    public String purpose_codes="ADULT";

    public String trainType[];
    public int[] starttime;
    public int[] arrTime;
    public String from_sta_str;
    public String to_sta_str;
    public String tour_flag="dc";

    public String username;
    public String password;

    public TrainParam(String username,String password,String[] st, String trainDate, String from_sta, String to_sta, String trainType[], int[] starttime, int[] arrTime, String from_sta_str, String to_sta_str) {
        this.username=username;
        this.password=password;
        this.st = st;
        this.trainDate = trainDate;
        this.from_sta = from_sta;
        this.to_sta = to_sta;
        this.trainType = trainType;
        this.starttime = starttime;
        this.arrTime = arrTime;
        this.from_sta_str = from_sta_str;
        this.to_sta_str = to_sta_str;

    }
}
