package com.sunrt.train;

import com.sunrt.train.data.Stations;
import com.sunrt.train.data.TrainConf;
import com.sunrt.train.service.LoginService;
import com.sunrt.train.service.CaptchaServiceImpl;
import com.sunrt.train.ticket.Param;
import com.sunrt.train.ticket.TicketService;
import com.sunrt.train.utils.HttpUtils;

public class Application {
    public static void main(String[] args) {
        if(!HttpUtils.cookiesFile.exists()){
            CaptchaServiceImpl captchaService=CaptchaServiceImpl.getInstance();
            System.out.println("正在连接验证码识别服务...");
            if(captchaService.conn(TrainConf.captchaHost,TrainConf.captchaPort)){
                System.out.println("验证码识别服务连接成功");
            }else{
                System.out.println("验证码服务连接失败");
                return;
            }
            System.out.println("开始登录...");
            LoginService loginService=LoginService.getInstance();
            String loginInfo=loginService.login(TrainConf.username,TrainConf.password);
            System.out.println(loginInfo);
            captchaService.close();
        }
        System.out.println("开始购票...");
        TicketService ticketService=TicketService.getInstance();
        ticketService.start(new Param(TrainConf.seatType,
                TrainConf.trainDate,Stations.getCode(TrainConf.fromSta), Stations.getCode(TrainConf.toSta),
                TrainConf.trainType,TrainConf.starttime, TrainConf.arrTime,TrainConf.fromSta,TrainConf.toSta));


    }
}