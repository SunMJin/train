package com.sunrt.train.ticket;

import com.sunrt.train.data.Stations;
import com.sunrt.train.utils.CharUtils;
import com.sunrt.train.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class Params {
    private static Scanner sc;
    private static String from_sta_str;
    private static String to_sta_str;
    private static StringBuilder trainTypeSbStr;
    private static StringBuilder stSbStr;

    private final static String trainTypes[]=new String[]{"GC-高铁/城际","D-动车","Z-直达","T-特快","K-快速","其他"};
    enum StaTypes{
        FROMSTA,TOSTA
    }
    static{
        sc=new Scanner(System.in);
    }


    public static void removeDuplicate(Object []objs){

        for(int i=0;i<objs.length;i++){

        }
    }

    private static Stum[] getSeatType(){
        System.out.println("请选择一组座位类型，逗号分隔，顺序优先级：");
        Seats sts[]= Seats.getSeats();
        for(int i=0;i<sts.length;i++){
            System.out.println(i+":"+sts[i].desc);
        }
        while(true){
            String line=sc.nextLine();
            if(line.length()==0){
                return null;
            }
            String iarr[]=null;
            if(line.indexOf(",")>-1){
                iarr=line.split(",");
            }else{
                iarr=line.split("，");
            }
            List<Stum>idxList=new ArrayList<>();
            stSbStr=new StringBuilder();
            for(String idx:iarr){
                try{
                    int index=Integer.parseInt(idx);
                    if(!(index<0||index>sts.length-1)){
                        stSbStr.append(sts[index].desc+",");
                        //mark
                        idxList.add(sts[index].stum);
                    }
                }catch (NumberFormatException e){}
            }
            if(idxList.size()>0){
                Set<Stum> set=new HashSet<Stum>(idxList);
                return set.toArray(new Stum[set.size()]);
            }
            System.out.println("请指定一个有效的数字");
        }
    }

    private static String getTrainDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        while(true){
            System.out.println("请输入出行日期：（格式如：2019-01-01，默认今日）");
            String train_date=sc.nextLine();
            if(train_date.length()==0){
                String today=DateUtils.getToday(sdf);
                System.out.println(today);
                return today;
            }
            if(DateUtils.isValid(train_date,sdf)){
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
            String line=sc.nextLine();
            List<String> list= Stations.selectStations(line);
            if(list.size()==1){
                String str=list.get(0);
                String sta[]=str.split("\\|");
                System.out.println("您是要选择：【"+sta[1]+"】吗？Y or N ?");
                String yn=sc.nextLine();
                if("y".equalsIgnoreCase(yn)){
                    if(staTypes==staTypes.FROMSTA) from_sta_str=sta[1];else to_sta_str=sta[1];
                    return sta[2];
                }
            }else if(list.size()>1){
                while(true){
                    System.out.println("请在下列站点中选择一个数字：");
                    for(int i=0;i<list.size();i++){
                        String str=list.get(i);
                        String sta[]=str.split("\\|");
                        System.out.println(i+":"+sta[1]);
                    }
                    try{
                        String staIndexStr=sc.nextLine();
                        int staIndex=Integer.parseInt(staIndexStr);
                        if(staIndex>list.size()-1||staIndex<0){
                            System.out.println("请选择一个有效的数字");
                        }else{
                            String sta[]=list.get(staIndex).split("\\|");
                            System.out.println("您确定选择【"+sta[1]+"】吗？ Y or N ?");
                            String yn=sc.nextLine();
                            if("y".equalsIgnoreCase(yn)){
                                if(staTypes==staTypes.FROMSTA) from_sta_str=sta[1];else to_sta_str=sta[1];
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

    private static String getTrainType(){
        while(true){
            trainTypeSbStr=new StringBuilder();
            System.out.println("请输入一组车次类型，逗号分隔，顺序优先级");
            for(int i=0;i<trainTypes.length;i++){
                System.out.println(i+":"+trainTypes[i]);
            }
            String indexStr=sc.nextLine();
            if(indexStr.length()==0){
                return null;
            }
            String indexStrArr[]=null;
            if(indexStr.indexOf(",")>-1){
                indexStrArr=indexStr.split(",");
            }else{
                indexStrArr=indexStr.split("，");
            }
            StringBuilder sb=new StringBuilder();
            for(String is:indexStrArr){
                try{
                    int index=Integer.parseInt(is);
                    if(!(index<0||index>trainTypes.length-1)){
                        int isa=Integer.parseInt(is);
                        if(CharUtils.isLetter(trainTypes[isa].charAt(0))){
                            String x[]=trainTypes[isa].split("-");
                            sb.append(x[0]);
                            trainTypeSbStr.append(trainTypes[isa]+",");
                        }else{
                            sb.append("Q");
                        }
                    }
                }catch (NumberFormatException e){}
            }
            if(sb.length()>0){
                return sb.toString();
            }
            System.out.println("请输入一组有效数字");
        }
    }

    private static int[] getArrtime(){
        while(true){
            System.out.println("请输入最佳到达时间（选填），格式：[时]:[分]");
            String time=sc.nextLine();
            if(time.length()==0){
                return null;
            }
            String tarr[];
            if(time.indexOf(":")>-1){
                tarr=time.split(":");
            }else{
                tarr=time.split("：");
            }
            try{
                if(tarr.length==2){
                    int hour=Integer.parseInt(tarr[0]);
                    int min=Integer.parseInt(tarr[1]);
                    if(hour>=0&&hour<=23&&min>=0&&min<=59){
                        return new int[]{hour,min};
                    }
                }
            }catch (NumberFormatException e){
            }
            System.out.println("时间格式错误！");
        }
    }

    public static void main(String[] args) {
        String s=getTrainType();
        System.out.println(s);
    }

    public static Param getParams() {
        while(true){
            String trainDate=getTrainDate();
            String from_sta=getStation(StaTypes.FROMSTA);
            String to_sta=getStation(StaTypes.TOSTA);
            String trainType=getTrainType();
            Stum st[]=getSeatType();
            int arrTime[]=getArrtime();

            System.out.println("请核对以下参数：");
            System.out.println("出行日期："+trainDate);
            System.out.println("出发站："+from_sta_str);
            System.out.println("到达站："+to_sta_str);
            if(trainTypeSbStr.length()!=0){
                System.out.println("车次类型："+trainTypeSbStr.substring(0, trainTypeSbStr.length()-1));
            }
            if(stSbStr.length()!=0){
                System.out.println("座位类型："+stSbStr.substring(0,stSbStr.length()-1));
            }
            if(arrTime!=null){
                System.out.println("最佳到达时间："+"("+arrTime[0]+":"+arrTime[1]+")");
            }
            System.out.println("Y or N ?");
            String yn=sc.nextLine();
            if(yn.equalsIgnoreCase("y")){
                return new Param(st,trainDate,from_sta,to_sta,"ADULT",trainType,arrTime);
            }
        }
    }
}
