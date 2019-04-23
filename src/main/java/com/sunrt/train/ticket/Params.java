package com.sunrt.train.ticket;

import com.sunrt.train.data.Stations;
import com.sunrt.train.utils.DateUtils;

import java.util.List;
import java.util.Scanner;

public class Params {

    private static Scanner sc;
    enum StaTypes{
        FROMSTA,TOSTA
    }

    static{
        sc=new Scanner(System.in);
    }
    private static Stum getSeatType(){
        System.out.println("请选择座位类型：");
        Seats sts[]= Seats.getSeats();
        for(int i=0;i<sts.length;i++){
            System.out.println(i+":"+sts[i].desc);
        }
        while(true){
            try{
                Integer index=Integer.parseInt(sc.nextLine());
                if(index<0||index>sts.length-1){
                    System.out.println("请指定一个有效的数字");
                }else{
                    return sts[index].stum;
                }
            }catch (NumberFormatException e){
                System.out.println("请指定一个有效的数字");
            }
        }
    }


    private static String getTrainDate(){
        while(true){
            System.out.println("请输入出行日期：（格式如：2019-01-01）");
            String train_date=sc.nextLine();
            if(DateUtils.isValidDate(train_date)){
                return train_date;
            }else{
                System.out.println("日期格式错误！");
            }
        }
    }

    private static String getStation(StaTypes staTypes){
        while(true){
            if(staTypes==StaTypes.FROMSTA)
                System.out.println("请输入出发站：");
            else
                System.out.println("请输入到达站：");
            List<String> list= Stations.selectStations(sc.nextLine());
            if(list.size()==1){
                String str=list.get(0);
                String sta[]=str.split("\\|");
                System.out.println("您是要选择：【"+sta[1]+"】吗？y or n");
                String yn=sc.nextLine();
                if("y".equalsIgnoreCase(yn)){
                    return sta[2];
                }
            }else if(list.size()>1){
                System.out.println("请在下列站点中选择一个数字：");
                for(int i=0;i<list.size();i++){
                    String str=list.get(i);
                    String sta[]=str.split("\\|");
                    System.out.println(i+":"+sta[1]);
                }
                while(true){
                    try{
                        String staIndexStr=sc.nextLine();
                        int staIndex=Integer.parseInt(staIndexStr);
                        if(staIndex>list.size()-1||staIndex<0){
                            System.out.println("请选择一个有效的数字");
                        }else{
                            String sta[]=list.get(staIndex).split("\\|");
                            System.out.println("您确定选择【"+sta[1]+"】吗？ y or n");
                            String yn=sc.nextLine();
                            if("y".equalsIgnoreCase(yn)){
                                return sta[2];
                            }
                        }
                    }catch (NumberFormatException e){
                        System.out.println("请指定一个有效的数字");
                    }
                }
            }else{
                System.out.println("没有找到此站");
            }
        }
    }
    private static ArriveDate ArriveDate (String cM, String cL){
        cM = cM.replace(":", "");
        cL = cL.replace(":", "");
        int hour_value = Integer.parseInt(cM.substring(0, 2)) + Integer.parseInt(cL.substring(0, 2));
        int min_value = Integer.parseInt(cM.substring(2, 4)) + Integer.parseInt(cL.substring(2, 4));
        if (min_value >= 60) {
            hour_value = hour_value + 1;
        }
        if (hour_value >= 24 && hour_value < 48) {
            return ArriveDate.TOMORROW;
        } else {
            if (hour_value >= 48 && hour_value < 72) {
                return ArriveDate.DAY2;
            } else {
                if (hour_value >= 72) {
                    return ArriveDate.DAY3;
                } else {
                    return ArriveDate.TODAY;
                }
            }
        }
    }

    private final static String trainTypes[]=new String[]{"GC-高铁/城际","D-动车","Z-直达","T-特快","K-快速","其他"};


    public static void main(String[] args) {
        String x=getTrainType();
        System.out.println(x);
    }
    private static String getTrainType(){
        String indexStrArr[]=null;
        while(true){
            System.out.println("请输入一组车次类型，用逗号分隔");
            for(int i=0;i<trainTypes.length;i++){
                System.out.println(i+":"+trainTypes[i]);
            }
            String indexStr=sc.nextLine();
            indexStrArr=indexStr.split(",");
            boolean flag=true;
            for(String is:indexStrArr){
                try{
                    int index=Integer.parseInt(is);
                    if(index<0||index>trainTypes.length-1){
                        flag=false;
                        System.out.println("请输入一组有效数字");
                        break;
                    }
                }catch (NumberFormatException e){
                    flag=false;
                    System.out.println("请输入一组有效数字");
                }
            }
            if(flag){
                break;
            }
        }

        StringBuilder sb=new StringBuilder();
        for(String isa_s:indexStrArr){
            int isa=Integer.parseInt(isa_s);
            if(isa!=trainTypes.length-1){
                String x[]=trainTypes[isa].split("-");
                sb.append(x[0]);
            }else{
                sb.append("Q");
            }
        }
        return sb.toString();
    }


    public static Param getParams() {
        Stum st=getSeatType();
        String trainDate=getTrainDate();
        String from_sta=getStation(StaTypes.FROMSTA);
        String to_sta=getStation(StaTypes.TOSTA);
        String trainType=getTrainType();
        return new Param(st,trainDate,from_sta,to_sta,"ADULT",trainType);
    }

}
