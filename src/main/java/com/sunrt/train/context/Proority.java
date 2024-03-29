package com.sunrt.train.context;

import java.io.IOException;
import java.util.*;

public class Proority {
    private final static String file="priority.properties";
    public int proorityDigit;
    public ProorityEnum proorityId;

    public Proority(int proorityDigit, ProorityEnum proorityId) {
        this.proorityDigit = proorityDigit;
        this.proorityId = proorityId;
    }

    public enum ProorityEnum{
        traintype,seattype,starttime,arrivetime,lishi
    }

    public static Proority[] getProority(){
        Properties p=new Properties();
        List<Proority> list=new ArrayList<>();
        try {
            p.load(Proority.class.getClassLoader().getResourceAsStream(file));
            list.add(new Proority(Integer.parseInt(p.getProperty("traintype")),ProorityEnum.traintype));
            list.add(new Proority(Integer.parseInt(p.getProperty("seattype")),ProorityEnum.seattype));
            list.add(new Proority(Integer.parseInt(p.getProperty("starttime")),ProorityEnum.starttime));
            list.add(new Proority(Integer.parseInt(p.getProperty("arrivetime")),ProorityEnum.arrivetime));
            list.add(new Proority(Integer.parseInt(p.getProperty("lishi")),ProorityEnum.lishi));
            Collections.sort(list, Comparator.comparingInt(p2 -> p2.proorityDigit));
            Proority proorities[]=new Proority[list.size()];
            list.toArray(proorities);
            return proorities;
        } catch (IOException e) {
        } catch (NumberFormatException e){}
        return null;
    }
}
