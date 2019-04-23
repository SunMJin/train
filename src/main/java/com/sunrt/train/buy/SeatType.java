package com.sunrt.train.buy;


public class SeatType{
    private static final String DESC[]=new String[]{"商务座","一等座","二等座","高级软卧","软卧一等卧","动卧","硬卧二等座","软座","硬座","无座"};
    public STUM stum;
    public String desc;

    public static SeatType[] getSeatTypes(){
        SeatType sts[]=new SeatType[DESC.length];
        int index=0;
        for(STUM stum:STUM.values()){
            SeatType st=new SeatType();
            st.stum=stum;
            st.desc=DESC[index];
            sts[index]=st;
            index++;
        }
        return sts;
    }
}



