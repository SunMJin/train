package com.sunrt.train;

import com.sunrt.train.utils.HttpUtils;

public class PublicHttp {
    private static HttpUtils httpUtils=new HttpUtils();
    public static HttpUtils getInstance(){
        return httpUtils;
    }
}
