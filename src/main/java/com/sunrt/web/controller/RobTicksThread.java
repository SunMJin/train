package com.sunrt.web.controller;

import com.sunrt.train.bean.LoginResult;
import com.sunrt.train.login.LoginService;
import com.sunrt.train.utils.HttpUtils;

public class RobTicksThread implements Runnable{
    private String username;
    private String password;
    private String trainDate;
    private String trainNo;
    private String seatType;

    public RobTicksThread(String username, String password, String trainDate, String trainNo, String seatType) {
        this.username = username;
        this.password = password;
        this.trainDate = trainDate;
        this.trainNo = trainNo;
        this.seatType = seatType;
    }

    @Override
    public void run() {
        HttpUtils http=new HttpUtils("/cookies/"+username);
        LoginService loginService=new LoginService(http);
        LoginResult loginResult=loginService.login(username,password);
        System.out.println(loginResult);
        //TicketService ticketService=new TicketService(http,loginService);
        //ticketService.start();
    }
}
