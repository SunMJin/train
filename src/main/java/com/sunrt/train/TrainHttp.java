package com.sunrt.train;

import com.sunrt.train.utils.HttpUtils;

public class TrainHttp {
    public static HttpUtils http=new HttpUtils();
    public static HttpUtils getInstance(){
        return http;
    }
}
