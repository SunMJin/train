package com.sunrt.train.conf;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class TrainConf {
    public static String captchaDownPath;
    public static String captchaHost;
    public static int captchaPort;
    public static String RAIL_DEVICEID;
    public static String username;
    public static String password;

    public static String trainDate;
    public static String fromSta;
    public static String toSta;
    public static String seatType[];
    public static String trainType;
    public static int []starttime;
    public static int []arrTime;

    public static String passenger_name[];
    public static String passenger_id_no[];

    static{
        Properties properties=new Properties();
        try (InputStreamReader in= new InputStreamReader(TrainConf.class.getResourceAsStream("/12306.properties"),"utf-8");){
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        passenger_name=properties.getProperty("姓名").split(" ");
        passenger_id_no=properties.getProperty("身份证").split(" ");

        captchaDownPath=properties.getProperty("captchaDownPath");
        captchaHost=properties.getProperty("captchaHost");
        captchaPort=Integer.valueOf(properties.getProperty("captchaPort"));
        RAIL_DEVICEID=properties.getProperty("RAIL_DEVICEID");
        username=properties.getProperty("username");
        password=properties.getProperty("password");
        trainDate=properties.getProperty("出发日期");
        fromSta=properties.getProperty("出发地");
        toSta=properties.getProperty("到达地");
        String seatTypes=properties.getProperty("座位");
        seatType=seatTypes.split(" ");
        trainType=properties.getProperty("车次类型");
        String st=properties.getProperty("出发时间");
        if(!StringUtils.isEmpty(st)){
            String []sts=st.split(":");
            starttime=new int[]{Integer.parseInt(sts[0]),Integer.parseInt(sts[1])};
        }
        String at=properties.getProperty("到达时间");
        if(!StringUtils.isEmpty(at)){
            String []ats=st.split(":");
            arrTime=new int[]{Integer.parseInt(ats[0]),Integer.parseInt(ats[1])};
        }
    }
}
