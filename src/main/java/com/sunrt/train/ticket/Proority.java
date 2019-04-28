package com.sunrt.train.ticket;

import java.io.IOException;
import java.util.*;

public class Proority {
    private final static String file="priority.properties";
    public int proorityDigit;
    public ProorityEnum proorityId;

    @Override
    public String toString() {
        return "Proority{" +
                "proorityDigit=" + proorityDigit +
                ", proorityId=" + proorityId +
                '}';
    }

    public Proority(int proorityDigit, ProorityEnum proorityId) {
        this.proorityDigit = proorityDigit;
        this.proorityId = proorityId;
    }

    enum ProorityEnum{
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
            Collections.sort(list, new Comparator<Proority>() {
                @Override
                public int compare(Proority p1, Proority p2) {
                    return p1.proorityDigit-p2.proorityDigit;
                }
            });
            Proority proorities[]=new Proority[list.size()];
            list.toArray(proorities);
            return proorities;
        } catch (IOException e) {
        } catch (NumberFormatException e){}
        return null;
    }
}
