package com.sunrt.train.utils;

public class PublicHttp {
    private static HttpUtils httpUtils=new HttpUtils();
    public static HttpUtils getInstance(){
        return httpUtils;
    }
}
