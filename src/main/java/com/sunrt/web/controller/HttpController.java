package com.sunrt.web.controller;

import com.sunrt.train.bean.TrainParam;
import com.sunrt.train.context.Stations;
import com.sunrt.web.bean.HttpReturn;
import com.sunrt.web.bean.RetuenCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HttpController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    static final int MAXTHREADCOUNT=10;
    ExecutorService executorService= Executors.newFixedThreadPool(MAXTHREADCOUNT);
    @PostMapping("/robTicks")
    public String robTicks(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String trainType[],
            @RequestParam String trainDate,
            @RequestParam String seatType[],
            @RequestParam String from_sta,
            @RequestParam String to_sta,
            @RequestParam String from_sta_str,
            @RequestParam String to_sta_str,
            @RequestParam Boolean together,
            @RequestParam Boolean autoWindow,
            @RequestParam String name[],
            @RequestParam String id_card[],
            @RequestParam(required=false) String starttime,
            @RequestParam(required=false) String endtime,
            @RequestParam(required=false) String ODTime,
            @RequestParam(required=false) String OATime
    ){
        TrainParam trainParam=new TrainParam(username,password,seatType,trainDate,from_sta, to_sta, trainType,null ,null , from_sta_str, to_sta_str);
        RobTicksThread robTicksThread=new RobTicksThread(trainParam);
        executorService.submit(robTicksThread);
        return null;
    }

    @GetMapping("/isNeedUpdate")
    public HttpReturn isNeedUpdate(
            String ver
    ){
        return new HttpReturn(RetuenCode.SUCCESS,Stations.isLastVer(ver,stringRedisTemplate));
    }

    @GetMapping("/getStations")
    public String getStations(){
        return Stations.getLastStations(stringRedisTemplate);
    }

}