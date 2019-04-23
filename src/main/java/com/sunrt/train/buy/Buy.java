package com.sunrt.train.buy;

import com.sunrt.train.screening.Stations;
import com.sunrt.train.screening.cP;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Scanner;

public class Buy {
    private static Scanner sc=new Scanner(System.in);

    public static void sc1(){
        System.out.println("请选择座位类型：");
        SeatType sts[]=SeatType.getSeatTypes();
        for(int i=0;i<sts.length;i++){
            System.out.println(i+":"+sts[i].desc);
        }
        System.out.println("请输入出行日期：（格式如：2019-01-01）");
        String train_date=sc.nextLine();
    }

    public static void sc(){
        String from_station=null;
        while(true){
            System.out.println("请输入出发站名称：");
            List<String> list= Stations.selectStations(sc.nextLine());
            if(list.size()==1){
                String str=list.get(0);
                String sta[]=str.split("\\|");
                System.out.println("您是要选择：【"+sta[1]+"】吗？y or n");
                String yn=sc.nextLine();
                if("y".equalsIgnoreCase(yn)){
                    from_station=sta[2];
                    break;
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
                                from_station=sta[2];
                            }
                            break;
                        }
                    }catch (NumberFormatException e){
                        System.out.println("无效的输入");
                    }
                }
                if(from_station!=null){
                    break;
                }
            }else{
                System.out.println("没有找到此站");
            }
        }
        System.out.println(from_station);
        if(true){
            return ;
        }
    }
    public static ArriveDate ArriveDate (String cM,String cL){
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

    public static String getSeatCountStr(STUM stum, cP cp){
        String numStr=null;
        switch(stum){
            case swz:
                numStr=cp.swz_num;
                break;
            case zy:
                numStr=cp.zy_num;
                break;
            case ze:
                numStr=cp.ze_num;
                break;
            case gr:
                numStr=cp.gr_num;
                break;
            case rw:
                numStr=cp.rw_num;
                break;
            case srrb:
                numStr=cp.srrb_num;
                break;
            case yw:
                numStr=cp.yw_num;
                break;
            case rz:
                numStr=cp.rz_num;
                break;
            case yz:
                numStr=cp.yz_num;
                break;
            case wz:
                numStr=cp.wz_num;
                break;
        }
        if(StringUtils.isNotEmpty(numStr)&&!"无".equals(numStr)&&!"--".equals(numStr)){
            return numStr;
        }
        return null;
    }
}
