package com.sunrt.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HttpController {
    static final int MAXTHREADCOUNT=10;
    ExecutorService executorService= Executors.newFixedThreadPool(MAXTHREADCOUNT);
    @PostMapping("/robTicks")
    public String robTicks(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String trainDate,
            @RequestParam String trainNo,
            @RequestParam String seatType
    ){
        RobTicksThread robTicksThread=new RobTicksThread(username,password,trainDate,trainNo,seatType);
        executorService.submit(robTicksThread);
        return null;
    }
}