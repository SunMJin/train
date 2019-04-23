package com.sunrt.train;

import com.sunrt.train.login.Login;
import com.sunrt.train.data.Stations;

public class Application {
    public static void main(String[] args) {
        //初始化站点
        if(!Stations.init()){
            System.out.println("站点初始化失败");
            return;
        }
        //登陆
        Login.main();
    }
}
