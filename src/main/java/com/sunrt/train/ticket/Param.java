package com.sunrt.train.ticket;

public class Param {
    public Stum st;
    public String trainDate;
    public String from_sta;
    public String to_sta;
    public String purpose_codes;
    public String trainType;

    public Param(Stum st, String trainDate, String from_sta, String to_sta, String purpose_codes, String trainType) {
        this.st = st;
        this.trainDate = trainDate;
        this.from_sta = from_sta;
        this.to_sta = to_sta;
        this.purpose_codes = purpose_codes;
        this.trainType = trainType;
    }
}
