package com.sunrt.train;

import com.sunrt.train.context.Stations;
import com.sunrt.train.conf.TrainConf;
import com.sunrt.train.login.LoginService;
import com.sunrt.train.login.CaptchaService;
import com.sunrt.train.bean.Param;
import com.sunrt.train.ticket.TicketService;

public class Application {
    public static void main(String[] args) {
        LoginService loginService=LoginService.getInstance();
        if(!loginService.checkUser()){
            CaptchaService captchaService= CaptchaService.getInstance();
            System.out.println("正在连接验证码识别服务...");
            if(captchaService.conn(TrainConf.captchaHost,TrainConf.captchaPort)){
                System.out.println("验证码识别服务连接成功");
            }else{
                System.out.println("验证码服务连接失败");
                return;
            }
            System.out.println("开始登录...");
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