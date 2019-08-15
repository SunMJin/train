package com.sunrt.web.controller;

import com.sunrt.train.bean.LoginResult;
import com.sunrt.train.bean.TrainParam;
import com.sunrt.train.login.LoginService;
import com.sunrt.train.utils.HttpUtils;
import org.springframework.util.DigestUtils;

import java.io.File;

public class RobTicksThread implements Runnable{
    private TrainParam param;

    public RobTicksThread(TrainParam param) {
        this.param = param;
    }

    @Override
    public void run() {
        HttpUtils http=new HttpUtils("cookies"+ File.separator+DigestUtils.md5DigestAsHex(param.username.getBytes()));
        LoginService loginService=new LoginService(http);
        LoginResult loginResult=loginService.login(param.username,param.password);
        System.out.println(loginResult);
        //TicketService ticketService=new TicketService(http,loginService);
        //ticketService.start();
    }
}
