package com.sunrt.train.ticket;

import com.sunrt.train.data.Cp;
import com.sunrt.train.data.Cr;
import com.sunrt.train.utils.CharUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SelectTickets {
    public static List<Cr> retrieve(List<Cr> list,Param p){
        if(list==null||p==null){
            throw new NullPointerException();
        }
        String trainType=p.trainType;
        Iterator<Cr> it=list.iterator();
        while(it.hasNext()){
            Cp cp=it.next().queryLeftNewDTO;
            String stacode=cp.station_train_code;
            char fchar=CharUtils.getTrainCodeFirstLetter(stacode.charAt(0));

            //删除无法购买的
            if(!"Y".equals(cp.canWebBuy)){
                it.remove();
                continue;
            }

            //车次筛选
            if(trainType!=null&&trainType.indexOf(fchar)==-1){
                it.remove();
                continue;
            }

            //座位类型筛选
            String st[]=p.st;
            if(st!=null){
                boolean flag=false;
                for(int i=0;i<st.length;i++){
                    String count=Seats.getSeatCount(st[i],cp);
                    if(count!=null){
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    it.remove();
                    continue;
                }
            }
        }
        return list;
    }

    public static Cr orderTicket(List<Cr> list,Param p){
        if(list==null||list.size()==0||p==null){
            throw new NullPointerException();
        }
        Proority prooritys[]=Proority.getProority();
        Collections.sort(list, (c1, c2) -> {
            int x=0;
            if(prooritys!=null){
                for(Proority proority:prooritys){
                    if(x==0){
                        switch (proority.proorityId){
                            case traintype:
                                if(p.trainType!=null){
                                    String tc1=c1.queryLeftNewDTO.station_train_code;
                                    String tc2=c2.queryLeftNewDTO.station_train_code;
                                    char le1= CharUtils.getTrainCodeFirstLetter(tc1.charAt(0));
                                    char le2=CharUtils.getTrainCodeFirstLetter(tc2.charAt(0));
                                    int a=p.trainType.indexOf(le1);
                                    int b=p.trainType.indexOf(le2);
                                    x=a-b;
                                }
                                break;
                            case seattype:
                                String psts[]=p.st;
                                if(psts!=null){
                                    for(int k=0;k<psts.length;k++){
                                        int sc1=0;
                                        int sc2=0;
                                        if(Seats.getSeatCount(psts[k],c1.queryLeftNewDTO)!=null){
                                            sc1=1;
                                        }
                                        if(Seats.getSeatCount(psts[k],c2.queryLeftNewDTO)!=null){
                                            sc2=1;
                                        }
                                        if(sc1!=sc2){
                                            x=sc2-sc1;
                                            break;
                                        }
                                    }
                                }
                                break;
                            case starttime:
                                if(p.starttime!=null){
                                    int hour=p.starttime[0];
                                    int min=p.starttime[1];
                                    String time1[]=c1.queryLeftNewDTO.start_time.split(":");
                                    String time2[]=c2.queryLeftNewDTO.start_time.split(":");
                                    int habs1=Math.abs(Integer.parseInt(time1[0])-hour);
                                    int mabs1=Math.abs(Integer.parseInt(time1[1])-min);
                                    int habs2=Math.abs(Integer.parseInt(time2[0])-hour);
                                    int mabs2=Math.abs(Integer.parseInt(time2[1])-min);
                                    x=habs1==habs2?mabs1-mabs2:habs1-habs2;
                                }
                                break;
                            case arrivetime:
                                if(p.arrTime!=null){
                                    int hour=p.arrTime[0];
                                    int min=p.arrTime[1];
                                    String time1[]=c1.queryLeftNewDTO.arrive_time.split(":");
                                    String time2[]=c2.queryLeftNewDTO.arrive_time.split(":");
                                    int habs1=Math.abs(Integer.parseInt(time1[0])-hour);
                                    int mabs1=Math.abs(Integer.parseInt(time1[1])-min);
                                    int habs2=Math.abs(Integer.parseInt(time2[0])-hour);
                                    int mabs2=Math.abs(Integer.parseInt(time2[1])-min);
                                    x=habs1==habs2?mabs1-mabs2:habs1-habs2;
                                }
                                break;
                            case lishi:
                                String lishi1[]=c1.queryLeftNewDTO.lishi.split(":");
                                String lishi2[]=c2.queryLeftNewDTO.lishi.split(":");
                                int hour1=Integer.parseInt(lishi1[0]);
                                int hour2=Integer.parseInt(lishi2[0]);
                                int min1=Integer.parseInt(lishi1[1]);
                                int min2=Integer.parseInt(lishi2[1]);
                                x=hour1==hour2?min1-min2:hour1-hour2;
                                break;
                        }
                    }else{
                        break;
                    }
                }
            }
            return x;
        });
        return list.get(0);
    }
}
